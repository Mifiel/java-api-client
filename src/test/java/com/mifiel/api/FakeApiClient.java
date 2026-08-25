package com.mifiel.api;

import org.apache.http.HttpEntity;

import com.mifiel.api.exception.MifielException;

/**
 * In-memory {@link ApiClient} that never opens a network connection.
 */
class FakeApiClient extends ApiClient {

    private HttpEntity nextResponse;
    private MifielException nextException;
    private String lastMethod;
    private String lastPath;
    private HttpEntity lastBody;
    private int postCount;

    FakeApiClient() {
        super("test-app-id", "test-app-secret");
    }

    void setNextResponse(final HttpEntity response) {
        this.nextResponse = response;
        this.nextException = null;
    }

    void setNextException(final MifielException exception) {
        this.nextException = exception;
        this.nextResponse = null;
    }

    String getLastMethod() {
        return lastMethod;
    }

    String getLastPath() {
        return lastPath;
    }

    HttpEntity getLastBody() {
        return lastBody;
    }

    int getPostCount() {
        return postCount;
    }

    @Override
    public HttpEntity get(final String path) throws MifielException {
        return record("GET", path, null);
    }

    @Override
    public HttpEntity post(final String path, final HttpEntity content) throws MifielException {
        postCount++;
        return record("POST", path, content);
    }

    @Override
    public HttpEntity delete(final String path) throws MifielException {
        return record("DELETE", path, null);
    }

    @Override
    public HttpEntity put(final String path, final HttpEntity content) throws MifielException {
        return record("PUT", path, content);
    }

    @Override
    public HttpEntity patch(final String path, final HttpEntity content) throws MifielException {
        return record("PATCH", path, content);
    }

    private HttpEntity record(final String method, final String path, final HttpEntity body) throws MifielException {
        lastMethod = method;
        lastPath = path;
        lastBody = body;
        if (nextException != null) {
            throw nextException;
        }
        return nextResponse;
    }
}
