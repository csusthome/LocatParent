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
import android.widget.AdapterView.OnItemClickListener;

import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.Pupillus;
import com.ms.utils.bean.WhiteNum;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.ImgTextListViewAdapter;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.dailog.AddWhiteDialog;
import com.myy.locatparent.task.QueryWhiteTask;
import com.myy.locatparent.task.RemoveWhiteTask;
import com.myy.locatparent.utils.FenceUtils;
import com.myy.locatparent.utils.LogUtils;
import com.myy.locatparent.utils.WhiteUtils;

/**
 * 紧急情况列表碎片
 * @author lenovo-Myy
 *
 */
public class WhiteListFrgment extends ActionListFragment {

	public static final int ITEM_IMG = R.drawable.user;
	public static final int VISIBLE_IMG = R.drawable.visible;
	private static final boolean DEBUG = true;
	private AddWhiteDialog addwhite_dlg = null;
	

	public WhiteListFrgment()
	{
		super(getWhiteListData());
		init();
	}
	
	public WhiteListFrgment(List<ImgTextListViewAdapter.Item> list_childs)
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
	
	public void setOnItemClickListner(OnItemClickListener listener)
	{
		super.setOnItemClickListener(listener);
	}
	
	private void init()
	{
		this.setMainSignEnable(false);
		this.setPostiveButtonVisible(true);
		this.setNegativeButtonVisible(true);
    	//设置监听器
    	this.setPostiveCallback(new PosivitveCallBack() {
			@Override
			public void callback(View v) {
				if(addwhite_dlg==null)
				{
					addwhite_dlg = new AddWhiteDialog(getActivity(),"添加白名单");
				}
				addwhite_dlg.show();
			}
		});
    	this.setNegativeCallback(new NegativeCallBack() {
			@Override
			public void callback(List<Item> checkedItems, View view) {
				List<WhiteNum> white_list = new LinkedList<WhiteNum>();
				for(Item item:checkedItems)
				{
					white_list.add(WhiteUtils.getWhiteByItem(item));
				}
				RemoveWhiteTask task = new RemoveWhiteTask(white_list);
				task.bindLoadingPopupWindow(MainActivity.showLoadingPopupWindow
						(view, Gravity.CENTER));
				task.bingActionListFragment(WhiteListFrgment.this);
				task.execute();
				
			}
		});
    	this.setRefreshCallback(new OnRefresCallBack() {
			@Override
			public void onRefesh(SwipeRefreshLayout srl) {
				Pupillus curChild = LocatParentApplication.getCurPupullus();
				if(curChild!=null)
				{
					QueryWhiteTask task = new QueryWhiteTask(curChild);
					task.bingActionListFragment(WhiteListFrgment.this);
					task.bindSwipeRefreshLayou(srl);
					task.execute();
				}
				else
				{
					MainActivity.showMessage("你没有子端或尚未同步完成，请稍后再试");
					srl.setRefreshing(false);
				}
			}
		});
	}


	public static  ArrayList<ImgTextListViewAdapter.Item> getTestWhiteListData()
	{
		ArrayList<ImgTextListViewAdapter.Item> list = new ArrayList<ImgTextListViewAdapter.Item>();
		for(int i=0;i<4;i++)
		{
			list.add(new ImgTextListViewAdapter.Item(0, R.drawable.visible,
					R.drawable.user,"白名单"+i,"电话：137370787415"));
		}
		return list;
	}
	
	public static ArrayList<ImgTextListViewAdapter.Item> getWhiteListData() {
		ArrayList<ImgTextListViewAdapter.Item> list = new ArrayList<ImgTextListViewAdapter.Item>();
		List<WhiteNum> list_bean = LocatParentApplication.getList_whites();
		Item item;
		for(WhiteNum white:list_bean)
		{
			item = WhiteUtils.getItemByWhite(white);
			list.add(item);
		}
		return list;
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.d(FenceListFrgment.class.getName(),"onDestory");
	}
}
