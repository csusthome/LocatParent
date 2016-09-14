package com.myy.loactparent.popupwinow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.myy.locatparent.R;

public class FanceCustomPopupWindow extends PopupWindow {
	private LayoutInflater inflater;
	private Activity activity;
	private View view;
	//保存自定义围栏浮动窗口中的四个按钮
	private TextView tv_confirm,tv_extend,tv_shrink,tv_cancle;
		
	public FanceCustomPopupWindow(Activity context) {
		
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.fancecustom_pop, null);
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		// 使用MeasureSpec后才能正确的得到popwindow的宽高
		view.measure(w, h);
		initTextView();
		
		// 设置了具体的宽高后才能在上方显示
		this.setContentView(view);
		this.setWidth(view.getMeasuredWidth());
		this.setHeight(view.getMeasuredHeight());
		// 设置一个空的背景才能够点击其他地方
		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);
		setFocusable(true);
		setOutsideTouchable(true);
	}
	
	private void initTextView()
	{
		tv_confirm = (TextView)view.findViewById(R.id.tv_confirm);
		tv_extend = (TextView)view.findViewById(R.id.tv_extend);
		tv_shrink = (TextView)view.findViewById(R.id.tv_shrink);
		tv_cancle = (TextView)view.findViewById(R.id.tv_cancle);
	}
	
	/**
	 * 传递4个点击监听器分别对应，围栏自定义的4个按钮
	 * @param listners
	 */
	public void setTextViewOnClickListeners(OnClickListener [] listners)
	{
		tv_confirm.setOnClickListener(listners[0]);
		tv_extend.setOnClickListener(listners[1]);
		tv_shrink.setOnClickListener(listners[2]);
		tv_cancle.setOnClickListener(listners[3]);
	}
	
	
	
}
