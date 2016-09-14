package com.myy.locatparent.fragment;

import java.util.List;
import java.util.Stack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.ms.utils.bean.Point;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.activity.MainActivity.RESULT_CODE;
import com.myy.locatparent.beans.Fance.FENCETYPE;
import com.myy.locatparent.dailog.AddFanceDialog;
import com.myy.locatparent.task.DoubleExitUtils;

public class MainFragment extends Fragment {

	// ��Ϣ����ı���Ϣ����Ƭ��ʾ,�켣��Ϣ�����Χ��
	public enum MSGTYPE {
		MSG, FRAGMNET, SHOWHISTRACE,SHOWREALTRACE,ADDFANCE,SHOWFANCE,SHOWMARKET,MAPSTATUS
	};

	public static final String ID = "com.myy.fragment.MainFragment";
	private AddFanceDialog addfance_dlg = null;
	private FragmentManager fm = null;
	private MyFrgmentManager myFm = null;
	private View view = null;
	// private Handler handler = null;
	private TitleFragment action_frgm = null;
	private NavigationFragment neviga_frgm = null;
	private Fragment login_frgm = null;
	// ���ڼ�¼��ǰ��ʾ��Fragment
	private Fragment cur_frgm = null;
	private ViewGroup rl_action = null, rl_neviga = null;
	private Toast toast = null;
	// ���ڼ�¼�������͵������Ĳ������ԣ����������Ƿ�ɼ�ʱ�ָ�����
	private LayoutWH layout_action = null, layout_neviga = null;
	
	
	// private LayoutParams title_param = null,naviga_param =null;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.main_frgm, container, false);
		try {
			initFragment();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return view;
	}

	private void initFragment() throws Exception {
		// ���FragmentLayout�����ã��������ø߶������ι������͵�����
		rl_action = (ViewGroup) view.findViewById(R.id.fl_actionbar);
		rl_neviga = (ViewGroup) view.findViewById(R.id.fl_navigation);

		// action_frgm = (Fragment)view.(R.id.frgm_title);
		// ��ʼ��Fragment������
		// getChildFragmentManager()
		fm = getActivity().getSupportFragmentManager();
		action_frgm = new TitleFragment();
		neviga_frgm = new NavigationFragment();
//		login_frgm = new LoginFragment();
		
		
		FragmentTransaction ft = fm.beginTransaction();
		// ��������������������Ĭ����ʾ�ĵ�ͼ������ʾ����
		ft.add(R.id.fl_actionbar, action_frgm);
		ft.add(R.id.fl_navigation, neviga_frgm);
		ft.commit();
		//��һ��ʾ����Ƭ�ǵ�¼����
		MainActivity.showFragment(LoginFragment.ID);
//		MainActivity.showFragment(EntityInfoFragment.ID);
	}

	private void setTitle(String title) {
		if (action_frgm != null) {
			action_frgm.setTitle(title);
		}
	}

	/**
	 * ���ñ������з��ذ�ť�Ŀɼ���
	 * @param isvisible
	 */
	public void setBackButtonVisible(boolean isvisible) {
		action_frgm.setBackVisible(isvisible);
	}

	/**
	 * ���ñ���Ŀɼ���
	 * 
	 * @param isvisible
	 */
	public void setTitleVisible(boolean isvisible) {

		LayoutParams param;
		param = rl_action.getLayoutParams();
		if (layout_action == null) {
			// ���𲼾ֲ������µĶ�����
			layout_action = new LayoutWH(param.width, param.height);
		}
		if (isvisible == false) {
			param.height = 0;
			rl_action.setLayoutParams(param);
		} else {
			param.height = layout_action.getHeight();
			rl_action.setLayoutParams(param);
		}
	}

