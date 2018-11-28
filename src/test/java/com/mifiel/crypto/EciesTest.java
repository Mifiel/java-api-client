package com.mifiel.crypto;

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

public class EciesTest {
	private static JSONArray valid;
	private static Ecies ecies;

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException, IOException, ParseException {

		ecies = new Ecies();
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser
				.parse(new FileReader(new File("./src/test/java/com/mifiel/crypto/fixtureEcies.json")));
		valid = (JSONArray) jsonObject.get("valid");
	}

	@Test
	public void testEncryptValidValues() {
		byte[] res = null;

		JSONObject jsonObject = (JSONObject) valid.get(0);
		String publicKey = (String) jsonObject.get("publicKey");
		String privateKey = (String) jsonObject.get("privateKey");

		String test = "Test de cifrado";
		byte[] encrypt;
		
		try {
			ecies.createIV();
			encrypt = ecies.encrypt(Hex.decodeHex(publicKey.toCharArray()), test.getBytes());
			res = ecies.decrypt(Hex.decodeHex(privateKey.toCharArray()), encrypt);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception NOT expected.");
		}
		assertTrue(test.equals(new String(res)));

	}

	@Test
	public void testDecryptValidValues() {
		byte[] res = null;

		for (Object o : valid) {

			JSONObject jsonObject = (JSONObject) o;
			String privateKey = (String) jsonObject.get("privateKey");
			String iv = (String) jsonObject.get("iv");
			String ephemPublicKey = (String) jsonObject.get("ephemPublicKey");
			String ciphertext = (String) jsonObject.get("ciphertext");
			String mac = (String) jsonObject.get("mac");
			String decrypted = (String) jsonObject.get("decrypted");

			try {
				ecies.setIV(Hex.decodeHex(iv.toCharArray()));
				res = ecies.decrypt(Hex.decodeHex(privateKey.toCharArray()),
						Hex.decodeHex((ephemPublicKey + ciphertext + mac).toCharArray()));
			} catch (Exception e) {
				e.printStackTrace();
				fail("Exception NOT expected.");
			}

			assertTrue(decrypted.equals(new String(res)));

		}
	}
}
