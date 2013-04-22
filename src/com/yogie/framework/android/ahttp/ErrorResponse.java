/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.android.ahttp;

import java.lang.reflect.Constructor;

/**
 * Class : ErrorResponse.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class ErrorResponse extends Response {

	private static final long serialVersionUID = -7539999509745028023L;

	/**
	 * Default {@link Constructor}
	 */
	public ErrorResponse() {
		super(Response.STATUS_FAILED);
	}

	/**
	 * {@link Constructor}
	 * 
	 * @param status
	 * @param statusDesc
	 */
	public ErrorResponse(String statusDesc) {
		super(Response.STATUS_FAILED, statusDesc);
	}
}
