package com.mifiel.api;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.mifiel.api.dao.Documents;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Document;
import com.mifiel.api.objects.Signature;
import com.mifiel.api.objects.SignatureResponse;
import com.mifiel.api.utils.DigestType;
import com.mifiel.api.utils.MifielUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DocumentsTest {

    private static ApiClient apiClient;
    private static Documents docs;
    private final String pdfFilePath;
    private final String mifielBase = "https://sandbox.mifiel.com";
    private static final String appId = "APP_ID"; // TODO: replace with your access token
    private static final String appSecret = "APP_SECRET"; // TODO: replace with your access token
    private static final String fileTest = "my_file.pdf";
    private final String path;

    ClassLoader classLoader = getClass().getClassLoader();

    @BeforeClass
    public static void beforeClass() throws MifielException {
        apiClient = new ApiClient(appId, appSecret);
        docs = new Documents(apiClient);
    }

    public DocumentsTest() {
        this.pdfFilePath = classLoader.getResource(fileTest).getFile();
        this.path = classLoader.getResource(fileTest).getPath().replace(fileTest, "");
    }

    @Test(expected = MifielException.class)
    public void testWrongUrlShouldThorwAnException() throws MifielException {
        apiClient.setUrl("www.google.com");
    }

    @Test
    public void testCorrectUrlShouldNotThorwAnException() throws MifielException {
        apiClient.setUrl("https://sandbox.mifiel.com");
    }

    @Test
    public void testGetAllDocumentsShouldReturnAList() throws MifielException {
        setSandboxUrl();
        final List<Document> allDocuments = docs.findAll();
        assertTrue(allDocuments != null);
    }

    @Test
    public void testGetAllDocumentsUsingHMACSHA256ShouldReturnAList() throws MifielException {
        apiClient = new ApiClient(appId, appSecret, DigestType.SHA256);
        docs = new Documents(apiClient);
        setSandboxUrl();
        final List<Document> allDocuments = docs.findAll();
        assertTrue(allDocuments != null);
    }

    @Test
    public void testSaveADocumentWithFilePath() throws MifielException {
        setSandboxUrl();
        Document doc = new Document();
        doc.setFile(pdfFilePath);

        List<Signature> signatures = new ArrayList<Signature>();

        Signature signature = new Signature();
        signature.setEmail("ja.zavala.aguilar@gmail.com");
        signature.setTaxId("ZAAJ8301061E0");
        signature.setSignature("Juan Antonio Zavala Aguilar");
        signatures.add(signature);
        doc.setSignatures(signatures);
        doc = docs.save(doc);
        assertTrue(doc != null);
    }

    @Test
    public void testSaveADocumentWithOriginalHashAndFileName() throws MifielException {
        setSandboxUrl();
        Document doc = new Document();
        doc.setOriginalHash(MifielUtils.getDocumentHash(pdfFilePath));
        doc.setFileName("20170201-50147577");
        doc = docs.save(doc);
        assertTrue(doc != null);
    }

    @Test(expected = MifielException.class)
    public void testSaveADocumentWithoutRequiredFieldsShouldThrowAnException() throws MifielException {
        setSandboxUrl();
        Document doc = new Document();
        doc.setCallbackUrl("http://www.google.com");

        docs.save(doc);
    }

    @Test
    public void testGetDocumentShouldReturnADocument() throws MifielException {
        setSandboxUrl();
        testSaveADocumentWithOriginalHashAndFileName();
        final List<Document> allDocuments = docs.findAll();
        if (allDocuments.size() > 0) {
            Document doc1 = docs.find(allDocuments.get(0).getId());
            assertTrue(doc1 != null);
        } else {
            throw new MifielException("No documents found");
        }
    }

    @Test
    public void testDeleteShouldRemoveADocument() throws MifielException {
        setSandboxUrl();

        Document doc = new Document();
        doc.setOriginalHash(MifielUtils.getDocumentHash(pdfFilePath));
        doc.setFileName("20170201-50147577");
        doc = docs.save(doc);

        docs.delete(doc.getId());
    }

    @Test
    public void testRequestSignatureShouldReturnASignatureResponse() throws MifielException {
        setSandboxUrl();

        Document doc = new Document();
        doc.setOriginalHash(MifielUtils.getDocumentHash(pdfFilePath));
        doc.setFileName("20170201-50147577");
        doc = docs.save(doc);

        SignatureResponse sig = docs.requestSignature(doc.getId(), "enrique@test.com", "enrique2@test.com");

        assertTrue(sig != null);
    }

    @Test
    public void testSaveFileShouldSaveFileOnSpecifiedPath() throws MifielException, IOException {
        setSandboxUrl();
        Document doc = new Document();
        doc.setFile(pdfFilePath);
        doc = docs.save(doc);

        String outputPath = this.path + doc.getId();
        docs.saveFile(doc.getId(), outputPath);

        assertTrue(Files.deleteIfExists(Paths.get(outputPath)));
    }

    private void setSandboxUrl() throws MifielException {
        apiClient.setUrl(mifielBase);
    }
}
