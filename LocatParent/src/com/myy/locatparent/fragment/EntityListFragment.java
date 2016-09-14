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
 * �Ӷ���ʾ�б�
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
	 * ������һ������Item,�������õ�ǰID
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
	 * ��ʼ���û���Ϣģ��
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
	 * ��ʼ���Ӷ��б�ListView
	 */
	private void initListViewFragment() {
		if (childs_frgm == null) {
			fm = getChildFragmentManager();
			if (DEBUG == true) {
				childs_frgm = new ActionListFragment(getTestData());
			} else if (DEBUG == false) {
				List<Item> item_list = getClientData();
				//�������б�Ϊ��
				if(item_list==null||item_list.size()<=0)
				{
					updateEntityData(childs_frgm);
				}
				childs_frgm = new ActionListFragment(item_list);
			}
			// ���Ѿ���ѡ���Ӷ�
			Pupillus mainPup = LocatParentApplication.getMainPupillus();
			if (mainPup!= null) {
				int childID = mainPup.getId();
				childs_frgm.setMainItemById(childID);
			}
			
			if (itemclick_listner != null) {
				childs_frgm.setOnItemClickListener(itemclick_listner);
			} else// Ĭ�ϵ�����������
			{
				childs_frgm.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
//						ClientListFragment.this.setMainItem(position);
						//���õ�ǰʵ��
						setCurrentItem(position);
						showClientInfoFragment();
					}
				});
			}
			// ����ˢ�»ص�����
			childs_frgm.setRefreshCallback(new OnRefresCallBack() {
				@Override
				public void onRefesh(SwipeRefreshLayout srl) {
					// ��ѯ�Ӷ��б�
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
						addchild_dlg = new AddChildDialog(getActivity(), "����Ӷ�");
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
			// ����Ӷ��б���Ƭ
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
		// ��������
		for (int i = 0; i < 10; i++) {
			list.add(new ImgTextListViewAdapter.Item(0, R.drawable.visible,
					R.drawable.child, "�Ӷ�" + i, "IMEI:12314212"));
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
	 * ��������������µ�ʵ���б�ֻ�е��û���ʼ���ɹ������Ч
     * ��Ϊִ�в�ѯΧ���Լ�֮��Ĳ�������Ҫ�ȵõ��Ӷ���Ϣ�����Կ���ʱ����Ҫ��
     * ��ʼ���Ӷ���Ϣ
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
