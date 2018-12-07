package com.mifiel.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Aes {
	   
	static final String ALGORITHM = "AES/CBC/PKCS5PADDING";
	static final int IV_LENGTH = 16;

	public static byte[] encrypt(byte[] keyByte, byte[] data, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		return encryptDecrypt(keyByte, data, iv, Cipher.ENCRYPT_MODE);
	}
	
	private static byte[] encryptDecrypt(byte[] keyByte, byte[] data, byte[] iv, int mode) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
		IvParameterSpec ivParameter = new IvParameterSpec(iv);
		SecretKey key = new SecretKeySpec(keyByte, 0, keyByte.length, "AES"); 
		Cipher cipher = Cipher.getInstance(ALGORITHM, new BouncyCastleProvider());
		cipher.init(mode, key, ivParameter);
		return cipher.doFinal(data);
	}
	
	public static byte[] decrypt(byte[] keyByte, byte[] encryptedData, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		return encryptDecrypt(keyByte, encryptedData, iv, Cipher.DECRYPT_MODE);
	}

	public static byte[] iv() {
		SecureRandom random = new SecureRandom();
		byte[] iv = new byte[IV_LENGTH];
		random.nextBytes(iv);
		return iv;
	}

}
