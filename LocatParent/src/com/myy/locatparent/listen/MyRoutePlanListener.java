package com.myy.locatparent.listen;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.myy.locatparent.activity.GuideActivity;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.application.LocatParentApplication;

public class MyRoutePlanListener implements RoutePlanListener {

	private BNRoutePlanNode mBNRoutePlanNode = null;

	public MyRoutePlanListener(BNRoutePlanNode node) {
		mBNRoutePlanNode = node;
	}

	public void setRoutePlanNode(BNRoutePlanNode node)
	{
		this.mBNRoutePlanNode = node;
	}
	
	@Override
	public void onJumpToNavigator() {
		/*
		 * 设置途径点以及resetEndNode会回调该接口
		 */
		if (MainActivity.guideAcitivity != null) {
			return;
		}
		Intent intent = new Intent(MainActivity.instance, GuideActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(LocatParentApplication.ROUTE_PLAN_NODE,
				(BNRoutePlanNode) mBNRoutePlanNode);
		intent.putExtras(bundle);
		MainActivity.instance.startActivity(intent);
	}

	@Override
	public void onRoutePlanFailed() {
		// TODO Auto-generated method stub
		MainActivity.showMessage("算路失败");
	}
}