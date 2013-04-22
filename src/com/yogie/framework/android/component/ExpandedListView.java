/**
 * Copyright (c) 2012 Yogie Kurniawan. All rights reserved.
 */
package com.yogie.framework.android.component;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Class : ExpandedListView.java
 * 
 * @author <a href="mailto:yogiekurn@gmail.com">Yogie Kurniawan</a>
 */
public class ExpandedListView extends ListView {

	private android.view.ViewGroup.LayoutParams params;

	public ExpandedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		params = getLayoutParams();
		params.height = 0;
		for (int i = 0; i < getCount(); i++) {
			params.height += getChildAt(i) != null ? getChildAt(i).getHeight()
					: getChildAt(0).getHeight();
		}
		setLayoutParams(params);
		super.onDraw(canvas);
	}
}
