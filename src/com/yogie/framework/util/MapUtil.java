/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.util;

/**
 * Class : MapUtil.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class MapUtil {

	/**
	 * Calculate distance
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return {@link Float}
	 */
	public static float distance(double lat1, double lng1, double lat2,
			double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return new Float(dist * meterConversion).floatValue();
	}
}
