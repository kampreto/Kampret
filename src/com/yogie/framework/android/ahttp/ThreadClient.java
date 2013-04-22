/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.android.ahttp;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import android.os.Handler;

/**
 * Class : ThreadClient.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class ThreadClient extends Thread {

	private static final HttpClient httpClient = Client.getThreadSafeClient();

	private HttpUriRequest request;
	private Handler handler;
	private CallbackWrapper wrapper;

	/**
	 * {@link Constructor}
	 * 
	 * @param request
	 * @param handler
	 * @param wrapper
	 */
	protected ThreadClient(HttpUriRequest request, Handler handler, CallbackWrapper wrapper) {
		this.request = request;
		this.handler = handler;
		this.wrapper = wrapper;
	}

	public void run() {
		HttpResponse response = null;
		try {
			synchronized (httpClient) {
				try {
					response = getClient().execute(request);

					wrapper.setResponse(response);
					handler.post(wrapper);
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			wrapper.setResponse(response);
			handler.post(wrapper);
		} catch (IOException e) {
			e.printStackTrace();
			wrapper.setResponse(response);
			handler.post(wrapper);
		} catch (Exception e) {
			e.printStackTrace();
			wrapper.setResponse(response);
			handler.post(wrapper);
		}
	}

	/**
	 * 
	 * @return {@link HttpClient}
	 */
	private HttpClient getClient() {
		return httpClient;
	}
}
