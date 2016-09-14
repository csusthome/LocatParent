package com.myy.locatparent.listen;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 用于实现与位置相关的Click监听器
 * @author lenovo-Myy
 *
 */
public class PositionClickListern implements OnClickListener {

	private int postion = -1;
	
	public PositionClickListern(int pos)
	{
		postion = pos;
	}
	
	@Override
	public void onClick(View view) {
		view.getTag();
	}

}
