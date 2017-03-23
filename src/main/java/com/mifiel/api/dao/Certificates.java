package com.mifiel.api.dao;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;

import com.mifiel.api.ApiClient;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Certificate;
import com.mifiel.api.objects.Document;
import com.mifiel.api.utils.MifielUtils;

public class Certificates extends BaseObjectDAO<Certificate> {
	private final String CERTIFICATE_CANONICAL_NAME = Certificate.class.getCanonicalName();

	public Certificates(final ApiClient apiClient) {
		super(apiClient);
	}

	@Override
	public Certificate find(final String id) throws MifielException {
		final String response = apiClient.get("documents/" + id);
		final Certificate certificate = (Certificate) MifielUtils.convertJsonToObject(response, CERTIFICATE_CANONICAL_NAME);
		return certificate;
	}

	@Override
	public List<Certificate> findAll() throws MifielException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(final Certificate certificate) throws MifielException {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(final String id) throws MifielException {
		// TODO Auto-generated method stub
	}

	@Override
	protected String buildHttpBody(final Certificate certificate) throws MifielException {
		return null;
	}

}
