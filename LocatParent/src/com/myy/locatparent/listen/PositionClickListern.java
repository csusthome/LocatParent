package com.myy.locatparent.listen;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * ����ʵ����λ����ص�Click������
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
