package com.myy.locatparent.task;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.User;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.fragment.LoginFragment;

/**
 * 绑定子端网络任务，不能调用两次
 * @author lenovo-Myy
 *
 */
public class UserRegisterTask extends AsyncTask<Object, String, Boolean> {

	private static final boolean DEBUG = true;
	private User user;
	private String result_str;
	private LoadingPopupWindow loading_pop;
	private Exception e;
	
	
	public UserRegisterTask(User user) {
		this.user = user;
	}

	public void bindLoadingPopupWindow(LoadingPopupWindow window) {
		loading_pop = window;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loading_pop != null) {
			loading_pop.setText("注册中,请稍后");
		}
	}

	@Override
	protected Boolean doInBackground(Object... parpam) {
		try {
			result_str = DataExchangeUtils.userRegister(user);
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
				MainActivity.showMessage("注册异常：" +"连接服务器超时，请检查当前网络配置");
			}
			else
			{
				MainActivity.showMessage("注册异常：" + e.getMessage());
			}
			if(loading_pop!=null&&loading_pop.isShowing())
			{
				loading_pop.dismiss();
			}
			return ;
		}
		if (result_str==null||result_str.trim().equals("-1")) {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("注册失败，请稍后重试");
		}
		else if(result_str.trim().equals("0"))
		{
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("该手机号已注册，请用别的手机号注册");
		}
		else {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("注册成功");
			MainActivity.showFragment(LoginFragment.ID);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

}
