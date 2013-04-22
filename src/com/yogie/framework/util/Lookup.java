/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.util;

/**
 * Class : Lookup.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public interface Lookup {

	interface API {
		interface ERROR {
			String SERVICE_UNAVAILABE = "666";
			String SERVICE_UNAVAILABE_MSG = "Failed connecting to server. Please try again.";

			String PARSE_ERROR = "680";
			String PARSE_ERROR_MSG = "Unable to parse the data";
		}

	}
}
