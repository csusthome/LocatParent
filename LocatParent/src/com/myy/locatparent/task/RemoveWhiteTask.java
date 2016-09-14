package com.myy.locatparent.task;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.WhiteNum;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.fragment.ActionListFragment;

/**
 * 解绑子端任务，不能调用两次(未完成)
 * @author lenovo-Myy
 *
 */
public class RemoveWhiteTask extends AsyncTask<Object, String, Boolean> {

	private static final boolean DEBUG = true;
	private LoadingPopupWindow loading_pop;
	private Exception e;
	private List<String> result_list = new LinkedList<String>();
	private List<WhiteNum> white_list;
	private ActionListFragment list_frgm ;
	
	public RemoveWhiteTask(List<WhiteNum> white_list) {
		this.white_list = white_list;
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

	private boolean checkResult()
	{
		for(String str_result:result_list)
		{
			if(str_result.equals("false"))
			{
				return false;
			}
			
		}
		return true;
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
			for(WhiteNum white:white_list)
			{
				result_list.add(DataExchangeUtils.delWhiteNum(white));
			}
		} catch (IOException e) {
			e.printStackTrace();
			this.e = e;
		}
		return null;
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
		if (result_list.size()<=0||checkResult()==false) {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("移除白名单失败，请重试");
		} else {
			//若已经绑定了列表视图则更新视图
			if(list_frgm!=null)
			{
				list_frgm.deleteCheckedItems();
			}
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("移除白名单成功");
		}
		
	}

}
