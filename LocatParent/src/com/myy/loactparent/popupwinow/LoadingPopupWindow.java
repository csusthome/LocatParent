package com.myy.loactparent.popupwinow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.myy.locatparent.R;

public class LoadingPopupWindow extends PopupWindow {
	private LayoutInflater inflater;
	private View view = null;
	private TextView tv_desc = null;

	public LoadingPopupWindow(Activity activity) {

		inflater = LayoutInflater.from(activity);
		view = inflater.inflate(R.layout.loading_layout, null);
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
		setBackgroundDrawable(null);
		setTouchable(true);
		setFocusable(true);
		setOutsideTouchable(false);
	}

	private void initTextView() {
		tv_desc = (TextView) view.findViewById(R.id.tv_desc);
	}

	/**
	 * ���ü��ؿ��е����壬��󳤶�Ϊ6���������ֲ���ʾ
	 * @param desc
	 */
	public void setText(String desc) {
		tv_desc.setText(desc);
	}

}
