package com.mifiel.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.validator.UrlValidator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.mifiel.api.exception.MifielException;
import com.mifiel.api.rest.HttpMethod;
import com.mifiel.api.utils.MifielUtils;

public final class ApiClient {

    private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
    private final String API_VERSION = "/api/v1/";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
    private final TimeZone gmtTime = TimeZone.getTimeZone("GMT");
    private final UrlValidator urlValidator = new UrlValidator();

    private String appId;
    private String appSecret;
    private String url = "https://www.mifiel.com";

    public ApiClient(final String appId, final String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        dateFormat.setTimeZone(gmtTime);
    }

    public HttpEntity get(final String path) throws MifielException {
        return sendRequest(HttpMethod.GET, path, MultipartEntityBuilder.create().build());
    }

    public HttpEntity post(final String path, final HttpEntity content) throws MifielException {
        return sendRequest(HttpMethod.POST, path, content);
    }

    public HttpEntity delete(final String path) throws MifielException {
        return sendRequest(HttpMethod.DELETE, path, MultipartEntityBuilder.create().build());
    }

    public HttpEntity put(final String path, final HttpEntity content) throws MifielException {
        return sendRequest(HttpMethod.PUT, path, content);
    }

	public HttpEntity patch(final String path, final HttpEntity content) throws MifielException {
        return sendRequest(HttpMethod.PATCH, path, content);
    }

    private HttpEntity sendRequest(final HttpMethod httpMethod, final String path, final HttpEntity body)
            throws MifielException {

        final String requestUrl = url + API_VERSION + path;
        final ContentType contentType = ContentType.getOrDefault(body);
        HttpRequestBase request = null;
        HttpResponse response = null;
        HttpEntity entityResponse = null;

        switch (httpMethod) {
            case POST:
                request = new HttpPost(requestUrl);
                ((HttpPost) request).setEntity(body);
                break;
            case GET:
                request = new HttpGet(requestUrl);
                break;
            case DELETE:
                request = new HttpDelete(requestUrl);
                break;
            case PUT:
                request = new HttpPut(requestUrl);
                ((HttpPut) request).setEntity(body);
                break;
            case PATCH:
                request = new HttpPatch(requestUrl);
                ((HttpPatch) request).setEntity(body);
                break;
            default:
                throw new MifielException("Unsupported HttpMethod: " + httpMethod);
        }

        String content;
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            body.writeTo(out);
            content = out.toString();
        } catch (Exception e) {
            throw new MifielException("Error on body request", e);
        }

        setAuthentication(httpMethod, path, content, request, contentType);

        try {
            response = HttpClientBuilder.create().build().execute(request);
        } catch (final IOException e) {
            throw new MifielException("Error sending Http request", e);
        }

        final int responseStatusCode = response.getStatusLine().getStatusCode();

        entityResponse = response.getEntity();
        if (entityResponse == null) {
            try {
                entityResponse = new StringEntity("");
            } catch (UnsupportedEncodingException e) {
                throw new MifielException("Error creating an empty Entity", e);
            }
        }

        if (MifielUtils.isSuccessfulHttpCode(responseStatusCode)) {
            return entityResponse;
        } else {
            try {
                throw new MifielException("Status code error: " + responseStatusCode,
                        EntityUtils.toString(entityResponse));
            } catch (final Exception e) {
                throw new MifielException("Status code error: " + responseStatusCode, e);
            }
        }
    }

    private void setAuthentication(final HttpMethod httpMethod, final String path, final String content,
            final HttpRequestBase request, final ContentType contentTypeBody) throws MifielException {
        final String contentType = contentTypeBody.toString();
        final String date = dateFormat.format(new Date());
        final String contentMd5 = "";// MifielUtils.calculateMD5(content);
        final String signature = getSignature(httpMethod, path, contentMd5, date, request, contentType);
        final String authorizationHeader = String.format("APIAuth %s:%s", appId, signature);

        request.addHeader("Authorization", authorizationHeader);
        request.addHeader("Content-MD5", contentMd5);
        request.addHeader("Content-Type", contentType);
        request.addHeader("Date", date);
    }

    private String getSignature(final HttpMethod httpMethod, final String path, final String contentMd5,
            final String date, final HttpRequestBase request, final String contentType) throws MifielException {
        final String canonicalString = String.format("%s,%s,%s,%s,%s", httpMethod.toString(), contentType, contentMd5,
                API_VERSION + path, date);
        return MifielUtils.calculateHMAC(appSecret, canonicalString, HMAC_SHA1_ALGORITHM);
    }

    public void setUrl(final String url) throws MifielException {
        if (urlValidator.isValid(url)) {
            this.url = url.replaceAll("/$", "");
        } else {
            throw new MifielException("Invalid URL format");
        }
    }

    public void setAppId(final String appId) {
        this.appId = appId;
    }

    public void setAppSecret(final String appSecret) {
        this.appSecret = appSecret;
    }
}
