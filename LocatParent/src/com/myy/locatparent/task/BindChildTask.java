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
 * ���Ӷ��������񣬲��ܵ�������
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
			loading_pop.setText("����������");
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
				MainActivity.showMessage("���Ӷ��쳣��" +"���ӷ�������ʱ�����鵱ǰ��������");
			}
			else
			{
				MainActivity.showMessage("���Ӷ��쳣��" + e.getMessage());
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
			MainActivity.showMessage("�Ӷ˰�ʧ�ܣ���ȷ��IMEI������֤��");
		}
		else if (str_result == null || str_result.trim().equals("0")) {
			if (loading_pop != null) {
				loading_pop.dismiss();
			}
			MainActivity.showMessage("�Ӷ˰�ʧ��,���Ѱ󶨹����Ӷ�");
		}
		else {
			if (loading_pop != null) {
				loading_pop.setText("�Ӷ˰󶨳ɹ�");
				loading_pop.dismiss();
			}
			MainActivity.showMessage("�Ӷ˰󶨳ɹ�");
		}
		
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		loading_pop.setText("��Ӳ�����ȡ��");
	}

}
