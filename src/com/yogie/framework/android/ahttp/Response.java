/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.android.ahttp;

import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * Class : Response.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class Response implements Serializable {

	private static final long serialVersionUID = 4345188665481144092L;

	public final static int STATUS_OK = 1;
	public final static int STATUS_FAILED = 0;

	private int status;
	private String statusDesc;

	/**
	 * Default {@link Constructor}
	 */
	public Response() {
		super();
		status = STATUS_OK;
	}

	/**
	 * {@link Constructor}
	 * 
	 * @param status
	 */
	public Response(int status) {
		super();
		this.status = status;
	}

	/**
	 * {@link Constructor}
	 * 
	 * @param status
	 * @param statusDesc
	 */
	public Response(int status, String statusDesc) {
		super();
		this.status = status;
		this.statusDesc = statusDesc;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the statusDesc
	 */
	public String getStatusDesc() {
		return statusDesc;
	}

	/**
	 * @param statusDesc
	 *            the statusDesc to set
	 */
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
}
