package com.mifiel.api.dao;

import java.util.List;

import com.mifiel.api.ApiClient;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.SignaturePackage;
import com.mifiel.api.utils.MifielUtils;

import org.apache.commons.lang.NotImplementedException;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public class SignaturePackages extends BaseObjectDAO<SignaturePackage> {

	public static final String SIGNATURE_PACKAGE_CANONICAL_NAME = SignaturePackage.class.getCanonicalName();
    public static final String SIGNATURE_PACKAGE_PATH = "signature_package";

	public SignaturePackages(ApiClient apiClient) {
		super(apiClient);
    }

	public SignaturePackage find(List<String> widgetIds) throws MifielException{
		final HttpEntity entityResponse = apiClient.get(SIGNATURE_PACKAGE_PATH + "?widget_ids=" + String.join(",", widgetIds));
        final String response = MifielUtils.entityToString(entityResponse);
        return (SignaturePackage) MifielUtils.convertJsonToObject(response, SIGNATURE_PACKAGE_CANONICAL_NAME);
    }

	@Override
	public SignaturePackage save(SignaturePackage signaturePackage) throws MifielException {
		final String json = MifielUtils.convertObjectToJson(signaturePackage);
		StringEntity httpContent;
		httpContent = new StringEntity(json, ContentType.APPLICATION_JSON);
    	final HttpEntity entityResponse = apiClient.patch(SIGNATURE_PACKAGE_PATH, httpContent);
		final String response = MifielUtils.entityToString(entityResponse);
        SignaturePackage saveSignaturePackage = (SignaturePackage) MifielUtils.convertJsonToObject(response, SIGNATURE_PACKAGE_CANONICAL_NAME);
		return saveSignaturePackage;
	}

	@Override
	public SignaturePackage find(String id) throws MifielException {
		throw new NotImplementedException();
	}

	@Override
	public List<SignaturePackage> findAll() throws MifielException {
		throw new NotImplementedException();
	}

	@Override
	public void delete(String id) throws MifielException {
		throw new NotImplementedException();
	}
}
