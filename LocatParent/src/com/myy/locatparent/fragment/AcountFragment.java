package com.myy.locatparent.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.Pupillus;
import com.ms.utils.bean.User;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.AcountListViewAdapter;
import com.myy.locatparent.application.LocatParentApplication;

public class AcountFragment extends IDFragment {

	public static final String ID = "AcountFragment";
	private View view = null;
	private TextView tv_main,tv_vice;
	private ViewGroup vg_notify,vg_acount,vg_exit;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.acount_frgm, container, false);
		initOptionList();
		initUserMessage();
		return view;
	}

	@Override
	public String getID() {
		return this.ID;
	}
	
	/**
	 * 初始化用户信息模块
	 */
	private void initUserMessage()
	{
		tv_main = (TextView)view.findViewById(R.id.tv_maindesc);
		tv_vice = (TextView)view.findViewById(R.id.tv_vicedesc);
	
		if(LocatParentApplication.getUser()!=null)
		{
			tv_main.setText(LocatParentApplication.getUser().getName());
		}
	}
	
	private void initOptionList() {
		vg_acount = (ViewGroup)view.findViewById(R.id.ll_acount);
		vg_exit = (ViewGroup)view.findViewById(R.id.ll_exit);
		vg_notify = (ViewGroup)view.findViewById(R.id.ll_notify);
		
		vg_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.showConfirmDialog("注销确认", "您确认要注销当前账户吗?",
						new DialogInterface.OnClickListener() {
							// 确认键事件
							public void onClick(DialogInterface dialog,
									int which) {
								logout();
							}
						} // 取消键事件
						, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
			}
		});
		vg_acount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.showMessage("账户管理功能尚未完成");
			}
		});
		
		vg_notify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.showMessage("通知推送管理功能尚未完成");
			}
		});
	}

	/**
	 * 注销的相关操作
	 */
	private void logout()
	{
		LocatParentApplication.setUser(null);
		MainActivity.showFragment(LoginFragment.ID);
	}
	

}
