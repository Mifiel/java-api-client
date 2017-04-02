package com.mifiel.api;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mifiel.api.dao.Certificates;
import com.mifiel.api.dao.Documents;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Document;
import com.mifiel.api.utils.MifielUtils;

public class ApiClientTest {
	
	private static ApiClient apiClient;
	private static Documents docs;
	private static Certificates keys;
	private static String pdfFilePath;
	
	@BeforeClass
    public static void beforeClass() throws MifielException {
		final String appId = "585763293c61baf5ac9d3819e4610dc25e76cade";
        final String appSecret = "SyD1xoS4JkaPoDPbfTnUG2QQ20SIV+WWxdQIZPSiH1WrK2E6LrWhWIjGeHeuSfxtteNsgQZo+Xq+fdriJexG7g==";
		
        apiClient = new ApiClient(appId, appSecret);
        
        docs = new Documents(apiClient);
        keys = new Certificates(apiClient);
        
        pdfFilePath = "/home/enrique/Desktop/20170201-50147577.pdf";
    }

    @Test(expected=MifielException.class)
    public void testWrongUrlShouldThorwAnException() throws MifielException {
        apiClient.setUrl("www.google.com");
    }
    
    @Test
    public void testCorrectUrlShouldNotThorwAnException() throws MifielException {
        apiClient.setUrl("https://sandbox.mifiel.com");
    }
    
    @Test
    public void testGetAllDocumentsShouldReturnsAList() throws MifielException {
    	apiClient.setUrl("https://sandbox.mifiel.com");
        final List<Document> allDocuments = docs.findAll();
        assertTrue(allDocuments != null);
    }
    
    @Test
    public void testSaveADocumentWithFilePath() throws MifielException {
    	apiClient.setUrl("https://sandbox.mifiel.com");
        Document doc = new Document();
        doc.setFile(pdfFilePath);
        
        doc = docs.save(doc);
        assertTrue(doc != null);
    }
    
    @Test
    public void testSaveADocumentWithOriginalHashAndFileName() throws MifielException {
    	apiClient.setUrl("https://sandbox.mifiel.com");
        Document doc = new Document();
        doc.setOriginalHash(MifielUtils.getDocumentHash(pdfFilePath));
        doc.setFileName("20170201-50147577");
                
        doc = docs.save(doc);
        assertTrue(doc != null);
    }
    
    @Test(expected=MifielException.class)
    public void testSaveADocumentWithoutRequiredFieldsShouldThrowAnException() throws MifielException {
    	apiClient.setUrl("https://sandbox.mifiel.com");
        Document doc = new Document();
        doc.setCallbackUrl("http://www.google.com");
                
        docs.save(doc);
    }
}