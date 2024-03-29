package com.mifiel.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Document;
import com.mifiel.api.objects.Signature;
import com.mifiel.api.objects.Viewer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MifielUtils {

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
            entityBuilder.addTextBody(paramName, paramValue);
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

    public static HttpEntity buildHttpBody(final Document document) throws MifielException {

        final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        final String filePath = document.getFile();
        final String fileName = document.getFileName();
        final String originalHash = document.getOriginalHash();
        final String callbackUrl = document.getCallbackUrl();

        if (!document.getAdditionalProperties().isEmpty()) {
            for (Map.Entry<String, Object> entry : document.getAdditionalProperties().entrySet()) {
                MifielUtils.appendTextParamToHttpBody(entityBuilder, entry.getKey(), entry.getValue().toString());
            }
        }

        addSignaturesAndViewers(entityBuilder, document.getSignatures(), document.getViewers());

        if (callbackUrl != null) {
            MifielUtils.appendTextParamToHttpBody(entityBuilder, "callback_url", callbackUrl);
        }

        if (!StringUtils.isEmpty(filePath)) {
            final File pdfFile = new File(filePath);
            entityBuilder.addBinaryBody("file", pdfFile, ContentType.create(MifielUtils.PDF_CONTENT_TYPE),
                    pdfFile.getName());
        } else if (!StringUtils.isEmpty(originalHash) && !StringUtils.isEmpty(fileName)) {
            entityBuilder.addTextBody("original_hash", originalHash);
            entityBuilder.addTextBody("name", fileName);
        } else {
            throw new MifielException("You must provide file or original hash and file name");
        }

        return entityBuilder.build();
    }

    private static void addSignaturesAndViewers(MultipartEntityBuilder entityBuilder, List<Signature> signatures, List<Viewer> viewers) {
        for (int i = 0; i < signatures.size(); i++) {
            MifielUtils.appendTextParamToHttpBody(entityBuilder, "signatories[" + i + "][name]",
                    signatures.get(i).getSignature());
            MifielUtils.appendTextParamToHttpBody(entityBuilder, "signatories[" + i + "][email]",
                    signatures.get(i).getEmail());
            MifielUtils.appendTextParamToHttpBody(entityBuilder, "signatories[" + i + "][tax_id]",
                    signatures.get(i).getTaxId());
            MifielUtils.appendTextParamToHttpBody(entityBuilder, "signatories[" + i + "][certificate_number]",
                    signatures.get(i).getCertificate_number());
            for (Map.Entry<String, Object> entry : signatures.get(i).getAdditionalProperties().entrySet()) {
                MifielUtils.appendTextParamToHttpBody(entityBuilder, "signatories[" + i + "][" + entry.getKey() + "]", entry.getValue().toString());
            }
        }

        for (int i = 0; i < viewers.size(); i++) {
            MifielUtils.appendTextParamToHttpBody(entityBuilder, "viewers[" + i + "][name]",
                    viewers.get(i).getName());
            MifielUtils.appendTextParamToHttpBody(entityBuilder, "viewers[" + i + "][email]",
                    viewers.get(i).getEmail());
            MifielUtils.appendTextParamToHttpBody(entityBuilder, "viewers[" + i + "][tax_id]",
                    viewers.get(i).getTaxId());
            MifielUtils.appendTextParamToHttpBody(entityBuilder, "viewers[" + i + "][certificate_number]",
                    viewers.get(i).getCertificate_number());
        }
    }

    public static byte[] readBytesFromFile(String filePath) throws MifielException {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException ex) {
            throw new MifielException(ex);
        }
    }
}
