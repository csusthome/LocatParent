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
	//�����Զ���Χ�����������е��ĸ���ť
	private TextView tv_confirm,tv_extend,tv_shrink,tv_cancle;
		
	public FanceCustomPopupWindow(Activity context) {
		
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.fancecustom_pop, null);
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		// ʹ��MeasureSpec�������ȷ�ĵõ�popwindow�Ŀ��
		view.measure(w, h);
		initTextView();
		
		// �����˾���Ŀ�ߺ�������Ϸ���ʾ
		this.setContentView(view);
		this.setWidth(view.getMeasuredWidth());
		this.setHeight(view.getMeasuredHeight());
		// ����һ���յı������ܹ���������ط�
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
	 * ����4������������ֱ��Ӧ��Χ���Զ����4����ť
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
