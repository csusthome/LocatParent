package com.myy.locatparent.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.TTSPlayStateListener;
import com.baidu.vi.VDeviceAPI;
import com.igexin.sdk.PushManager;
import com.ms.utils.bean.Point;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.R;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.beans.Fance.FENCETYPE;
import com.myy.locatparent.fragment.MainFragment;
import com.myy.locatparent.fragment.MainFragment.MSGTYPE;
import com.myy.locatparent.listen.MyRoutePlanListener;
import com.myy.locatparent.listen.MyTTSPlayStateListener;
import com.myy.locatparent.listen.MyTTSPlayerCallback;
import com.myy.locatparent.task.DoubleExitUtils;
import com.myy.locatparent.task.DownRealTraceThread;
import com.myy.locatparent.utils.LogUtils;

public class MainActivity extends FragmentActivity {

	public static enum RESULT_CODE {
		FENCELIST
	}

	private static final String APP_FOLDER_NAME = "LocatParent";

	public static DownRealTraceThread downRealTraceThread;
	public static Boolean DEBUG = true;
	private FragmentManager fm = null;
	private static Handler MainHandler = null;
	private MainFragment main_frgm;

	private static LoadingPopupWindow loading_pop = null;
	public static MainActivity instance;
	private static Dialog deleteConfirm_dlg;
	private String cid;
	private String mSDCardPath = null;
	private Handler ttsHandler;
	private TTSPlayStateListener ttsListener = new MyTTSPlayStateListener();
	private static MyRoutePlanListener routeListener;
	private static DoubleExitUtils doubleExit;

