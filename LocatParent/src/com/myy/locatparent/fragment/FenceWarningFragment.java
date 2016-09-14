package com.myy.locatparent.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.utils.bean.AnomalyRecord;
import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.FencingRecord;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.ImgTextListViewAdapter;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.fragment.ActionListFragment.OnRefresCallBack;
import com.myy.locatparent.task.QueryAnomalyTask;
import com.myy.locatparent.task.QueryFenceWaringTask;
import com.myy.locatparent.utils.AnomalyUtils;
import com.myy.locatparent.utils.FenceRecordUtils;

public class FenceWarningFragment extends IDFragment {

	private static boolean DEBUG = true;
	public static final String ID = "com.myy.locatparent.fragment.FanceInfoFragment"; 
	public static final int ITEM_IMG = R.drawable.fance;
	public static final int VISIBLE_IMG = R.drawable.visible;
	private View view = null;
	private ActionListFragment childs_frgm = null;
	private DateFragment date_frgm = null;
	private FragmentManager fm = null;
	private TextView main_desc,vice_desc;
	private ImageView iv_delete;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fanceinfo_frgm,container,false);
		initDateFragment();
		initListFragment();
		initTitleMessage();
		initImageView();
		return view;
	}
	
	@Override
	public String getID() {
		return this.ID;
	}
	
	private void initImageView()
	{
		iv_delete = (ImageView)view.findViewById(R.id.iv_delete);
		iv_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.showConfirmDialog("删除确认"
						,"确认删除当前围栏？",null,null);
			}
		});
	}
	
	private void initTitleMessage()
	{
		if(main_desc==null)
		{
			main_desc = (TextView)view.findViewById(R.id.tv_maindesc);
			vice_desc = (TextView)view.findViewById(R.id.tv_vicedesc);
		}
		Fencing fence = LocatParentApplication.getCurFence();
		main_desc.setText(fence.getName());
		vice_desc.setText("经度："+fence.getLongitude()+"纬度："+fence.getLatitude()+
				"半径:"+fence.getRadius()+"米");
	}
	
	/**
	 * 初始化时间选择布局
	 */
	private void initDateFragment()
	{
		if(fm==null)
		{
			fm = this.getChildFragmentManager();
		}
		if(date_frgm==null)
		{
		date_frgm = new DateFragment();
		FragmentTransaction ft = fm.beginTransaction().replace(R.id.fl_data,date_frgm);
		ft.commit();
		}
	}
	
	/**
	 * 初始化围栏历史警报布局
	 */
	private void initListFragment()
	{
		if(childs_frgm==null)
		{
		if(fm==null)
		{
			fm = this.getChildFragmentManager();
		}
		if(DEBUG == true)
		{
			childs_frgm = new ActionListFragment(getFenceWarningData());
		}
		else
		{
			childs_frgm = new ActionListFragment(null);
		}
		childs_frgm.setNegativeButtonVisible(false);
		childs_frgm.setMainSignEnable(false);
		childs_frgm.setPostiveButtonVisible(false);
		childs_frgm.setRefreshCallback(new OnRefresCallBack() {
			@Override
			public void onRefesh(SwipeRefreshLayout srl) {
				Fencing curFence = LocatParentApplication.getCurFence();
				if(curFence!=null)
				{
					try {
					QueryFenceWaringTask task;
					task = new QueryFenceWaringTask
							(curFence,date_frgm.getDate());
					task.bingActionListFragment(childs_frgm);
					task.bindSwipeRefreshLayou(srl);
					task.execute();
					} catch (Exception e) {
						e.printStackTrace();
						MainActivity.showMessage("查询历史围栏警报异常："+e.getMessage());
					}
				}
				else
				{
					MainActivity.showMessage("你没有子端或尚未同步完成，请稍后再试");
				}
			}
		});
		FragmentTransaction ft = fm.beginTransaction().replace(R.id.fl_list,childs_frgm);
		ft.commit();
		}
	}
	
	private static ArrayList<ImgTextListViewAdapter.Item> getFenceWarningData() {
		ArrayList<ImgTextListViewAdapter.Item> list = new ArrayList<ImgTextListViewAdapter.Item>();
		List<FencingRecord> list_bean = LocatParentApplication.getList_fenRecords();
		Item item;
		for(FencingRecord record:list_bean)
		{
			item = FenceRecordUtils.getItemByFenceRecord(record);
			list.add(item);
		}
		return list;
	}
	
	private ArrayList<ImgTextListViewAdapter.Item> getFanceTestWarningData()
	{
		ArrayList<ImgTextListViewAdapter.Item> list = 
				new ArrayList<ImgTextListViewAdapter.Item>();
		//测试数据
		for(int i=0;i<10;i++)
		{
			list.add(new ImgTextListViewAdapter.Item(i,R.drawable.visible,
					R.drawable.fance,
					"围栏警报"+i,"类型：进入围栏"));
		}
		return list;
	}
	
}
