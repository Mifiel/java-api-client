package com.mifiel.crypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.EphemeralKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.KeyEncoder;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.generators.EphemeralKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.IESParameters;
import org.bouncycastle.crypto.params.IESWithCipherParameters;
import org.bouncycastle.crypto.params.KDFParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.parsers.ECIESPublicKeyParser;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;


public class EciesEngine {
	ECDomainParameters parameters;
	BasicAgreement agree;
	DerivationFunction kdf;
	Mac mac;
	BufferedBlockCipher cipher;
	byte[] macBuf;
	boolean forEncryption;
	CipherParameters privParam, pubParam;
	IESParameters param;
	byte[] ephemeralKey;
	private EphemeralKeyPairGenerator keyPairGenerator;
	private byte[] IV;

	/**
	 * Set up for use in conjunction with a block cipher to handle the message.
	 * It is <b>strongly</b> recommended that the cipher is not in ECB mode.
	 *
	 * @param agree
	 *            the key agreement used as the basis for the encryption
	 * @param kdf
	 *            the key derivation function used for byte generation
	 * @param mac
	 *            the message authentication code generator for the message
	 * @param cipher
	 *            the cipher to used for encrypting the message
	 */
	public EciesEngine(BasicAgreement agree, DerivationFunction kdf, Mac mac, BufferedBlockCipher cipher,
			ECDomainParameters parameters) {

		this.agree = agree;
		this.kdf = kdf;
		this.mac = mac;
		this.macBuf = new byte[mac.getMacSize()];
		this.cipher = cipher;
		this.parameters = parameters;
	}

	/**
	 * Initialise the encryptor.
	 *
	 * @param privParam
	 *            our private key parameters
	 * @param params
	 *            encoding and derivation parameters, may be wrapped to include
	 *            an IV for an underlying block cipher.
	 */
	public void initDecryption(CipherParameters privParam, CipherParameters params) {
		this.forEncryption = false;
		this.privParam = privParam;
		this.ephemeralKey = new byte[0];

		extractParams(params);
	}

	/**
	 * Initialise the decryptor.
	 *
	 * @param publicKey
	 *            the recipient's/sender's public key parameters
	 * @param params
	 *            encoding and derivation parameters, may be wrapped to include
	 *            an IV for an underlying block cipher.
	 * @param ephemeralKeyPairGenerator
	 *            the ephemeral key pair generator to use.
	 */
	public void initEncryption(AsymmetricKeyParameter publicKey, CipherParameters params) {
		this.forEncryption = true;
		this.pubParam = publicKey;
		// this.keyPairGenerator = ephemeralKeyPairGenerator;

		ECKeyPairGenerator generator = new ECKeyPairGenerator();
		generator.init(new ECKeyGenerationParameters(this.parameters, new SecureRandom()));

		keyPairGenerator = new EphemeralKeyPairGenerator(generator, new KeyEncoder() {
			@Override
			public byte[] getEncoded(AsymmetricKeyParameter keyParameter) {
				if (!(keyParameter instanceof ECPublicKeyParameters))
					throw new IllegalArgumentException();
				return ((ECPublicKeyParameters) keyParameter).getQ().getEncoded(false);
			}
		});

		extractParams(params);
	}

	private void extractParams(CipherParameters params) {
		if (params instanceof ParametersWithIV) {
			this.IV = ((ParametersWithIV) params).getIV();
			this.param = (IESParameters) ((ParametersWithIV) params).getParameters();
		} else {
			this.IV = null;
			this.param = (IESParameters) params;
		}
	}

	public BufferedBlockCipher getCipher() {
		return cipher;
	}

	public Mac getMac() {
		return mac;
	}

	private byte[] encryptBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
		byte[] encrypt = null, K = null, cipherKey = null, hmacKey = null;
		int len;

		// Block cipher mode.
		cipherKey = new byte[((IESWithCipherParameters) param).getCipherKeySize() / 8];
		hmacKey = new byte[param.getMacKeySize() / 8];
		K = new byte[cipherKey.length + hmacKey.length];

		kdf.generateBytes(K, 0, K.length);
		System.arraycopy(K, 0, cipherKey, 0, cipherKey.length);
		System.arraycopy(K, cipherKey.length, hmacKey, 0, hmacKey.length);

		// If iv provided use it to initialise the cipher
		if (IV != null) {
			cipher.init(true, new ParametersWithIV(new KeyParameter(cipherKey), IV));
		} else {
			cipher.init(true, new KeyParameter(cipherKey));
		}

		encrypt = new byte[cipher.getOutputSize(inLen)];
		len = cipher.processBytes(in, inOff, inLen, encrypt, 0);
		len += cipher.doFinal(encrypt, len);

		// Apply the MAC.
		byte[] computedMAC = new byte[mac.getMacSize()];

