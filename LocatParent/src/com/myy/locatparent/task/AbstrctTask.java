package com.myy.locatparent.task;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import org.apache.http.NoHttpResponseException;

import android.R.bool;
import android.os.AsyncTask;

import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.utils.LogUtils;

/**
 * �첽�������,��������ͬʱִ��ͬһ������������,����װ�����������߼�
 * ps:��Ϊ��������Ǻ���������ô�������ģ�����֮ǰ��Task������̫��
 * �����в��־�û���ˣ�sorry
 * @author lenovo-Myy
 *
 */
public abstract class AbstrctTask extends AsyncTask<Object, String, Boolean> {
	
	protected String taskName = "����ִ��"; 
	protected LoadingPopupWindow loading_pop;
	private Exception e;
	private MaxLimiter limiter;
	//����Ƿ�����У��ڲ�����������ʱ����
	private boolean isRunable=false;
	//���ڼ�¼����ִ�н��
	private boolean bool_result=false;

	public final void bindLoadingPopupWindow(LoadingPopupWindow window)
	{
		loading_pop = window;
	}
	
	public final void setMaxLimiter(MaxLimiter limiter)
	{
		this.limiter = limiter;
	}
	/*
	 * ��������ʵ��
	 */
//	public static <E extends AbstrctTask> E getInstance(Class<E> clazz)
//	{
//		synchronized (TASK_NUM) {
//			if(TASK_NUM<=MAX_TASKNUM)
//			{
//				TASK_NUM++;
//				try {
//					return clazz.newInstance();
//				} catch (Exception e) {
//					e.printStackTrace();
//				} 
//			}
//			return null;
//		}
//	}
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(loading_pop!=null)
		{
			loading_pop.setText(taskName+"��..");
		}
		if(limiter!=null)
		{
			isRunable = limiter.beforeStart();
			LogUtils.d(taskName,taskName+" onpreExecute:"+limiter.toString()+" max:"+limiter.maxNum+" cur:"+limiter.curNum);
			if(isRunable==false)
			{
				MainActivity.showMessage(this.taskName
						+"�Ѿ���ִ����,�����β���");
				LogUtils.i(AbstrctTask.class.getName(),this.taskName
						+"����ִ�дﵽ����:"+limiter.getMaxNum());
			}
		}
		else
		{
			//��δ�����޼��������Ĭ��ͨ��
			isRunable = true;
		}
	}

	protected final void setTaskName(String name)
	{
		this.taskName = name;
	}
	
	
	@Override	
	protected Boolean doInBackground(Object... params) {
		//����ʼ���δͨ��������ִ��
		if(isRunable==false)
		{
			bool_result = false;
			return bool_result;
		}
		try {
			bool_result = process();
		} catch (Exception e) {
			this.e = e;
			e.printStackTrace();
		}
		return bool_result;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(limiter!=null)
		{
			if(isRunable==false)
			{
				LogUtils.i(taskName, "�����ʼ��ʧ�ܣ���ȡ��");
				return;
			}
			limiter.afterProcess();
			LogUtils.d(taskName,taskName+" afterProcess:"+limiter.toString()+" max:"+limiter.maxNum+" cur:"+limiter.curNum);
		}
		if (e != null) {
			if (e instanceof TimeoutException
					|| e instanceof SocketTimeoutException
					|| e instanceof NoHttpResponseException) {
				LogUtils.i(AbstrctTask.class.getName(),taskName+"�쳣�����ӷ�������ʱ�����鵱ǰ��������");
				MainActivity.showMessage(taskName+"�쳣�����ӷ�������ʱ�����鵱ǰ��������");
			} else {
				MainActivity.showMessage(taskName+"�쳣��" + e.getMessage());
				LogUtils.i(AbstrctTask.class.getName(),taskName+"�쳣��" + e.getMessage());
			}
			if (loading_pop != null && loading_pop.isShowing()) {
				loading_pop.dismiss();
			}
			doInFail();
			return;
		}
		if (this.bool_result==false) {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			
			LogUtils.i(AbstrctTask.class.getName(),taskName+"ʧ��");
			boolean isCut = doInFail();
			if(isCut==false)
			{
				MainActivity.showMessage(taskName+"ʧ��");
			}
		} else {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			LogUtils.i(AbstrctTask.class.getName(),taskName+"�ɹ�");
			boolean isCut = doInSuccess();
			if(isCut==false)
			{
				MainActivity.showMessage(taskName+"�ɹ�");
			}
		}
	}
	/**
	 * ��Ҫ�첽ִ�е��߳�������д
	 * @return ����ɹ�����true
	 */
	protected abstract boolean process()throws Exception;
	
	/**
	 * ������ִ�гɹ�,�����߳�����
	 * @return Ϊture��ض���Ϣ���ٴ���
	 */
	protected abstract boolean doInSuccess();
	/**
	 * ������ִ��ʧ�ܺ������ִ��,�����߳�����
	 * @return Ϊture��ض���Ϣ���ٴ���
	 */
	protected abstract boolean doInFail();
	
	protected static class MaxLimiter
	{
		private Integer maxNum=1;
		private Integer curNum=0;
		
		public MaxLimiter()
		{
			
		}
		
		public MaxLimiter(int maxNum)
		{
			this.maxNum = maxNum;
		}
		
		
		public Integer getMaxNum() {
			return maxNum;
		}

		public void setMaxNum(Integer maxNum) {
			this.maxNum = maxNum;
		}

		public Integer getCurNum() {
			return curNum;
		}

		public void setCurNum(Integer curNum) {
			this.curNum = curNum;
		}

		/**
		 * ִ��������ʼǰ�����Ƿ�ﵽ��������
		 * @return trueͨ����ʼ������
		 */
		private boolean beforeStart()
		{
			synchronized (curNum) {
				if(curNum<maxNum)
				{
					curNum++;
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		
		/**
		 * ��ִ�����̨�����ִ��
		 */
		private void afterProcess()
		{
			synchronized (curNum) {
				curNum--;
			}
		}
		
	}
}