	/**
	 * ���õ������Ŀɼ���
	 * 
	 * @param isvisible
	 */
	public void setNevigationVisible(boolean isvisible) {
		LayoutParams param;
		param = rl_neviga.getLayoutParams();
		if (layout_neviga == null) {
			// ���𲼾ֲ������µĶ�����
			layout_neviga = new LayoutWH(param.width, param.height);
		}
		if (isvisible == false) {
			param.height = 0;
			// param.width=0;
			rl_neviga.setLayoutParams(param);
		} else {
			param.height = layout_neviga.getHeight();
			// param.width = layout_neviga.getWidth();
			rl_neviga.setLayoutParams(param);
		}
	}

	/**
	 * 
	 * @param message
	 *            ������Handler���ܵ�Message
	 * @throws Exception
	 */
	public void handleMessage(Message msg) throws Exception {
		if (fm == null) {
			fm = this.getChildFragmentManager();
		}
		if(myFm==null)
		{
			myFm = new MyFrgmentManager();
		}
		// Ϊ��Ϣ
		if (msg.what == MainFragment.MSGTYPE.MSG.ordinal()) {
			if (msg.obj != null) {
				if(toast ==null)
				{
					toast = Toast.makeText(getActivity(),"",
						Toast.LENGTH_SHORT);
				}
				toast.setText(msg.obj.toString());
				toast.show();
			}
			return;
		} else if (msg.what == MainFragment.MSGTYPE.FRAGMNET.ordinal()) {
			FragmentTransaction ft = fm.beginTransaction();
			MyFrgmentTransaction myFt = myFm.beginTracation();
			// ��ʾ��ͼ���
			if (msg.obj.toString().equals(MapFragment.ID)) {
				myFm.clearBackStack();
				showFragment(MapFragment.class, ft);
				myFt.setTitle("��ͼ���");
				myFt.setTitleVisible(false);
				myFt.setNevigationVisible(true);
				myFt.setNavigationPosition(0);
				myFt.addToBackStack();
			}
			// ��ʾ��¼����
			else if (msg.obj.toString().equals(LoginFragment.ID)) {
				showFragment(LoginFragment.class, ft);
				myFm.clearBackStack();
				myFt.setTitleVisible(false);
				myFt.setTitle("��¼����");
				myFt.setNevigationVisible(false);
				//��Ϊ��ע���������Ҫ�������ջ
				myFt.addToBackStack();
			}
			// ��ʾע�����
			else if (msg.obj.toString().equals(RegisterFragment.ID)) {
				showFragment(RegisterFragment.class, ft);
				ft.addToBackStack(null);
				myFt.setTitle("ע�����");
				myFt.setTitleVisible(true);
				myFt.setBackButtonVisible(true);
				myFt.setNevigationVisible(false);
				//ֻ�����ü������ջ
				myFt.addToBackStack();
			}
			// ��ʾ�Ӷ˹������
			else if (msg.obj.toString().equals(EntityListFragment.ID)) {
				showFragment(EntityListFragment.class, ft);
				myFm.clearBackStack();
				myFt.setTitleVisible(true);
				myFt.setTitle("�Ӷ˹���");
				myFt.setNevigationVisible(true);
				myFt.setBackButtonVisible(false);
				myFt.setNavigationPosition(1);
				myFt.addToBackStack();
				
			}
			// ��ʾ�û��������
			else if (msg.obj.toString().equals(AcountFragment.ID)) {
				showFragment(AcountFragment.class, ft);
				myFm.clearBackStack();
				myFt.setTitleVisible(true);
				myFt.setTitle("�û�����");
				myFt.setNevigationVisible(true);
				myFt.setBackButtonVisible(false);
				myFt.setNavigationPosition(2);
				myFt.addToBackStack();
			}
			// �Ӷ���ϸ��Ϣҳ��
			else if (msg.obj.toString().equals(EntityInfoFragment.ID)) {
				showFragment(EntityInfoFragment.class, ft);
				ft.addToBackStack(null);
				myFt.setTitleVisible(true);
				myFt.setTitle("�Ӷ�����");
				myFt.setNevigationVisible(false);
				myFt.setBackButtonVisible(true);
				myFt.addToBackStack();
			}
			// Χ����ϸ��Ϣҳ��
			else if (msg.obj.toString().equals(FenceWarningFragment.ID)) {
				showFragment(FenceWarningFragment.class, ft);
				ft.addToBackStack(null);
				myFt.setTitleVisible(true);
				myFt.setTitle("Χ����ʷ����");
				myFt.setNevigationVisible(false);
				myFt.setBackButtonVisible(true);
				myFt.addToBackStack();
			}
			// ִ�в���
			ft.commit();
		}//��ʾʵʱ�켣
		else if(msg.what==MSGTYPE.SHOWREALTRACE.ordinal())
		{
			if(msg.obj instanceof List<?>)
			{
				List<Point> point_list = (List<Point>)msg.obj;
				if(cur_frgm instanceof MapFragment)
				{
					((MapFragment)cur_frgm).showRealTraceMarker(point_list);
				}
			}
			else
			{
				MainActivity.showMessage("���󣺷��صĹ켣�쳣");
			}
		}
		//���Χ��
		else if(msg.what==MSGTYPE.ADDFANCE.ordinal())
		{
			if(cur_frgm instanceof MapFragment==false)
			{
				MainActivity.showFragment(MapFragment.ID);
			}
			MapFragment map_frgm = (MapFragment)fm.
					findFragmentByTag(MapFragment.ID);
			if(addfance_dlg ==null&&msg.obj.equals(FENCETYPE.CRICLE))
			{
				addfance_dlg = new AddFanceDialog(getActivity(), "���Բ��Χ��",
						map_frgm);
			}
			addfance_dlg.show();
		}
		//���õ�ͼ״̬
		else if(msg.what==MSGTYPE.MAPSTATUS.ordinal())
		{
			if(cur_frgm instanceof MapFragment==false)
			{
				MainActivity.showFragment(MapFragment.ID);
			}
			MapFragment map_frgm = (MapFragment)fm.
					findFragmentByTag(MapFragment.ID);
			LatLng point = (LatLng)msg.obj;
			map_frgm.setMapFocus(point);
		}
	}
	
