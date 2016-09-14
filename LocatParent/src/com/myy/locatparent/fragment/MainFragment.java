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

	// 消息类别：文本消息，碎片显示,轨迹信息，添加围栏
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
	// 用于记录当前显示的Fragment
	private Fragment cur_frgm = null;
	private ViewGroup rl_action = null, rl_neviga = null;
	private Toast toast = null;
	// 用于记录工具栏和导航栏的布局属性，用于设置是否可见时恢复作用
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
		// 获得FragmentLayout的引用，用于设置高度来隐蔽工具栏和导航栏
		rl_action = (ViewGroup) view.findViewById(R.id.fl_actionbar);
		rl_neviga = (ViewGroup) view.findViewById(R.id.fl_navigation);

		// action_frgm = (Fragment)view.(R.id.frgm_title);
		// 初始化Fragment管理器
		// getChildFragmentManager()
		fm = getActivity().getSupportFragmentManager();
		action_frgm = new TitleFragment();
		neviga_frgm = new NavigationFragment();
//		login_frgm = new LoginFragment();
		
		
		FragmentTransaction ft = fm.beginTransaction();
		// 将工作栏，导航栏，和默认显示的地图窗口显示出来
		ft.add(R.id.fl_actionbar, action_frgm);
		ft.add(R.id.fl_navigation, neviga_frgm);
		ft.commit();
		//第一显示的碎片是登录界面
		MainActivity.showFragment(LoginFragment.ID);
