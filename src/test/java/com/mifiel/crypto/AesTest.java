package com.mifiel.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

public class AesTest {

	private static JSONArray valid;

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser
				.parse(new FileReader(new File("./src/test/java/com/mifiel/crypto/fixtureAes.json")));
		valid = (JSONArray) jsonObject.get("valid");
	}

	@Test
	public void testEncryptValidValues() {
		byte[] res = null;
		
		for (Object o : valid) {
			
			JSONObject jsonObject = (JSONObject) o;
			String key = (String) jsonObject.get("key");
			String dataToEncrypt = (String) jsonObject.get("dataToEncrypt");
			String iv = (String) jsonObject.get("iv");
			String encrypted = (String) jsonObject.get("encrypted");
			
			try {
				res = Aes.encrypt(key.getBytes(), dataToEncrypt.getBytes(), iv.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
				fail("Exception NOT expected.");
			} 
			assertTrue(encrypted.equals(Hex.encodeHexString(res)));

		}
	}
	
	@Test
	public void testDecryptValidValues() {
		byte[] res = null;
		
		for (Object o : valid) {
			
			JSONObject jsonObject = (JSONObject) o;
			String key = (String) jsonObject.get("key");
			String dataToEncrypt = (String) jsonObject.get("dataToEncrypt");
			String iv = (String) jsonObject.get("iv");
			String encrypted = (String) jsonObject.get("encrypted");
			
			try {
				res = Aes.decrypt(key.getBytes(), Hex.decodeHex(encrypted.toCharArray()), iv.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
				fail("Exception NOT expected.");
			} 
			assertTrue(dataToEncrypt.equals(new String(res)));

		}
	}

	@Test
	public void testIvLength() {
		byte[] res = Aes.iv();
		assertTrue( res.length == 16);
	}
	
	@Test
	public void testIvValidValues() {
		try {
			Aes.encrypt("abcdfghijklmnopq".getBytes(), "data".getBytes(), "123456".getBytes());
			fail("Exception expected.");
		} catch (Exception e) {
			assertEquals("IV must be 16 bytes long.", e.getMessage());
		} 
	}
	
	@Test
	public void testKeyLength() {
		try {
			Aes.encrypt("abcdfgdgfkgfdsgdfio49845749483487hijklmnopq".getBytes(), "data".getBytes(), Aes.iv());
			fail("Exception expected.");
		} catch (Exception e) {
			assertEquals("Key length not 128/192/256 bits.", e.getMessage());
		} 
	}
	
	@Test
	public void testEmptyData() {
		byte[] res = null;
		try {
			res = Aes.encrypt("abcdfghijklmnopq".getBytes(), "".getBytes(), Aes.iv());
		} catch (Exception e) {
			fail("Exception NOT expected.");
		} 
		assertTrue(res.length != 0);
	}	
}
