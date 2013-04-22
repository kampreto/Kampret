/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class : CollectionUtil.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class CollectionUtil {

	/**
	 * Sort collection by param given
	 * 
	 * @param collection
	 * @param field
	 * @param sortAsc
	 */
	public static void sort(Collection collection, String field, boolean sortAsc) {
		DynamicComparator.sort(collection, field, sortAsc);
	}

	/**
	 * Sort string list by param given
	 * 
	 * @param list
	 * @param sortAsc
	 */
	public static void sort(List<String> list, boolean sortAsc) {
		if (sortAsc) {
			Collections.sort(list);
		} else {
			Collections.sort(list, new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					return o2.compareTo(o1);
				}
			});
		}
	}

	public static void main(String[] args) {
		String[] array = new String[] { "bravo", "romeo", "hotel", "tango", "alpha", "zulu" };
		
		List<String> list = Arrays.asList(array);
		sort(list, false);
	}
}
