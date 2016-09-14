package com.myy.locatparent.dailog;

import java.util.LinkedList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.model.LatLng;
import com.ms.utils.bean.Fencing;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.fragment.MapFragment;
import com.myy.locatparent.listen.ButtonEanbleTextWather;
import com.myy.locatparent.task.AddFanceTask;

public class AddFanceDialog extends Dialog implements android.view.View.OnClickListener{

	private View view = null;
	private Button but_add;
	private EditText et_desc,et_radius;
	private BaiduMap map ;
	private int radius = 100;
	private MapFragment map_frgm;
	private View.OnClickListener [] fanceCustom_listners ;
	private Fencing fence;
	//围栏的变化量
	private static int gap = 10;
	
	public AddFanceDialog(Context context,String title,MapFragment map_frgm) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.addcriclefance_layout,null);
		this.map = map_frgm.getMap();
		this.map_frgm=map_frgm;
		this.setTitle(title);
		setContentView(view);
		initButton();
	}

	
	private void initButton()
	{
		but_add = (Button)findViewById(R.id.but_add);
		but_add.setOnClickListener(this);
		but_add.setEnabled(false);
		et_desc = (EditText)findViewById(R.id.et_desc);
		et_radius = (EditText)findViewById(R.id.et_radius);
		//设置默认半径
		et_radius.setText(radius+"");
		List<EditText> et_list = new LinkedList<EditText>();
		et_list.add(et_desc);
		et_list.add(et_radius);
		//只有当2个文本框都有内容是点击按钮才可用
		ButtonEanbleTextWather text_wather = new ButtonEanbleTextWather
				(et_list,but_add);
		et_desc.addTextChangedListener(text_wather);
		et_radius.addTextChangedListener(text_wather);
	}
	
	
	private void showFenceOnMap()
	{
		String str_r = et_radius.getText().toString();
		radius = Integer.valueOf(str_r);
		MainActivity.showMessage("点击地图中的一个点作为围栏的中心");
		//初始化围栏自定义4个按钮的监听器
		if(fanceCustom_listners == null)
		{
			fanceCustom_listners = new android.view.View.OnClickListener[4];
			//确认
			fanceCustom_listners[0] = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					map.setOnMapClickListener(null);
					map_frgm.hideFanceCustomPopWindow();
					if(fence!=null)
					{
						String name = et_desc.getText().toString();
						fence.setLatitude(fence.getLatitude());
						fence.setLongitude(fence.getLongitude());
						fence.setName(name);
						fence.setRadius(fence.getRadius());
						fence.setEntity(LocatParentApplication.getCurEntity());
						AddFanceTask task = new AddFanceTask(fence);
						//显示加载窗口
						LoadingPopupWindow pop = MainActivity.
								showLoadingPopupWindow(v,Gravity.CENTER);
						task.bindLoadingPopupWindow(pop);
						task.execute();
					}
				}
			};
			//拓宽
			fanceCustom_listners[1] = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					radius+=gap;
					fence.setRadius(radius);
					map_frgm.showNewFence(fence);
				}
			};
			//缩小
			fanceCustom_listners[2] = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					radius-=gap;
					fence.setRadius(radius);
					map_frgm.showNewFence(fence);
				}
			};
			//取消
			fanceCustom_listners[3] = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					map.setOnMapClickListener(null);
					map_frgm.clearFanceOption();
					map_frgm.hideFanceCustomPopWindow();
				}
			};
		}
		
		map.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}
			@Override
			public void onMapClick(LatLng point) {
				if(fence==null)
				{
					fence = new Fencing();
				}
				fence.setLatitude(point.latitude);
				fence.setLongitude(point.longitude);
				fence.setRadius(radius);
				map_frgm.showNewFence(fence);
				map_frgm.showFanceCustomPopWindow(fanceCustom_listners);
			}
		});
		this.dismiss();
	}
	
	@Override
	public void onClick(View v) {

		int id = v.getId();
		if (id == R.id.but_add) {
			//			map_frgm.setFenceVisible(true);
			showFenceOnMap();
		}
	}

	
	

}
