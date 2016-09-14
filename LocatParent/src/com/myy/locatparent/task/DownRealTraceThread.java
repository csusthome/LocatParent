package com.myy.locatparent.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Point;
import com.myy.locatparent.activity.MainActivity;

public class DownRealTraceThread extends Thread {

	private boolean key=true;
	private int duration = 1000;
	private List<Point> real_list = new ArrayList<Point>();
	private int pup_id;
	private Date date; 
	private Point temp_p;
	
	public DownRealTraceThread(int pup_id)
	{
		this.pup_id = pup_id;
	}
	
	public void stopThread()
	{
		key = false;
	}
	@Override
	public void run() {
		super.run();
		while(key)
		{
			List<Point> recive_list = null;
			try {
				date = new Date(System.currentTimeMillis());
				recive_list = DataExchangeUtils.getRealTime(pup_id, date);
			} catch (Exception e) {
				MainActivity.showMessage("获取实时轨迹异常:"+e.getMessage());
				e.printStackTrace();
			}
			if(recive_list==null)
			{
				MainActivity.showMessage("获取实时轨迹异常,无轨迹点");
				continue;
			}
			if(temp_p==null&&recive_list.size()>0)
			{
				temp_p = recive_list.get(0);
				real_list.add(temp_p);
				real_list.add(temp_p);
			}
			else if(temp_p!=null&&recive_list.size()>0)
			{
				Point p = recive_list.get(0);
				if(temp_p.equals(p)==false)
				{
					temp_p = recive_list.get(0);
					real_list.add(temp_p);
				}
			}
			if(real_list.size()>=2)
			{
				MainActivity.showRealTrace(real_list);
//				MainActivity.showHistoryTrace(real_list);
			}
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
				MainActivity.showMessage("获取实时轨迹休眠异常:"+e.getMessage());
				e.printStackTrace();
			}
		}
	}

	
}
