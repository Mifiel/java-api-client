package com.mifiel.api.dao;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.mifiel.api.ApiClient;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Document;
import com.mifiel.api.objects.Signature;
import com.mifiel.api.objects.SignatureResponse;
import com.mifiel.api.utils.MifielUtils;

public class Documents extends BaseObjectDAO<Document> {
    
    private final String DOCUMENT_CANONICAL_NAME = Document.class.getCanonicalName();
    private final String SIGNATURE_RESPONSE_CANONICAL_NAME = SignatureResponse.class.getCanonicalName();
    private final String DOCUMENTS_PATH = "documents";

    public Documents(final ApiClient apiClient) {
        super(apiClient);
    }

    @Override
    public Document find(final String id) throws MifielException {
    	final HttpEntity entityResponse = apiClient.get(DOCUMENTS_PATH + "/" + id);
        final String response = MifielUtils.entityToString(entityResponse);
        return (Document) MifielUtils.convertJsonToObject(response, DOCUMENT_CANONICAL_NAME);
    }

    @Override
    public List<Document> findAll() throws MifielException {
    	final HttpEntity entityResponse = apiClient.get(DOCUMENTS_PATH);
        final String response = MifielUtils.entityToString(entityResponse);
        return (List<Document>)(Object)MifielUtils.convertJsonToObjects(response, DOCUMENT_CANONICAL_NAME);
    }   

    @Override
    public void delete(final String id) throws MifielException {
        apiClient.delete(DOCUMENTS_PATH + "/" + id);
    }
    
    public void saveFile(final String id, final String localPath) throws MifielException {
        final HttpEntity entityResponse = apiClient.get(DOCUMENTS_PATH + "/" + id + "/file");		
		MifielUtils.saveEntityResponseToFile(entityResponse, localPath);
    }
    
    public void saveXml(final String id, final String localPath) throws MifielException {
		final HttpEntity entityResponse = apiClient.get(DOCUMENTS_PATH + "/" + id + "/xml");
		MifielUtils.saveEntityResponseToFile(entityResponse, localPath);
    }

    @Override
    public Document save(final Document document) throws MifielException {
        if (StringUtils.isEmpty(document.getId())) {
        	final HttpEntity httpContent = buildHttpBody(document);
        	final HttpEntity entityResponse = apiClient.post(DOCUMENTS_PATH, httpContent);
            final String response = MifielUtils.entityToString(entityResponse);
            return (Document)MifielUtils.convertJsonToObject(response, DOCUMENT_CANONICAL_NAME);
        } else {
        	final String json = MifielUtils.convertObjectToJson(document);
        	
        	StringEntity httpContent;
			try {
				httpContent = new StringEntity(json);
			} catch (UnsupportedEncodingException e) {
				throw new MifielException("Error creating Http Body for PUT verb", e);
			}
        	
			final HttpEntity entityResponse = apiClient.put(DOCUMENTS_PATH, httpContent);
            final String response = MifielUtils.entityToString(entityResponse);
            return (Document)MifielUtils.convertJsonToObject(response, DOCUMENT_CANONICAL_NAME);
        }
    }
    
    public SignatureResponse requestSignature(final String id, final String email, 
                                                final String cc) throws MifielException {
        final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.addTextBody("email", email);
        entityBuilder.addTextBody("cc", cc);
        
        final HttpEntity entityResponse = apiClient.post(DOCUMENTS_PATH + "/" + id + "/request_signature", entityBuilder.build());
        final String response = MifielUtils.entityToString(entityResponse);
        final SignatureResponse signatureResponse = (SignatureResponse) MifielUtils.convertJsonToObject(response, SIGNATURE_RESPONSE_CANONICAL_NAME);
        return signatureResponse;
    }

    private HttpEntity buildHttpBody(final Document document) throws MifielException {
        
        final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        
        final List<Signature> signatures = document.getSignatures();
        final String filePath = document.getFile();
        final String fileName = document.getFileName();
        final String originalHash = document.getOriginalHash();
        
        if (!StringUtils.isEmpty(filePath)) {
            final File pdfFile = new File(filePath);
            
            entityBuilder.addBinaryBody(
                "file",
                pdfFile,
                ContentType.create(MifielUtils.PDF_CONTENT_TYPE),
                pdfFile.getName()
            );
        } else if (!StringUtils.isEmpty(originalHash) && !StringUtils.isEmpty(fileName)) {
            entityBuilder.addTextBody("original_hash", originalHash);
            entityBuilder.addTextBody("name", fileName);
            
            MifielUtils.appendTextParamToHttpBody(entityBuilder, 
                    "callback_url", document.getCallbackUrl());
            
            if (signatures != null) {
                for (int i = 0; i < signatures.size(); i++) {
                    MifielUtils.appendTextParamToHttpBody(entityBuilder, 
                            "signatories[" + i + "][name]", signatures.get(i).getSignature());
                    MifielUtils.appendTextParamToHttpBody(entityBuilder, 
                            "signatories[" + i + "][email]", signatures.get(i).getEmail());
                    MifielUtils.appendTextParamToHttpBody(entityBuilder, 
                            "signatories[" + i + "][tax_id]", signatures.get(i).getTaxId());
                }
            }
        } else {
            throw new MifielException("You must provide file or original hash and file name");
        }
        
        return entityBuilder.build();
    }

}