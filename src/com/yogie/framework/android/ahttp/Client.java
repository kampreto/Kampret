/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.android.ahttp;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.Handler;

/**
 * Class : Client.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class Client {
	public static final int TIMEOUT = 60 * 1000;

	/**
	 * Send request
	 * @param request
	 * @param callback
	 * @param async
	 */
	public static void sendRequest(Request request, ResponseListener callback, boolean async) {
		HttpUriRequest httpRequest = request.getHttpRequest();
		HttpResponse response = null;
		
		if(httpRequest == null) {
			callback.onResponseReceived(response);
			return;
		}
		if (async) {
			// (new AsynchronousSender(request, new Handler(), new CallbackWrapper(callback))).start();
			new AsyncTaskClient(httpRequest, new Handler(), new CallbackWrapper(callback)).execute();
		} else {
			HttpClient client = getThreadSafeClient();
			try {
				response = client.execute(httpRequest);
				callback.onResponseReceived(response);
			} catch (IOException e) {
				e.printStackTrace();
				callback.onResponseReceived(response);
			} catch (Exception e) {
				e.printStackTrace();
				callback.onResponseReceived(response);
			}
		}
	}

	/**
	 * Reusable HttpClient
	 * 
	 * @return {@link DefaultHttpClient}
	 */
	public static DefaultHttpClient getThreadSafeClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();

		HttpParams httpParameters = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT);
		ConnManagerParams.setTimeout(httpParameters, TIMEOUT);

		client = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParameters, mgr.getSchemeRegistry()), httpParameters);

		return client;
	}

}