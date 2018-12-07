package com.mifiel.crypto;

import com.mifiel.api.exception.MifielException;

public class DocumentCrypto {

	private static final int AES_KEY_SIZE = 24;
	private byte[] pkcs5Bytes = null;

	public String encryptDocument(byte[] document) throws MifielException  {

		String password;
		PKCS5 pkcs5 = new PKCS5();
		Pbe a = new Pbe();
		pkcs5.salt = a.getSalt();
		password = a.randomPassword();
		byte[] secretKey;
		try {
			secretKey = a.derivedKey(password, pkcs5.salt , AES_KEY_SIZE);
		} catch (Exception e) {
			throw new MifielException("Could not generate derived key:" + e.getMessage());
		} 

		pkcs5.iv = Aes.iv();
		try {
			pkcs5.encrypted = Aes.encrypt(secretKey, document, pkcs5.iv);
		} catch (Exception e) {
			throw new MifielException("Could not ENCRYPT:" + e.getMessage());
		}
		pkcs5.sizeKey = secretKey.length;
		pkcs5.iterations = Pbe.ITERATIONS;
		try {
			pkcs5Bytes = pkcs5.create();
		} catch (Exception e) {
			throw new MifielException("Could not create PKCS5:" + e.getMessage());
		}
		
		return password;
	}

	public byte[] decryptDocument(String password) throws MifielException {
		PKCS5 pkcs5 = new PKCS5();
		try {
			pkcs5.read(pkcs5Bytes);
		} catch (Exception e) {
			throw new MifielException("Could not read PKCS5:" + e.getMessage());
		}
		
		Pbe a = new Pbe();
		byte[] secretKey;
		try {
			secretKey = a.derivedKey(password, pkcs5.salt, pkcs5.sizeKey, pkcs5.iterations);
		} catch (Exception e) {
			throw new MifielException("Could not generate derived key:" + e.getMessage());
		}
		
		byte[] decrypted;
		try {
			decrypted = Aes.decrypt(secretKey, pkcs5.encrypted, pkcs5.iv);
		} catch (Exception e) {
			throw new MifielException("Could not DECRYPT:" + e.getMessage());
		}
		return decrypted;
	}

	public byte[] getPkcs5Bytes() {
		return pkcs5Bytes;
	}

	public void setPkcs5Bytes(byte[] pkcs5Bytes) {
		this.pkcs5Bytes = pkcs5Bytes;
	}

	


}
