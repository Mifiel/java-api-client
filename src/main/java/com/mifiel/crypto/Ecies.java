package com.mifiel.crypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.IESParameters;
import org.bouncycastle.crypto.params.IESWithCipherParameters;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.parsers.ECIESPublicKeyParser;

public class Ecies {

	public static final ECDomainParameters CURVE;
	private static final int MAC_KEY_BITS = 256;
	private static final int CIPHER_KEY_BITS = 256;
	private static final String CURVE_NAME = "secp256k1";
	private byte[] IV = null;
	
	static {
		X9ECParameters params = SECNamedCurves.getByName(CURVE_NAME);
		CURVE = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
	}

	private BasicAgreement agreement;
	private DerivationFunction kdf;
	private Mac mac;
	private PaddedBufferedBlockCipher buff;
	private IESParameters p;

	
	public Ecies() {
		this.agreement = (BasicAgreement) new ECDHBasicAgreement();
		this.kdf = new SimpleKDFGenerator(new SHA512Digest());
		this.mac = new HMac(new SHA256Digest());
		AESEngine aesEngine = new AESEngine();
		this.buff = new PaddedBufferedBlockCipher(new CBCBlockCipher((BlockCipher) aesEngine));
		p = new IESWithCipherParameters(new byte[] {}, new byte[] {}, MAC_KEY_BITS, CIPHER_KEY_BITS);
	}

	public byte[] encrypt(byte[] pubKey, byte[] plaintext) throws CryptoException, IOException {

		InputStream in = new ByteArrayInputStream(pubKey);
		ECIESPublicKeyParser a = new ECIESPublicKeyParser(CURVE);
		ECPublicKeyParameters publicKey = (ECPublicKeyParameters) a.readKey(in);
		
		ParametersWithIV parametersWithIV = new ParametersWithIV(p, this.getIV());

		EciesEngine engine = new EciesEngine(this.agreement, this.kdf, this.mac, this.buff, CURVE);
		engine.initEncryption(publicKey, parametersWithIV );
		return engine.processBlock(plaintext, 0, plaintext.length);
	}

	public byte[] decrypt(byte[] privateKey, byte[] cipher)
			throws InvalidCipherTextException, IOException {
		BigInteger prv = new BigInteger(1,privateKey);
		EciesEngine iesEngine = new EciesEngine(this.agreement, this.kdf, this.mac, this.buff, CURVE);
		ParametersWithIV parametersWithIV = new ParametersWithIV(p, this.getIV());
		iesEngine.initDecryption(new ECPrivateKeyParameters(prv, CURVE), parametersWithIV);
		return iesEngine.processBlock(cipher, 0, cipher.length);
	}

	public void createIV(){
		IV = Aes.iv();
	}
	
	public byte[] getIV() {
		return IV;
	}

	public void setIV(byte[] iV) {
		IV = iV;
	}

}
