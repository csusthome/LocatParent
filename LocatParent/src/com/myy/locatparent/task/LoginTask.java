package com.myy.locatparent.task;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.User;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.fragment.MapFragment;

/**
 * 绑定子端网络任务，不能调用两次
 * @author lenovo-Myy
 *
 */
public class LoginTask extends AsyncTask<Object, String, Boolean> {

	private static final boolean DEBUG = true;
	private User user,result_user;
	private LoadingPopupWindow loading_pop;
	private Exception e;
	
	
	public LoginTask(User user) {
		this.user = user;
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
			result_user = DataExchangeUtils.userLogin(user);
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
			if(e instanceof TimeoutException|| e instanceof SocketTimeoutException)
			{
				MainActivity.showMessage("登录异常：" +"连接服务器超时，请检查当前网络配置");
			}
			else
			{
				MainActivity.showMessage("登录异常：" + e.getMessage());
			}
			if(loading_pop!=null&&loading_pop.isShowing())
			{
				loading_pop.dismiss();
			}
			return ;
		}
		if (result_user.getId()==null||result_user.getId()<=0) {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("账号或秘密错误");
		}
		else {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("登录成功");
			LocatParentApplication.setUser(result_user);
			MainActivity.showFragment(MapFragment.ID);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

}
