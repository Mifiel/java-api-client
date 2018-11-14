package com.mifiel.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.lang.RandomStringUtils;

public class Pbe {

	static final int PASSWORD_LENGTH = 32;
	static final int SALT_SIZE = 16;
	static final int ITERATIONS = 1000;
	static final String ALGORITHM = "PBKDF2WithHmacSHA256";

	public String randomPassword() {
		return randomPassword(PASSWORD_LENGTH);
	}

	public String randomPassword(int lenght) {
		char[] characters = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_+=#&.*"))
				.toCharArray();
		return RandomStringUtils.random(lenght, 0, characters.length, false, false, characters, new SecureRandom());
	}

	public byte[] getSalt() {
		return getSalt(SALT_SIZE);
	}

	public byte[] getSalt(int size) {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[size];
		random.nextBytes(salt);
		return salt;
	}

	public byte[] derivedKey(String password, byte[] salt, int size) 
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException{
		return derivedKey(password, salt, size,  ITERATIONS);
	}
	
	public byte[] derivedKey(String password, byte[] salt, int size, int iterations)
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, size * 8);
		SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
		return f.generateSecret(spec).getEncoded();
	}

}
