package com.myy.locatparent.task;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Fencing;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.fragment.ActionListFragment;

/**
 * 解绑子端任务，不能调用两次(未完成)
 * @author lenovo-Myy
 *
 */
public class RemoveFenceTask extends AsyncTask<Object, String, Boolean> {

	private static final boolean DEBUG = true;
	private LoadingPopupWindow loading_pop;
	private Exception e;
	private List<Fencing> fence_list;
	private ActionListFragment list_frgm ;
	private List<String> result_list = new LinkedList<String>();
	public RemoveFenceTask(List<Fencing> fence_list) {
		this.fence_list = fence_list;
	}
	
	/**
	 * 将显示列表与任务绑定，用于完成任务完成后的子项删除
	 * @param frgm
	 */
	public void bingActionListFragment(ActionListFragment frgm)
	{
		this.list_frgm = frgm;
	}

	public void bindLoadingPopupWindow(LoadingPopupWindow window) {
		loading_pop = window;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loading_pop != null) {
			loading_pop.setText("网络连接中...");
		}
	}

	@Override
	protected Boolean doInBackground(Object... parpam) {
		try {
			String str_result;
			for(Fencing fence:fence_list)
			{
				str_result = DataExchangeUtils.delFencings(fence.getId());
				result_list.add(str_result);
			}
		} catch (IOException e) {
			e.printStackTrace();
			this.e = e;
		}
		return null;
	}

	private boolean checkResult()
	{
		for(String str_result:result_list)
		{
			if(str_result.trim().equals("false"))
			{
				return false;
			}
			
		}
		return true;
	}
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		//若有异常
		if (e != null) {
			if(e instanceof TimeoutException)
			{
				MainActivity.showMessage("连接服务器超时，请检查当前网络配置");
			}
			else
			{
				MainActivity.showMessage("异常：" + e.getMessage());
			}
			if(loading_pop!=null&&loading_pop.isShowing())
			{
				loading_pop.dismiss();
			}
		}
		if(checkResult()==false)
		{
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("部分围栏移除失败，请重试");
		}
		else
		{
			//若已经绑定了列表视图则更新视图
			if(list_frgm!=null)
			{
				list_frgm.deleteCheckedItems();
			}
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("围栏删除成功");
		}
		
	}

}
