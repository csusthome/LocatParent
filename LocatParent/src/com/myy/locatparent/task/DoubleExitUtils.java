package com.myy.locatparent.task;

import com.myy.locatparent.activity.MainActivity;

/**
 * 用于实现双击退出键退出程序的功能
 * @author lenovo-Myy
 *
 */
public class DoubleExitUtils {

	//用于记录上次点击的时间
	private long lastTime=0;
	//两次按键时间不超过1秒
	private final int duration = 2000;
	
	public boolean exitCheck()
	{
		long curTime = System.currentTimeMillis();
		long gap = curTime-lastTime;
		lastTime = curTime;
		if(gap>duration)
		{
			MainActivity.showMessage("再次点击回退键退出应用");
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public void reset()
	{
		lastTime = 0;
	}

}
