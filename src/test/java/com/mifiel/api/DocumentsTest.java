package com.mifiel.api;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mifiel.api.dao.Documents;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Document;
import com.mifiel.api.objects.SignatureResponse;
import com.mifiel.api.utils.MifielUtils;

public class DocumentsTest {

    private static ApiClient apiClient;
    private static Documents docs;
    private static String pdfFilePath;

    @BeforeClass
    public static void beforeClass() throws MifielException {
        final String appId = "585763293c61baf5ac9d3819e4610dc25e76cade";
        final String appSecret = "SyD1xoS4JkaPoDPbfTnUG2QQ20SIV+WWxdQIZPSiH1WrK2E6LrWhWIjGeHeuSfxtteNsgQZo+Xq+fdriJexG7g==";

        pdfFilePath = "/home/enrique/Desktop/20170201-50147577.pdf";
        apiClient = new ApiClient(appId, appSecret);
        docs = new Documents(apiClient);
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
    public void testSaveADocumentWithFilePath() throws MifielException {
        setSandboxUrl();
        Document doc = new Document();
        doc.setFile(pdfFilePath);

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
    public void testSaveFileShouldSaveFileOnSpecifiedPath() throws MifielException {
        setSandboxUrl();
        Document doc = new Document();
        doc.setFile(pdfFilePath);

        doc = docs.save(doc);

        docs.saveFile(doc.getId(), "/home/enrique/Desktop/test123.pdf");
    }

    private void setSandboxUrl() throws MifielException {
        apiClient.setUrl("https://sandbox.mifiel.com");
    }
}