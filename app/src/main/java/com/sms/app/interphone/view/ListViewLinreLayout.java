package com.sms.app.interphone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


public class ListViewLinreLayout extends ListView {

	public ListViewLinreLayout(Context context) {
		super(context);
	}

	public ListViewLinreLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewLinreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

