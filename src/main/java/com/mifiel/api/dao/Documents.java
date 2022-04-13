package com.mifiel.api.dao;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import com.mifiel.api.ApiClient;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Document;
import com.mifiel.api.objects.SignatureResponse;
import com.mifiel.api.utils.MifielUtils;

public class Documents extends BaseObjectDAO<Document> {

    public static final String DOCUMENT_CANONICAL_NAME = Document.class.getCanonicalName();
    private final String SIGNATURE_RESPONSE_CANONICAL_NAME = SignatureResponse.class.getCanonicalName();
    public static final String DOCUMENTS_PATH = "documents";

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
        return (List<Document>) (Object) MifielUtils.convertJsonToObjects(response, DOCUMENT_CANONICAL_NAME);
    }

    @Override
    public void delete(final String id) throws MifielException {
        apiClient.delete(DOCUMENTS_PATH + "/" + id);
    }

    public void saveFile(final String id, final String localPath) throws MifielException {
        final HttpEntity entityResponse = apiClient.get(DOCUMENTS_PATH + "/" + id + "/file");
        MifielUtils.saveEntityResponseToFile(entityResponse, localPath);
    }

    public void saveSignedFile(final String id, final String localPath) throws MifielException {
        final HttpEntity entityResponse = apiClient.get(DOCUMENTS_PATH + "/" + id+ "/file_signed");
        MifielUtils.saveEntityResponseToFile(entityResponse, localPath);
    }

    public void saveZip(final String id, final String localPath) throws MifielException {
        final HttpEntity entityResponse = apiClient.get(DOCUMENTS_PATH + "/" + id + "/zip");
        MifielUtils.saveEntityResponseToFile(entityResponse, localPath);
    }

    public void saveXml(final String id, final String localPath) throws MifielException {
        final HttpEntity entityResponse = apiClient.get(DOCUMENTS_PATH + "/" + id + "/xml");
        MifielUtils.saveEntityResponseToFile(entityResponse, localPath);
    }

    @Override
    public Document save(Document document) throws MifielException {
        final HttpEntity entityResponse;
        if (StringUtils.isEmpty(document.getId())) {
            final HttpEntity httpContent = MifielUtils.buildHttpBody(document);
            entityResponse = apiClient.post(DOCUMENTS_PATH, httpContent);
        } else {
            final String json = MifielUtils.convertObjectToJson(document);

            StringEntity httpContent;
            httpContent = new StringEntity(json, "UTF-8");

            entityResponse = apiClient.put(DOCUMENTS_PATH + "/" + document.getId(), httpContent);
        }

        final String response = MifielUtils.entityToString(entityResponse);
        Document saveDocument = (Document) MifielUtils.convertJsonToObject(response, DOCUMENT_CANONICAL_NAME);
        saveDocument.setSignatures(document.getSignatures());
        saveDocument.setViewers(document.getViewers());

        return saveDocument;
    }

    public SignatureResponse requestSignature(final String id, final String email, final String cc)
            throws MifielException {
        final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.addTextBody("email", email);
        entityBuilder.addTextBody("cc", cc);

        final HttpEntity entityResponse = apiClient.post(DOCUMENTS_PATH + "/" + id + "/request_signature",
                entityBuilder.build());
        final String response = MifielUtils.entityToString(entityResponse);
        final SignatureResponse signatureResponse = (SignatureResponse) MifielUtils.convertJsonToObject(response,
                SIGNATURE_RESPONSE_CANONICAL_NAME);
        return signatureResponse;
    }
}
