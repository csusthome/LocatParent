package com.myy.locatparent.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.FencingRecord;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.fragment.ActionListFragment;
import com.myy.locatparent.utils.FenceRecordUtils;

/**
 * 查询异常情况任务，不能调用两次(未完成)
 * @author lenovo-Myy
 *
 */
public class QueryFenceWaringTask extends AsyncTask<Object, String, Boolean> {

	private static final boolean DEBUG = true;
	private LoadingPopupWindow loading_pop;
	private Exception e;
	private ActionListFragment list_frgm ;
	private SwipeRefreshLayout srl;
	private List<FencingRecord> fr_list = null;
	private Fencing fence;
	private Date date;
	public QueryFenceWaringTask(Fencing fence,Date date) {
		this.fence = fence;
		this.date =date;
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
			fr_list = DataExchangeUtils.getHistoryFencingRecord(fence.getId(),date);
		} catch (Exception e) {
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
		if (fr_list == null || fr_list.size()<=0) {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			list_frgm.updateItems(null);
			MainActivity.showMessage("无围栏信息，请重试");
		} else {
			//若已经绑定了列表视图则更新视图
			if(list_frgm!=null)
			{
				List<Item> list = new ArrayList<Item>();
				Item item;
				for(FencingRecord record:fr_list)
				{
					item = FenceRecordUtils.getItemByFenceRecord(record);
					list.add(item);
				}
				list_frgm.updateItems(list);
			}
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			LocatParentApplication.setList_fenRecords(fr_list);
			MainActivity.showMessage("查询围栏列表完成");
		}
		
	}

}
