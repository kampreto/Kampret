/**
 * Copyright (c) 2012 Maven Lab Private Limited. All rights reserved.
 */
package com.yogie.framework.android.ahttp;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * Class : Request.java
 * 
 * @author <a href="mailto:yogie@mavenlab.com">Yogie Kurniawan</a>
 */
public class Request implements Serializable {

	private static final long serialVersionUID = -3091899132262325890L;

	private HttpUriRequest httpRequest;
	private ArrayList<NameValuePair> parameters;

	/**
	 * Default {@link Constructor}
	 */
	public Request() {
		super();
		parameters = new ArrayList<NameValuePair>();
	}

	/**
	 * {@link Constructor}
	 * 
	 * @param url
	 * @param post
	 */
	public Request(String url, boolean post) {
		super();
		parameters = new ArrayList<NameValuePair>();

		if (post)
			httpRequest = new HttpPost(url);
		else
			httpRequest = new HttpGet(url);
	}

	/**
	 * Add parameter value
	 * 
	 * @param name
	 * @param value
	 */
	public void addParameter(String name, String value) {
		parameters.add(new BasicNameValuePair(name, value));
	}

	/**
	 * @return the request
	 */
	public HttpUriRequest getHttpRequest() {
		try {
			HttpEntity entity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
			if (httpRequest instanceof HttpPost) {
				((HttpPost) httpRequest).setEntity(entity);
			} else {
				((HttpGet) httpRequest).setURI(URI.create(httpRequest.getURI() + "?" + EntityUtils.toString(entity)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpRequest;
	}

	/**
	 * @param request
	 *            the request to set
	 */
	public void setHttpRequest(HttpUriRequest request) {
		this.httpRequest = request;
	}

	/**
	 * @return the parameters
	 */
	public ArrayList<NameValuePair> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(ArrayList<NameValuePair> parameters) {
		this.parameters = parameters;
	}
}
