package com.myy.locatparent.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Pupillus;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.application.LocatParentApplication;

public class EntityInfoFragment extends IDFragment {

	public static final String ID = "com.myy.locatparent.fragment.ClientInfoFragment"; 
	private View view = null;
	private ActionListFragment childs_frgm = null;
	private TabHostFragment tabhost_frgm = null;
	private FragmentManager fm = null;
	private ImageView iv_delete;
	private TextView main_desc,vice_desc;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.clientinfo_frgm,container,false);
		initTabhostFragment();
		initImageView();
		initTitleMessage();
		return view;
	}
	
	@Override
	public String getID() {
		return this.ID;
	}
	
	/**
	 * 初始化删除图片按钮
	 */
	private void initImageView()
	{
		iv_delete = (ImageView)view.findViewById(R.id.iv_delete);
		iv_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.showConfirmDialog("删除确认","确认解除与该子端的绑定?"
						, null,null);
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
		Entity curEntity = LocatParentApplication.getCurEntity();
		Pupillus curPup = LocatParentApplication.getCurPupullus();
		if(curEntity!=null)
		{
			main_desc.setText(curEntity.getAlias());
		}
		if(curPup!=null)
		{
			vice_desc.setText(curPup.getMeid());
		}
	}
	
	/**
	 * 初始化切换表头布局
	 */
	private void initTabhostFragment()
	{
		if(fm==null)
		{
			fm = this.getChildFragmentManager();
		}
		tabhost_frgm = new TabHostFragment();
		FragmentTransaction ft = fm.beginTransaction().replace(R.id.fl_tabhost,tabhost_frgm);
		ft.commit();
	}
	

	
}
