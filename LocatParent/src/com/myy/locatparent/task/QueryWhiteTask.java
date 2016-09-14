package com.myy.locatparent.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.Pupillus;
import com.ms.utils.bean.WhiteNum;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.fragment.ActionListFragment;
import com.myy.locatparent.fragment.TabHostFragment;
import com.myy.locatparent.utils.FenceUtils;
import com.myy.locatparent.utils.WhiteUtils;

/**
 * 查询子端白名单列表任务，不能调用两次(未完成)
 * @author lenovo-Myy
 *
 */
public class QueryWhiteTask extends AsyncTask<Object, String, Boolean> {

	private static final boolean DEBUG = true;
	private LoadingPopupWindow loading_pop;
	private Exception e;
	private ActionListFragment list_frgm ;
	private SwipeRefreshLayout srl;
	private List<WhiteNum> white_list = null;
	private Pupillus pup;
	
	public QueryWhiteTask(Pupillus pup) {
		this.pup = pup;
	}
	
	/**
	 * 将显示列表与任务绑定，用于完成任务完成更新子项
	 * @param frgm
	 */
	public void bingActionListFragment(ActionListFragment frgm)
	{
		this.list_frgm = frgm;
	}
	/**
	 * 绑定刷新进度条，用于任务结束时停止刷新
	 * @param srl
	 */
	public void bindSwipeRefreshLayou(SwipeRefreshLayout srl)
	{
		this.srl = srl;
	}
	
	/**
	 * 与进度浮动窗口绑定，用于完成状态更新
	 * @param window
	 */
	public void bindLoadingPopupWindow(LoadingPopupWindow window) {
		loading_pop = window;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loading_pop != null) {
			loading_pop.setText("请稍后");
		}
	}

	@Override
	protected Boolean doInBackground(Object... parpam) {
		try {
			white_list = DataExchangeUtils.getWhiteList(pup);
		} catch (IOException e) {
			e.printStackTrace();
			this.e = e;
		}
		return null;
	}


	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		//若已绑定刷新布局，则关闭刷新
		if(srl!=null)
		{
			srl.setRefreshing(false);
		}
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
		if (white_list == null || white_list.size()<=0) {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			list_frgm.updateItems(null);
			MainActivity.showMessage("无白名单信息，请重试");
		} else {
			//若已经绑定了列表视图则更新视图
			if(list_frgm!=null)
			{
				List<Item> list = new ArrayList<Item>();
				Item item;
				for(WhiteNum white:white_list)
				{
					item = WhiteUtils.getItemByWhite(white);
					list.add(item);
				}
				list_frgm.updateItems(list);
			}
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			LocatParentApplication.setList_whites(white_list);
			MainActivity.showMessage("查询白名单列表完成");
		}
		
	}

}
