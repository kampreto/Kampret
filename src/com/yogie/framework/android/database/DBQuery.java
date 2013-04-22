/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.android.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Class : DBQuey.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class DBQuery {

	public static final int STARTS_WITH = 1;
	public static final int CONTAINS_WITH = 2;
	public static final int ENDS_WITH = 3;

	/**
	 * Query to content provider based on _id parameter given
	 * 
	 * @param activity
	 * @param uri
	 * @param id
	 * @return {@link Object}
	 */
	public static Object getObjectById(Activity activity, 
			@SuppressWarnings("rawtypes") Class className, int id) {
		try {
			ContentResolver resolver = activity.getContentResolver();
			Class<?> clazz = Class.forName(className.getName());
			Constructor<?> ctor = clazz.getConstructor(Cursor.class);
			Uri uri = (Uri) clazz.getDeclaredField("CONTENT_URI").get(clazz);

			Cursor c = resolver.query(ContentUris.withAppendedId(uri, id),
					null, null, null, null);
			activity.startManagingCursor(c);

			if (c.moveToFirst()) {
				return ctor.newInstance(new Object[] { c });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Query to content provider to get single record based on column criteria
	 * 
	 * @param activity
	 * @param className
	 * @param whereColumns
	 * @param whereArgs
	 * @return {@link List}
	 */
	public static Object getSingleObjectByColumn(Activity activity,
			@SuppressWarnings("rawtypes") Class className, String whereColumn, 
			String whereArg) {
		try {
			Class<?> clazz = Class.forName(className.getName());
			Constructor<?> ctor = clazz.getConstructor(Cursor.class);
			Uri uri = (Uri) clazz.getDeclaredField("CONTENT_URI").get(clazz);
			String wc = null;
			if ((whereColumn != null && whereColumn.length() > 0)
					&& (whereArg != null && whereArg.length() > 0)) {
				wc = whereColumn + "=?";
			}

			ContentResolver resolver = activity.getContentResolver();
			Cursor c = resolver.query(uri, null, wc, new String[] { whereArg },
					null);
			activity.startManagingCursor(c);

			if (c.moveToFirst()) {
				Object object = ctor.newInstance(new Object[] { c });
				return object;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Query to content provider to records based on projection, column criteria
	 * and can be ordered as well
	 * 
	 * @param activity
	 * @param className
	 * @param whereColumns
	 * @param whereArgs
	 * @param orderColumns
	 * @param asc
	 * @return {@link List}
	 */
	public static List<Object> getDistinctObjects(Activity activity,
			@SuppressWarnings("rawtypes") Class className, String columnName, 
			String[] whereColumns, String[] whereArgs, boolean asc) {

		List<Object> objects = new ArrayList<Object>();

		try {
			Class<?> clazz = Class.forName(className.getName());
			Uri uri = (Uri) clazz.getDeclaredField("CONTENT_URI").get(clazz);

			String distinct = "DISTINCT " + columnName;
			String wc = null;
			if ((whereColumns != null && whereColumns.length > 0)
					&& (whereArgs != null && whereArgs.length > 0)
					&& (whereColumns.length == whereArgs.length)) {
				wc = "";
				for (int i = 0; i < whereColumns.length; i++) {
					wc += whereColumns[i] + "=?";
					if (i < (whereColumns.length - 1)) {
						wc += " AND ";
					}
				}
			}
			String oc = columnName;
			oc += asc ? " ASC" : " DESC";

			String[] projection = { distinct };

			ContentResolver resolver = activity.getContentResolver();
			Cursor c = resolver.query(uri, projection, wc, whereArgs, oc);
			activity.startManagingCursor(c);

			while (c.moveToNext()) {
				String data = c.getString(0);
				objects.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objects;
	}

	/**
	 * Delete tables based on column criteria 
	 * 
	 * @param activity
	 * @param className
	 * @param whereColumns
	 * @param whereArgs
	 * @return {@link integer}
	 */
	public static int deleteObjects(Activity activity, 
			@SuppressWarnings("rawtypes") Class className,
			String[] whereColumns, String[] whereArgs) {
		try {
			Class<?> clazz = Class.forName(className.getName());
			Uri uri = (Uri) clazz.getDeclaredField("CONTENT_URI").get(clazz);
			String wc = null;
			if ((whereColumns != null && whereColumns.length > 0)
					&& (whereArgs != null && whereArgs.length > 0)
					&& (whereColumns.length == whereArgs.length)) {
				wc = "";
				for (int i = 0; i < whereColumns.length; i++) {
					wc += whereColumns[i] + "=?";
					if (i < (whereColumns.length - 1)) {
						wc += " AND ";
					}
				}
			}

			ContentResolver resolver = activity.getContentResolver();
			return resolver.delete(uri, wc, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Query to content provider to records based on column criteria and can be
	 * ordered as well
	 * 
	 * @param activity
	 * @param className
	 * @param whereColumns
	 * @param whereArgs
	 * @param orderColumns
	 * @param asc
	 * @return {@link List}
	 */
	public static List<Object> getObjects(Activity activity, 
			@SuppressWarnings("rawtypes") Class className,
			String[] whereColumns, String[] whereArgs, String[] orderColumns,
			boolean asc) {

		List<Object> objects = new ArrayList<Object>();

		try {
			Class<?> clazz = Class.forName(className.getName());
			Constructor<?> ctor = clazz.getConstructor(Cursor.class);
			Uri uri = (Uri) clazz.getDeclaredField("CONTENT_URI").get(clazz);
			String wc = null;
			if ((whereColumns != null && whereColumns.length > 0)
					&& (whereArgs != null && whereArgs.length > 0)
					&& (whereColumns.length == whereArgs.length)) {
				wc = "";
				for (int i = 0; i < whereColumns.length; i++) {
					wc += whereColumns[i] + "=?";
					if (i < (whereColumns.length - 1)) {
						wc += " AND ";
					}
				}
			}
			String oc = null;
			if (orderColumns != null && orderColumns.length > 0) {
				oc = "";
				for (int i = 0; i < orderColumns.length; i++) {
					oc += orderColumns[i];
					if (i < (orderColumns.length - 1)) {
						oc += ",";
					}
				}
				oc += asc ? " ASC" : " DESC";
			}

			ContentResolver resolver = activity.getContentResolver();
			Cursor c = resolver.query(uri, null, wc, whereArgs, oc);
			activity.startManagingCursor(c);

			while (c.moveToNext()) {
				Object object = ctor.newInstance(new Object[] { c });
				objects.add(object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objects;
	}

	/**
	 * Query to content provider to get records count based on column criteria
	 * 
	 * @param activity
	 * @param className
	 * @param whereColumns
	 * @param whereArgs
	 * @return int
	 */
	public static int getObjectCount(Activity activity, 
			@SuppressWarnings("rawtypes") Class className,
			String[] whereColumns, String[] whereArgs) {

		int count = 0;

		try {
			Class<?> clazz = Class.forName(className.getName());
			Uri uri = (Uri) clazz.getDeclaredField("CONTENT_URI").get(clazz);
			String wc = null;
			if ((whereColumns != null && whereColumns.length > 0)
					&& (whereArgs != null && whereArgs.length > 0)
					&& (whereColumns.length == whereArgs.length)) {
				wc = "";
				for (int i = 0; i < whereColumns.length; i++) {
					wc += whereColumns[i] + "=?";
					if (i < (whereColumns.length - 1)) {
						wc += " AND ";
					}
				}
			}
			
			ContentResolver resolver = activity.getContentResolver();
			Cursor c = resolver.query(uri, null, wc, whereArgs, null);
			count = c.getCount();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * Query to content provider to records based on column criteria, like query
	 * and can be ordered as well
	 * 
	 * @param activity
	 * @param className
	 * @param whereColumns
	 * @param likeType
	 * @param whereArgs
	 * @param orderColumns
	 * @param asc
	 * @return {@link List}
	 */
	public static List<Object> getObjectsByLike(Activity activity,
			@SuppressWarnings("rawtypes") Class className, 
			String[] whereColumns, String[] whereArgs,
			int likeType, String[] orderColumns, boolean asc) {

		List<Object> objects = new ArrayList<Object>();

		try {
			Class<?> clazz = Class.forName(className.getName());
			Constructor<?> ctor = clazz.getConstructor(Cursor.class);
			Uri uri = (Uri) clazz.getDeclaredField("CONTENT_URI").get(clazz);
			String wc = null;
			String[] wa = new String[whereArgs.length];
			if ((whereColumns != null && whereColumns.length > 0)
					&& (whereArgs != null && whereArgs.length > 0)
					&& (whereColumns.length == whereArgs.length)) {
				wc = "";
				for (int i = 0; i < whereColumns.length; i++) {
					wc += whereColumns[i] + " LIKE ?";
					if (i < (whereColumns.length - 1)) {
						wc += " AND ";
					}
				}
				for (int i = 0; i < whereArgs.length; i++) {
					switch (likeType) {
					case STARTS_WITH:
						wa[i] = "%" + whereArgs[i];
						break;
					case CONTAINS_WITH:
						wa[i] = "%" + whereArgs[i] + "%";
						break;
					case ENDS_WITH:
						wa[i] = whereArgs[i] + "%";
						break;
					default:
						break;
					}
				}
			}
			String oc = null;
			if (orderColumns != null && orderColumns.length > 0) {
				oc = "";
				for (int i = 0; i < orderColumns.length; i++) {
					oc += orderColumns[i];
					if (i < (orderColumns.length - 1)) {
						oc += ",";
					}
				}
				oc += asc ? " ASC" : " DESC";
			}

			ContentResolver resolver = activity.getContentResolver();
			Cursor c = resolver.query(uri, null, wc, wa, oc);
			activity.startManagingCursor(c);

			while (c.moveToNext()) {
				Object object = ctor.newInstance(new Object[] { c });
				objects.add(object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objects;
	}
	
	/**
	 * Bulk insert to content provider
	 * 
	 * @param activity
	 * @param object
	 * @return {@link integer}
	 */
	public static int bulkInsert(Activity activity, Class className, Object object) {
		try {
			List<Object> objects = (List<Object>) object;
			ContentResolver resolver = activity.getContentResolver();
			Class<?> clazz = Class.forName(className.getName());
			Constructor<?> ctor = clazz.getConstructor(Cursor.class);
			Uri uri = (Uri) clazz.getDeclaredField("CONTENT_URI").get(clazz);
			
			return resolver.bulkInsert(uri, getContentValues(className, objects));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Construct Content Values
	 * @param objects
	 * @return {@link List}
	 */
	private static ContentValues[] getContentValues(Class clazz, List<Object> objects){
		ContentValues[] values = new ContentValues[objects.size()];
		try {
			for(Object o : objects) {
				Method m = clazz.getDeclaredMethod("getContentValues", new Class[] {});
				ContentValues cv = (ContentValues) m.invoke(o, null);
				values[objects.indexOf(o)] = cv; 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}
}
