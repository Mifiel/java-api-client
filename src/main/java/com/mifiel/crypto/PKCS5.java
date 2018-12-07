package com.mifiel.crypto;

import java.io.IOException;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.EncryptionScheme;
import org.bouncycastle.asn1.pkcs.KeyDerivationFunc;
import org.bouncycastle.asn1.pkcs.PBES2Parameters;
import org.bouncycastle.asn1.pkcs.PBKDF2Params;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

import com.mifiel.api.exception.MifielException;

public class PKCS5 {

	byte[] salt;
	int iterations;
	byte[] iv;
	byte[] encrypted;
	int sizeKey;

	private KeyDerivationFunc generatePkbdAlgorithmIdentifier(byte[] pbkdSalt, int iterations) {
		return new KeyDerivationFunc(PKCSObjectIdentifiers.id_PBKDF2, new PBKDF2Params(pbkdSalt, iterations,
				new AlgorithmIdentifier(PKCSObjectIdentifiers.id_hmacWithSHA256)));
	}

	public byte[] create() throws IOException, MifielException {
		validateData();
		KeyDerivationFunc pbkdAlgId = generatePkbdAlgorithmIdentifier(salt, iterations);
		ASN1ObjectIdentifier id = null;
		switch (sizeKey) {
		case 16:
			id = NISTObjectIdentifiers.id_aes128_CBC;
			break;
		case 24:
			id = NISTObjectIdentifiers.id_aes192_CBC;
			break;
		case 32:
			id = NISTObjectIdentifiers.id_aes256_CBC;
			break;
		default:
			throw new MifielException("Key length not 128/192/256 bits");
		}

		DEROctetString iv_Octet = new DEROctetString(iv);

		PBES2Parameters pbeParams = new PBES2Parameters(pbkdAlgId,
				new EncryptionScheme(id, ASN1Primitive.fromByteArray(iv_Octet.getEncoded())));
		EncryptedPrivateKeyInfo keyInfo = new EncryptedPrivateKeyInfo(
				new AlgorithmIdentifier(PKCSObjectIdentifiers.id_PBES2, pbeParams), encrypted);
		return keyInfo.getEncoded();
	}

	private void validateData() throws MifielException {
		if (salt == null) {
			throw new MifielException("SALT cannot be null");
		}
		if (iv == null) {
			throw new MifielException("IV cannot be null");
		}
		if (encrypted == null) {
			throw new MifielException("Data ENCRYPTED cannot be null");
		}
	}

	public void read(byte[] pkcs5Bytes) throws MifielException {
		EncryptedPrivateKeyInfo encPkInfo = null;
		try {
			encPkInfo = EncryptedPrivateKeyInfo.getInstance(pkcs5Bytes);
		} catch (Exception e1) {
			throw new MifielException("Exception decoding bytes: Bytes are not PKCS5");
		}
		encrypted = encPkInfo.getEncryptedData();
		PBES2Parameters alg = PBES2Parameters.getInstance(encPkInfo.getEncryptionAlgorithm().getParameters());
		PBKDF2Params func = PBKDF2Params.getInstance(alg.getKeyDerivationFunc().getParameters());
		EncryptionScheme scheme = alg.getEncryptionScheme();

		if (!PKCSObjectIdentifiers.id_hmacWithSHA256.equals(func.getPrf().getAlgorithm())) {
			throw new MifielException("Digest algorithm not supported");
		}

		if (!(NISTObjectIdentifiers.id_aes128_CBC.equals(scheme.getAlgorithm())
				|| NISTObjectIdentifiers.id_aes192_CBC.equals(scheme.getAlgorithm())
				|| NISTObjectIdentifiers.id_aes256_CBC.equals(scheme.getAlgorithm()))) {
			throw new MifielException("Encryption algorithm not supported");
		}
		
		setSizeKey(scheme.getAlgorithm());
				
		iterations = func.getIterationCount().intValue();
		salt = func.getSalt();
		iv = ASN1OctetString.getInstance(scheme.getParameters()).getOctets();
	}

	private void setSizeKey(ASN1ObjectIdentifier algorithm) {
		if(NISTObjectIdentifiers.id_aes128_CBC.equals(algorithm)){
			sizeKey = 16;
		}
		if(NISTObjectIdentifiers.id_aes192_CBC.equals(algorithm)){
			sizeKey = 24;
		}
		if(NISTObjectIdentifiers.id_aes256_CBC.equals(algorithm)){
			sizeKey = 32;
		}
	}

}
