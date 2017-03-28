package com.mifiel.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.mifiel.api.dao.Certificates;
import com.mifiel.api.dao.Documents;
import com.mifiel.api.dao.SignatureResponse;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Certificate;
import com.mifiel.api.objects.Data;
import com.mifiel.api.objects.Document;
import com.mifiel.api.objects.Owner;
import com.mifiel.api.objects.Signature;
import com.mifiel.api.utils.MifielUtils;

//TODO: Change to java 1.6
public class App {
    public static void main( String[] args ) throws MifielException, IOException {
        final String appId = "1de27e16406cf9b485a1cd9b19a51015049fc245";//"e35ed52c6571362d675095bf4b15fa0756c03099";//"1de27e16406cf9b485a1cd9b19a51015049fc245";
        final String appSecret = "ul2NEPJyNKD/3MILk5K2IKFb45I7dsZvC21DkvOkNDyhVM0SzBUeeXsDloGoWoIoVBLpayIzTEvRg50YDvI5/A==";//"dPG1uZhGghNRVDen4YWlUGhqWn7ZqEoW1L7nMzhtGOQr5HDy0u57NlO5ixHrA9K0Tw0azxCqQS5plSAkrrooag==";//"ul2NEPJyNKD/3MILk5K2IKFb45I7dsZvC21DkvOkNDyhVM0SzBUeeXsDloGoWoIoVBLpayIzTEvRg50YDvI5/A==";
        
        ApiClient apiClient = new ApiClient(appId, appSecret);
        apiClient.setUrl("https://sandbox.mifiel.com");//"https://cede3319.ngrok.io");//"https://sandbox.mifiel.com/");
        
        String documentPath = "/home/enrique/Desktop/test.cer";
        //Certificates cers = new Certificates(apiClient);
        //Certificate cer = new Certificate();
        //cer.setFile(documentPath);
        
        Documents docs = new Documents(apiClient);
        
        try {
        	List<Document> docList = docs.findAll();
        	if (docList.size() > 0) {
        		SignatureResponse reqSig = docs.requestSignature(docList.get(0).getId(), "enrique@test.com", "enrique2@test.com");
        		System.out.println(reqSig);
        	}
        	
        } catch (final MifielException e) {
        	System.out.println(e);
        	System.out.println(e.getMifielError());
        }
    }
}
