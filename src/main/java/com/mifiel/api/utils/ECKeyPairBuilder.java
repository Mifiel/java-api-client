/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mifiel.api.utils;

import com.mifiel.api.exception.MifielException;
import com.nimbusds.jose.JOSEException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author jazavala
 */
public class ECKeyPairBuilder {

    private final String keyID;
    private final String BEGIN_EC_PRIVATE_KEY = "-----BEGIN EC PRIVATE KEY-----\n";
    private final String END_EC_PRIVATE_KEY = "-----END EC PRIVATE KEY-----\n";
    private final String BEGIN_EC_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n";
    private final String END_EC_PUBLIC_KEY = "-----END PUBLIC KEY-----\n";

    public ECKeyPairBuilder(String keyID) {
        this.keyID = keyID;
    }

    public ECKey generateECKeyPair() throws MifielException {
        try {
            return new ECKeyGenerator(Curve.P_256)
                    .keyID(keyID)
                    .generate();
        } catch (JOSEException ex) {
            throw new MifielException(ex);
        }
    }

    public void saveECPrivateKey(final ECPrivateKey ecKey, final String pathToSave) throws MifielException {
        saveKey(ecKey.getEncoded(), BEGIN_EC_PRIVATE_KEY, END_EC_PRIVATE_KEY, pathToSave);

    }

    public void saveECPublicKey(final ECPublicKey ecKey, final String pathToSave) throws MifielException {
        saveKey(ecKey.getEncoded(), BEGIN_EC_PUBLIC_KEY, END_EC_PUBLIC_KEY, pathToSave);
    }

    private void saveKey(final byte[] encoded, final String begin, final String end, final String pathToSave) throws MifielException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(pathToSave);
            os.write(begin.getBytes("US-ASCII"));
            os.write(Base64.encodeBase64(encoded, true));
            os.write(end.getBytes("US-ASCII"));
            os.close();
        } catch (FileNotFoundException ex) {
            throw new MifielException(ex);
        } catch (UnsupportedEncodingException ex) {
            throw new MifielException(ex);
        } catch (IOException ex) {
            throw new MifielException(ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                throw new MifielException(ex);
            }
        }
    }
}
