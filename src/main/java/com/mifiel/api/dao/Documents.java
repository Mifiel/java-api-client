package com.mifiel.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.mime.MultipartEntity;

import com.mifiel.api.ApiClient;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Document;
import com.mifiel.api.objects.Signature;
import com.mifiel.api.utils.MifielUtils;

public class Documents extends BaseObjectDAO<Document> {
	
	private final String DOCUMENT_CANONICAL_NAME = Document.class.getCanonicalName();
	private final String SIGNATURE_RESPONSE_CANONICAL_NAME = SignatureResponse.class.getCanonicalName();

	public Documents(final ApiClient apiClient) {
		super(apiClient);
	}

	@Override
	public Document find(final String id) throws MifielException {
		final String response = apiClient.get("documents/" + id);
		final Document document = (Document) MifielUtils.convertJsonToObject(response, DOCUMENT_CANONICAL_NAME);
		return document;
	}

	@Override
	public List<Document> findAll() throws MifielException {
		final String response = apiClient.get("documents");
		final List<Document> documents = (List<Document>)(Object)MifielUtils.convertJsonToObjects(response, DOCUMENT_CANONICAL_NAME);
		return documents;
	}	

	@Override
	public void delete(final String id) throws MifielException {
		apiClient.delete("documents/" + id);
	}

	@Override
	public void save(final Document document) throws MifielException {
		final String httpContent = buildHttpBody(document);
		apiClient.post("documents", httpContent);
	}
	
	public SignatureResponse requestSignature(final String id) throws MifielException {
		final String response = apiClient.post("documents/" + id + "/request_signature", "");
		final SignatureResponse signatureResponse = (SignatureResponse) MifielUtils.convertJsonToObject(response, SIGNATURE_RESPONSE_CANONICAL_NAME);
		return signatureResponse;
	}

	@Override
	protected String buildHttpBody(final Document document) throws MifielException {
		StringBuilder httpBody = new StringBuilder();
		final List<Signature> signatures = document.getSignatures();
		final String filePath = document.getFile();
		final String fileName = document.getFileFileName();
		final String originalHash = document.getOriginalHash();
		
		if (!StringUtils.isEmpty(filePath)) {
			MifielUtils.appendParamToHttpBody(httpBody, "file", filePath);
		} else if (!StringUtils.isEmpty(originalHash) && !StringUtils.isEmpty(fileName)) {
			MifielUtils.appendParamToHttpBody(httpBody, "original_hash", originalHash);
			MifielUtils.appendParamToHttpBody(httpBody, "name", fileName);
		} else {
			throw new MifielException("You must provide file or original hash and file name");
		}
				
		MifielUtils.appendParamToHttpBody(httpBody, "callback_url", document.getCallbackUrl());
		
		for (int i = 0; i < signatures.size(); i++) {
			MifielUtils.appendParamToHttpBody(httpBody, "signatories[" + i + "][name]", signatures.get(i).getSignature());
			MifielUtils.appendParamToHttpBody(httpBody, "signatories[" + i + "][email]", signatures.get(i).getEmail());
			MifielUtils.appendParamToHttpBody(httpBody, "signatories[" + i + "][tax_id]", signatures.get(i).getTaxId());
			MifielUtils.appendParamToHttpBody(httpBody, "signatories[" + i + "][signer]", "");
		}
		
		return httpBody.toString();
	}

}