	/**
	 * ���õ�����ͼ�������λ��
	 * @param pos
	 */
	private void setNavigationPosition(int pos)
	{
		neviga_frgm.setCheckedPoosition(pos);
	
	}
	
	/**
	 * MainActivity���յ������¼�ʱ����
	 */
	public void handleBackPressed()
	{
		if(myFm!=null)
		{
			//����һ������
			myFm.popBackStack();
		}
	}
	
	public int isFragmentStackCount()
	{
		return myFm.getTransactionCount();
	}

	
	
	/**
	 * ��ʾָ�������Ƭ������֮ǰ��ʾ����Ƭ����
	 * 
	 * @param frgmClass
	 * @param ft
	 * @throws Exception
	 */
	private void showFragment(Class<? extends IDFragment> frgmClass,
			FragmentTransaction ft) throws Exception {
		// ����ǰ����ʾ����Ƭ����������
		if (cur_frgm != null) {
			ft.hide(cur_frgm);
		}
		
		IDFragment fragInstance = frgmClass.newInstance();
		Fragment show_frgm = fm.findFragmentByTag(fragInstance.getID());
		// ��һ�δ���
		if (show_frgm == null) {
			show_frgm = fragInstance;
			ft.add(R.id.fl_content, show_frgm, fragInstance.getID());
		}
		// �Ѿ��������˾�ֱ��show
		else {
			ft.show(show_frgm);
		}
		cur_frgm = show_frgm;
		// ������ʾ����Ч��
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	}

