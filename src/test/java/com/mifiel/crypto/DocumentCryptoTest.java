package com.mifiel.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.codec.DecoderException;
import org.bouncycastle.util.encoders.Hex;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

public class DocumentCryptoTest {
	private static JSONArray valid;
	private static JSONArray invalid;	
	private static DocumentCrypto docCrypto;
	private static String decrypted = "This memo represents a republication of PKCS #5 v2.0 from RSA\n"
			+ "Laboratories' Public-Key Cryptography Standards (PKCS) series, and\n"
			+ "change control is retained within the PKCS process.  The body of this\n"
			+ "document, except for the security considerations section, is taken\n"
			+ "directly from that specification.\n" +

			"This document provides recommendations for the implementation of\n"
			+ "password-based cryptography, covering key derivation functions,\n"
			+ "encryption schemes, message-authentication schemes, and ASN.1 syntax\n" + "identifying the techniques.\n"
			+

			"The recommendations are intended for general application within\n"
			+ "computer and communications systems, and as such include a fair\n"
			+ "amount of flexibility. They are particularly intended for the\n"
			+ "protection of sensitive information such as private keys, as in PKCS\n"
			+ "#8 [25]. It is expected that application standards and implementation\n"
			+ "profiles based on these specifications may include additional\n" + "constraints.\n" +

			"Other cryptographic techniques based on passwords, such as password-\n"
			+ "based key entity authentication and key establishment protocols\n"
			+ "[4][5][26] are outside the scope of this document.  Guidelines for\n"
			+ "the selection of passwords are also outside the scope.	";

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser
				.parse(new FileReader(new File("./src/test/java/com/mifiel/crypto/fixturePKCS5.json")));
		valid = (JSONArray) jsonObject.get("valid");
		invalid = (JSONArray) jsonObject.get("invalid");
		docCrypto = new DocumentCrypto();
	}

	@Test
	public void testDecrypt() throws DecoderException {
		byte[] res = null;
		for (Object o : valid) {
			JSONObject jsonObject = (JSONObject) o;
			String pkcs = (String) jsonObject.get("pkcs5");
			String password = (String) jsonObject.get("password");
			byte[] pkcs5Bytes = Hex.decode(pkcs);
			try {
				docCrypto.setPkcs5Bytes(pkcs5Bytes);
				res = docCrypto.decryptDocument(password);
			} catch (Exception e) {
				e.printStackTrace();
				fail("Exception NOT expected.");
			}
			assertTrue(decrypted.equals(new String(res)));
		}
	}

	@Test
	public void testEncrypt() throws DecoderException {
		byte[] res = null;
		byte[] docEncrypt = "Documento cifrado".getBytes(); 
		try {
			String pass = docCrypto.encryptDocument(docEncrypt);
			res = docCrypto.getPkcs5Bytes(); 
			docCrypto.setPkcs5Bytes(res);
			res = docCrypto.decryptDocument(pass);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception NOT expected.");
		}
		assertTrue(new String(docEncrypt).equals(new String(res)));
	}
	
	@Test
	public void testNotPKCS5(){
		byte[] res = null;
		JSONObject jsonObject = (JSONObject) invalid.get(0);
		String pkcs = (String) jsonObject.get("pkcs5");
		byte[] pkcs5Bytes = Hex.decode(pkcs);
		
		try {
			docCrypto.setPkcs5Bytes(pkcs5Bytes);
			res = docCrypto.decryptDocument("");
			fail("Exception expected.");
		} catch (Exception e) {
			assertEquals("Could not read PKCS5:Exception decoding bytes: Bytes are not PKCS5", e.getMessage());
		}
	}
	@Test
	public void testNotAES(){
		byte[] res = null;
		JSONObject jsonObject = (JSONObject) invalid.get(1);
		String pkcs = (String) jsonObject.get("pkcs5");
		byte[] pkcs5Bytes = Hex.decode(pkcs);
		
		try {
			docCrypto.setPkcs5Bytes(pkcs5Bytes);
			res = docCrypto.decryptDocument("");
			fail("Exception expected.");
		} catch (Exception e) {
			assertEquals("Could not read PKCS5:Encryption algorithm not supported", e.getMessage());
		}
	}
	@Test
	public void testNotSHA256(){
		byte[] res = null;
		JSONObject jsonObject = (JSONObject) invalid.get(2);
		String pkcs = (String) jsonObject.get("pkcs5");
		byte[] pkcs5Bytes = Hex.decode(pkcs);
		
		try {
			docCrypto.setPkcs5Bytes(pkcs5Bytes);
			res = docCrypto.decryptDocument("");
			fail("Exception expected.");
		} catch (Exception e) {
			assertEquals("Could not read PKCS5:Digest algorithm not supported", e.getMessage());
		}
	}	
}
