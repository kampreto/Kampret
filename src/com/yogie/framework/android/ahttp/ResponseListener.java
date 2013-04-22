/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.android.ahttp;

import org.apache.http.HttpResponse;

/**
 * Class : ResponseListener.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public interface ResponseListener {
	void onResponseReceived(HttpResponse response);

}