package com.mavenlab.framework;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.UUID;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.kampret.R;

/**
 * Application subclass with common tools for Maven Lab projects.  Can be used directly, or further subclasses.
 * To use this application class, set the android:name property of the application element to the class path in the
 * Android manifest. e.g.
 * <application android:name="com.mavenlab.framework.MvnApplication" ...> ... </application>
 *
 * Created by IntelliJ IDEA.
 * User: Diego Garcia <diego.garcia@mavenlab.com>
 * Date: 6/21/11
 * Time: 1:25 PM
 */
public class MvnApplication extends Application {
  static private final String TAG = "MvnApplication";

  // Constants
  static private final String ID_TYPE_PHONE = "PHONE";
  static private final String ID_TYPE_UUID = "UUID";
  static private final String ID_TYPE_RAND = "RAND";
  static private final String ID_TYPE_NONE = "NOID";

  // Shared context (application context)
  static private MvnApplication mMvnApplication;

  // Application info
  static private String mVersion;
  static private int mBuild;
  static private String mUAID;
  static private boolean mDebuggable;


  /**
   * Static class holding labels for preferences
   * @author Diego Garcia <diego.garcia@mavenlab.com>
   */
  static public class PrefKey {
    static private final String KEY_BASE = "com.mavenlab.framework";

    // This class cannot be instantiated
    protected PrefKey() {}

    protected static final String MVN_UAID = KEY_BASE + ".UAID";
  }

  @Override
  public void onCreate() {
    super.onCreate();

    mMvnApplication = this;

    // Load values from package info
    String packageName = getPackageName();
    PackageInfo pi;
    try {
      pi = getPackageManager().getPackageInfo(packageName, 0);
      mVersion = pi.versionName;
      mBuild = pi.versionCode;
      mDebuggable = (pi.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    } catch (PackageManager.NameNotFoundException e) {
      // I mean... c'mon!  Seriously?
      Log.e(TAG, "Couldn't find package " + packageName, e);
    }

    // Inform if debugging
    if (mDebuggable) Log.i(TAG, "Application started in DEBUG mode");
  }

  /*
   * **** Getters / Setters
   */

  /**
   *  Context from application singleton.  Use under your own risk!
   *  @return MvnApplication singleton.
   */
  static public Context getMvnApplication() { return mMvnApplication; }

  /**
   * The running application's version name (e.g. 1.2.0).
   * @return the application's version name.
   */
  static public String getVersion() { return mVersion; }

  /**
   * The running application's build number.
   * @return the application's build number.
   */
  static public int getBuild() { return mBuild; }

  /**
   * Indicates whether the application is set debuggable in the Android manifest.
   * @return true if debuggable, false otherwise.
   */
  static public boolean isDebuggable() { return mDebuggable; }

  /**
   * Returns a string with the application's universal ID
   * @return The application's UAID
   */
  static public String getUAID() {
    String UAID = mUAID;
    
    if (UAID == null) {
      // Load persistent values from preferences
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mMvnApplication);

      UAID = preferences.getString(PrefKey.MVN_UAID, null);
      if (UAID == null) {
        // Just make a new one
        UAID = mMvnApplication.newUAID();

        // Persist
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PrefKey.MVN_UAID, UAID);
        editor.commit();
      }

      mUAID = UAID;
    }
    return UAID;
  }

  /*
   * **** Universal Application ID generation
   */

  /**
   * Creates and return a new UAID.  Does not persist the new id.
   * @return the UAID string.
   */
  private String newUAID() {
    Log.d(TAG, "Generating a new UAID");
    String familyId;
    String instanceId;

    // Ids consist of two parts. First a static app identifier
    familyId = getString(R.string.appMvnFamilyID);
    if (familyId == null) {
      Log.e(TAG, "Application missing appMvnFamilyID configuration key");
      familyId = "XXXX";
    }

    // Then the id and id type, which depends on the hardware capabilities and app permissions.
    // We'll try different sources in order of preference until we get an id.
    instanceId = newTelephonyID();
    if (instanceId == null) instanceId = newUniqueUniversalID();
    if (instanceId == null) instanceId = newRandomID();
    if (instanceId == null) {
      // Whelp, I'm out of ideas...
      instanceId = ID_TYPE_NONE;
    }

    // Join them into the app id
    return familyId + "-" + instanceId;
  }

  /**
   * Creates a new random ID for the application.  Since this type of ID is not based on the hardware, a
   * single device will with high probability return a different ID from this function every time.
   * @return the random type id, or null if unable to create the id.
   */
  private String newRandomID() {
    byte[] bytes = new byte[16];
    final SecureRandom random = new SecureRandom();

    // First, the 128 bit random number
    random.nextBytes(bytes);
    String randHex = String.format(Locale.US,
        "%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X",
        bytes[0], bytes[1], bytes[2], bytes[3],
        bytes[4], bytes[5], bytes[6], bytes[7],
        bytes[8], bytes[9], bytes[10], bytes[11],
        bytes[12], bytes[13], bytes[14], bytes[15]);

    // MD5 validation bytes
    MessageDigest digester;
    String digest;
    try {
      digester = MessageDigest.getInstance("MD5");
      digester.update(bytes, 0, bytes.length);
      bytes = digester.digest();
      digest = String.format(Locale.US, "%02X%02X%02X%02X", bytes[4], bytes[5], bytes[6], bytes[7]);
    } catch (NoSuchAlgorithmException e) {
      Log.w(TAG, "Couldn't find MD5 algorithm for RandomID digest");
      return null;
    }

    // Done
    return ID_TYPE_RAND + "-" + randHex + digest;
  }

  /**
   * Creates a new UUID-based ID for the application.  Since this type of ID is not only based on the hardware, a
   * single device will always return a different ID from this function.
   * @return the UUID type id, or null if unable to create the id.
   */
  private String newUniqueUniversalID() {
    Log.d(TAG, "Generating UUID-type id");

    String uuid = UUID.randomUUID().toString();
    if (TextUtils.isEmpty(uuid)) return null;

    return ID_TYPE_UUID + "-" + uuid;
  }

  /**
   * Creates a "new" telephony ID for the application.  Since this type of ID is tied to the hardware, a single device
   * would normally always return the same ID from this function.
   * @return the telephony type id, or null if unable to get the id.
   */
  private String newTelephonyID() {
    Log.d(TAG, "Generating Telephony-type id");
    int permission;
    String phoneId;

    // First check if we have access to the id.
    permission = getPackageManager().checkPermission("android.permission.READ_PHONE_STATE", getPackageName());
    if (permission == PackageManager.PERMISSION_DENIED) {
      Log.d(TAG, "No READ_PHONE_STATE permission");
      return null;
    }

    // Get the actual id, if the device has any.
    TelephonyManager manager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);

    try {
      phoneId = manager.getDeviceId();
    }
    catch (SecurityException e) {
      // We checked for permission, so this shouldn't happen.
      Log.w(TAG, "Couldn't get telephony id: " + e.getMessage());
      return null;
    }

    if (phoneId == null) {
      Log.d(TAG, "Telephony id is null");
      return null;
    }

    return ID_TYPE_PHONE + "-" + phoneId;
  }
}
