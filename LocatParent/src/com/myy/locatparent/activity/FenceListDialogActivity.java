package com.myy.locatparent.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ms.utils.bean.Fencing;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.R;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.fragment.FenceListFrgment;

/**
 * 因为对话框中无法加载碎片，静态和动态都使用过了，动态找不到容器ID，静态加载View时不能有 碎片，只能采用窗口格式的活动来实现了
 * 
 * @author lenovo-Myy
 * 
 */
public class FenceListDialogActivity extends FragmentActivity {

	public static Boolean DEBUG = true;
	private FragmentManager fm = null;
	private FenceListFrgment fences_frgm = null;
	public View view;
	public LoadingPopupWindow loading_pop = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题栏和AcitonBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = this.getLayoutInflater().inflate(R.layout.frame_layout, null,
				false);
		setContentView(view);
		initActionListFragment();
	}

	private void initActionListFragment() {
		Intent intent = getIntent();
		intent.getExtras();
		fm = this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		// 围栏列表
		fences_frgm = new FenceListFrgment();
		fences_frgm.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				fences_frgm.setMainItem(position);
				List<Fencing> listFence = LocatParentApplication
						.getList_fences();
				Fencing fence = listFence.get(position);
				// 更新地图焦点
				// MapStatusUpdate update = MapStatusUpdateFactory
				// .newLatLng(new
				// LatLng(fence.getLatitude(),fence.getLongitude()));
				Intent intent = new Intent();
				intent.putExtra("fenceId", fence.getId());
				//设置返回的数据
				FenceListDialogActivity.this.setResult(MainActivity.RESULT_CODE.FENCELIST.ordinal()
						,intent);
//				MainActivity.setMapFocus(new LatLng(fence.getLatitude(), fence
//						.getLongitude()));

				// 关闭对话框
				finish();
			}
		});
		ft.add(R.id.fl_frgm, fences_frgm);
		ft.commit();
	}

	/**
	 * 显示正在加载浮动窗口
	 * 
	 * @param v
	 * @return
	 */
	public LoadingPopupWindow showLoadingPopupWindow(View v, int gravity) {
		if (loading_pop == null) {
			loading_pop = new LoadingPopupWindow(this);
		}
		int loc[] = new int[2];
		v.getLocationOnScreen(loc);
		loading_pop.showAtLocation(v, gravity, 0, 0);
		return loading_pop;
	}
}
