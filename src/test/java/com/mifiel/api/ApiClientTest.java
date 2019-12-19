/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mifiel.api;

import com.mifiel.api.exception.MifielException;
import com.mifiel.api.utils.DigestType;
import com.mifiel.api.utils.JWTGenerator;
import com.mifiel.api.utils.PemUtils;
import com.nimbusds.jose.JOSEException;
import java.io.IOException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author jazavala
 */
public class ApiClientTest {

    private static final String appId = "AppId";
    private static final String appSecret = "appSecret";
    private static ApiClient apiClient;

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    public void testUseDefaultDigestType() throws JOSEException, IOException, Exception {
        apiClient = new ApiClient(appId, appSecret);
        try {
            HttpEntity http = apiClient.get("some");
        } catch (MifielException e) {
            Header header = apiClient.getRequest().getHeaders("Authorization")[0];
            Assert.assertNotNull(header);
            assertTrue(!header.getValue().contains("APIAuth appId:"));
        }
    }

    @Test
    public void testUseSHA256() throws JOSEException, IOException, Exception {
        apiClient = new ApiClient(appId, appSecret, DigestType.SHA256);
        try {
            HttpEntity http = apiClient.get("some");
        } catch (MifielException e) {
            Header header = apiClient.getRequest().getHeaders("Authorization")[0];
            Assert.assertNotNull(header);
            assertTrue(header.getValue().contains("APIAuth-HMAC-SHA256"));
        }
    }
    
    @Test
    public void testUseJWTEC256() throws JOSEException, IOException, Exception {
        String pathPrivKey = classLoader.getResource("my_ec").getPath();
        ECPrivateKey ecPrivKey = (ECPrivateKey) PemUtils.readPrivateKeyFromFile(pathPrivKey, "EC");
        apiClient = new ApiClient(appId, ecPrivKey);
        
        try {
            HttpEntity http = apiClient.get("some");
        } catch (MifielException e) {
            Header header = apiClient.getRequest().getHeaders("Authorization")[0];
            Assert.assertNotNull(header);
            
            String pathPubKey = classLoader.getResource("my_ec.pub").getPath();
            ECPublicKey ecPubKey = (ECPublicKey) PemUtils.readPublicKeyFromFile(pathPubKey, "EC");
            
            Assert.assertNotNull(JWTGenerator.verify(header.getValue(), ecPubKey));
        }
    }
}
