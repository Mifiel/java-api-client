package com.mifiel.api.utils;

import com.mifiel.api.exception.MifielException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import java.io.IOException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jazavala
 */
public class JWTGeneratorTest {

    private static String appId = "AppId";
    private static JWTGenerator jwtWebToken;

    ClassLoader classLoader = getClass().getClassLoader();

    @BeforeClass
    public static void beforeClass() throws MifielException {
        jwtWebToken = new JWTGenerator(appId);
    }

    @Test
    public void testGenerateECKeyPair() throws JOSEException, IOException, Exception {
        String pathPrivKey = classLoader.getResource("my_ec").getPath();
        String pathPubKey = classLoader.getResource("my_ec.pub").getPath();
        
        ECPrivateKey privKey = (ECPrivateKey) PemUtils.readPrivateKeyFromFile(pathPrivKey, "EC");
        ECPublicKey pubKey = (ECPublicKey) PemUtils.readPublicKeyFromFile(pathPubKey, "EC");

        String subject = "mifiel.com";
        String issuer = "microservices.mifiel.com";
        String jwt = jwtWebToken.generateJWT(privKey, subject, issuer);

        assertTrue(!jwt.isEmpty());
        SignedJWT signedJWT = JWTGenerator.verify(jwt, pubKey);
 
        assertEquals(subject, signedJWT.getJWTClaimsSet().getSubject());
        assertEquals(issuer, signedJWT.getJWTClaimsSet().getIssuer());
        assertTrue(signedJWT.getJWTClaimsSet().getExpirationTime() != null);
        assertTrue(signedJWT.getHeader().getKeyID().equals(appId));
        
    }
}
