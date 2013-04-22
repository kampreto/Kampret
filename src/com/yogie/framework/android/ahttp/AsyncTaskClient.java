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

import android.os.AsyncTask;
import android.os.Handler;

/**
 * Class : AsyncTaskClient.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class AsyncTaskClient extends AsyncTask<Void, Void, HttpResponse> {

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
	protected AsyncTaskClient(HttpUriRequest request, Handler handler,
			CallbackWrapper wrapper) {
		this.request = request;
		this.handler = handler;
		this.wrapper = wrapper;
	}

	/**
	 * @return {@link HttpClient}
	 */
	private HttpClient getClient() {
		return httpClient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected HttpResponse doInBackground(Void... params) {
		HttpResponse response = null;
		try {
			synchronized (httpClient) {
				try {
					response = getClient().execute(request);
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(HttpResponse result) {
		wrapper.setResponse(result);
		handler.post(wrapper);
	}
}
