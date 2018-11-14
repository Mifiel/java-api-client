package com.mifiel.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

public class PbeTest {

	private static Pbe pbe;
	private static JSONArray valid;
	private static JSONArray invalid;

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser
				.parse(new FileReader(new File("./src/test/java/com/mifiel/crypto/fixturePbe.json")));
		valid = (JSONArray) jsonObject.get("valid");
		invalid = (JSONArray) jsonObject.get("invalid");
		pbe = new Pbe();
	}

	@Test
	public void testValidValues() {
		byte[] res = null;
		
		for (Object o : valid) {
			JSONObject jsonObject = (JSONObject) o;
			String key = (String) jsonObject.get("key");
			String salt = (String) jsonObject.get("salt");
			Long iter = (Long) jsonObject.get("iterations");
			Long lenght = (Long) jsonObject.get("keylen");
			String result = (String) jsonObject.get("result");
			
			try {
				res = pbe.derivedKey(key, salt.getBytes(), lenght.intValue(), iter.intValue());
			} catch (Exception e) {
				fail("Exception NOT expected.");
			} 
			assertTrue(result.equals(Hex.encodeHexString(res)));

		}
	}
	@Test
	public void testKeyTooLong() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException { 
		JSONObject jsonObject = (JSONObject) invalid.get(0);
		String key = (String) jsonObject.get("key");
		String salt = (String) jsonObject.get("salt");
		Long iter = (Long) jsonObject.get("iterations");
		Long lenght = (Long) jsonObject.get("keylen");
		try{
			pbe.derivedKey(key, salt.getBytes(), lenght.intValue(), iter.intValue());
			fail("IllegalArgumentException expected.");
		} catch(IllegalArgumentException illegalArgumentException){
			assertEquals("invalid keyLength value", illegalArgumentException.getMessage());
		}
		
	}
	
	@Test
	public void testEmptySalt() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException { 
		JSONObject jsonObject = (JSONObject) invalid.get(1);
		String key = (String) jsonObject.get("key");
		String salt = (String) jsonObject.get("salt");
		Long iter = (Long) jsonObject.get("iterations");
		Long lenght = (Long) jsonObject.get("keylen");
		try{
			pbe.derivedKey(key, salt.getBytes(), lenght.intValue(), iter.intValue());
			fail("IllegalArgumentException expected.");
		} catch(IllegalArgumentException illegalArgumentException){
			assertEquals("the salt parameter must not be empty", illegalArgumentException.getMessage());
		}
	}
	
	@Test
	public void testRandomPassword(){
		String pass = pbe.randomPassword();
		assertEquals(pass.length(), 32);
	}

	@Test
	public void testRandomSalt(){
		byte[] salt = pbe.getSalt(20);
		assertEquals(salt.length, 20);
	}
}
