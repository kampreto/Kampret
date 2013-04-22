/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.android.handler;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Class : JSONHandler.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public abstract class JSONHandler implements Serializable {

	private static final long serialVersionUID = -7359139066731636482L;

	private ArrayList<JSONParserListener> listeners;

	/**
	 * Abstract method to parse a json object
	 * 
	 * @param json
	 */
	protected abstract void parse(String json);

	/**
	 * Default {@link Constructor}
	 */
	public JSONHandler() {
		listeners = new ArrayList<JSONParserListener>();
	}

	/**
	 * Add a listener
	 * 
	 * @param listener
	 */
	public void addListener(JSONParserListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove a listener
	 * 
	 * @param listener
	 */
	public void removeListener(JSONParserListener listener) {
		listeners.remove(listener);
	}
}
