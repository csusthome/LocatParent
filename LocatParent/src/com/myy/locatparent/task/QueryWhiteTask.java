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
 * ��ѯ�Ӷ˰������б����񣬲��ܵ�������(δ���)
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
	 * ����ʾ�б�������󶨣��������������ɸ�������
	 * @param frgm
	 */
	public void bingActionListFragment(ActionListFragment frgm)
	{
		this.list_frgm = frgm;
	}
	/**
	 * ��ˢ�½������������������ʱֹͣˢ��
	 * @param srl
	 */
	public void bindSwipeRefreshLayou(SwipeRefreshLayout srl)
	{
		this.srl = srl;
	}
	
	/**
	 * ����ȸ������ڰ󶨣��������״̬����
	 * @param window
	 */
	public void bindLoadingPopupWindow(LoadingPopupWindow window) {
		loading_pop = window;
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
		//���Ѱ�ˢ�²��֣���ر�ˢ��
		if(srl!=null)
		{
			srl.setRefreshing(false);
		}
		//�����쳣
		if (e != null) {
			if(e instanceof TimeoutException)
			{
				MainActivity.showMessage("���ӷ�������ʱ�����鵱ǰ��������");
			}
			else
			{
				MainActivity.showMessage("�쳣��" + e.getMessage());
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
			MainActivity.showMessage("�ް�������Ϣ��������");
		} else {
			//���Ѿ������б���ͼ�������ͼ
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
			MainActivity.showMessage("��ѯ�������б����");
		}
		
	}

}
