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
	// ʵʱ�켣ͼ�긲����
    private static MarkerOptions realMarker = null;
	private BitmapDescriptor bm_real;
	// private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final String ID = "MapFragment";
	private View view = null;
	
	private ImageView iv_operation,iv_child_list,iv_fance_list,iv_navigate;
	// map��Ҫ�����ͼ���߼������͵�ͼ�����ص�
	private BaiduMap map = null;
	// mapview��Ҫ��װ�˵�ͼ��ͼ�����ò���
	private MapView map_view = null;
	private MapOpeartionPopupWinow operation_pop = null;
	private FanceCustomPopupWindow fancecustom_pop;
	//��ͼ�����Ｏ��
	private List<CircleOptions> fence_list = new LinkedList<CircleOptions>();
	//������ʾ����Χ��ʱ��ʾ��Χ��
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
		//��¼��ɺ��ȳ�ʼ���Ӷ�
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
	 * ����Χ���б���ʾ�´����Χ���б����һ��Χ����ʾΪ��Χ��
	 * @param list_fences
	 */
	public void showFences(List<Fencing> list_fences)
	{
		fence_list.clear();
		int i = 0;
		for(Fencing fence:list_fences)
		{
			//���һ��Χ����ʾΪ��Χ��
			if(i==list_fences.size()-1)
			{
				CircleOptions option = FenceUtils.getCircleOptionByFencing
						(fence,FENCECOLOR.MAIN);
				fence_list.add(option);
				LatLng latlng = new LatLng(fence.getLatitude(),fence.getLongitude());
				//����ͼ����ۼ�����Χ����
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
		//������Χ��
		newFence=null;
		setFenceVisible(true);
		updateMarket();
	}
	
	/**
	 * ��ʾ�Ѵ��ڵ�Χ���б�
	 * @param mainFence ��Ҫ��Ҫ��ʾ��Χ��ID����������Χ���б���
	 * ��������۽���ʾ��������������
	 */
	public void showFences(int fenceId)
	{
		List<Fencing> list_fences = LocatParentApplication.getList_fences();
		fence_list.clear(); 
		for(Fencing fence:list_fences)
		{
			//��Ϊ����ʾΧ��
			if(fence.getId().equals(fenceId))
			{
				CircleOptions option = FenceUtils.getCircleOptionByFencing
						(fence,FENCECOLOR.MAIN);
				fence_list.add(option);
				LatLng latlng = new LatLng(fence.getLatitude(),fence.getLongitude());
				//����ͼ����ۼ�����Χ����
				setMapFocus(latlng);
			}
			else
			{
				CircleOptions option = FenceUtils.getCircleOptionByFencing
						(fence,FENCECOLOR.OLD );
				fence_list.add(option);
			}
			
		}
		//������Χ��
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
	 * ����Χ���ɼ�
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
	 * ˢ�µ�ͼ�������չ�ʱ�ĸ�����
	 */
	private void updateMarket()
	{
		//������и������WindowInfo
		map.clear();
		//��ʾΧ��
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
		//��ʼ����ͼ����
		MapStatus mMapStatus = new MapStatus.Builder().zoom(15).build();
        MapStatusUpdate msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        // �������õ�ͼ״̬
		map = map_view.getMap();
		map.setMapStatus(msUpdate);
		if (DEBUG == false) {
			// ����ʾ�Ŵ���С�ı�־
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
	 * ����4������������ֱ��Ӧ��Χ���Զ����4����ť
	 * @param listners
	 */
	public void showFanceCustomPopWindow(OnClickListener [] listners)
	{
		fancecustom_pop.setTextViewOnClickListeners(listners);
		WindowManager wm = (WindowManager) getActivity()
				.getSystemService(Context.WINDOW_SERVICE);
		int w_height = wm.getDefaultDisplay().getHeight();
		//������ߴ�ֱ������ʾ
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
	 * ��ĳ���ؼ��Ϸ���ʾpop
	 * 
	 * @param v ������ؼ�����ʾ 
	 */
	private void showOprationPopWindow(View v) {
		int loc[] = new int[2];
		v.getLocationOnScreen(loc);

		operation_pop.showAtLocation(v, Gravity.NO_GRAVITY, loc[0], loc[1]
				- operation_pop.getHeight());
	}


	/**
	 * ��ʾģ��ɶԻ���Ķ�ͯ�б�Activity
	 */
	private void showChildrenListActivity() {
		Intent intent = new Intent(getActivity(),
				EntityListDialogActivity.class);
		startActivity(intent);
	}

	/**
	 * ��ʾģ��ɶԻ����Χ���б�Activity
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
		// Ϊ��ͼ��Ӹ�����
		map.addOverlay(polyline);
	}
	
	/**
	 * ���һ����ʷ�켣����ͼ��
	 * @param point_list
	 */
	public void showHistoryTraceMarker(List<Point> point_list) {
		//��ƫ
//		point_list = TraceUtils.correctTrace(point_list);
		ArrayList<LatLng> p_list = new ArrayList<LatLng>();
		Point last_p = point_list.get(point_list.size()-1);
		//��λ���������һ����λ��
		LatLng latlng = new LatLng(last_p.getLatitude()
				,last_p.getLongitude());
		setMapFocus(latlng);
		int count=0;
		if(point_list.size()<2)
		{
			MainActivity.showMessage("�켣����٣����Ժ�����");
			return ;
		}
		int color = Color.RED;
		for(Point p : point_list)
		{
			latlng = new LatLng(p.getLatitude(), p.getLongitude());
			p_list.add(latlng);
			if(count>99990)
			{
				MainActivity.showMessage("�켣����ֻ࣬��ʾ���ֹ켣");
				break;
			}
			count++;
		}
		PolylineOptions polyline = new PolylineOptions().width(10)
				.color(color).points(p_list);
		// Ϊ��ͼ��Ӹ�����
		map.addOverlay(polyline);
		historyTrace=polyline;
		MainActivity.showMessage("��ʷ�켣��ʾ"+count+"����");
		updateMarket();
	}
	
	/**
	 * ��ʾʵʱ�켣
	 * @param point_list
	 */
	public void showRealTraceMarker(List<Point> point_list) {
		//��ƫ
//		point_list = TraceUtils.correctTrace(point_list);
		ArrayList<LatLng> p_list = new ArrayList<LatLng>();
		Point last_p = point_list.get(point_list.size()-1);
		LatLng latlng = new LatLng(last_p.getLatitude()
				,last_p.getLongitude());
		if(bm_real==null)
		{
			bm_real = BitmapDescriptorFactory.fromResource(R.drawable.real_here);
		}
		// ���ʵʱ�켣ͼ��
        realMarker = new MarkerOptions()
                .position(latlng).icon(bm_real)
                .zIndex(9).draggable(true);
		int count=0;
		
		setMapFocus(latlng);
		if(point_list.size()<2)
		{
			MainActivity.showMessage("�켣����٣����Ժ�����");
			return ;
		}
		int color = Color.GREEN;
		for(Point p : point_list)
		{
			latlng = new LatLng(p.getLatitude(), p.getLongitude());
			p_list.add(latlng);
			if(count>99990)
			{
				MainActivity.showMessage("�켣����ֻ࣬��ʾ���ֹ켣");
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
        // �������õ�ͼ״̬
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
		// ����������ط��رո���
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
//			//�򿪵�������
//			MainActivity.routeplanToNavi(null, null);
			Pupillus curPup = LocatParentApplication.getCurPupullus();
			if(curPup==null)
			{
				MainActivity.showMessage("�Ӷ���Ϣδ��ʼ����ɣ����Ժ�����");
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
