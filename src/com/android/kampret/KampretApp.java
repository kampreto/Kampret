/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.android.kampret;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mavenlab.framework.MvnApplication;
import com.yogie.framework.android.misc.CurrentLocation;
import com.yogie.framework.android.misc.CurrentLocation.LocationResult;
import com.yogie.framework.util.LocationUtil;

/**
 * Class : KampretApp.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class KampretApp extends MvnApplication {

	private final static String TAG = "com.android.kampret.KampretApp";
	private final static String GCM_ID = "879481069237";

	// Global instances
	private static ConnectivityManager mConnectivityManager = null;

	private static String mAPIBaseURLStaging = null;
	private static String mAPIBaseURLProduction = null;
	private static String registrationID = null;

	private static double latitude;
	private static double longitude;

	public static boolean firstStart = true;
	public static HashMap<String, Object> contexts;
	private static Location currentLocation;

	private static boolean login = false;

	public static class PrefKey extends MvnApplication.PrefKey {
		public final static String USERNAME = "username";
		public final static String PASSWORD = "password";
		public final static String REMEMBER = "rememeber";
		public final static String LAST_LATITUDE = "lastLatitude";
		public final static String LAST_LONGITUDE = "lastLongitude";
		public final static String IS_LOGGED_IN = "isLoggedIn";
		public final static String REGISTRATION_ID = "registrationID";
	}

	@Override
	public void onCreate() {
		super.onCreate();
		contexts = new HashMap<String, Object>();
		mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		// Load static configuration values from resources
		Resources res = getResources();

		mAPIBaseURLStaging = res.getString(R.string.APIBaseURLStaging);
		mAPIBaseURLProduction = res.getString(R.string.APIBaseURLProduction);

		Log.d(TAG, "INITIALIZING LOCATION");
		initLocation();
	}

	/**
	 * Shared ConnectivityManager for the whole application.
	 * 
	 * @return the connectivity manager.
	 */
	public static ConnectivityManager sharedConnectivityManager() {
		return mConnectivityManager;
	}

	public static String getCurrentTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	public static double getCurrentLatitude() {
		return latitude;
	}
	
	public static double getCurrentLongitude() {
		return longitude;
	}

	/**
	 * Get an API Base URL
	 * @return {@link String}
	 */
	public static String getAPIBaseURL() {
		if (isDebuggable()) {
			return mAPIBaseURLStaging;
		} else {
			return mAPIBaseURLProduction;
		}
	}

	public static void setRegistrationID(String registrationID) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getMvnApplication());
		SharedPreferences.Editor editor = preferences.edit();
		KampretApp.registrationID = registrationID;
		editor.putString(PrefKey.REGISTRATION_ID, KampretApp.registrationID);
		editor.commit();
	}

	/**
	 * @return the Registration ID of C2DM
	 */
	public static String getRegistrationID() {
		if (registrationID == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getMvnApplication());
			registrationID = preferences.getString(PrefKey.REGISTRATION_ID, null);
		}
		return registrationID;
	}

	/**
	 * Update current location
	 */
	public static void updateCurrentLocation() {
		CurrentLocation myLocation = new CurrentLocation();
		myLocation.find(getMvnApplication(), locationResult, 1000);
	}

	/**
	 * Init location
	 */
	private void initLocation() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getMvnApplication());
		latitude = Double.parseDouble(preferences.getString(PrefKey.LAST_LATITUDE, "1.341422"));
		longitude = Double.parseDouble(preferences.getString(PrefKey.LAST_LONGITUDE, "103.859164"));

		CurrentLocation myLocation = new CurrentLocation();
		myLocation.find(getMvnApplication(), locationResult, 10000);
	}

	static LocationResult locationResult = new LocationResult() {
		@Override
		public void getCurrentLocation(Location location) {
			if (location != null && LocationUtil.isBetterLocation(location, currentLocation)) {

				latitude = location.getLatitude();
				longitude = location.getLongitude();

				// Validation for Singapore Lat Long
				//if (!(latitude >= 1 && latitude <= 1.470184)
				//		&& !(longitude >= 103.55 || longitude <= 104.055706)) {
				//	latitude = 1.341422;
				//	longitude = 103.859164;
				//}

				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(getMvnApplication());
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString(PrefKey.LAST_LATITUDE, Double.toString(latitude));
				editor.putString(PrefKey.LAST_LONGITUDE, Double.toString(longitude));
				editor.commit();
				
				currentLocation = location;
			} 
			Log.d(TAG, "CURRENT LOCATION : " + latitude + "," + longitude + "|" + location.getProvider());
		}
	};

	public static void registerUserEmailForRegId() {
		Intent registrationIntent = new Intent(
				"com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(getMvnApplication().getApplicationContext(), 0, new Intent(), 0));
		// registrationIntent.putExtra("sender", C2DM_EMAIL);
		registrationIntent.putExtra("sender", GCM_ID);

		getMvnApplication().getApplicationContext().startService(registrationIntent);
	}

	public static boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getMvnApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		
		// There are no active networks.
		if (ni == null)
			return false;
		else
			return ni.isConnected();
	}

	/**
	 * Check if logged in
	 * 
	 * @return {@link Boolean}
	 */
	public static boolean isLoggedIn() {
		return login;
	}
}
