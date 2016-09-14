package com.myy.locatparent.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.myy.locatparent.R;


public class AcountListViewAdapter extends BaseAdapter {

	private ArrayList<ChildItem> list = new ArrayList<AcountListViewAdapter.ChildItem>();
	private Context context = null;
	
	
	public AcountListViewAdapter(Context context) {
		super();
		this.context=context;
		initData();
	}

	private void initData()
	{
		if(list.size()<=0)
		{
			list.add(new ChildItem(R.drawable.ic_launcher,"修改密码",true));
			list.add(new ChildItem(R.drawable.eraser,"消息推送",true));
		}
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vholder = null;
		if(convertView==null)
		{
			vholder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.imgswitch_item,parent,false);
			ImageView iv_func = (ImageView)convertView.findViewById(R.id.iv_func);
			Switch sw_func = (Switch)convertView.findViewById(R.id.switch_but);
			TextView tv_func = (TextView)convertView.findViewById(R.id.tv_func);
			vholder.iv_func=iv_func;
			vholder.sw_func=sw_func;
			vholder.tv_func=tv_func;
			convertView.setTag(vholder);
		}
		else
		{
			vholder=(ViewHolder)convertView.getTag();
		}
		ChildItem item  = list.get(position);
		vholder.iv_func.setImageResource(item.img_id);
		vholder.tv_func.setText(item.func_name);
		//不显示开关按钮
		vholder.sw_func.setVisibility(View.INVISIBLE);
//		vholder.sw_func.setChecked(item.is_checked);
		return convertView;
	}

	private class ChildItem
	{
		public int img_id;
		public String func_name;
		public boolean is_checked;
		
		public ChildItem(int img_id,String func_name, boolean is_opend) {
			super();
			this.img_id = img_id;
			this.func_name = func_name;
			this.is_checked = is_opend;
		}
		
	}
	
	private class ViewHolder
	{
		ImageView iv_func;
		TextView tv_func;
		Switch sw_func;
	}
}
