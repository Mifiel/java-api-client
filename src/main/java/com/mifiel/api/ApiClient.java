package com.mifiel.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.validator.UrlValidator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.mifiel.api.exception.MifielException;
import com.mifiel.api.rest.HttpMethod;
import com.mifiel.api.utils.MifielUtils;

public final class ApiClient {
	private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private final String DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss z";
	private final String API_VERSION = "/api/v1/";
	private final String CONTENT_TYPE = "application/x-www-form-urlencoded";//"application/json";
	private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	private final TimeZone gmtTime = TimeZone.getTimeZone("GMT");
	private final UrlValidator urlValidator = new UrlValidator();
	
	private String appId;
    private String appSecret;
    private String url = "https://www.mifiel.com/api/v1/";
    
    public ApiClient(final String appId, final String appSecret) {
    	this.appId = appId;
    	this.appSecret = appSecret;
    	dateFormat.setTimeZone(gmtTime);
    }
    
    public String get(final String path) throws MifielException {
    	return sendRequest(HttpMethod.GET, path, "");
    }
    
    public String post(final String path, final String content) throws MifielException {
    	return sendRequest(HttpMethod.POST, path, content);
    }
    
    public String delete(final String path) throws MifielException {
    	return sendRequest(HttpMethod.DELETE, path, "");
    }

    public String put(final String path, final String content) throws MifielException {
    	return sendRequest(HttpMethod.PUT, path, content);
    }
    
    private String sendRequest(final HttpMethod httpMethod, final String path,
    							final String content) throws MifielException {
    	final String requestUrl = url + API_VERSION + path;//"http://rve.org.uk/dumprequest";//
        
        HttpRequestBase request = null;
        HttpResponse response = null;
        
        StringEntity body;
		try {
			body = new StringEntity(content);
		} catch (final UnsupportedEncodingException e) {
			throw new MifielException("Error creating request body", e);
		}
            
		switch (httpMethod) {
			case POST:
				request = new HttpPost(requestUrl);
				((HttpPost)request).setEntity(body);
				break;
			case GET:
				request = new HttpGet(requestUrl);
				break;
			case DELETE:
				request = new HttpDelete(requestUrl);
				break;
			case PUT:
				request = new HttpPut(requestUrl);
				((HttpPut)request).setEntity(body);
				break;
			default:
				throw new UnsupportedOperationException("Unsupported HttpMethod: " + httpMethod);
		}
		
		setAuthentication(httpMethod, path, content, request);
		
		try {
			response = HttpClientBuilder.create().build().execute(request);
		} catch (final IOException e) {
			throw new MifielException("Error sending Http request", e);
		}
		
		final int responseStatusCode = response.getStatusLine().getStatusCode();
		String httpResponse;
		try {
			httpResponse = EntityUtils.toString(response.getEntity());
		} catch (final Exception e) {
			throw new MifielException("Error reading Http Response", e);
		}
		
		System.out.println(httpResponse);
		
		if (responseStatusCode == HttpStatus.SC_OK) {
			return httpResponse;
		} else {
			throw new MifielException("Status code error: " + responseStatusCode, httpResponse);
		}        
    }
    
    private void setAuthentication(final HttpMethod httpMethod,
    								final String path,
    								final String content, 
    								final HttpRequestBase request) throws MifielException {
    	final String date = dateFormat.format(new Date());    	
    	final String contentMd5 = MifielUtils.calculateMD5(content);
    	final String signature = getSignature(httpMethod, path, contentMd5, date);
    	final String authorizationHeader = String.format("APIAuth %s:%s", appId, signature);
    	
    	System.out.println("signature = " + signature);
    	System.out.println("Date = " + date);
    	System.out.println("Content-MD5 = " + contentMd5);
    	System.out.println("Authorization = " + authorizationHeader);
    	System.out.println("Content-Type = " + CONTENT_TYPE);
    	System.out.println("content = " + content);
    	
    	request.addHeader("Authorization", authorizationHeader);
    	request.addHeader("Content-MD5", contentMd5);
    	request.addHeader("Content-Type", CONTENT_TYPE);
    	request.addHeader("Date", date);
    }
    
    private String getSignature(final HttpMethod httpMethod,
    							final String path,
    							final String contentMd5,
    							final String date) throws MifielException {
    	final String canonicalString = String.format("%s,%s,%s,%s,%s", 
		    								httpMethod.toString(),
		    								CONTENT_TYPE,
		    								contentMd5,
		    								API_VERSION + path,
		    								date);
    	System.out.println("canonical_string = " + canonicalString);
    	return MifielUtils.calculateHMAC(appSecret, canonicalString, HMAC_SHA1_ALGORITHM);
    }
	    
    public void setUrl(final String url) throws MifielException {
    	if(urlValidator.isValid(url)) {
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