	public void handlerActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode==RESULT_CODE.FENCELIST.ordinal())
		{
			//��δ����intent����
			if(data==null)
			{
				return;
			}
			int fenceId = data.getIntExtra("fenceId",-1);
			if(fenceId<=0)
			{
				return ;
			}
			MapFragment map_frgm = (MapFragment)fm.
					findFragmentByTag(MapFragment.ID);
			map_frgm.showFences(fenceId);
		}
	}
	// ���ڼ�¼�����еĿ��
	private class LayoutWH {
		private int height = 0, width = 0;

		public LayoutWH(int w, int h) {
			height = h;
			width = w;
		}

		public int getHeight() {
			return height;
		}

		public int getWidth() {
			return width;
		}
	}
	
	/**
	 * �Զ������Ƭ�������ڼ�¼��Ƭ�Ĳ�������,��¼ִ������ִ�й��ò���
	 * @author lenovo-Myy
	 *
	 */
	private class MyFrgmentTransaction
	{
		
		public Boolean titleVisible,neviageionVisible,backVisible;
		public String title;
		public Integer naviPos;
		private MyFrgmentManager fm;
		private MyFrgmentTransaction pre_ft;
		
		public MyFrgmentTransaction(MyFrgmentManager fm,MyFrgmentTransaction pre_ft)
		{
			this.fm = fm;
			this.pre_ft = pre_ft;
		}
		
		public void setTitle(String title)
		{
			this.title = title;
			MainFragment.this.setTitle(title);
		}
		
		public void setTitleVisible(boolean visible)
		{
			this.titleVisible = visible;
			MainFragment.this.setTitleVisible(visible);
		}
		
		public void setNevigationVisible(boolean visible)
		{
			this.neviageionVisible = visible;
			MainFragment.this.setNevigationVisible(visible);
		}
		
		public void setBackButtonVisible(boolean visible)
		{
			this.backVisible = visible;
			MainFragment.this.setBackButtonVisible(visible);
		}
		
		public void setNavigationPosition(int position)
		{
			this.naviPos = position;
			MainFragment.this.setNavigationPosition(position);
		}
		/**
		 * ���˲��������˵�ǰһ������Ĳ���
		 */
		private void back()
		{
			if(pre_ft!=null)
			{
				if(pre_ft.titleVisible!=null)
				{
					setTitleVisible(pre_ft.titleVisible);
				}
				if(pre_ft.neviageionVisible!=null)
				{
					setNevigationVisible(pre_ft.neviageionVisible);
				}
				if(pre_ft.backVisible!=null)
				{
					setBackButtonVisible(pre_ft.backVisible);
				}
				if(pre_ft.title!=null)
				{
					setTitle(pre_ft.title);
				}
				if(pre_ft.naviPos!=null)
				{
					setNavigationPosition(pre_ft.naviPos);
				}
			}
		}
		
		public void addToBackStack()
		{
			fm.addToBackStack(this);
		}
		
	}
	
	private class MyFrgmentManager
	{
		private Stack<MyFrgmentTransaction> transactionStack = new Stack<MyFrgmentTransaction>();
		
		public MyFrgmentTransaction beginTracation()
		{
			MyFrgmentTransaction ft;
			if(transactionStack.size()<=0)
			{
				ft = new MyFrgmentTransaction(this,
						null);
			}
			else
			{
				int index = transactionStack.size()-1;
				ft = new MyFrgmentTransaction(this,
						transactionStack.get(index));
			}
			return ft;
		}
		
		private void addToBackStack(MyFrgmentTransaction ft)
		{
			synchronized (transactionStack) {
				transactionStack.push(ft);
			}
		}
		
		/**
		 * ������һ��������,����ջΪ��ʱ��ִ��
		 */
		public void popBackStack()
		{
			if(getTransactionCount()>0)
			{
				synchronized (transactionStack) {
					MyFrgmentTransaction ft = transactionStack.pop();
					ft.back();
				}
				
			}
		}
		
		public int getTransactionCount()
		{
			synchronized (transactionStack) {
				return transactionStack.size();
			}
		}
		
		/**
		 * ��ջ���ջ
		 */
		public void clearBackStack()
		{
			synchronized (transactionStack) {
				transactionStack.clear();
			}
		}
		
	}
	
	
}
