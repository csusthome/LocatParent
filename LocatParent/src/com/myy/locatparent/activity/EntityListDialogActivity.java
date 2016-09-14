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

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.ms.utils.bean.Fencing;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.R;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.dailog.AddChildDialog;
import com.myy.locatparent.fragment.EntityListFragment;
import com.myy.locatparent.fragment.FenceListFrgment;
import com.myy.locatparent.utils.FenceUtils;

/**
 * ��Ϊ�Ի������޷�������Ƭ����̬�Ͷ�̬��ʹ�ù��ˣ���̬�Ҳ�������ID����̬����Viewʱ������ ��Ƭ��ֻ�ܲ��ô��ڸ�ʽ�Ļ��ʵ����
 * 
 * @author lenovo-Myy
 * 
 */
public class EntityListDialogActivity extends FragmentActivity {

	public static Boolean DEBUG = true;
	private FragmentManager fm = null;
	private EntityListFragment childs_frgm = null;
	private AddChildDialog addchild_dlg = null;
	public View view;
	public LoadingPopupWindow loading_pop = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �����ޱ�������AcitonBar
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
		// �Ӷ��б�
		childs_frgm = new EntityListFragment();
		childs_frgm.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				// showClientInfoFragment();
				// ���õ�ǰʵ��
				LocatParentApplication.setCurEntity(childs_frgm
						.getEntityByPostition(pos));
				childs_frgm.setMainItem(pos);
			}
		});
		ft.add(R.id.fl_frgm, childs_frgm);
		ft.commit();
	}

	/**
	 * ��ʾ���ڼ��ظ�������
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
