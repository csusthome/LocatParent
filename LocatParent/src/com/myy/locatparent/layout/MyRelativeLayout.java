package com.myy.locatparent.layout;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MyRelativeLayout extends RelativeLayout {

	public MyRelativeLayout(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		super.onInterceptTouchEvent(ev);
		//�����¼�ʹ�¼��ڸ�Layout�������
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		performClick();
		super.onTouchEvent(event);
		return true;
	}
	
	
}
