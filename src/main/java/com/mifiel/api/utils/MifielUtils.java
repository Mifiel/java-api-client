package com.mifiel.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;


import com.mifiel.api.exception.MifielException;

public final class MifielUtils {
    public final static String PDF_CONTENT_TYPE = "application/pdf";
    private final static ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private final static ObjectMapper objectMapper = new ObjectMapper();

    private MifielUtils() {
    }

    public static String getDocumentHash(final String filePath) throws MifielException {
        try {
            byte[] fileContent = IOUtils.toByteArray(new FileInputStream(filePath));
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(fileContent);
            return new String(Hex.encodeHex(digest.digest()));
        } catch (final Exception e) {
            throw new MifielException("Error calculating Hash(SHA-256)", e);
        }
    }

    public static String calculateHMAC(final String secret, final String data, final String algorithm)
            throws MifielException {
        try {
            final SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), algorithm);
            final Mac mac = Mac.getInstance(algorithm);
            mac.init(signingKey);
            final byte[] rawHmac = mac.doFinal(data.getBytes());
            return new String(Base64.encodeBase64(rawHmac));
        } catch (final GeneralSecurityException e) {
            throw new MifielException("Error calculating HMAC", e);
        }
    }

    public static String calculateMD5(final String content) throws MifielException {
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return new String(Base64.encodeBase64(digest.digest()));
        } catch (final NoSuchAlgorithmException e) {
            throw new MifielException("Error calculating MD5", e);
        }
    }

    public static String convertObjectToJson(final Object object) throws MifielException {
        try {
            return objectWriter.writeValueAsString(object);
        } catch (final Exception e) {
            throw new MifielException("Error converting object to JSON", e);
        }
    }

    public static Object convertJsonToObject(final String json, final String className) throws MifielException {
        try {
            return objectMapper.readValue(json, Class.forName(className));
        } catch (final Exception e) {
            throw new MifielException("Error converting JSON to Object", e);
        }
    }

    public static List<Object> convertJsonToObjects(final String json, final String className) throws MifielException {
        try {
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Class.forName(className)));
        } catch (final Exception e) {
            throw new MifielException("Error converting JSON to List<Object>", e);
        }
    }

    public static void appendTextParamToHttpBody(final MultipartEntityBuilder entityBuilder, final String paramName,
            final String paramValue) {
        if (!StringUtils.isEmpty(paramValue)) {
			entityBuilder.addTextBody(paramName,paramValue,ContentType.APPLICATION_JSON);
        }
    }

    public static boolean isSuccessfulHttpCode(final int httpStatusCode) {
        return httpStatusCode >= 200 && httpStatusCode < 300;
    }

    public static String entityToString(final HttpEntity entity) throws MifielException {
        try {
            return EntityUtils.toString(entity);
        } catch (final Exception e) {
            throw new MifielException("Error converting Entity to String", e);
        }
    }

    public static void saveEntityResponseToFile(final HttpEntity entityResponse, final String localPath)
            throws MifielException {
        try {
            final byte[] fileContent = EntityUtils.toByteArray(entityResponse);
            FileUtils.writeByteArrayToFile(new File(localPath), fileContent);
        } catch (final Exception e) {
            throw new MifielException("Error saving file", e);
        }
    }
}
