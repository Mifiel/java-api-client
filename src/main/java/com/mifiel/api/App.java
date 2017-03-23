package com.mifiel.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mifiel.api.dao.Certificates;
import com.mifiel.api.dao.Documents;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Certificate;
import com.mifiel.api.objects.Data;
import com.mifiel.api.objects.Document;
import com.mifiel.api.objects.Owner;
import com.mifiel.api.objects.Signature;
import com.mifiel.api.utils.MifielUtils;

//TODO: Change to java 1.6
public class App {
    public static void main( String[] args ) throws MifielException {
        final String appId = "1de27e16406cf9b485a1cd9b19a51015049fc245";
        final String appSecret = "ul2NEPJyNKD/3MILk5K2IKFb45I7dsZvC21DkvOkNDyhVM0SzBUeeXsDloGoWoIoVBLpayIzTEvRg50YDvI5/A==";
        
        System.out.println("appId = " + appId);
        ApiClient apiClient = new ApiClient(appId, appSecret);
        apiClient.setUrl("https://sandbox.mifiel.com/");
        
        Documents docs = new Documents(apiClient);
        Document doc = new Document();
        Signature signature = new Signature();
        signature.setEmail("test@test.com");
        List<Signature> signatures = new ArrayList<Signature>();
        signatures.add(signature);
        doc.setSignatures(signatures);
        //doc.setFile("/home/enrique/Desktop/20170201-50147577.pdf");
        doc.setFileFileName("20170201-50147577.pdf");
        doc.setOriginalHash(MifielUtils.getDocumentHash("/home/enrique/Desktop/20170201-50147577.pdf"));
        
        try {
        	//List<Document> docList = docs.findAll();
        	//for (Document document : docList) {
			//	docs.delete(document.getId());
			//}
        	
        	docs.save(doc);
        } catch (MifielException e) {
        	System.out.println(e.getMifielError());
        }
        //List<Document> doc = docs.findAll();
        //docs.find("1");
        
        //System.out.println(doc);
        
        //Document doc = new Document();
        //doc.setFile("123");
        //Owner owner = new Owner();
        //owner.setEmail("asd@asd.com");
        //doc.setOwner(owner);
        
        //String json = MifielUtils.convertObjectToJson(doc);
        //System.out.println(json);
        
        //Document doc2 = (Document) MifielUtils.convertJsonToObject(json, Document.class.getCanonicalName());
        //System.out.println(doc2);
    }
}
