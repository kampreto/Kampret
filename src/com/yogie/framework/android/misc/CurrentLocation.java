/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.android.misc;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.yogie.framework.util.LocationUtil;

/**
 * Class : CurrentLocation.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class CurrentLocation {

	private Timer timer;
	private LocationManager lm;
	private LocationResult lr;
	private boolean gpsEnabled = false;
	private boolean networkEnabled = false;

	/**
	 * find new location
	 * 
	 * @param context
	 * @param result
	 * @param timeout
	 * @return {@link Boolean}
	 */
	public boolean find(Context context, LocationResult result, int timeout) {
		lr = result;
		
		if (lm == null)
			lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		try {
			gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!isGpsEnabled() && !isNetworkEnabled())
			return false;

		if (isGpsEnabled())
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
		
		if (isNetworkEnabled() && !isGpsEnabled())
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);

		timer = new Timer();
		timer.schedule(new LocationTimerTask(), timeout);
		
		return true;
	}

	LocationListener gpsListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer.cancel();
			lm.removeUpdates(networkListener);
			lm.removeUpdates(this);
			lr.getCurrentLocation(location);
		}
		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};

	LocationListener networkListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer.cancel();
			lm.removeUpdates(gpsListener);
			lm.removeUpdates(this);
			lr.getCurrentLocation(location);
		}

		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};

	class LocationTimerTask extends TimerTask {
		
		@Override
		public void run() {
			Location networkLocation = null, gpsLocation = null;
			/**
			 * Find Best Location
			 */
			Location location = LocationUtil.getLocation(lm);
			if (location != null) {
				lr.getCurrentLocation(location);
				return;
			}
			
			if (isGpsEnabled())
				gpsLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			if (isNetworkEnabled() && !isGpsEnabled())
				networkLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			if (gpsLocation != null && networkLocation != null) {
				if (gpsLocation.getTime() > networkLocation.getTime())
					lr.getCurrentLocation(gpsLocation);
				else
					lr.getCurrentLocation(networkLocation);
				return;
			}

			if (gpsLocation != null) {
				lr.getCurrentLocation(gpsLocation);
				return;
			}
			if (networkLocation != null) {
				lr.getCurrentLocation(networkLocation);
				return;
			}
			
			lr.getCurrentLocation(null);
		}
	}

	/**
	 * @return the gpsEnabled
	 */
	public boolean isGpsEnabled() {
		return gpsEnabled;
	}

	/**
	 * @param gpsEnabled
	 *            the gpsEnabled to set
	 */
	public void setGpsEnabled(boolean gpsEnabled) {
		this.gpsEnabled = gpsEnabled;
	}

	/**
	 * @return the networkEnabled
	 */
	public boolean isNetworkEnabled() {
		return networkEnabled;
	}

	/**
	 * @param networkEnabled
	 *            the networkEnabled to set
	 */
	public void setNetworkEnabled(boolean networkEnabled) {
		this.networkEnabled = networkEnabled;
	}

	public static abstract class LocationResult {
		public abstract void getCurrentLocation(Location location);
	}
}
