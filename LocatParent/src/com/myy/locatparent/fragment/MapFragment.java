package com.myy.locatparent.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.Point;
import com.ms.utils.bean.Pupillus;
import com.myy.loactparent.popupwinow.FanceCustomPopupWindow;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.loactparent.popupwinow.MapOpeartionPopupWinow;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.EntityListDialogActivity;
import com.myy.locatparent.activity.FenceListDialogActivity;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.beans.Fance.FENCECOLOR;
import com.myy.locatparent.task.QueryLastestPointTask;
import com.myy.locatparent.utils.FenceUtils;

public class MapFragment extends IDFragment implements OnClickListener {

	public static enum TRACETYPE
	{
		REALTRACE,HISTRACE;
	}
	private boolean DEBUG = false;
	// 实时轨迹图标覆盖物
    private static MarkerOptions realMarker = null;
	private BitmapDescriptor bm_real;
	// private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final String ID = "MapFragment";
	private View view = null;
	
	private ImageView iv_operation,iv_child_list,iv_fance_list,iv_navigate;
	// map主要负责地图的逻辑操作和地图监听回调
	private BaiduMap map = null;
	// mapview主要包装了地图视图的设置操作
	private MapView map_view = null;
	private MapOpeartionPopupWinow operation_pop = null;
	private FanceCustomPopupWindow fancecustom_pop;
	//地图覆盖物集合
	private List<CircleOptions> fence_list = new LinkedList<CircleOptions>();
	//用于显示创建围栏时显示的围栏
	private CircleOptions newFence;
	private PolylineOptions realTrace ;
	private PolylineOptions historyTrace ;
	
//	private List<MarkerOptions> market_list = new LinkedList<MarkerOptions>();
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.map_fragment, container, false);
		initImageButton();
		initMap();
		initPopuWindow(inflater);
		//登录完成后先初始化子端
		EntityListFragment.updateEntityData(null);
		return view;
	}
	@Override
	public String getID() {
		return this.ID;
	}

	public BaiduMap getMap()
	{
		return map;
	}
	
	public MapView getMapView()
	{
		return map_view;
	}
	
	public void clearFanceOption()
	{
		fence_list.clear();
		updateMarket();
	}
	
	public void clearRealTrace()
	{
		realTrace = null;
		updateMarket();
	}
	
	public void clearHistoryTrace()
	{
		historyTrace = null;
		updateMarket();
	}
	
	/**
	 * 重置围栏列表并显示新传入的围栏列表，最后一个围栏显示为主围栏
	 * @param list_fences
	 */
	public void showFences(List<Fencing> list_fences)
	{
		fence_list.clear();
		int i = 0;
		for(Fencing fence:list_fences)
		{
			//最后一个围栏显示为主围栏
			if(i==list_fences.size()-1)
			{
				CircleOptions option = FenceUtils.getCircleOptionByFencing
						(fence,FENCECOLOR.MAIN);
				fence_list.add(option);
				LatLng latlng = new LatLng(fence.getLatitude(),fence.getLongitude());
				//将地图焦点聚集在主围栏处
				setMapFocus(latlng);
			}
			else
			{
				CircleOptions option = FenceUtils.getCircleOptionByFencing
						(fence,FENCECOLOR.OLD );
				fence_list.add(option);
			}
			i++;
		}
		//重置新围栏
		newFence=null;
		setFenceVisible(true);
		updateMarket();
	}
	
	/**
	 * 显示已存在的围栏列表
	 * @param mainFence 需要主要显示的围栏ID，若存在在围栏列表中
	 * 则高亮并聚焦显示，不存在则跳过
	 */
	public void showFences(int fenceId)
	{
		List<Fencing> list_fences = LocatParentApplication.getList_fences();
		fence_list.clear(); 
		for(Fencing fence:list_fences)
		{
			//若为主显示围栏
			if(fence.getId().equals(fenceId))
			{
				CircleOptions option = FenceUtils.getCircleOptionByFencing
						(fence,FENCECOLOR.MAIN);
				fence_list.add(option);
				LatLng latlng = new LatLng(fence.getLatitude(),fence.getLongitude());
				//将地图焦点聚集在主围栏处
				setMapFocus(latlng);
			}
			else
			{
				CircleOptions option = FenceUtils.getCircleOptionByFencing
						(fence,FENCECOLOR.OLD );
				fence_list.add(option);
			}
			
		}
		//重置新围栏
		newFence=null;
		setFenceVisible(true);
		updateMarket();
	}
	
	public void showNewFence(Fencing fence)
	{
		newFence = FenceUtils.getCircleOptionByFencing(fence,FENCECOLOR.NEW);
		operation_pop.setFenceVisible(true);
		updateMarket();
	}
	
	/**
	 * 设置围栏可见
	 * 
	 * @param visible
	 */
	public void setFenceVisible(boolean visible)
	{
		operation_pop.setFenceVisible(visible);
		updateMarket();
	}
	
	public void setRealTraceVisible(boolean visible)
	{
		if(visible==false)
		{
			realTrace = null;
		}
		operation_pop.setRealTraceVisible(visible);
		updateMarket();
	}
	
	public void setHisTraceVisible(boolean visible)
	{
		if(visible==false)
		{
			historyTrace = null;
		}
		operation_pop.setHisTraceVisible(visible);
		updateMarket();
	}
	
	
	/**
	 * 刷新地图覆盖物，清空过时的覆盖物
	 */
	private void updateMarket()
	{
		//清除所有覆盖物和WindowInfo
		map.clear();
		//显示围栏
		if(fence_list.size()>0&&operation_pop.isFenceVisible()==true)
		{
			for(CircleOptions option:fence_list)
			{
				map.addOverlay(option);
			}
		}
		if(newFence!=null&&operation_pop.isFenceVisible()==true)
		{
			map.addOverlay(newFence);
		}
		if(historyTrace!=null&&operation_pop.isHisTraceVisible()==true)
		{
			map.addOverlay(historyTrace);
		}
		if(realTrace!=null&&operation_pop.isRealTraceVisible()==true)
		{
			map.addOverlay(realTrace);
			map.addOverlay(realMarker);
		}
	}
	
	private void initImageButton() {
		iv_operation = (ImageView) view.findViewById(R.id.iv_operation);
		iv_child_list = (ImageView) view.findViewById(R.id.iv_child_list);
		iv_fance_list = (ImageView) view.findViewById(R.id.iv_fance_list);
		iv_navigate = (ImageView) view.findViewById(R.id.iv_navigate);

		iv_operation.setOnClickListener(this);
		iv_child_list.setOnClickListener(this);
		iv_fance_list.setOnClickListener(this);
		iv_navigate.setOnClickListener(this);
	}

	private void initMap() {
		map_view = (MapView) view.findViewById(R.id.mapview);
		//初始化地图配置
		MapStatus mMapStatus = new MapStatus.Builder().zoom(15).build();
        MapStatusUpdate msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        // 用于设置地图状态
		map = map_view.getMap();
		map.setMapStatus(msUpdate);
		if (DEBUG == false) {
			// 不显示放大缩小的标志
			map_view.showZoomControls(false);
		}
	}

	private void initPopuWindow(LayoutInflater inflater) {
		if(operation_pop == null)
		{
			operation_pop = new MapOpeartionPopupWinow(getActivity(),this);
		}
		if(fancecustom_pop==null)
		{
			fancecustom_pop = new FanceCustomPopupWindow(getActivity());
		}
		
	}
	
	/**
	 * 传递4个点击监听器分别对应，围栏自定义的4个按钮
	 * @param listners
	 */
	public void showFanceCustomPopWindow(OnClickListener [] listners)
	{
		fancecustom_pop.setTextViewOnClickListeners(listners);
		WindowManager wm = (WindowManager) getActivity()
				.getSystemService(Context.WINDOW_SERVICE);
		int w_height = wm.getDefaultDisplay().getHeight();
		//紧靠左边垂直居中显示
		fancecustom_pop.showAtLocation(iv_operation, Gravity.NO_GRAVITY
				,10,(w_height-fancecustom_pop.getHeight())/2);
	}
	
	public void hideFanceCustomPopWindow()
	{
		if(fancecustom_pop!=null && fancecustom_pop.isShowing())
		{
			fancecustom_pop.dismiss();
		}
	}

	/**
	 * 在某个控件上方显示pop
	 * 
	 * @param v 在这个控件上显示 
	 */
	private void showOprationPopWindow(View v) {
		int loc[] = new int[2];
		v.getLocationOnScreen(loc);

		operation_pop.showAtLocation(v, Gravity.NO_GRAVITY, loc[0], loc[1]
				- operation_pop.getHeight());
	}


	/**
	 * 显示模拟成对话框的儿童列表Activity
	 */
	private void showChildrenListActivity() {
		Intent intent = new Intent(getActivity(),
				EntityListDialogActivity.class);
		startActivity(intent);
	}

	/**
	 * 显示模拟成对话框的围栏列表Activity
	 */
	private void showFanceListActivity() {
		Intent intent = new Intent(getActivity(),
				FenceListDialogActivity.class);
//		startActivity(intent);
		startActivityForResult(intent,0);
	}

	private void testShowTrace()
	{
		LatLng latlng = new LatLng(28.070277,113.017536);
		LatLng latlng1 = new LatLng(28.070276,113.017544);
		LatLng latlng2 = new LatLng(28.070279,113.017555);
		MapStatus mMapStatus = new MapStatus.Builder().target(latlng).zoom(18).build();
        MapStatusUpdate msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        map.setMapStatus(msUpdate);
        ArrayList<LatLng> p_list = new ArrayList<LatLng>();
        p_list.add(latlng);
        p_list.add(latlng1);
        p_list.add(latlng2);
        PolylineOptions polyline = new PolylineOptions().width(10)
				.color(Color.RED).points(p_list);
		// 为地图添加覆盖物
		map.addOverlay(polyline);
	}
	
	/**
	 * 添加一段历史轨迹到地图中
	 * @param point_list
	 */
	public void showHistoryTraceMarker(List<Point> point_list) {
		//纠偏
//		point_list = TraceUtils.correctTrace(point_list);
		ArrayList<LatLng> p_list = new ArrayList<LatLng>();
		Point last_p = point_list.get(point_list.size()-1);
		//定位焦点在最后一个的位置
		LatLng latlng = new LatLng(last_p.getLatitude()
				,last_p.getLongitude());
		setMapFocus(latlng);
		int count=0;
		if(point_list.size()<2)
		{
			MainActivity.showMessage("轨迹点过少，请稍后再试");
			return ;
		}
		int color = Color.RED;
		for(Point p : point_list)
		{
			latlng = new LatLng(p.getLatitude(), p.getLongitude());
			p_list.add(latlng);
			if(count>99990)
			{
				MainActivity.showMessage("轨迹点过多，只显示部分轨迹");
				break;
			}
			count++;
		}
		PolylineOptions polyline = new PolylineOptions().width(10)
				.color(color).points(p_list);
		// 为地图添加覆盖物
		map.addOverlay(polyline);
		historyTrace=polyline;
		MainActivity.showMessage("历史轨迹显示"+count+"个点");
		updateMarket();
	}
	
	/**
	 * 显示实时轨迹
	 * @param point_list
	 */
	public void showRealTraceMarker(List<Point> point_list) {
		//纠偏
//		point_list = TraceUtils.correctTrace(point_list);
		ArrayList<LatLng> p_list = new ArrayList<LatLng>();
		Point last_p = point_list.get(point_list.size()-1);
		LatLng latlng = new LatLng(last_p.getLatitude()
				,last_p.getLongitude());
		if(bm_real==null)
		{
			bm_real = BitmapDescriptorFactory.fromResource(R.drawable.real_here);
		}
		// 添加实时轨迹图标
        realMarker = new MarkerOptions()
                .position(latlng).icon(bm_real)
                .zIndex(9).draggable(true);
		int count=0;
		
		setMapFocus(latlng);
		if(point_list.size()<2)
		{
			MainActivity.showMessage("轨迹点过少，请稍后再试");
			return ;
		}
		int color = Color.GREEN;
		for(Point p : point_list)
		{
			latlng = new LatLng(p.getLatitude(), p.getLongitude());
			p_list.add(latlng);
			if(count>99990)
			{
				MainActivity.showMessage("轨迹点过多，只显示部分轨迹");
				break;
			}
			count++;
		}
		PolylineOptions polyline = new PolylineOptions().width(10)
				.color(color).points(p_list);
		realTrace=polyline;
		updateMarket();
	}
	
