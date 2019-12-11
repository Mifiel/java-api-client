package com.mifiel.api.dao;

import java.util.List;

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

    public abstract T save(T object) throws MifielException;

    public abstract void delete(String id) throws MifielException;
}
