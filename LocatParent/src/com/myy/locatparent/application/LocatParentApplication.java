package com.myy.locatparent.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.ms.utils.bean.AnomalyRecord;
import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.FencingRecord;
import com.ms.utils.bean.Point;
import com.ms.utils.bean.Pupillus;
import com.ms.utils.bean.User;
import com.ms.utils.bean.WhiteNum;
import com.myy.locatparent.listen.ParentLocationListener;
import com.myy.locatparent.listen.ParentLocationListener.LocateCallBack;

/**
 * 初始化资源，并存储一些全局数据
 * 任何标记数据切换后，应该重置与之关联的其他数据
 * @author lenovo-Myy
 *
 */
public class LocatParentApplication extends Application {
	
	//百度定位所需的变量
	private static LocationClient mLocationClient;
	private static ParentLocationListener mMyLocationListener;
	//定位配置
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
    private String tempcoor="bd09ll";
    private int span = 1000;
	
	//用于记录父端当前的位置
//	private static Point curPoint;
	private static List<Entity> list_entities = new ArrayList<Entity>() ;
	private static List<Fencing> list_fences = new ArrayList<Fencing>();
	private static List<Pupillus> list_pups = new ArrayList<Pupillus>();
	private static List<AnomalyRecord> list_anomalys = new ArrayList<AnomalyRecord>();
	private static List<FencingRecord> list_fenRecords = new ArrayList<FencingRecord>();
	private static List<WhiteNum> list_whites = new ArrayList<WhiteNum>();
	//main表示在地图中聚焦（主要显示）的对象，而cur表示当前操作的对象
	//设置main的同时会设置cur
	private static Pupillus mainPup,curPup;
	private static User curUser = null;
	private static Entity mainEntity,curEntity;
	private static Fencing mainFence,curFence;
	
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	@Override
	public void onCreate() {
		super.onCreate();
		initLocatServer();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
	}

	/**
	 * 初始化定位服务
	 */
	private void initLocatServer()
	{
		if(mLocationClient==null)
		{
			mMyLocationListener = new ParentLocationListener();
			mLocationClient = new LocationClient(getApplicationContext());
			mLocationClient.registerLocationListener(mMyLocationListener);
		}
		
		LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
        //test
//        startLocatService(null);
	}
	
	
	
	public static void startLocatService(LocateCallBack callback)
	{
		if(mLocationClient.isStarted()==false)
		{
			mMyLocationListener.setLocatCallBack(callback);
			mMyLocationListener.reset();
			mLocationClient.start();
			
		}
	}
	
	public static void stopLocatService()
	{
		if(mLocationClient.isStarted())
		{
			mLocationClient.stop();
		}
	}
	
	public static User getUser() {
		return curUser;
	}


	public static void setUser(User user) {
		//若User更改为不同的User则为注销操作
		if(user==null||LocatParentApplication.curUser==null||
				LocatParentApplication.curUser.getId().equals(user.getId())==false)
		{
			setList_entities(null);
			setMainEntity(null);
		}
		LocatParentApplication.curUser = user;
	}
	
	public static Pupillus getMainPupillus() {
		return mainPup;
	}


	public static void setMainPupillus(Pupillus mainPup) {
		LocatParentApplication.mainPup = mainPup;
		setCurPupullus(mainPup);
	}


	public static Pupillus getCurPupullus() {
		return curPup;
	}

	
	public static void setCurPupullus(Pupillus curPup) {
		//若当前子端以及改变，则将与其对应的其他数据重置(只用重置直接下级)
		if(curPup==null||LocatParentApplication.curPup==null
				||curPup.equals(curPup)==false)
		{
			setMainFence(null);
			setList_fences(null);
		}
		LocatParentApplication.curPup = curPup;
	}


	


	public static Entity getMainEntity() {
		return mainEntity;
	}


	public static void setMainEntity(Entity mainEntity) {
		LocatParentApplication.mainEntity = mainEntity;
		setCurEntity(mainEntity);
	}


	public static Entity getCurEntity() {
		return curEntity;
	}


	public static void setCurEntity(Entity curEntity) {
		if(curEntity==null||LocatParentApplication.curEntity==null||
				curEntity.equals(LocatParentApplication.curEntity)==false)
		{
			setMainFence(null);
			setList_fences(null);
		}
		LocatParentApplication.curEntity = curEntity;
	}


	public static Fencing getMainFence() {
		return mainFence;
	}


	public static void setMainFence(Fencing mainFence) {
		LocatParentApplication.mainFence = mainFence;
//		curFence = mainFence;
		setCurFence(mainFence);
	}


	public static Fencing getCurFence() {
		return curFence;
	}


	public static void setCurFence(Fencing curFence) {
		if(curFence==null||LocatParentApplication.curFence==null||
				curFence.equals(LocatParentApplication.curFence)==false)
		{
			setList_anomalys(null);
			setList_fenRecords(null);
			setList_whites(null);
		}
		LocatParentApplication.curFence = curFence;
	}


	public static List<Entity> getList_entities() {
		return list_entities;
	}

	public static void setList_entities(List<Entity> list_entities) {
		LocatParentApplication.list_entities.clear();
		if(list_entities!=null)
		{
			LocatParentApplication.list_entities.addAll(list_entities);
		}
	}

	public static List<Fencing> getList_fences() {
		return list_fences;
	}

	public static void setList_fences(List<Fencing> list_fences) {
		LocatParentApplication.list_fences.clear();
		if(list_fences!=null)
		{
			LocatParentApplication.list_fences.addAll(list_fences);
		}
	}

	public static List<Pupillus> getList_pups() {
		return list_pups;
	}

	public static void setList_pups(List<Pupillus> list_pups) {
		LocatParentApplication.list_pups.clear();
		if(list_pups!=null)
		{
			LocatParentApplication.list_pups.addAll(list_pups);
		}
	}

	public static List<AnomalyRecord> getList_anomalys() {
		return list_anomalys;
	}

	public static void setList_anomalys(List<AnomalyRecord> list_anomalys) {
		LocatParentApplication.list_anomalys.clear();
		if(list_anomalys!=null)
		{
			LocatParentApplication.list_anomalys.addAll(list_anomalys);
		}
	}

	public static List<FencingRecord> getList_fenRecords() {
		return list_fenRecords;
	}

	public static void setList_fenRecords(List<FencingRecord> list_fenRecords) {
		LocatParentApplication.list_fenRecords.clear();
		if(list_fenRecords!=null)
		{
			LocatParentApplication.list_fenRecords.addAll(list_fenRecords);
		}
	}

	public static List<WhiteNum> getList_whites() {
		return list_whites;
	}

	public static void setList_whites(List<WhiteNum> list_whites) {
		LocatParentApplication.list_whites.clear();
		if(list_whites!=null)
		{
			LocatParentApplication.list_whites.addAll(list_whites);
		}
	}

}
