package com.myy.locatparent.listen;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class OperationPopClickListener implements OnTouchListener {

	private ViewGroup vg;
	private Drawable bg;
	public OperationPopClickListener(ViewGroup vg)
	{
		this.vg = vg;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if(event.equals(MotionEvent.ACTION_DOWN))
		{
			bg = vg.getBackground();
			vg.setBackgroundColor(Color.BLUE);
		}
		else if(event.equals(MotionEvent.ACTION_UP))
		{
			vg.setBackground(bg);
		}
		v.performClick();
		return false;
	}


}
