package com.myy.locatparent.fragment;

import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.myy.locatparent.R;
import com.myy.locatparent.adapter.ImgTextListViewAdapter;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.listen.SwpipeListViewOnScrollListener;

public class ActionListFragment extends IDFragment implements OnRefreshListener{

	private View view = null;
	protected ViewGroup vg_postvie=null,vg_negative=null,vg_button=null;
	private ListView lv_items = null;
	//只用于保存初始化时传入的子项数据，并不和适配器中的数据关联
	private List<ImgTextListViewAdapter.Item> list_childs = null;
	private SwipeRefreshLayout srl = null;
	private ViewGroup rl_content = null;
	private TextView tv_desc;
	private ImgTextListViewAdapter lv_adapter = null;
	//用于保存子项点击监听器，因为父组件会自主设置监听器，但在oncrearView前设置是无用的
	//所以保存监听器实例下来在OncreatView后再设置
	protected OnItemClickListener itemclick_listener = null;
	protected OnItemLongClickListener longclick_listener = null;
	private PosivitveCallBack positive_callback;
	private NegativeCallBack negative_callback;
	private OnRefresCallBack refresh_callback;
	private Dialog  comfirm_dlg;
	//用于记录功能键的可见性
	private boolean func_visible = false,post_visible=true,nega_visible=true,mainsign_enable=true;
	private boolean desc_visible = true;
	
	protected Integer mainItemPos,mainItemID;
	//用于静态创建需要的无参构造函数
	public ActionListFragment()
	{
		
	}
	
	public ActionListFragment(List<ImgTextListViewAdapter.Item> list_childs)
	{
		this.list_childs = list_childs;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(view==null)
		{
			view = inflater.inflate(R.layout.funclistview_frgm,container,false);
			initPullToRefresh();
			initButton();
			initContentLayout();
			initListView();
		}
		initDescTextView();
		return view;
	}
	
	@Override
	public String getID() {
		return this.ID;
	}

	private void initContentLayout()
	{
		rl_content = (ViewGroup)view.findViewById(R.id.rl_content);
	}
	
	private void initListView()
	{
		
		if(list_childs==null)
		{
			list_childs = new LinkedList<Item>();
		}
		if(lv_adapter==null)
		{
			lv_adapter = new ImgTextListViewAdapter(getActivity(), list_childs);
			lv_adapter.setSignFuncEanble(mainsign_enable);
		}
		if(lv_items==null)
		{
			lv_items = (ListView)view.findViewById(R.id.fl_items);
			lv_items.setAdapter(lv_adapter);
			//设置子项点击监听器
			lv_items.setOnItemClickListener(itemclick_listener);
			lv_items.setOnItemLongClickListener(longclick_listener);
			lv_items.setOnScrollListener(new SwpipeListViewOnScrollListener(srl));
		}
		//初始化前只有通过ID来设置MainItem
		if(mainItemID!=null)
		{
			setMainItemById(mainItemID);
		}
//		initDescTextView();
	}
	
	private void initDescTextView()
	{
		if(list_childs!=null&&list_childs.size()>0)
		{
			setDescribeVisible(false);
		}
		else
		{
			setDescribeVisible(true);
		}
	}
	/**
	 * 用新的子项数据,更新列表信息
	 * @param items 若为空则清空列表
	 */
	public void updateItems(List<Item> items)
	{
		//设置List数据
		lv_adapter.updateItems(items);
		//设置主要Item
		if(mainItemID!=null)
		{
			setMainItemById(mainItemID);
		}
		if(items==null||items.size()<=0)
		{
			setDescribeVisible(true);
		}
		else
		{
			setDescribeVisible(false);
		}
		
	}
	
	/**
	 * 用户自定义的子项点击事件
	 * @param listener
	 */
	public void setOnItemClickListener(OnItemClickListener listener)
	{
		if(view!=null)
		{
			lv_items.setOnItemClickListener(listener);
		}
		itemclick_listener = listener;
	}
	
	public void setonItemLongClickListener(OnItemLongClickListener listener)
	{
		if(view!=null)
		{
			lv_items.setOnItemLongClickListener(listener);
		}
		longclick_listener = listener;
	}
	
	public void setPostiveCallback(PosivitveCallBack callback)
	{
		positive_callback = callback;
	}
	
	public void setNegativeCallback(NegativeCallBack callback)
	{
		negative_callback = callback;
	}
	
