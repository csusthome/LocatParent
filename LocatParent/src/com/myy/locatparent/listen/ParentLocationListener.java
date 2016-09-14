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
 * ���˶�λ����������Ҫ���ڵ���ʱ��λ�õĶ�λ��ֻ��Ҫ����һ��
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
		builder.append("ʱ��:"+str_date+"\n");
		builder.append("����:"+location.getLongitude()+"\n");
		builder.append("γ��:"+location.getLatitude()+"\n");
		
		if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS��λ���
			builder.append("�ص���Ϣ gps��λ�ɹ�"+"\n");
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// ���綨λ���
			builder.append("�ص���Ϣ ���綨λ�ɹ�"+"\n");
		} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// ���߶�λ���
			builder.append("�ص���Ϣ ���߶�λ�ɹ�"+"\n");
		} else if (location.getLocType() == BDLocation.TypeServerError) {
			builder.append("�ص���Ϣ ��������綨λʧ�ܣ�"+
		"���Է���IMEI�źʹ��嶨λʱ�䵽loc-bugs@baidu.com��������׷��ԭ��"+"\n");
		} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
			builder.append("�ص���Ϣ ���粻ͬ���¶�λʧ�ܣ�" +
					"���������Ƿ�ͨ��"+"\n");
		} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
			builder.append("�ص���Ϣ �޷���ȡ��Ч��λ���ݵ��¶�λʧ��" +
					",һ���������ֻ���ԭ�򣬴��ڷ���ģʽ��һ���������ֽ�����������������ֻ�"+"\n");
		}
		
		LogUtils.d(ParentLocationListener.class.getName(),builder.toString());
//		LocatParentApplication.setCurPoint(p);
		//���ö�λ�ɹ��ص�����
		if(callback!=null)
		{
			callback.onLocatSuccess(p);
		}
		//��ѯ��һ�κ���ر�
		LocatParentApplication.stopLocatService();
		//�رռ���������Ϊ��λ���񲻻�����ֹͣ
		isRun=false;
		//�رճ�ʱ������
		if(thread!=null&&thread.isAlive())
		{
			thread.stopThread();
		}
	}
	
	/**
	 * ����һ�β�ѯ����Ҫ���ò����ٴ����ü�����
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
	 * ���ڼ�ⶨλ�Ƿ�ʱ
	 * @author lenovo-Myy
	 *
	 */
	private class TimeOutThread extends Thread
	{
		private static final int timeoutSecond = 15;//15��
		private boolean flag = true;
		
		public void stopThread()
		{
			flag = false;
		}
		
		@Override
		public void run() {
			super.run();
			LogUtils.d(TimeOutThread.class.getName(),"��λ��ʱ�����̿���");
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
			//����־λtrue������ʱ�䣬��˵���Ѿ���ʱ
			if(flag==true)
			{
				LocatParentApplication.stopLocatService();
				if(callback!=null)
				{
					callback.onLocatFailure();
				}
			}
			LogUtils.d(TimeOutThread.class.getName(),"��λ��ʱ�����̹ر�");
		}
		
	}
	
}
