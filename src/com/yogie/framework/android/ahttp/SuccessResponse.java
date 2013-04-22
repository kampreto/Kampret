/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.android.ahttp;

import java.lang.reflect.Constructor;

/**
 * Class : SuccessResponse.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class SuccessResponse extends Response {

	private static final long serialVersionUID = 5032845450189368649L;

	/**
	 * Default {@link Constructor}
	 */
	public SuccessResponse() {
		super(STATUS_OK);
	}

	/**
	 * {@link Constructor}
	 * 
	 * @param status
	 * @param statusDesc
	 */
	public SuccessResponse(String statusDesc) {
		super(STATUS_OK, statusDesc);
	}

	/**
	 * {@link Constructor}
	 * 
	 * @param status
	 * @param statusDesc
	 */
	public SuccessResponse(int status, String statusDesc) {
		super(status, statusDesc);
	}
}
