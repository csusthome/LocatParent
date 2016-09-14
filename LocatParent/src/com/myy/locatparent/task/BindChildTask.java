package com.myy.locatparent.task;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Pupillus;
import com.ms.utils.bean.User;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.activity.MainActivity;

/**
 * 绑定子端网络任务，不能调用两次
 * @author lenovo-Myy
 *
 */
public class BindChildTask extends AsyncTask<Object, String, Boolean> {

	private static final boolean DEBUG = true;
	private User user;
	private String desc, IMEI, checkCode;
	private LoadingPopupWindow loading_pop;
	private Exception e;
	private String str_result;

	
	public BindChildTask(User user, String alias, String IMEI,
			String checkCode) {
		this.checkCode = checkCode;
		this.user = user;
		this.desc = alias;
		this.IMEI = IMEI;
	}

	public void bindLoadingPopupWindow(LoadingPopupWindow window) {
		loading_pop = window;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loading_pop != null) {
			loading_pop.setText("网络连接中");
		}
	}

	@Override
	protected Boolean doInBackground(Object... parpam) {
		try {
			Entity entity = new Entity();
			Pupillus child = new Pupillus();
			child.setMeid(IMEI);
			child.setVerification_code(checkCode);
			User parent = user;
			entity.setAlias(desc);
			entity.setPupillus(child);
			entity.setUser(parent);
			str_result = DataExchangeUtils.banding(entity);
		} catch (IOException e) {
			e.printStackTrace();
			this.e = e;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (e != null) {
			if(e instanceof TimeoutException)
			{
				MainActivity.showMessage("绑定子端异常：" +"连接服务器超时，请检查当前网络配置");
			}
			else
			{
				MainActivity.showMessage("绑定子端异常：" + e.getMessage());
			}
			if(loading_pop!=null&&loading_pop.isShowing())
			{
				loading_pop.dismiss();
			}
		}
		if (str_result == null || str_result.trim().equals("-1")) {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("子端绑定失败，请确认IMEI码与验证码");
		}
		else if (str_result == null || str_result.trim().equals("0")) {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("子端绑定失败,您已绑定过该子端");
		}
		else {
			if (loading_pop != null) {
				loading_pop.setText("子端绑定成功");
				loading_pop.dismiss();
			}
			MainActivity.showMessage("子端绑定成功");
		}
		
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		loading_pop.setText("添加操作已取消");
	}

}
