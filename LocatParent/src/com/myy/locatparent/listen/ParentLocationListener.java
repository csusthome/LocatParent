package com.myy.locatparent.listen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.ms.utils.bean.Point;
import com.ms.utils.bean.Pupillus;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.task.LoginTask;
import com.myy.locatparent.utils.LogUtils;

/**
 * 父端定位监听器，主要用于导航时对位置的定位，只需要调用一次
 * @author lenovo-Myy
 *
 */
public class ParentLocationListener implements BDLocationListener {

//	private Context context = null;
	public static String DATEFOMAT ="yyyy-MM-dd HH:mm:ss" ;
	private LocateCallBack callback;
	private TimeOutThread thread;
	private boolean isRun=true;
	
	public void setLocatCallBack(LocateCallBack callback)
	{
		this.callback = callback;
	}
	
	private boolean pointIsAvialue(Point p)
	{
		double defaultPoint = 4.9E-324;
		if(p.getLatitude()==0.0d&&p.getLongitude()==0.0d)
		{
			return false;
		}
		if(p.getLatitude()==defaultPoint&&p.getLongitude()==defaultPoint)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public void onReceiveLocation(BDLocation location) {
		if(thread==null||thread.isAlive()==false)
		{
			thread = new TimeOutThread();
			thread.start();
		}
		// TODO Auto-generated method stub
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat(DATEFOMAT);
		String str_date = sdf.format(date);
		Point p = new Point();
		p.setLatitude(location.getLatitude());
		p.setLongitude(location.getLongitude());
		if(pointIsAvialue(p)==false||isRun==false)
		{
			return ;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("时间:"+str_date+"\n");
		builder.append("经度:"+location.getLongitude()+"\n");
		builder.append("纬度:"+location.getLatitude()+"\n");
		
		if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
			builder.append("回调信息 gps定位成功"+"\n");
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
			builder.append("回调信息 网络定位成功"+"\n");
		} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
			builder.append("回调信息 离线定位成功"+"\n");
		} else if (location.getLocType() == BDLocation.TypeServerError) {
			builder.append("回调信息 服务端网络定位失败，"+
		"可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因"+"\n");
		} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
			builder.append("回调信息 网络不同导致定位失败，" +
					"请检查网络是否通畅"+"\n");
		} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
			builder.append("回调信息 无法获取有效定位依据导致定位失败" +
					",一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机"+"\n");
		}
		
		LogUtils.d(ParentLocationListener.class.getName(),builder.toString());
//		LocatParentApplication.setCurPoint(p);
		//调用定位成功回调函数
		if(callback!=null)
		{
			callback.onLocatSuccess(p);
		}
		//查询过一次后则关闭
		LocatParentApplication.stopLocatService();
		//关闭监听器，因为定位服务不会马上停止
		isRun=false;
		//关闭超时检测进程
		if(thread!=null&&thread.isAlive())
		{
			thread.stopThread();
		}
	}
	
	/**
	 * 做完一次查询后需要重置才能再次启用监听器
	 */
	public void reset()
	{
		isRun = true;
	}
	
	public static interface LocateCallBack
	{
		public void onLocatSuccess(Point point);
		public void onLocatFailure();
	}
	
	/**
	 * 用于检测定位是否超时
	 * @author lenovo-Myy
	 *
	 */
	private class TimeOutThread extends Thread
	{
		private static final int timeoutSecond = 15;//15秒
		private boolean flag = true;
		
		public void stopThread()
		{
			flag = false;
		}
		
		@Override
		public void run() {
			super.run();
			LogUtils.d(TimeOutThread.class.getName(),"定位超时检测进程开启");
			int i=0;
			while(i<=timeoutSecond&&flag==true)
			{
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
			}
			//若标志位true但超过时间，则说明已经超时
			if(flag==true)
			{
				LocatParentApplication.stopLocatService();
				if(callback!=null)
				{
					callback.onLocatFailure();
				}
			}
			LogUtils.d(TimeOutThread.class.getName(),"定位超时检测进程关闭");
		}
		
	}
	
}
