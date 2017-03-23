package com.mifiel.api.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;

import com.mifiel.api.ApiClient;
import com.mifiel.api.exception.MifielException;

public abstract class BaseObjectDAO<T> {
	protected ApiClient apiClient;

	public BaseObjectDAO(final ApiClient apiClient) {
		this.apiClient = apiClient;
	}
	
	public ApiClient getApiClient() {
		return apiClient;
	}

	public void setApiClient(final ApiClient apiClient) {
		this.apiClient = apiClient;
	}
	
	public abstract T find(String id) throws MifielException;
	public abstract List<T> findAll() throws MifielException;
	public abstract void save(T object) throws MifielException;
	public abstract void delete(String id) throws MifielException;
	protected abstract String buildHttpBody(T object) throws MifielException;
}