//		MainActivity.showFragment(EntityInfoFragment.ID);
	}

	private void setTitle(String title) {
		if (action_frgm != null) {
			action_frgm.setTitle(title);
		}
	}

	/**
	 * 设置标题栏中返回按钮的可见性
	 * @param isvisible
	 */
	public void setBackButtonVisible(boolean isvisible) {
		action_frgm.setBackVisible(isvisible);
	}

	/**
	 * 设置标题的可见性
	 * 
	 * @param isvisible
	 */
	public void setTitleVisible(boolean isvisible) {

		LayoutParams param;
		param = rl_action.getLayoutParams();
		if (layout_action == null) {
			// 负责布局参数到新的对象中
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
	 * 设置导航栏的可见性
	 * 
	 * @param isvisible
	 */
	public void setNevigationVisible(boolean isvisible) {
		LayoutParams param;
		param = rl_neviga.getLayoutParams();
		if (layout_neviga == null) {
			// 负责布局参数到新的对象中
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
	 *            主窗体Handler接受的Message
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
		// 为信息
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
			// 显示地图监控
			if (msg.obj.toString().equals(MapFragment.ID)) {
				myFm.clearBackStack();
				showFragment(MapFragment.class, ft);
				myFt.setTitle("地图监控");
				myFt.setTitleVisible(false);
				myFt.setNevigationVisible(true);
				myFt.setNavigationPosition(0);
				myFt.addToBackStack();
			}
			// 显示登录界面
			else if (msg.obj.toString().equals(LoginFragment.ID)) {
				showFragment(LoginFragment.class, ft);
				myFm.clearBackStack();
				myFt.setTitleVisible(false);
				myFt.setTitle("登录界面");
				myFt.setNevigationVisible(false);
				//因为有注册界面所以要加入回退栈
				myFt.addToBackStack();
			}
			// 显示注册界面
			else if (msg.obj.toString().equals(RegisterFragment.ID)) {
				showFragment(RegisterFragment.class, ft);
				ft.addToBackStack(null);
				myFt.setTitle("注册界面");
				myFt.setTitleVisible(true);
				myFt.setBackButtonVisible(true);
				myFt.setNevigationVisible(false);
				//只把设置加入回退栈
				myFt.addToBackStack();
			}
			// 显示子端管理界面
			else if (msg.obj.toString().equals(EntityListFragment.ID)) {
				showFragment(EntityListFragment.class, ft);
				myFm.clearBackStack();
				myFt.setTitleVisible(true);
				myFt.setTitle("子端管理");
				myFt.setNevigationVisible(true);
				myFt.setBackButtonVisible(false);
				myFt.setNavigationPosition(1);
				myFt.addToBackStack();
				
			}
			// 显示用户管理界面
			else if (msg.obj.toString().equals(AcountFragment.ID)) {
				showFragment(AcountFragment.class, ft);
				myFm.clearBackStack();
				myFt.setTitleVisible(true);
				myFt.setTitle("用户管理");
				myFt.setNevigationVisible(true);
				myFt.setBackButtonVisible(false);
				myFt.setNavigationPosition(2);
				myFt.addToBackStack();
			}
			// 子端详细信息页面
			else if (msg.obj.toString().equals(EntityInfoFragment.ID)) {
				showFragment(EntityInfoFragment.class, ft);
				ft.addToBackStack(null);
				myFt.setTitleVisible(true);
				myFt.setTitle("子端详情");
				myFt.setNevigationVisible(false);
				myFt.setBackButtonVisible(true);
				myFt.addToBackStack();
			}
			// 围栏详细信息页面
			else if (msg.obj.toString().equals(FenceWarningFragment.ID)) {
				showFragment(FenceWarningFragment.class, ft);
				ft.addToBackStack(null);
				myFt.setTitleVisible(true);
				myFt.setTitle("围栏历史警报");
				myFt.setNevigationVisible(false);
				myFt.setBackButtonVisible(true);
				myFt.addToBackStack();
			}
			// 执行操作
			ft.commit();
		}//显示实时轨迹
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
				MainActivity.showMessage("错误：返回的轨迹异常");
			}
		}
		//添加围栏
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
				addfance_dlg = new AddFanceDialog(getActivity(), "添加圆形围栏",
						map_frgm);
			}
			addfance_dlg.show();
		}
		//设置地图状态
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
	 * 设置导航栏图标高亮的位置
	 * @param pos
	 */
	private void setNavigationPosition(int pos)
	{
		neviga_frgm.setCheckedPoosition(pos);
	
	}
	
	/**
	 * MainActivity接收到回退事件时调用
	 */
	public void handleBackPressed()
	{
		if(myFm!=null)
		{
			//回退一个事务
			myFm.popBackStack();
		}
	}
	
	public int isFragmentStackCount()
	{
		return myFm.getTransactionCount();
	}

	
	
	/**
	 * 显示指定类的碎片，并将之前显示的碎片隐藏
	 * 
	 * @param frgmClass
	 * @param ft
	 * @throws Exception
	 */
	private void showFragment(Class<? extends IDFragment> frgmClass,
			FragmentTransaction ft) throws Exception {
		// 若当前有显示的碎片则隐藏起来
		if (cur_frgm != null) {
			ft.hide(cur_frgm);
		}
		
		IDFragment fragInstance = frgmClass.newInstance();
		Fragment show_frgm = fm.findFragmentByTag(fragInstance.getID());
		// 第一次创建
		if (show_frgm == null) {
			show_frgm = fragInstance;
			ft.add(R.id.fl_content, show_frgm, fragInstance.getID());
		}
		// 已经创建过了就直接show
		else {
			ft.show(show_frgm);
		}
		cur_frgm = show_frgm;
		// 设置显示动画效果
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	}

	public void handlerActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode==RESULT_CODE.FENCELIST.ordinal())
		{
			//若未返回intent跳过
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
	// 用于记录布局中的宽高
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
	 * 自定义的碎片事务，用于记录碎片的操作事务,记录执行事务执行过得操作
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
		 * 回退操作，回退到前一个事务的操作
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
		 * 回退上一个的事务,回退栈为空时不执行
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
		 * 清空回退栈
		 */
		public void clearBackStack()
		{
			synchronized (transactionStack) {
				transactionStack.clear();
			}
		}
		
	}
	
	
}
