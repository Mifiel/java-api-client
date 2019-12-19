package com.mifiel.api.utils;

import com.mifiel.api.exception.MifielException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.text.ParseException;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jazavala
 */
public class JWTGenerator {

    private final String appId;
    private final Date expirationTime= new Date(new Date().getTime() + 60 * 10000);
    
    public JWTGenerator(final String appId) {
        this.appId = appId;
    }
    
    public String generateJWT(final ECPrivateKey privKey) throws MifielException {
        return generateJWT(privKey, null, null, this.expirationTime, JWSAlgorithm.ES256);
    }
    
    public String generateJWT(final ECPrivateKey privKey, final String subject, final String issuer) throws MifielException {
        return generateJWT(privKey, subject, issuer, this.expirationTime, JWSAlgorithm.ES256);
    }
    
    public String generateJWT(final ECPrivateKey privKey, final String subject, final String issuer, final JWSAlgorithm jWSAlgorithm) throws MifielException {
        return generateJWT(privKey, subject, issuer, this.expirationTime, jWSAlgorithm);
    }
    
    public String generateJWT(final ECPrivateKey privKey, final String subject, final String issuer, final Date expirationTime, final JWSAlgorithm jWSAlgorithm) throws MifielException {
        try {
            JWSSigner signer = new ECDSASigner(privKey);
            
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(subject)
                    .issuer(issuer)
                    .expirationTime(expirationTime)
                    .build();
            
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(jWSAlgorithm).keyID(this.appId).build(),
                    claimsSet);
            
            signedJWT.sign(signer);
            
            return signedJWT.serialize();
        } catch (JOSEException ex) {
           throw new MifielException(ex);
        }
    }
    
    public static SignedJWT verify(final String jwt, ECPublicKey ecPublicJWK) throws MifielException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            
            JWSVerifier verifier = new ECDSAVerifier(ecPublicJWK);
            if (!signedJWT.verify(verifier)) {
                throw new MifielException("jwt authentication failed");
            }
            
            return signedJWT;
        } catch (ParseException ex) {
            throw new MifielException(ex);
        } catch (JOSEException ex) {
            throw new MifielException(ex);
        }
    }
}
