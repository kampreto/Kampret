/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.android.ahttp;

import org.apache.http.HttpResponse;

/**
 * Class : CallbackWrapper.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class CallbackWrapper implements Runnable {

	private ResponseListener callbackActivity;
	private HttpResponse response;

	public CallbackWrapper(ResponseListener callbackActivity) {
		this.callbackActivity = callbackActivity;
	}

	public void run() {
		callbackActivity.onResponseReceived(response);
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
	}
}