//	public void setMapStatus(MapStatusUpdate statusUpdate)
//	{
//		if(map!=null)
//		{
//			map.setMapStatus(statusUpdate);
//		}
//	}
	
	public void setMapFocus(LatLng point)
	{
		MapStatus mMapStatus = new MapStatus.Builder().target(point).build();
        MapStatusUpdate msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        // 用于设置地图状态
  		map.setMapStatus(msUpdate);
	}
	
	private void showNavigationDialog(Point pointEnd)
	{
		Pupillus curPup = LocatParentApplication.getCurPupullus();
		if(curPup!=null)
		{
			
		}
	}
	
	@Override
	public void onClick(View v) {
		// 若点击其他地方关闭浮动
		if (operation_pop != null && operation_pop.isShowing()) {
			operation_pop.dismiss();
		}
		int id = v.getId();
		if (id == R.id.iv_operation) {
			showOprationPopWindow(v);
		} else if (id == R.id.iv_child_list) {
			showChildrenListActivity();
		} else if (id == R.id.iv_fance_list) {
			showFanceListActivity();
		}else if (id == R.id.iv_navigate) {
//			//打开导航界面
//			MainActivity.routeplanToNavi(null, null);
			Pupillus curPup = LocatParentApplication.getCurPupullus();
			if(curPup==null)
			{
				MainActivity.showMessage("子端信息未初始化完成，请稍后再试");
				return ;
			}
			Date date = new Date(System.currentTimeMillis());
			LoadingPopupWindow popWindow = MainActivity.showLoadingPopupWindow
					(this.getView(),Gravity.CENTER);
			QueryLastestPointTask task = new QueryLastestPointTask(curPup,date,popWindow);
			task.execute();
		}

	}

}
