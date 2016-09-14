package com.myy.locatparent.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.myy.locatparent.R;

/**
 * 
 * @author lenovo-Myy
 *
 */
public class ImgTextListViewAdapter extends BaseAdapter{

	private Context context;
	private List<Item> list;
	//���ڼ�¼�Ѿ�ѡ�е�Item
	private List<Integer> checked_list = new ArrayList<Integer>();
	//�����ʶ��ҪĿ�꣨�Ƿ�ɼ���־��
	private int main_pos = 0;
	private int mainItemID = 0;
	//���ڱ�ʾ���ܼ��ǹ��ɼ�
	private boolean show_func = false;
	//�������ù��ܼ��ĵ���¼�
	private MyOnCheckedChangeListener check_listener = null;
	private boolean signfunc_enable = true;
	
	public ImgTextListViewAdapter(Context context,List<Item> list)
	{
		this.context=context;
		this.list=list;
	}
	
	/**
	 * ���ñ�ʶ�����Ƿ���ã�false���ʶ�����ɼ�
	 */
	public void setSignFuncEanble(boolean enable)
	{
		signfunc_enable = enable;
	}
	
	public int getCheckedSize()
	{
		return checked_list.size();
	}
	
	public List<Item> getCheckedItemList()
	{
		List<Item> items_list = new LinkedList<Item>();
		for(Integer index:checked_list)
		{
			items_list.add(list.get(index));
		}
		return items_list;
	}
	
	/**
	 * ͨ��Item��ID��������Ҫ��־
	 * @param itemID
	 */
	public void setMainItem(int itemID)
	{
		int pos = 0;
		for(Item item:list)
		{
			if(item.getItem_id()==itemID)
			{
				main_pos = pos; 
			}
			pos++;
		}
	}
	
	public void setMainPosition(int pos)
	{
		main_pos= pos;
		this.notifyDataSetChanged();
	}
	
	public void setCheckBoxVisible(boolean isvisible)
	{
		show_func = isvisible;
		this.notifyDataSetChanged();
	}
	
	public void addItem(Item item)
	{
		list.add(item);
		notifyDataSetChanged();
	}
	
	/**
	 * ����Item��ID����λ�ã�����ɾ��Item
	 * @param itemId
	 */
	public void removeItemByItemID(int itemId)
	{
		for(Item item:list)
		{
			if(item.item_id == itemId)
			{
				list.remove(item);
				break;
			}
		}
		notifyDataSetChanged();
	}
	
	/**
	 * ���°󶨵��б���ͼ
	 * @param items �µ��������ݣ���Ϊnull�޲���
	 */
	public void updateItems(List<Item> items)
	{
		if(items==null)
		{
//			list.clear();
		}
		else
		{
			list = items;
		}
		this.notifyDataSetChanged();
	}
	
	/**
	 * ���ݱ�ѡ�е�Itemɾ��
	 */
	public void deleteCheckedItems()
	{
		//ɾ�������Ŀʱ������һ����ɾ����Ϊ��Ż�䣬����Ӧ��װ��������һ��ɾ��
		ArrayList<Item> dle_list = new ArrayList<Item>();
		//��ʶǰ�Ƶ�λ��
		int main_pre_pos = 0;
		for(int i=0;i<checked_list.size();i++)
		{
			int pos = checked_list.get(i).intValue();
			//���ڱ�ʶǰ����Ŀ��ɾ��������ʶǰ��
			if(pos<=main_pos)
			{
				main_pre_pos++;
			}
			Item dle_item = list.get(pos);
			dle_list.add(dle_item);
		}
		//�Լ�����ʽɾ���������
		list.removeAll(dle_list);
		checked_list.clear();
		//���±�ʶ��λ��
		main_pos-=main_pre_pos;
		if(main_pos<0)
		{
			main_pos=0;
		}
		
		this.notifyDataSetChanged();
		
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int pos) {
		return list.get(pos);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.imgtextdle_item,null);
			ImageView iv_item =(ImageView)convertView.findViewById(R.id.iv_item);
			ImageView iv_visible =(ImageView)convertView.findViewById(R.id.iv_visible);
			CheckBox cb_func = (CheckBox)convertView.findViewById(R.id.cb_func);
			TextView tv_maindesc = (TextView)convertView.findViewById(R.id.tv_maindesc);
			TextView tv_vicedesc = (TextView)convertView.findViewById(R.id.tv_vicedesc);
			
