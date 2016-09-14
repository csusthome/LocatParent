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
 * ����Ӷ����񣬲��ܵ�������(δ���)
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
	 * ��ˢ�½������������������ʱֹͣˢ��
	 * 
	 * @param srl
	 */
	public void bindSwipeRefreshLayou(SwipeRefreshLayout srl) {
		this.srl = srl;
	}

	/**
	 * ����ȸ������ڰ󶨣��������״̬����
	 * 
	 * @param window
	 */
	public void bindLoadingPopupWindow(LoadingPopupWindow window) {
		loading_pop = window;
	}

	/**
	 * ���ͼ��Ƭ��,������ִ����ɺ󽫻���µ�ͼ��Ƭ�е�Χ���б�
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
			loading_pop.setText("���Ժ�");
		}
	}

	@Override
	protected Boolean doInBackground(Object... parpam) {
		try {
			LogUtils.i("DownHistroyThread", "��ʼ��ѯ��ʷ�켣");
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
		// ���Ѱ�ˢ�²��֣���ر�ˢ��
		if (srl != null) {
			srl.setRefreshing(false);
		}
		// �����쳣
		if (e != null) {
			if (e instanceof TimeoutException) {
				MainActivity.showMessage("���ӷ�������ʱ�����鵱ǰ��������");
			} else {
				MainActivity.showMessage("�쳣��" + e.getMessage());
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
			MainActivity.showMessage("����ʷ�켣��Ϣ��������");
		} else {
			int gap = 1;
			List<Point> p_list = new ArrayList<Point>();
			for (int i = 0; i < point_list.size(); i += gap) {
				p_list.add(point_list.get(i));
			}
			if (frag_map != null) {
				frag_map.showHistoryTraceMarker(p_list);
			}
			MainActivity.showMessage("��ѯ��ʷ�켣���");
		}
		if (loading_pop != null) {
			loading_pop.dismiss();
		}
		

	}

}