		mac.init(new KeyParameter(hmacKey));
		if (IV != null) {
			mac.update(IV, 0, IV.length);
		}
		if (ephemeralKey.length != 0) {
			mac.update(ephemeralKey, 0, ephemeralKey.length);
		}
		mac.update(encrypt, 0, encrypt.length);

		mac.doFinal(computedMAC, 0);

		// Output the triple (V,C,T).
		byte[] Output = new byte[ephemeralKey.length + len + computedMAC.length];
		System.arraycopy(ephemeralKey, 0, Output, 0, ephemeralKey.length);
		System.arraycopy(encrypt, 0, Output, ephemeralKey.length, len);
		System.arraycopy(computedMAC, 0, Output, ephemeralKey.length + len, computedMAC.length);
		return Output;
	}

	private byte[] decryptBlock(byte[] in_enc, int inOff, int inLen) throws InvalidCipherTextException {
		byte[] decryptedBytes, K, cipherKey, hmacKey;
		int len = 0;

		// Ensure that the length of the input is greater than the MAC in bytes
		if (inLen < ephemeralKey.length + mac.getMacSize()) {
			throw new InvalidCipherTextException("Length of input must be greater than the MAC and V combined");
		}

		// Block cipher mode.
		cipherKey = new byte[((IESWithCipherParameters) param).getCipherKeySize() / 8];
		hmacKey = new byte[param.getMacKeySize() / 8];
		K = new byte[cipherKey.length + hmacKey.length];

		kdf.generateBytes(K, 0, K.length);
		System.arraycopy(K, 0, cipherKey, 0, cipherKey.length);
		System.arraycopy(K, cipherKey.length, hmacKey, 0, hmacKey.length);

		CipherParameters cp = new KeyParameter(cipherKey);

		// If IV provide use it to initialize the cipher
		if (IV != null) {
			cp = new ParametersWithIV(cp, IV);
		}

		cipher.init(false, cp);

		decryptedBytes = new byte[cipher.getOutputSize(inLen - ephemeralKey.length - mac.getMacSize())];

		// do initial processing
		len = cipher.processBytes(in_enc, inOff + ephemeralKey.length, inLen - ephemeralKey.length - mac.getMacSize(),
				decryptedBytes, 0);

		// Verify the MAC.
		int end = inOff + inLen;
		byte[] originMac = Arrays.copyOfRange(in_enc, end - mac.getMacSize(), end);

		byte[] computedMac = new byte[originMac.length];
		mac.init(new KeyParameter(hmacKey));

		if (IV != null) {
			mac.update(IV, 0, IV.length);
		}
		if (ephemeralKey.length != 0) {
			mac.update(ephemeralKey, 0, ephemeralKey.length);
		}
		mac.update(in_enc, inOff + ephemeralKey.length, inLen - ephemeralKey.length - computedMac.length);

		mac.doFinal(computedMac, 0);

		if (!Arrays.constantTimeAreEqual(originMac, computedMac)) {
			throw new InvalidCipherTextException("invalid MAC");
		}

		if (cipher == null) {
			return decryptedBytes;
		} else {
			len += cipher.doFinal(decryptedBytes, len);

			return Arrays.copyOfRange(decryptedBytes, 0, len);
		}
	}

	public byte[] processBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException, IOException {
		if (forEncryption) {
			if (keyPairGenerator != null) {
				EphemeralKeyPair ephKeyPair = keyPairGenerator.generate();

				this.privParam = ephKeyPair.getKeyPair().getPrivate();
				this.ephemeralKey = ephKeyPair.getEncodedPublicKey();
			}
		} else {
			ByteArrayInputStream bIn = new ByteArrayInputStream(in, inOff, inLen);
			ECIESPublicKeyParser a = new ECIESPublicKeyParser(parameters);

			try {
				this.pubParam = a.readKey(bIn);
			} catch (IOException e) {
				throw new InvalidCipherTextException("unable to recover ephemeral public key: " + e.getMessage(), e);
			} catch (IllegalArgumentException e) {
				throw new InvalidCipherTextException("unable to recover ephemeral public key: " + e.getMessage(), e);
			}
			int encLength = (inLen - bIn.available());
			this.ephemeralKey = Arrays.copyOfRange(in, inOff, inOff + encLength);

		}

		// Compute the common value and convert to byte array.
		agree.init(privParam);
		BigInteger z = agree.calculateAgreement(pubParam);

		byte[] Z = BigIntegers.asUnsignedByteArray(agree.getFieldSize(), z);

		// Initialize the KDF.
		KDFParameters kdfParam = new KDFParameters(Z, param.getDerivationV());
		kdf.init(kdfParam);

		return forEncryption ? encryptBlock(in, inOff, inLen) : decryptBlock(in, inOff, inLen);

	}

}
