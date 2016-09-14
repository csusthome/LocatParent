package com.myy.locatparent.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

import com.ms.utils.bean.AnomalyRecord;
import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.Pupillus;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.ImgTextListViewAdapter;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.task.QueryAnomalyTask;
import com.myy.locatparent.task.QueryFenceTask;
import com.myy.locatparent.utils.AnomalyUtils;
import com.myy.locatparent.utils.FenceUtils;
import com.myy.locatparent.utils.LogUtils;

/**
 * 紧急情况列表碎片
 * @author lenovo-Myy
 *
 */
public class AnomalyListFrgment extends ActionListFragment {

	public static final int ITEM_IMG = R.drawable.attention;
	public static final int VISIBLE_IMG = R.drawable.visible;
	private static final boolean DEBUG = false;
	
//	private OnItemClickListener itemClick_listner = null;
	public AnomalyListFrgment()
	{
		super(getAnomalyData());
		init();
	}
	
	public AnomalyListFrgment(List<ImgTextListViewAdapter.Item> list_childs)
	{
		super(list_childs);
		init();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtils.d(AnomalyListFrgment.class.getName(),"onDestoryView");
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		LogUtils.d(AnomalyListFrgment.class.getName(),"onCreatView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	public void setOnItemClickListner(OnItemClickListener listener)
	{
		super.setOnItemClickListener(listener);
	}
	
	private void init()
	{
    	this.setNegativeButtonVisible(false);
    	this.setMainSignEnable(false);
    	this.setPostiveButtonVisible(false);
    	this.setRefreshCallback(new OnRefresCallBack() {
			@Override
			public void onRefesh(SwipeRefreshLayout srl) {
				Pupillus pup = LocatParentApplication.getCurPupullus();
				if(pup!=null)
				{
					QueryAnomalyTask task = new QueryAnomalyTask(pup);
					task.bingActionListFragment(AnomalyListFrgment.this);
					task.bindSwipeRefreshLayou(srl);
					task.execute();
				}
				else
				{
					MainActivity.showMessage("你没有子端或尚未同步完成，请稍后再试");
				}
			}
		});
	}


	public static ArrayList<ImgTextListViewAdapter.Item> getTestWarnListData()
	{
		ArrayList<ImgTextListViewAdapter.Item> list = new ArrayList<ImgTextListViewAdapter.Item>();
		for(int i=0;i<10;i++)
		{
			String type = null;
			if(i%2==0)
			{
				type = "低电量";
			}
			else
			{
				type = "信号丢失";
			}
			list.add(new ImgTextListViewAdapter.Item(0, R.drawable.visible,
					R.drawable.attention,"警报："+type,"经度：23.22 纬度：22:45 时间 2016-5-14 17:36"));
		}
		return list;
	}
	
	public static ArrayList<ImgTextListViewAdapter.Item> getAnomalyData() {
		ArrayList<ImgTextListViewAdapter.Item> list = new ArrayList<ImgTextListViewAdapter.Item>();
		List<AnomalyRecord> list_bean = LocatParentApplication.getList_anomalys();
		Item item;
		for(AnomalyRecord record:list_bean)
		{
			item = AnomalyUtils.getItemByAnomaly(record);
			list.add(item);
		}
		return list;
	}
	
}
