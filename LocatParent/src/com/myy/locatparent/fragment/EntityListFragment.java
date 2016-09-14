package com.myy.locatparent.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Pupillus;
import com.ms.utils.bean.User;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.ImgTextListViewAdapter;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.dailog.AddChildDialog;
import com.myy.locatparent.fragment.ActionListFragment.NegativeCallBack;
import com.myy.locatparent.fragment.ActionListFragment.OnRefresCallBack;
import com.myy.locatparent.fragment.ActionListFragment.PosivitveCallBack;
import com.myy.locatparent.task.QueryEntityTask;
import com.myy.locatparent.task.UnBindChildTask;
import com.myy.locatparent.utils.EntityUtils;

/**
 * 子端显示列表
 * 
 * @author lenovo-Myy
 * 
 */
public class EntityListFragment extends IDFragment {

	public static final int ITEM_IMG = R.drawable.child;
	public static final int VISIBLE_IMG = R.drawable.visible;
	public static final String ID = "com.myy.locatparent.fragment.ClientListFragment";
	private static final boolean DEBUG = false;
	private View view = null;
	private ActionListFragment childs_frgm = null;
	private FragmentManager fm = null;
	private AddChildDialog addchild_dlg = null;
	private OnItemClickListener itemclick_listner;
	private TextView tv_main,tv_vice;
	private ImageView iv_delete;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.client_frgm, container, false);
		initListViewFragment();
		initUserMessage();
		return view;
	}

	@Override
	public String getID() {
		return this.ID;
	}

	private void initImageView()
	{
		
	}
	
	public Entity getEntityByPostition(int position)
	{
		List<Entity> listEntity = LocatParentApplication.getList_entities();
		Entity entity = listEntity.get(position);
		return entity;
	}
	/**
	 * 设置哪一项是主Item,并且设置当前ID
	 * 
	 * @param pos
	 */
	public void setMainItem(int position) {
		childs_frgm.setMainItemByPosition(position);
		List<Entity> listEntity = LocatParentApplication.getList_entities();
		Entity entity = listEntity.get(position);
		LocatParentApplication.setMainEntity(entity);
		LocatParentApplication.setMainPupillus(entity.getPupillus());
		setCurrentItem(position);
	}
	
	public void setCurrentItem(int position)
	{
//		childs_frgm.setMainItemByPosition(position);
		List<Entity> listEntity = LocatParentApplication.getList_entities();
		Entity entity = listEntity.get(position);
		LocatParentApplication.setCurEntity(entity);
		LocatParentApplication.setCurPupullus(entity.getPupillus());
	}

	/**
	 * 初始化用户信息模块
	 */
	private void initUserMessage()
	{
		tv_main = (TextView)view.findViewById(R.id.tv_maindesc);
		tv_vice = (TextView)view.findViewById(R.id.tv_vicedesc);
	
		User user = LocatParentApplication.getUser();
		if(user!=null)
		{
			tv_main.setText(user.getName());
		}
	}
	/**
	 * 初始化子端列表ListView
	 */
	private void initListViewFragment() {
		if (childs_frgm == null) {
			fm = getChildFragmentManager();
			if (DEBUG == true) {
				childs_frgm = new ActionListFragment(getTestData());
			} else if (DEBUG == false) {
				List<Item> item_list = getClientData();
				//若数据列表为空
				if(item_list==null||item_list.size()<=0)
				{
					updateEntityData(childs_frgm);
				}
				childs_frgm = new ActionListFragment(item_list);
			}
			// 若已经有选定子端
			Pupillus mainPup = LocatParentApplication.getMainPupillus();
			if (mainPup!= null) {
				int childID = mainPup.getId();
				childs_frgm.setMainItemById(childID);
			}
			
			if (itemclick_listner != null) {
				childs_frgm.setOnItemClickListener(itemclick_listner);
			} else// 默认的子项点击函数
			{
				childs_frgm.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
//						ClientListFragment.this.setMainItem(position);
						//设置当前实体
						setCurrentItem(position);
						showClientInfoFragment();
					}
				});
			}
			// 设置刷新回调函数
			childs_frgm.setRefreshCallback(new OnRefresCallBack() {
				@Override
				public void onRefesh(SwipeRefreshLayout srl) {
					// 查询子端列表
					QueryEntityTask task = new QueryEntityTask(LocatParentApplication.getUser());
					if (task != null) {
						task.bindActionListFragment(childs_frgm);
						task.execute();
					}
				}
			});
			childs_frgm.setPostiveCallback(new PosivitveCallBack() {
				@Override
				public void callback(View v) {
					if (addchild_dlg == null) {
						addchild_dlg = new AddChildDialog(getActivity(), "添加子端");
					}
					addchild_dlg.show();
				}
			});
			childs_frgm.setNegativeCallback(new NegativeCallBack() {
				@Override
				public void callback(List<Item> checkedItems, View view) {
					List<Entity> entity_list = new LinkedList<Entity>();
					for(Item item:checkedItems)
					{
						entity_list.add(EntityUtils.getEntityByItem(item));
					}
					UnBindChildTask task = new UnBindChildTask(entity_list);
					task.bindLoadingPopupWindow(MainActivity.showLoadingPopupWindow
							(view, Gravity.CENTER));
					task.bingActionListFragment(childs_frgm);
					task.execute();
					
				}
			});
			// 填充子端列表碎片
			fm.beginTransaction().replace(R.id.fl_childs, childs_frgm).commit();
			
		}

	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		itemclick_listner = listener;
	}

	public void showClientInfoFragment() {
		MainActivity.showFragment(EntityInfoFragment.ID);
	}

	public static ArrayList<ImgTextListViewAdapter.Item> getTestData() {
		ArrayList<ImgTextListViewAdapter.Item> list = new ArrayList<ImgTextListViewAdapter.Item>();
		// 测试数据
		for (int i = 0; i < 10; i++) {
			list.add(new ImgTextListViewAdapter.Item(0, R.drawable.visible,
					R.drawable.child, "子端" + i, "IMEI:12314212"));
		}
		return list;
	}
	
	public static ArrayList<ImgTextListViewAdapter.Item> getClientData() {
		ArrayList<ImgTextListViewAdapter.Item> list = new ArrayList<ImgTextListViewAdapter.Item>();
		List<Entity> list_entity = LocatParentApplication.getList_entities();
		Item item;
		for(Entity entity:list_entity)
		{
			item = EntityUtils.getItemByEntity(entity);
			list.add(item);
		}
		return list;
	}

	/**
	 * 向服务器请求最新的实体列表，只有当用户初始化成功后才有效
     * 因为执行查询围栏以及之后的操作都需要先得到子端信息，所以开启时就需要先
     * 初始化子端信息
     */
	public static void updateEntityData(ActionListFragment frgm) {
		User user = LocatParentApplication.getUser();
		if(user!=null)
		{
			QueryEntityTask task = new QueryEntityTask(user);
			if (task != null) {
				task.bindActionListFragment(frgm);
				task.execute();
			}
		}
	}

}
