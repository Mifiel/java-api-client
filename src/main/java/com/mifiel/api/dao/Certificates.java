package com.mifiel.api.dao;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.mifiel.api.ApiClient;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Certificate;
import com.mifiel.api.utils.MifielUtils;

public class Certificates extends BaseObjectDAO<Certificate> {
    
    private final String CERTIFICATE_CANONICAL_NAME = Certificate.class.getCanonicalName();
    private final String CERTIFICATES_PATH = "keys";

    public Certificates(final ApiClient apiClient) {
        super(apiClient);
    }

    @Override
    public Certificate find(final String id) throws MifielException {
    	final HttpEntity entityResponse = apiClient.get(CERTIFICATES_PATH + "/" + id);
        final String response = MifielUtils.entityToString(entityResponse);
        return (Certificate) MifielUtils.convertJsonToObject(response, CERTIFICATE_CANONICAL_NAME);
    }

    @Override
    public List<Certificate> findAll() throws MifielException {
    	final HttpEntity entityResponse = apiClient.get(CERTIFICATES_PATH);
    	final String response = MifielUtils.entityToString(entityResponse);
        return (List<Certificate>)(Object)MifielUtils.convertJsonToObjects(response, CERTIFICATE_CANONICAL_NAME);
    }
    
    @Override
    public void delete(final String id) throws MifielException {
        apiClient.delete(CERTIFICATES_PATH + "/" + id);
    }

    @Override
    public Certificate save(final Certificate certificate) throws MifielException {
        final HttpEntity httpContent = buildHttpBody(certificate);
        final HttpEntity entityResponse = apiClient.post(CERTIFICATES_PATH, httpContent);
        final String response = MifielUtils.entityToString(entityResponse);
        return (Certificate)MifielUtils.convertJsonToObject(response, CERTIFICATE_CANONICAL_NAME);
    }
    
    private HttpEntity buildHttpBody(final Certificate certificate) 
                                        throws MifielException {

        final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        final String fileName = certificate.getFile();

        if (!StringUtils.isEmpty(fileName)) {
            entityBuilder.addBinaryBody("file", new File(fileName));
        } else {
            throw new MifielException("You must provide a certificate file name");
        }
        
        return entityBuilder.build();
    }

}