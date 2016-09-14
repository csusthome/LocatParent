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
 * ��ʼ����Դ�����洢һЩȫ������
 * �κα�������л���Ӧ��������֮��������������
 * @author lenovo-Myy
 *
 */
public class LocatParentApplication extends Application {
	
	//�ٶȶ�λ����ı���
	private static LocationClient mLocationClient;
	private static ParentLocationListener mMyLocationListener;
	//��λ����
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
    private String tempcoor="bd09ll";
    private int span = 1000;
	
	//���ڼ�¼���˵�ǰ��λ��
//	private static Point curPoint;
	private static List<Entity> list_entities = new ArrayList<Entity>() ;
	private static List<Fencing> list_fences = new ArrayList<Fencing>();
	private static List<Pupillus> list_pups = new ArrayList<Pupillus>();
	private static List<AnomalyRecord> list_anomalys = new ArrayList<AnomalyRecord>();
	private static List<FencingRecord> list_fenRecords = new ArrayList<FencingRecord>();
	private static List<WhiteNum> list_whites = new ArrayList<WhiteNum>();
	//main��ʾ�ڵ�ͼ�о۽�����Ҫ��ʾ���Ķ��󣬶�cur��ʾ��ǰ�����Ķ���
	//����main��ͬʱ������cur
	private static Pupillus mainPup,curPup;
	private static User curUser = null;
	private static Entity mainEntity,curEntity;
	private static Fencing mainFence,curFence;
	
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	@Override
	public void onCreate() {
		super.onCreate();
		initLocatServer();
		// ��ʹ�� SDK �����֮ǰ��ʼ�� context ��Ϣ������ ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
	}

	/**
	 * ��ʼ����λ����
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
        option.setLocationMode(tempMode);//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
        option.setCoorType(tempcoor);//��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ��
        option.setScanSpan(span);//��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
        option.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
        option.setOpenGps(true);//��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
        option.setLocationNotify(true);//��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
        option.setIgnoreKillProcess(true);//��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
        option.setEnableSimulateGps(false);//��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
        option.setIsNeedLocationDescribe(true);//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
        option.setIsNeedLocationPoiList(true);//��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
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
		//��User����Ϊ��ͬ��User��Ϊע������
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
		//����ǰ�Ӷ��Լ��ı䣬�������Ӧ��������������(ֻ������ֱ���¼�)
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
