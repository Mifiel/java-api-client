package com.mifiel.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.mifiel.api.ApiClient;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.objects.Webhook;
import com.mifiel.api.utils.MifielUtils;

/**
 * CRUD + trigger helpers for account-level webhooks.
 *
 * @see <a href="https://docs.mifiel.com/en/#tag/Webhooks">Webhooks API</a>
 */
public class Webhooks extends BaseObjectDAO<Webhook> {

    private final String WEBHOOK_CANONICAL_NAME = Webhook.class.getCanonicalName();
    private final String WEBHOOKS_PATH = "webhooks";

    public Webhooks(final ApiClient apiClient) {
        super(apiClient);
    }

    @Override
    public Webhook find(final String id) throws MifielException {
        final HttpEntity entityResponse = apiClient.get(WEBHOOKS_PATH + "/" + id);
        final String response = MifielUtils.entityToString(entityResponse);
        return (Webhook) MifielUtils.convertJsonToObject(response, WEBHOOK_CANONICAL_NAME);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Webhook> findAll() throws MifielException {
        final HttpEntity entityResponse = apiClient.get(WEBHOOKS_PATH);
        final String response = MifielUtils.entityToString(entityResponse);
        return (List<Webhook>) (Object) MifielUtils.convertJsonToObjects(response, WEBHOOK_CANONICAL_NAME);
    }

    @Override
    public void delete(final String id) throws MifielException {
        apiClient.delete(WEBHOOKS_PATH + "/" + id);
    }

    @Override
    public Webhook save(final Webhook webhook) throws MifielException {
        final String json = MifielUtils.convertObjectToJson(webhook);
        final StringEntity httpContent = new StringEntity(json, ContentType.APPLICATION_JSON);
        final HttpEntity entityResponse = apiClient.post(WEBHOOKS_PATH, httpContent);
        final String response = MifielUtils.entityToString(entityResponse);
        return (Webhook) MifielUtils.convertJsonToObject(response, WEBHOOK_CANONICAL_NAME);
    }

    /**
     * Trigger delivery for a webhook.
     *
     * @param id webhook id
     * @param resource UUID of the related resource included in the callback payload
     * @param instant when true, deliver immediately once instead of enqueueing retries
     */
    public String trigger(final String id, final String resource, final boolean instant) throws MifielException {
        final Map<String, Object> body = new HashMap<String, Object>();
        body.put("resource", resource);
        body.put("instant", instant);
        final String json = MifielUtils.convertObjectToJson(body);
        final StringEntity httpContent = new StringEntity(json, ContentType.APPLICATION_JSON);
        final HttpEntity entityResponse = apiClient.post(WEBHOOKS_PATH + "/" + id + "/trigger", httpContent);
        return MifielUtils.entityToString(entityResponse);
    }

    public String trigger(final String id, final String resource) throws MifielException {
        return trigger(id, resource, false);
    }
}