			vholder.iv_item=iv_item;
			vholder.iv_visible=iv_visible;
			vholder.cb_func=cb_func;
			vholder.tv_maindesc=tv_maindesc;
			vholder.tv_vicedesc=tv_vicedesc;
			//Ϊ��ѡ�����ü�����
			if(check_listener==null)
			{
				check_listener = new MyOnCheckedChangeListener();
			}
			vholder.cb_func.setOnCheckedChangeListener(check_listener);
			
			convertView.setTag(vholder);
		}
		else
		{
			vholder=(ViewHolder)convertView.getTag();
		}
		Item item = list.get(position);
		vholder.iv_item.setImageResource(item.item_img);
		vholder.iv_visible.setImageResource(item.visible_img);
		vholder.tv_maindesc.setText(item.main_desc);
		vholder.tv_vicedesc.setText(item.vice_desc);
//		vholder.cb_func.setBackgroundResource(item.func_img);
		//ˢ�¶�ѡ���״̬
		if(itemIsChecked(position)==true)
		{
			vholder.cb_func.setChecked(true);
		}
		else
		{
			vholder.cb_func.setChecked(false);
		}
		
		//������Ҫ��ʶ��״̬
		if(main_pos!=position||signfunc_enable==false)
		{
			vholder.iv_visible.setVisibility(View.INVISIBLE);
		}
		else
		{
			vholder.iv_visible.setVisibility(View.VISIBLE);
		}
		
		//Ϊ���ܰ�ť���λ�ñ�ǩ�����ڵ���������е���
		vholder.cb_func.setTag(position);
		
		//���ù��ܼ�����ѡ��ť���Ƿ�ɼ�
		if(show_func==false)
		{
			vholder.cb_func.setVisibility(View.INVISIBLE);
		}
		else
		{
			vholder.cb_func.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	/**
	 * ��ѡ��������
	 * @author lenovo-Myy
	 *
	 */
	private class MyOnCheckedChangeListener implements OnCheckedChangeListener
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			Integer pos = (Integer)buttonView.getTag();
			if(isChecked==true)
			{
				checked_list.add(pos);
			}
			else
			{
				checked_list.remove(pos);
			}
		}
	}
	
	private class ViewHolder 
	{
		ImageView iv_item,iv_visible;
		CheckBox cb_func;
		TextView tv_maindesc;
		TextView tv_vicedesc;
	}

	public static class Item
	{
		private int item_img,item_id,visible_img;
		//��Item����Ӧ�Ĺ��������ID������
		private int item_childId;
		private String main_desc,vice_desc;
		
		public Item(int item_id,int visible_img,int item_img ,String main_desc,
				String vice_desc) {
			super();
			this.item_id = item_id;
			this.item_img = item_img;
			this.visible_img = visible_img;
			this.main_desc = main_desc;
			this.vice_desc = vice_desc;
		}

		public int getItem_img() {
			return item_img;
		}

		public int getItem_id() {
			return item_id;
		}

		public int getVisible_img() {
			return visible_img;
		}

		public String getMain_desc() {
			return main_desc;
		}

		public String getVice_desc() {
			return vice_desc;
		}

		public int getItem_childId() {
			return item_childId;
		}

		public void setItem_childId(int item_childId) {
			this.item_childId = item_childId;
		}
		
	}
	
	private boolean itemIsChecked(int pos)
	{
		for(Integer i:checked_list)
		{
			if(i.intValue()==pos)
			{
				return true;
			}
		}
		return false;
	}
	
}
