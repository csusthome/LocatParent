package com.myy.locatparent.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Fencing;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.ImgTextListViewAdapter;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.beans.Fance.FENCETYPE;
import com.myy.locatparent.task.QueryFenceTask;
import com.myy.locatparent.task.RemoveFenceTask;
import com.myy.locatparent.utils.EntityUtils;
import com.myy.locatparent.utils.FenceUtils;
import com.myy.locatparent.utils.LogUtils;

public class FenceListFrgment extends ActionListFragment {

	
	public FenceListFrgment()
	{
		super(getFencesData());
		init();
	}
	
	public FenceListFrgment(List<ImgTextListViewAdapter.Item> list_childs)
	{
		super(list_childs);
		init();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		LogUtils.d(FenceListFrgment.class.getName(),"onCreatView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onDestroy() {
		LogUtils.d(FenceListFrgment.class.getName(),"onDestory");
		super.onDestroy();
	}

	private void initMain()
	{
		Fencing mainFence = LocatParentApplication.getMainFence();
		if(mainFence!=null)
		{
			setMainItemById(mainFence.getId());
		}
	}
	private void init()
	{
		this.setNegativeButtonVisible(true);
		this.setMainSignEnable(true);
    	this.setPostiveButtonVisible(true);
    	//设置监听器
    	this.setPostiveCallback(new PosivitveCallBack() {
			@Override
			public void callback(View v) {
				MainActivity.addFance(FENCETYPE.CRICLE);
				//关闭当前活动
				if(getActivity() instanceof MainActivity == false)
				{
					getActivity().finish();
				}
			}
		});
    	
    	this.setNegativeCallback(new NegativeCallBack() {
			@Override
			public void callback(List<Item> checkedItems, View view) {
				List<Fencing> fence_list = new LinkedList<Fencing>();
				for(Item item:checkedItems)
				{
					fence_list.add(FenceUtils.getFencingByItem(item));
				}
				RemoveFenceTask task = new RemoveFenceTask(fence_list);
				task.bindLoadingPopupWindow(MainActivity.showLoadingPopupWindow
						(view, Gravity.CENTER));
				task.bingActionListFragment(FenceListFrgment.this);
				task.execute();
				
			}
		});
    	
    	this.setRefreshCallback(new OnRefresCallBack() {
			@Override
			public void onRefesh(SwipeRefreshLayout srl) {
				updateFenceData(FenceListFrgment.this,null);
			}
		});
    	if(itemclick_listener==null)
    	{
    		setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					setCurFence(position);
				}
			});
    	}
    	initMain();
    	List<Fencing> list_fence = LocatParentApplication.getList_fences();
    	if(list_fence==null||list_fence.size()<=0)
    	{
    		updateFenceData(this,null);
    	}
	}

	public static ArrayList<ImgTextListViewAdapter.Item> getTestFanceListData()
	{
		ArrayList<ImgTextListViewAdapter.Item> list = new ArrayList<ImgTextListViewAdapter.Item>();
		for(int i=0;i<10;i++)
		{
			list.add(new ImgTextListViewAdapter.Item(0, R.drawable.visible,
					R.drawable.fance,"围栏"+i,"经度：23.22 纬度：22:45"));
		}
		return list;
	}
	
	public static ArrayList<ImgTextListViewAdapter.Item> getFencesData() {
		ArrayList<ImgTextListViewAdapter.Item> list = new ArrayList<ImgTextListViewAdapter.Item>();
		List<Fencing> list_bean = LocatParentApplication.getList_fences();
		Item item;
		for(Fencing fence:list_bean)
		{
			item = FenceUtils.getItemByFence(fence);
			list.add(item);
		}
		return list;
	}
	
	/**
	 * 更新围栏列表的数据
	 * @param list_frgm
	 */
	public static void updateFenceData(ActionListFragment list_frgm,MapFragment map_frgm)
	{
		Entity curEntity = LocatParentApplication.getCurEntity();
		//若未查询子端，先查询子端
		if(curEntity==null)
		{
			EntityListFragment.updateEntityData(null);
			MainActivity.showMessage("正在初始化子端信息或未绑定任何子端，请稍后再试");
			return;
		}
		else
		{
			QueryFenceTask task = new QueryFenceTask(curEntity);
			task.bindActionListFragment(list_frgm);
			task.bindMapFragment(map_frgm);
			task.execute();
		}
	}

	public void setCurFence(int position)
	{
		List<Fencing> listFence = LocatParentApplication.getList_fences();
		Fencing fence = listFence.get(position);
		LocatParentApplication.setCurFence(fence);
	}
	
	public void setMainItem(int position) {
		setMainItemByPosition(position);
//		Item item = getItemByPostion(position);
		List<Fencing> listFence = LocatParentApplication.getList_fences();
		Fencing fence = listFence.get(position);
		LocatParentApplication.setMainFence(fence);
	}

}
