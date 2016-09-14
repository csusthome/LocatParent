package com.myy.locatparent.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Point;
import com.ms.utils.bean.Pupillus;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.fragment.ActionListFragment;
import com.myy.locatparent.fragment.MapFragment;
import com.myy.locatparent.utils.LogUtils;

/**
 * 解绑子端任务，不能调用两次(未完成)
 * 
 * @author lenovo-Myy
 * 
 */
public class QueryHistoryTraceTask extends AsyncTask<Object, String, Boolean> {

	private LoadingPopupWindow loading_pop;
	private Exception e;
	private Pupillus pup;
	private ActionListFragment list_frgm;
	private SwipeRefreshLayout srl;
	private MapFragment frag_map = null;
	private Date date = null;
	List<Point> point_list;
	private static Integer NUM = 0;

	private QueryHistoryTraceTask(Date date, Pupillus pup) {
		this.date = date;
		this.pup = pup;
	}

	public static synchronized QueryHistoryTraceTask getInstance(Date date,
			Pupillus pup) {
		if (NUM <= 0 && pup != null && pup.getId() > 0) {
			return new QueryHistoryTraceTask(date, pup);
		} else {
			return null;
		}
	}

	/**
	 * 绑定刷新进度条，用于任务结束时停止刷新
	 * 
	 * @param srl
	 */
	public void bindSwipeRefreshLayou(SwipeRefreshLayout srl) {
		this.srl = srl;
	}

	/**
	 * 与进度浮动窗口绑定，用于完成状态更新
	 * 
	 * @param window
	 */
	public void bindLoadingPopupWindow(LoadingPopupWindow window) {
		loading_pop = window;
	}

	/**
	 * 与地图碎片绑定,在任务执行完成后将会更新地图碎片中的围栏列表
	 * 
	 * @param frag_map
	 */
	public void bindMapFragment(MapFragment frag_map) {
		this.frag_map = frag_map;
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
			LogUtils.i("DownHistroyThread", "开始查询历史轨迹");
			point_list = DataExchangeUtils.getHistory(pup.getId(), date);
		} catch (Exception e) {
			e.printStackTrace();
			this.e = e;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		synchronized (NUM) {
			NUM--;
		}
		// 若已绑定刷新布局，则关闭刷新
		if (srl != null) {
			srl.setRefreshing(false);
		}
		// 若有异常
		if (e != null) {
			if (e instanceof TimeoutException) {
				MainActivity.showMessage("连接服务器超时，请检查当前网络配置");
			} else {
				MainActivity.showMessage("异常：" + e.getMessage());
			}
			if (loading_pop != null && loading_pop.isShowing()) {
				loading_pop.dismiss();
			}
			return ;
		}

		if (point_list == null || point_list.size() <= 0) {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			if (list_frgm != null) {
				list_frgm.updateItems(null);
			}
			MainActivity.showMessage("无历史轨迹信息，请重试");
		} else {
			int gap = 1;
			List<Point> p_list = new ArrayList<Point>();
			for (int i = 0; i < point_list.size(); i += gap) {
				p_list.add(point_list.get(i));
			}
			if (frag_map != null) {
				frag_map.showHistoryTraceMarker(p_list);
			}
			MainActivity.showMessage("查询历史轨迹完成");
		}
		if (loading_pop != null) {
			loading_pop.dismiss();
		}
		

	}

}
