package com.myy.locatparent.listen;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;

import com.myy.locatparent.R;

public class PopWindowClickListener implements OnClickListener {

	private static final int BG_PRE = R.drawable.popbg_shape;
	private Drawable bg = null;
	private boolean is_pred = false;
	private View vg;
	private CallBack callback = null;
	
	public PopWindowClickListener(View v)
	{
		this.vg = v;
	}

	public void setCallBack(CallBack callback)
	{
		this.callback = callback;
	}
	
	/**
	 * 设置按钮的选中状态,不调用按钮事件
	 * @param checked
	 */
	public void setViewChecked(boolean checked)
	{
		if(bg==null)
		{
			bg = vg.getBackground();
		}
		if(checked!=is_pred)
		{
			if(checked==true)
			{
				vg.setBackgroundResource(BG_PRE);
			}
			else
			{
				vg.setBackgroundDrawable(bg);
			}
			is_pred = checked;
		}
	}
	
	public boolean getIsChecked()
	{
		return is_pred;
	}
	
	@Override
	public void onClick(View v) {
		setViewChecked(!is_pred);
		if(callback!=null)
		{
			callback.onCallBack(is_pred);
		}
	}
	
	/**
	 * 用于自定义让浮动窗口实现点击事件，在浮动窗口监听器总需要传入
	 * @author lenovo-Myy
	 *
	 */
	public static interface CallBack
	{
		public void onCallBack(boolean is_pred);
	}

}
