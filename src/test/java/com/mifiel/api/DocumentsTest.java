package com.mifiel.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;

import com.mifiel.api.dao.Documents;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Document;
import com.mifiel.api.objects.Signature;
import com.mifiel.api.objects.SignatureResponse;
import com.mifiel.api.utils.MifielUtils;

public class DocumentsTest {

    private static final String DOCUMENT_ID = "doc-123";
    private static final String FILE_TEST = "my_file.pdf";

    private FakeApiClient apiClient;
    private Documents docs;
    private String pdfFilePath;

    @Before
    public void setUp() {
        apiClient = new FakeApiClient();
        docs = new Documents(apiClient);
        pdfFilePath = getClass().getClassLoader().getResource(FILE_TEST).getFile();
    }

    @Test(expected = MifielException.class)
    public void testWrongUrlShouldThorwAnException() throws MifielException {
        new ApiClient("app-id", "app-secret").setUrl("www.google.com");
    }

    @Test
    public void testCorrectUrlShouldNotThorwAnException() throws MifielException {
        new ApiClient("app-id", "app-secret").setUrl("https://app-sandbox.mifiel.com");
    }

    @Test
    public void testGetAllDocumentsShouldReturnAList() throws Exception {
        apiClient.setNextResponse(jsonEntity("[{\"id\":\"" + DOCUMENT_ID + "\",\"name\":\"doc.pdf\"}]"));

        final List<Document> allDocuments = docs.findAll();

        assertNotNull(allDocuments);
        assertEquals(1, allDocuments.size());
        assertEquals(DOCUMENT_ID, allDocuments.get(0).getId());
        assertEquals("GET", apiClient.getLastMethod());
        assertEquals(Documents.DOCUMENTS_PATH, apiClient.getLastPath());
    }

    @Test
    public void testSaveADocumentWithFilePath() throws Exception {
        apiClient.setNextResponse(jsonEntity("{\"id\":\"" + DOCUMENT_ID + "\",\"name\":\"my_file.pdf\"}"));

        Document doc = new Document();
        doc.setFile(pdfFilePath);

        List<Signature> signatures = new ArrayList<Signature>();
        Signature signature = new Signature();
        signature.setEmail("signer@example.com");
        signature.setTaxId("ZAAJ8301061E0");
        signature.setSignature("Test Signer");
        signatures.add(signature);
        doc.setSignatures(signatures);

        doc = docs.save(doc);

        assertNotNull(doc);
        assertEquals(DOCUMENT_ID, doc.getId());
        assertEquals(1, doc.getSignatures().size());
        assertEquals("signer@example.com", doc.getSignatures().get(0).getEmail());
        assertEquals("POST", apiClient.getLastMethod());
        assertEquals(Documents.DOCUMENTS_PATH, apiClient.getLastPath());
        assertNotNull(apiClient.getLastBody());
    }

    @Test
    public void testSaveADocumentWithOriginalHashAndFileName() throws Exception {
        apiClient.setNextResponse(jsonEntity("{\"id\":\"" + DOCUMENT_ID + "\",\"name\":\"20170201-50147577\"}"));

        Document doc = new Document();
        doc.setOriginalHash(MifielUtils.getDocumentHash(pdfFilePath));
        doc.setFileName("20170201-50147577");
        doc = docs.save(doc);

        assertNotNull(doc);
        assertEquals(DOCUMENT_ID, doc.getId());
        assertEquals("POST", apiClient.getLastMethod());
        assertEquals(Documents.DOCUMENTS_PATH, apiClient.getLastPath());
    }

    @Test(expected = MifielException.class)
    public void testSaveADocumentWithoutRequiredFieldsShouldThrowAnException() throws Exception {
        Document doc = new Document();
        doc.setCallbackUrl("http://www.google.com");

        try {
            docs.save(doc);
        } finally {
            assertEquals(0, apiClient.getPostCount());
        }
    }

    @Test
    public void testGetDocumentShouldReturnADocument() throws Exception {
        apiClient.setNextResponse(jsonEntity("{\"id\":\"" + DOCUMENT_ID + "\",\"name\":\"doc.pdf\"}"));

        Document doc = docs.find(DOCUMENT_ID);

        assertNotNull(doc);
        assertEquals(DOCUMENT_ID, doc.getId());
        assertEquals("GET", apiClient.getLastMethod());
        assertEquals(Documents.DOCUMENTS_PATH + "/" + DOCUMENT_ID, apiClient.getLastPath());
    }

    @Test
    public void testDeleteShouldRemoveADocument() throws Exception {
        apiClient.setNextResponse(jsonEntity(""));

        docs.delete(DOCUMENT_ID);

        assertEquals("DELETE", apiClient.getLastMethod());
        assertEquals(Documents.DOCUMENTS_PATH + "/" + DOCUMENT_ID, apiClient.getLastPath());
    }

    @Test
    public void testRequestSignatureShouldReturnASignatureResponse() throws Exception {
        apiClient.setNextResponse(jsonEntity("{\"status\":\"success\",\"message\":\"Signature requested\"}"));

        SignatureResponse sig = docs.requestSignature(DOCUMENT_ID, "enrique@test.com", "enrique2@test.com");

        assertNotNull(sig);
        assertEquals("success", sig.getStatus());
        assertEquals("POST", apiClient.getLastMethod());
        assertEquals(Documents.DOCUMENTS_PATH + "/" + DOCUMENT_ID + "/request_signature", apiClient.getLastPath());
    }

    @Test
    public void testSaveFileShouldSaveFileOnSpecifiedPath() throws Exception {
        File output = File.createTempFile("mifiel-doc-", ".bin");
        output.deleteOnExit();

        apiClient.setNextResponse(new StringEntity("pdf-bytes", StandardCharsets.UTF_8));

        docs.saveFile(DOCUMENT_ID, output.getAbsolutePath());

        assertTrue(output.length() > 0);
        assertEquals("GET", apiClient.getLastMethod());
        assertEquals(Documents.DOCUMENTS_PATH + "/" + DOCUMENT_ID + "/file", apiClient.getLastPath());
    }

    private static HttpEntity jsonEntity(String json) throws Exception {
        return new StringEntity(json, StandardCharsets.UTF_8);
    }
}
