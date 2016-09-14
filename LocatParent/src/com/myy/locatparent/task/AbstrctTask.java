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
 * 异步任务基类,可以设置同时执行同一任务的最大上限,还封装了其他基本逻辑
 * ps:因为这个基类是后面打算重用代码才做的，而改之前的Task工作量太大
 * 所以有部分就没改了，sorry
 * @author lenovo-Myy
 *
 */
public abstract class AbstrctTask extends AsyncTask<Object, String, Boolean> {
	
	protected String taskName = "任务执行"; 
	protected LoadingPopupWindow loading_pop;
	private Exception e;
	private MaxLimiter limiter;
	//标记是否可运行，在测试运行上限时设置
	private boolean isRunable=false;
	//用于记录任务执行结果
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
	 * 返回任务实例
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
			loading_pop.setText(taskName+"中..");
		}
		if(limiter!=null)
		{
			isRunable = limiter.beforeStart();
			LogUtils.d(taskName,taskName+" onpreExecute:"+limiter.toString()+" max:"+limiter.maxNum+" cur:"+limiter.curNum);
			if(isRunable==false)
			{
				MainActivity.showMessage(this.taskName
						+"已经在执行中,请勿多次操作");
				LogUtils.i(AbstrctTask.class.getName(),this.taskName
						+"任务执行达到上限:"+limiter.getMaxNum());
			}
		}
		else
		{
			//若未绑定上限检测器，则默认通过
			isRunable = true;
		}
	}

	protected final void setTaskName(String name)
	{
		this.taskName = name;
	}
	
	
	@Override	
	protected Boolean doInBackground(Object... params) {
		//若初始检测未通过则跳过执行
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
				LogUtils.i(taskName, "任务初始化失败，已取消");
				return;
			}
			limiter.afterProcess();
			LogUtils.d(taskName,taskName+" afterProcess:"+limiter.toString()+" max:"+limiter.maxNum+" cur:"+limiter.curNum);
		}
		if (e != null) {
			if (e instanceof TimeoutException
					|| e instanceof SocketTimeoutException
					|| e instanceof NoHttpResponseException) {
				LogUtils.i(AbstrctTask.class.getName(),taskName+"异常：连接服务器超时，请检查当前网络配置");
				MainActivity.showMessage(taskName+"异常：连接服务器超时，请检查当前网络配置");
			} else {
				MainActivity.showMessage(taskName+"异常：" + e.getMessage());
				LogUtils.i(AbstrctTask.class.getName(),taskName+"异常：" + e.getMessage());
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
			
			LogUtils.i(AbstrctTask.class.getName(),taskName+"失败");
			boolean isCut = doInFail();
			if(isCut==false)
			{
				MainActivity.showMessage(taskName+"失败");
			}
		} else {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			LogUtils.i(AbstrctTask.class.getName(),taskName+"成功");
			boolean isCut = doInSuccess();
			if(isCut==false)
			{
				MainActivity.showMessage(taskName+"成功");
			}
		}
	}
	/**
	 * 需要异步执行的线程在这里写
	 * @return 任务成功返回true
	 */
	protected abstract boolean process()throws Exception;
	
	/**
	 * 当任务执行成功,在主线程运行
	 * @return 为ture则截断消息不再传递
	 */
	protected abstract boolean doInSuccess();
	/**
	 * 当任务执行失败后或出错后执行,在主线程运行
	 * @return 为ture则截断消息不再传递
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
		 * 执行于任务开始前，看是否达到任务上限
		 * @return true通过初始化检验
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
		 * 在执行完后台服务后执行
		 */
		private void afterProcess()
		{
			synchronized (curNum) {
				curNum--;
			}
		}
		
	}
}
