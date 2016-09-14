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
	//ֻ���ڱ����ʼ��ʱ������������ݣ��������������е����ݹ���
	private List<ImgTextListViewAdapter.Item> list_childs = null;
	private SwipeRefreshLayout srl = null;
	private ViewGroup rl_content = null;
	private TextView tv_desc;
	private ImgTextListViewAdapter lv_adapter = null;
	//���ڱ�������������������Ϊ��������������ü�����������oncrearViewǰ���������õ�
	//���Ա��������ʵ��������OncreatView��������
	protected OnItemClickListener itemclick_listener = null;
	protected OnItemLongClickListener longclick_listener = null;
	private PosivitveCallBack positive_callback;
	private NegativeCallBack negative_callback;
	private OnRefresCallBack refresh_callback;
	private Dialog  comfirm_dlg;
	//���ڼ�¼���ܼ��Ŀɼ���
	private boolean func_visible = false,post_visible=true,nega_visible=true,mainsign_enable=true;
	private boolean desc_visible = true;
	
	protected Integer mainItemPos,mainItemID;
	//���ھ�̬������Ҫ���޲ι��캯��
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
			//����������������
			lv_items.setOnItemClickListener(itemclick_listener);
			lv_items.setOnItemLongClickListener(longclick_listener);
			lv_items.setOnScrollListener(new SwpipeListViewOnScrollListener(srl));
		}
		//��ʼ��ǰֻ��ͨ��ID������MainItem
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
	 * ���µ���������,�����б���Ϣ
	 * @param items ��Ϊ��������б�
	 */
	public void updateItems(List<Item> items)
	{
		//����List����
		lv_adapter.updateItems(items);
		//������ҪItem
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
	 * �û��Զ�����������¼�
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
		//���õĲ��������ڸ������ʼ����ʱ��͵�����ΪViewû����
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
		//�������ɾ�����������򽫹��ܿ�Ҳ����
		if(post_visible==false&&nega_visible==false)
		{
			android.view.ViewGroup.LayoutParams but_params = vg_button.getLayoutParams();
			but_params.height=0;
			vg_button.setLayoutParams(but_params);
		}
		
		vg_postvie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//���ð�ť������
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
	 * ѡ�е����Ӧ����CallBack������ɺ�ִ��
	 */
	public void selectCheckedItems()
	{
		//����ɾ������
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
	 * ���ñ�־�����Ƿ����
	 * @return
	 */
	public ActionListFragment setMainSignEnable(boolean isenable)
	{
		mainsign_enable = isenable;
//		lv_adapter.setSignFuncEanble(isvisible);
		return this;
	}
	
	/**
	 * ��������������Ϣ�Ƿ�ɼ�������������Ϣʱ��ʾ
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
	 * ������Ӽ��Ƿ�ɼ�
	 * @param isvisible
	 */
	public ActionListFragment setPostiveButtonVisible(boolean isvisible)
	{
		post_visible = isvisible;
		return this;
	}
	
	/**
	 * ����ɾ�����Ƿ�ɼ�
	 * @param isvisible
	 */
	public ActionListFragment setNegativeButtonVisible(boolean isvisible)
	{
		nega_visible = isvisible;
		return this;
	}
	
	
	/**
	 * ��ʼ������ˢ��
	 */
	private void initPullToRefresh()
	{
		srl = (SwipeRefreshLayout)view.findViewById(R.id.srl_client);
		srl.setOnRefreshListener(this);
		srl.setColorSchemeResources(R.drawable.refresh_1,R.drawable.refresh_2,
				R.drawable.refresh_3,R.drawable.refresh_4);
	}
	
	
	/**
	 * ���ö�ѡ���Ƿ�ɼ�
	 * @param is_visible
	 */
	private void setCheckBoxVisible(boolean is_visible)
	{
		lv_adapter.setCheckBoxVisible(is_visible);
	}
	
	/**
	 * ��ʾɾ������Ի��򣬲����к�������
	 */
	private void showDeleteConfirmDialog()
	{
		if(comfirm_dlg==null)
		{
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("ȷ��ɾ��ѡ���");
		builder.setTitle("ɾ��ȷ��");
		builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener() {
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
		builder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener() {
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
		//�ȽӴ�Fragment��View�Ĺ�ϵ
		((ViewGroup)getView()).removeView(view);
		super.onDestroyView();
	}

	/**
	 * ɾ����ѡ�е�Item��������ɾ����
	 */
	public void deleteCheckedItems()
	{
		lv_adapter.deleteCheckedItems();;
	}
	
	/**
	 * ֹͣ���ض���
	 */
	public void stopRefresh()
	{
		if(srl!=null&&srl.isRefreshing())
		{
			srl.setRefreshing(false);
		}
	}
	/**
	 * ͨ������Refreshing����ֹˢ��,Ӧ����������������
	 */
	@Override
	public void onRefresh() {
		//ͨ������Refreshing����ֹˢ��,
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
		//��ListView��δ��ʼ���ȷ��أ����ڳ�ʼ��ʱ������ҪItem
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
	 * ���Item�������б�
	 * @param item
	 */
	public void addItem(Item item)
	{
		lv_adapter.addItem(item);
	}
	
	/**
	 * ɾ���������б�
	 * @param itemID
	 */
	public void removeItem(int itemID)
	{
		lv_adapter.removeItemByItemID(itemID);
	}
	
	public static interface NegativeCallBack
	{
		/**
		 * �����ã������ִ�б���ɾ�������Ĭ�Ϸ���selectCheckedItems
		 * ���ȷ��ɾ����ִ��
		 * �������ã�����ڵ��ɾ�������ִ��lv_adapter.deleteItems()
		 * @param checkedItems ��ѡ��ɾ����Item�б�
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
