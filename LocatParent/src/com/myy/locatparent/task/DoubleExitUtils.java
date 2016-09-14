package com.myy.locatparent.task;

import com.myy.locatparent.activity.MainActivity;

/**
 * ����ʵ��˫���˳����˳�����Ĺ���
 * @author lenovo-Myy
 *
 */
public class DoubleExitUtils {

	//���ڼ�¼�ϴε����ʱ��
	private long lastTime=0;
	//���ΰ���ʱ�䲻����1��
	private final int duration = 2000;
	
	public boolean exitCheck()
	{
		long curTime = System.currentTimeMillis();
		long gap = curTime-lastTime;
		lastTime = curTime;
		if(gap>duration)
		{
			MainActivity.showMessage("�ٴε�����˼��˳�Ӧ��");
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
