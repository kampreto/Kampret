/**
 * Copyright (c) 2010 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.util;

/**
 * Class : DynamicComparator.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.Serializable;

public final class DynamicComparator implements Comparator, Serializable {

	private static final long serialVersionUID = 5205106848208110465L;
	private String method;
	private boolean sortAsc;

	public static final int EQUAL = 0;
	public static final int LESS_THAN = -1;
	public static final int GREATER_THAN = 1;

	private DynamicComparator(Collection collection, String field,
			boolean sortAsc) {
		super();
		this.method = constructMethodName(field);
		this.sortAsc = sortAsc;
	}

	private final static String constructMethodName(String name) {
		StringBuffer fieldName = new StringBuffer("get");
		fieldName.append(name.substring(0, 1).toUpperCase());
		fieldName.append(name.substring(1));
		return fieldName.toString();
	}

	public static void sort(Collection collection, String field, boolean sortAsc) {
		Collections.sort((List) collection, new DynamicComparator(collection,
				field, sortAsc));
	}

	public int compare(Object o1, Object o2) {
		boolean val1Null = false;
		boolean val2Null = false;
		try {
			// determine the return type
			Method method = getMethod(o1);
			final String returnType = method.getReturnType().getName();

			if (returnType.equals("java.lang.String")) {
				final String f1 = (String) invoke(method, o1);

				method = getMethod(o2);
				final String f2 = (String) invoke(method, o2);

				if (f1 != null && f2 != null) {
					return f1.compareTo(f2) * getSortOrder();
				}

				if (f1 == null && f2 != null) {
					return LESS_THAN * getSortOrder();
				}

				if (f1 != null && f2 == null) {
					return GREATER_THAN * getSortOrder();
				}

				if (f1 == null && f2 == null) {
					return EQUAL * getSortOrder();
				}
			}

			if (returnType.equals("int") || returnType.equals("java.lang.Integer")) {
				int f1 = 0;
				try {
					f1 = ((Integer) invoke(method, o1)).intValue();
				} catch (NullPointerException npe) {
					val1Null = true;
				}

				method = getMethod(o2);

				int f2 = 0;
				try {
					f2 = ((Integer) invoke(method, o2)).intValue();
				} catch (NullPointerException npe) {
					val2Null = true;
				}

				if (!val1Null && !val2Null) {
					int retType = 0;
					if (f1 == f2)
						retType = EQUAL;
					if (f1 < f2)
						retType = LESS_THAN;
					if (f1 > f2)
						retType = GREATER_THAN;
					return retType * getSortOrder();
				}

				if (val1Null && !val2Null) {
					return LESS_THAN * getSortOrder();
				}

				if (!val1Null && val2Null) {
					return GREATER_THAN * getSortOrder();
				}

				if (val1Null && val2Null) {
					return EQUAL * getSortOrder();
				}
			}
			
			if (returnType.equals("short") || returnType.equals("java.lang.Short")) {
				short f1 = 0;
				try {
					f1 = ((Short) invoke(method, o1)).shortValue();
				} catch (NullPointerException npe) {
					val1Null = true;
				}

				method = getMethod(o2);

				short f2 = 0;
				try {
					f2 = ((Short) invoke(method, o2)).shortValue();
				} catch (NullPointerException npe) {
					val2Null = true;
				}

				if (!val1Null && !val2Null) {
					int retType = 0;
					if (f1 == f2)
						retType = EQUAL;
					if (f1 < f2)
						retType = LESS_THAN;
					if (f1 > f2)
						retType = GREATER_THAN;
					return retType * getSortOrder();
				}

				if (val1Null && !val2Null) {
					return LESS_THAN * getSortOrder();
				}

				if (!val1Null && val2Null) {
					return GREATER_THAN * getSortOrder();
				}

				if (val1Null && val2Null) {
					return EQUAL * getSortOrder();
				}
			}
			
			if (returnType.equals("float") || returnType.equals("java.lang.Float")) {
				float f1 = 0;
				try {
					f1 = ((Float) invoke(method, o1)).floatValue();
				} catch (NullPointerException npe) {
					val1Null = true;
				}

				method = getMethod(o2);

				float f2 = 0;
				try {
					f2 = ((Float) invoke(method, o2)).floatValue();
				} catch (NullPointerException npe) {
					val2Null = true;
				}

				if (!val1Null && !val2Null) {
					int retType = 0;
					if (f1 == f2)
						retType = EQUAL;
					if (f1 < f2)
						retType = LESS_THAN;
					if (f1 > f2)
						retType = GREATER_THAN;
					return retType * getSortOrder();
				}

				if (val1Null && !val2Null) {
					return LESS_THAN * getSortOrder();
				}

				if (!val1Null && val2Null) {
					return GREATER_THAN * getSortOrder();
				}

				if (val1Null && val2Null) {
					return EQUAL * getSortOrder();
				}
			}

			if (returnType.equals("double") || returnType.equals("java.lang.Double")) {
				double f1 = 0;
				try {
					f1 = ((Double) invoke(method, o1)).doubleValue();
				} catch (NullPointerException npe) {
					val1Null = true;
				}

				method = getMethod(o2);

				double f2 = 0;
				try {
					f2 = ((Double) invoke(method, o2)).doubleValue();
				} catch (NullPointerException npe) {
					val2Null = true;
				}

				if (!val1Null && !val2Null) {
					int retType = 0;
					if (f1 == f2)
						retType = EQUAL;
					if (f1 < f2)
						retType = LESS_THAN;
					if (f1 > f2)
						retType = GREATER_THAN;
					return retType * getSortOrder();
				}

				if (val1Null && !val2Null) {
					return LESS_THAN * getSortOrder();
				}

				if (!val1Null && val2Null) {
					return GREATER_THAN * getSortOrder();
				}

				if (val1Null && val2Null) {
					return EQUAL * getSortOrder();
				}
			}

			throw new RuntimeException(
					"DynamicComparator does not currently support "
							+ returnType + "!");
		} catch (NoSuchMethodException nsme) {
			return LESS_THAN;
		} catch (IllegalAccessException iae) {
			return LESS_THAN;
		} catch (InvocationTargetException ite) {
			return LESS_THAN;
		}
	}

	/**
	 * Not used for sorting. Only here to meet the requirements of the
	 * Comparator interface.
	 * 
	 * @param o
	 *            The object for comparison
	 * @return boolean
	 */
	public boolean equals(Object o) {
		return true;
	}

	private final Method getMethod(Object o) throws NoSuchMethodException {
		return o.getClass().getMethod(method, null);
	}

	private final static Object invoke(Method method, Object o)
			throws InvocationTargetException, IllegalAccessException {
		return method.invoke(o, null);
	}

	/**
	 * 
	 * @return -1 to change the sort order if appropriate.
	 */
	private int getSortOrder() {
		return sortAsc ? 1 : -1;
	}

}