	public void setRefreshCallback(OnRefresCallBack callback)
	{
		refresh_callback = callback;
	}
	
	
	private void initButton()
	{
		vg_negative = (ViewGroup)view.findViewById(R.id.rl_negative);
		vg_postvie = (ViewGroup)view.findViewById(R.id.rl_positive);
		vg_button = (ViewGroup)view.findViewById(R.id.ll_action);
		//设置的参数不能在父组件初始化的时候就调用因为View没创建
		LinearLayout.LayoutParams params = null;
		if(post_visible==true)
		{
			params = (LinearLayout.LayoutParams)vg_postvie.getLayoutParams();
			params.weight=1;
			vg_postvie.setLayoutParams(params);
		}
		else
		{
			params = (LinearLayout.LayoutParams)vg_postvie.getLayoutParams();
			params.weight=0;
			vg_postvie.setLayoutParams(params);
		}
		if(nega_visible==true)
		{
			params = (LinearLayout.LayoutParams)vg_negative.getLayoutParams();
			params.weight=1;
			vg_negative.setLayoutParams(params);
		}
		else
		{
			params = (LinearLayout.LayoutParams)vg_negative.getLayoutParams();
			params.weight=0;
			vg_negative.setLayoutParams(params);
		}
		//若添加与删除都隐藏了则将功能框也隐藏
		if(post_visible==false&&nega_visible==false)
		{
			android.view.ViewGroup.LayoutParams but_params = vg_button.getLayoutParams();
			but_params.height=0;
			vg_button.setLayoutParams(but_params);
		}
		
		vg_postvie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//设置按钮监听器
				if(post_visible==true&&positive_callback!=null)
				{
					positive_callback.callback(v);
				}
			}
		});
		
		
		vg_negative.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(nega_visible==true)
				{
					selectCheckedItems();
				}
			}
		});
		
	}
	
	/**
	 * 选中的子项，应该在CallBack任务完成后执行
	 */
	public void selectCheckedItems()
	{
		//本地删除操作
		if(lv_adapter.getCheckedSize()>0&&func_visible==true)
		{
			showDeleteConfirmDialog();
		}
		else
		{
			func_visible = !func_visible;
			setCheckBoxVisible(func_visible);
		}
	}
	/**
	 * 设置标志功能是否可用
	 * @return
	 */
	public ActionListFragment setMainSignEnable(boolean isenable)
	{
		mainsign_enable = isenable;
//		lv_adapter.setSignFuncEanble(isvisible);
		return this;
	}
	
	/**
	 * 设置文字描述信息是否可见，常用于无信息时显示
	 * @param visible
	 */
	private void setDescribeVisible(boolean visible)
	{
		if(view!=null)
		{
			if(tv_desc==null)
			{
				tv_desc = (TextView)view.findViewById(R.id.tv_desc);
			}
			if(visible!=desc_visible)
			{
				LayoutParams param = tv_desc.getLayoutParams();
				if(visible==true)
				{
					param.height = LayoutParams.WRAP_CONTENT;
				}
				else
				{
					param.height = 0;
				}
				tv_desc.setLayoutParams(param);
			}
		}
		desc_visible = visible;
	}
	
	/**
	 * 设置添加键是否可见
	 * @param isvisible
	 */
	public ActionListFragment setPostiveButtonVisible(boolean isvisible)
	{
		post_visible = isvisible;
		return this;
	}
	
	/**
	 * 设置删除键是否可见
	 * @param isvisible
	 */
	public ActionListFragment setNegativeButtonVisible(boolean isvisible)
	{
		nega_visible = isvisible;
		return this;
	}
	
	
	/**
	 * 初始化下拉刷新
	 */
	private void initPullToRefresh()
	{
		srl = (SwipeRefreshLayout)view.findViewById(R.id.srl_client);
		srl.setOnRefreshListener(this);
		srl.setColorSchemeResources(R.drawable.refresh_1,R.drawable.refresh_2,
				R.drawable.refresh_3,R.drawable.refresh_4);
	}
	
	
	/**
	 * 设置多选框是否可见
	 * @param is_visible
	 */
	private void setCheckBoxVisible(boolean is_visible)
	{
		lv_adapter.setCheckBoxVisible(is_visible);
	}
	
	/**
	 * 显示删除子项对话框，并进行后续处理
	 */
	private void showDeleteConfirmDialog()
	{
		if(comfirm_dlg==null)
		{
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("确认删除选中项？");
		builder.setTitle("删除确认");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(nega_visible==true&&negative_callback==null)
				{
					deleteCheckedItems();
				}
				else if(nega_visible==true&&negative_callback!=null)
				{
					negative_callback.callback(lv_adapter.getCheckedItemList(),vg_negative);
				}
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		comfirm_dlg = builder.create();
		}
		comfirm_dlg.show();
	}
	
	
	@Override
	public void onDestroyView() {
		//先接触Fragment与View的关系
		((ViewGroup)getView()).removeView(view);
		super.onDestroyView();
	}

	/**
	 * 删除被选中的Item，（本地删除）
	 */
	public void deleteCheckedItems()
	{
		lv_adapter.deleteCheckedItems();;
	}
	
	/**
	 * 停止加载动画
	 */
	public void stopRefresh()
	{
		if(srl!=null&&srl.isRefreshing())
		{
			srl.setRefreshing(false);
		}
	}
	/**
	 * 通过设置Refreshing来中止刷新,应该在任务结束后调用
	 */
	@Override
	public void onRefresh() {
		//通过设置Refreshing来中止刷新,
		if(refresh_callback!=null)
		{
			refresh_callback.onRefesh(srl);
		}

	}
	
	public void setMainItemByPosition(int position)
	{
		mainItemPos = position;
		if(view == null)
		{
			return ;
		}
		lv_adapter.setMainPosition(position);
	}
	
	public void setMainItemById(int itemID)
	{
		mainItemID = itemID;
		//若ListView还未初始化先返回，会在初始化时设置主要Item
		if(view==null)
		{
			return ;
		}
		lv_adapter.setMainItem(itemID);
	}
	
//	public Item getItemByPostion(int position)
//	{
//		return (Item) lv_adapter.getItem(position);
//	}
	
	/**
	 * 添加Item并更新列表
	 * @param item
	 */
	public void addItem(Item item)
	{
		lv_adapter.addItem(item);
	}
	
	/**
	 * 删除并更新列表
	 * @param itemID
	 */
	public void removeItem(int itemID)
	{
		lv_adapter.removeItemByItemID(itemID);
	}
	
	public static interface NegativeCallBack
	{
		/**
		 * 若设置，则会在执行本地删除子项的默认方法selectCheckedItems
		 * 点击确认删除后执行
		 * 若不设置，则会在点击删除子项后执行lv_adapter.deleteItems()
		 * @param checkedItems 被选中删除的Item列表
		 */
		public void callback(List<Item> checkedItems,View view);
	}
	
	public static interface PosivitveCallBack
	{
		public void callback(View v);
	}
	
	public static interface OnRefresCallBack
	{
		public void onRefesh(SwipeRefreshLayout srl);
	}
}