	public static GuideActivity guideAcitivity;
	// 播报状态回调函数
	private BNOuterTTSPlayerCallback mTTSCallback;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		initPushService();
		// 设置无标题栏和AcitonBar
		initHandler();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initFragment();
		initNavigateService();
	}

	/**
	 * 初始化导航资源
	 */
	private void initNavigateService() {
		BNOuterLogUtil.setLogSwitcher(true);
		if (mTTSCallback == null) {
			mTTSCallback = new MyTTSPlayerCallback();
		}
		if (initDirs() == true) {
			if (ttsHandler == null) {
				ttsHandler = new TTSHandler();
			}
			BaiduNaviManager.getInstance().init(this, mSDCardPath,
					APP_FOLDER_NAME, new NaviInitListener() {
						@Override
						public void onAuthResult(int status, String msg) {
							if (0 == status) {
								LogUtils.d(MainActivity.class.getName(),
										"key校验成功!");
							} else {
								LogUtils.d(MainActivity.class.getName(),
										"key校验失败!");
							}
						}

						public void initSuccess() {
							LogUtils.i(MainActivity.class.getName(),
									"百度导航引擎初始化成功");
							initNavigationManager();
						}

						public void initStart() {
							LogUtils.i(MainActivity.class.getName(),
									"百度导航引擎初始化开始");
						}

						public void initFailed() {
							LogUtils.i(MainActivity.class.getName(),
									"百度导航引擎初始化失败");
						}

					}, null, ttsHandler, null);
		}
	}

	/**
	 * 反初始化导航服务,否则退出程序会报接收器未注销的错误
	 */
	private void unInitNavigationService() {
		BaiduNaviManager naviManager = BaiduNaviManager.getInstance();
		if (BaiduNaviManager.isNaviInited()) {
			naviManager.uninit();
		}
		VDeviceAPI.unsetNetworkChangedCallback();
	}

	private boolean initDirs() {
		mSDCardPath = getSdcardDir();
		if (mSDCardPath == null) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);
		if (!f.exists()) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	/**
	 * 初始化导航管理器的配置
	 */
	private void initNavigationManager() {
		BNaviSettingManager
				.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
		BNaviSettingManager
				.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
		BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
		BNaviSettingManager
				.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
		BNaviSettingManager
				.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
	}

	/**
	 * 规划导航
	 * 
	 * @param coType
	 *            目前默认只使用百度经纬度坐标导航
	 */
	public static void routeplanToNavi(Point startPoint, Point endPoint) {
		BNRoutePlanNode sNode = null;
		BNRoutePlanNode eNode = null;
		CoordinateType nodeType = CoordinateType.BD09LL;
		//test
//		sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436, "百度大厦", null, nodeType);
//		eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085, "北京天安门", null, nodeType);
		
		if (startPoint != null) {
			sNode = new BNRoutePlanNode(startPoint.getLongitude(),startPoint.getLatitude(),
					"起始地点", null, nodeType);
		}
		if (endPoint != null) {
			eNode = new BNRoutePlanNode(endPoint.getLongitude(),endPoint.getLatitude(),
					 "目标地点", null, nodeType);
		}
		if (sNode != null && eNode != null) {
			List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
			list.add(sNode);
			list.add(eNode);
			if (routeListener == null) {
				routeListener = new MyRoutePlanListener(sNode);
			} else {
				routeListener.setRoutePlanNode(sNode);
			}
			BaiduNaviManager.getInstance().launchNavigator(instance, list, 1,
					true, routeListener);
		}
	}

	/**
	 * 初始化消息推送的SDK资源
	 */
	private void initPushService() {
		PushManager.getInstance().initialize(this.getApplicationContext());
		// 手动获取cid
		cid = PushManager.getInstance().getClientid(this);
		LogUtils.d(MainActivity.class.getName(), "cid:" + cid);
	}

	private void stopPushService() {
		PushManager.getInstance().stopService(this.getApplicationContext());
	}

	/**
	 * 显示删除对话框，并进行后续处理
	 */
	public static void showConfirmDialog(String title, String message,
			DialogInterface.OnClickListener pos_listener,
			DialogInterface.OnClickListener nega_listener) {
		AlertDialog.Builder builder = new Builder(MainActivity.instance);
		builder.setPositiveButton("确认", pos_listener);
		builder.setNegativeButton("取消", nega_listener);
		if (message == null) {
			builder.setMessage("确认删除选中项？");
		} else {
			builder.setMessage(message);
		}
		if (title == null) {
			builder.setTitle("删除确认");
		} else {
			builder.setTitle(title);
		}
		deleteConfirm_dlg = builder.create();
		deleteConfirm_dlg.show();
	}

	/**
	 * 显示加载窗口
	 * 
	 * @param v
	 *            加载窗口的父视图
	 * @param gravity
	 *            窗口对于父视图的位置 Gravity.TOP
	 * @return
	 */
	public static LoadingPopupWindow showLoadingPopupWindow(View v, int gravity) {
		if (loading_pop == null) {
			loading_pop = new LoadingPopupWindow(MainActivity.instance);
		}
		int loc[] = new int[2];
		v.getLocationOnScreen(loc);
		loading_pop.showAtLocation(v, gravity, 0, 0);
		return loading_pop;
	}

	/**
	 * 用于活动Handler并发送消息，现在已经不适用了 使用Activity下的相关方法直接发送消息
	 * 
	 * @return
	 */
	public static Handler getHandler() {
		return MainHandler;
	}

	private void initFragment() {
		// 初始化Fragment管理器
		fm = getSupportFragmentManager();
		// 初始化主Fragment并将其中的内容设为登录界面
		main_frgm = new MainFragment();
		// login_frgm = new LoginFragment();
		fm.beginTransaction().add(R.id.fl_main, main_frgm).commit();
		// fm.beginTransaction().add(R.id.fl_content,login_frgm).commit();
	}

	private void initHandler() {
		MainHandler = new MainHandler();
	}

	private class MainHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// Message交给MainFragment处理
			try {
				main_frgm.handleMessage(msg);
			} catch (Exception e) {
				if (MainActivity.DEBUG == true) {
					MainActivity.showMessage("异常:" + e.getMessage());
				}
				e.printStackTrace();
			}
			super.handleMessage(msg);
		}
	}

	public static void showRealTrace(List<Point> point) {
		Message msg = MainHandler.obtainMessage();
		msg.what = MSGTYPE.SHOWREALTRACE.ordinal();
		msg.obj = point;
		MainHandler.sendMessage(msg);
	}

	public static void showMessage(String str_msg) {
		Message msg = MainHandler.obtainMessage();
		msg.what = MSGTYPE.MSG.ordinal();
		msg.obj = str_msg;
		MainHandler.sendMessage(msg);
	}

	public static void addFance(FENCETYPE type) {
		Message msg = MainHandler.obtainMessage();
		msg.what = MSGTYPE.ADDFANCE.ordinal();
		msg.obj = type;
		MainHandler.sendMessage(msg);
	}

	public static void showFragment(String frgm_id) {
		Message msg = MainHandler.obtainMessage();
		msg.what = MSGTYPE.FRAGMNET.ordinal();
		msg.obj = frgm_id;
		MainHandler.sendMessage(msg);
//		if(doubleExit!=null)
//		{
//			//重置退出检测器，修复切换到子窗体后仍然显示退出程序的错误
//			doubleExit.reset();
//		}
	}

	/**
	 * 设置地图的状体，例如设置地图显示等级已经焦点等等
	 */
	public static void setMapFocus(LatLng point) {
		Message msg = MainHandler.obtainMessage();
		msg.what = MSGTYPE.MAPSTATUS.ordinal();
		msg.obj = point;
		MainHandler.sendMessage(msg);
	}

	@Override
	protected void onDestroy() {
		// 关闭实时轨迹线程
		if (downRealTraceThread != null
				&& downRealTraceThread.isAlive() == true) {
			downRealTraceThread.stopThread();
		}
		// 反初始化百度导航服务
		unInitNavigationService();
		LocatParentApplication.stopLocatService();
		super.onDestroy();
	}

	/**
	 * 处理Acitivity返回的结果，传递给MainFragment处理
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (main_frgm != null) {
			main_frgm.handlerActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			//除当前碎片没有其他View存在回退栈中
			if (main_frgm.isFragmentStackCount() <= 1) {
				if (doubleExit == null) {
					doubleExit = new DoubleExitUtils();
				}
				boolean flag = doubleExit.exitCheck();
				if (flag == false) {
					//若返回true则消息会被截断
					return true;
				}
			}
		}
		main_frgm.handleBackPressed();
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 内部TTS播报状态回传handler,用于监听播报的状态
	 */
	// private Handler ttsHandler = new Handler() {
	private class TTSHandler extends Handler {
		public void handleMessage(Message msg) {
			int type = msg.what;
			switch (type) {
			case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
				LogUtils.i(MainActivity.class.getName(),
						"Handler : TTS play start");
				break;
			}
			case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
				LogUtils.i(MainActivity.class.getName(),
						"Handler : TTS play end");
				break;
			}
			default:
				break;
			}
		}
	};

}
