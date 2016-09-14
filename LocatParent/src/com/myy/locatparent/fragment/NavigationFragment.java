package com.myy.locatparent.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.baidu.platform.comapi.map.m;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.fragment.MainFragment.MSGTYPE;

public class NavigationFragment extends Fragment implements OnClickListener {

	public static final String ID = "NavigationFragment";
	private Handler main_handler = null;
	private View view = null;
	private ArrayList<ImageButton> ibut_list = new ArrayList<ImageButton>();
	private ArrayList<ItemData> data_list = new ArrayList<ItemData>();
	//用于记录被点击的Tab
	private int checked_pos = 0;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.navigation_frgm, container,false);
		//得到MainAcitivity的Handler
		main_handler = MainActivity.getHandler();
		initItemData();
		initNavigationBar();
		return view;
	}
	
	private void initNavigationBar()
	{
		ImageButton ibut_map = (ImageButton)view.findViewById(R.id.ibut_map);
		ImageButton ibut_client = (ImageButton)view.findViewById(R.id.ibut_client);
		ImageButton ibut_acount = (ImageButton)view.findViewById(R.id.ibut_acount);
		
		ibut_map.setOnClickListener(this);
		ibut_client.setOnClickListener(this);
		ibut_acount.setOnClickListener(this);
		
		ibut_list.add(ibut_map);
		ibut_list.add(ibut_client);
		ibut_list.add(ibut_acount);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.ibut_map) {
			checked_pos=0;
		} else if (id == R.id.ibut_client) {
			checked_pos=1;
		} else if (id == R.id.ibut_acount) {
			checked_pos=2;
		}
		sendSwitchFragmentMessage();
		updateNviagationBar();
		
	}

	/**
	 * 向主窗体发出切换内存页面的请求
	 */
	private void sendSwitchFragmentMessage()
	{
		Message msg = main_handler.obtainMessage();
		msg.what = MSGTYPE.FRAGMNET.ordinal();
		switch (checked_pos) {
		case 0:
			msg.obj=MapFragment.ID;
			break;
		case 1:
			msg.obj=EntityListFragment.ID;
			break;
		case 2:
			msg.obj=AcountFragment.ID;
			break;
		}
		main_handler.sendMessage(msg);
		
	}
	
	public void setCheckedPoosition(int pos)
	{
		this.checked_pos = pos;
		updateNviagationBar();
	}
	
	private void updateNviagationBar()
	{
		ImageButton ibut=null;
		for(int i=0;i<ibut_list.size();i++)
		{
			ibut = ibut_list.get(i);
			if(i==checked_pos)
			{
				//设为按下的图标
				ibut.setImageResource(data_list.get(i).img_pre);
			}
			else
			{
				//设为默认的图标
				ibut.setImageResource(data_list.get(i).img_defualt);
			}
		}
	}
	
	private void initItemData()
	{
		data_list.add(new ItemData(R.drawable.map,R.drawable.map_pre,"监控"));
		data_list.add(new ItemData(R.drawable.kid,R.drawable.kid_pre,"子端"));
		data_list.add(new ItemData(R.drawable.user,R.drawable.user_pre,"父端"));
	}
	
	private class ItemData
	{
		int img_defualt,img_pre;
		String name;
		
		public ItemData(int img_defualt, int img_pre, String name) {
			super();
			this.img_defualt = img_defualt;
			this.img_pre = img_pre;
			this.name = name;
		}
		
	}
	
}
