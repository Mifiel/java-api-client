package com.mifiel.api.utils;

import com.mifiel.api.exception.MifielException;
import static org.junit.Assert.assertTrue;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import org.junit.BeforeClass;

import org.junit.Test;

public class ECKeyPairBuilderTest {

    private static final String appId = "59f1acf3ae071415751e31481f15b84789b37da9";
    private static final String fileTest = "my_file.pdf";
    private final String path;
    private static ECKeyPairBuilder keyBuilder;

    ClassLoader classLoader = getClass().getClassLoader();

    @BeforeClass
    public static void beforeClass() throws MifielException {
        keyBuilder = new ECKeyPairBuilder(appId);
    }

    public ECKeyPairBuilderTest() {
        this.path = classLoader.getResource(fileTest).getPath().replace(fileTest, "");
    }

    @Test
    public void testGenerateECKeyPair() throws JOSEException, IOException, Exception {
        ECKey ecKey = keyBuilder.generateECKeyPair();

        Path pathPrivKey = Paths.get(path, "ec");
        Path pathPubKey = Paths.get(path, "ec.pub");
        
        assertTrue(ecKey.getCurve().equals(Curve.P_256));
        assertTrue(ecKey.getKeyID().equals(appId));

        keyBuilder.saveECPrivateKey(ecKey.toECPrivateKey(), pathPrivKey.toString());
        keyBuilder.saveECPublicKey(ecKey.toECPublicKey(), pathPubKey.toString());

        assertTrue(Files.deleteIfExists(pathPrivKey));
        assertTrue(Files.deleteIfExists(pathPubKey));
    }

    @Test
    public void testBuildECKeyPairFromFiles() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException, ParseException, MifielException {
        String pathFixturePrivKey = Paths.get(this.path, "my_ec").toString();
        String pathFixturePubKey = Paths.get(this.path, "my_ec.pub").toString();
 
        ECPrivateKey privKey = (ECPrivateKey) PemUtils.readPrivateKeyFromFile(pathFixturePrivKey, "EC");
        ECPublicKey pubKey = (ECPublicKey) PemUtils.readPublicKeyFromFile(pathFixturePubKey, "EC");

        assertTrue(privKey.getAlgorithm().equals("EC"));
        assertTrue(pubKey.getAlgorithm().equals("EC"));  
    }
}
