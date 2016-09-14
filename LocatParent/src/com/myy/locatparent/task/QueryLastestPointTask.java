package com.myy.locatparent.task;

import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Point;
import com.ms.utils.bean.Pupillus;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.listen.ParentLocationListener.LocateCallBack;
import com.myy.locatparent.utils.LogUtils;

/**
 * �õ��Ӷ������ֵĵ�,Ӧ��ʹ��ģ̬��ʾ
 * @author lenovo-Myy
 *
 */
public class QueryLastestPointTask extends AbstrctTask{

	private Pupillus queryPup;
	private Point lastPoint;
	private Date queryData;
	private List<Point> listPoint;
//	private Dialog dialog;
	//�����ø���ļ��ضԻ���ʹ�����Լ��ܹ����Ƽ��ضԻ������������
	private LoadingPopupWindow popWinodw;
	public QueryLastestPointTask(Pupillus pup,Date date,LoadingPopupWindow popWiondow)
	{
		setTaskName("��ȡ�Ӷ�λ��");
		queryPup = pup;
		queryData = date;
		this.popWinodw = popWiondow;
	}
	
	
//	public void bindDialog(Dialog dialog)
//	{
//		this.dialog = dialog;
//	}
	
	

	@Override
	protected boolean process() throws Exception {
		if(queryPup==null||queryPup.getId()==null||queryData==null)
		{
			LogUtils.i(taskName, taskName+"����������");
			return false;
		}
		listPoint = DataExchangeUtils.getRealTime(queryPup.getId(),queryData);
		if(listPoint==null||listPoint.size()<=0)
		{
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		popWinodw.setText(taskName+"��");
	}


	@Override
	protected boolean doInSuccess() {
//		if(listPoint.size()<=0)
//		{
//			LogUtils.i(taskName, taskName+"��������");
//			MainActivity.showMessage(taskName+":�Ӷ˽�����λ����Ϣ������ȡ��");
//			return;
//		}
		lastPoint = listPoint.get(0);
		
		if(popWinodw!=null)
		{
			popWinodw.setText("��ȡ����λ��");
		}
		LocatParentApplication.startLocatService(new LocateCallBack() {
			private Point startPoint;
			private Point endPoint;
			@Override
			public void onLocatSuccess(Point point) {
				startPoint = point;
				endPoint = lastPoint;
				Entity curEntity = LocatParentApplication.getCurEntity();
				MainActivity.showConfirmDialog("��ʼ����","��ȷ��Ҫ������"+curEntity.getAlias()+"�����λ�ã�"
						,new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								MainActivity.routeplanToNavi(startPoint, endPoint);
								LogUtils.i(LocateCallBack.class.getName(),"���������滮����ʼ�� ���ȣ� "+startPoint.getLongitude()+" γ�ȣ�"+startPoint.getLatitude()
										+"Ŀ��� ���ȣ�"+endPoint.getLongitude()+" γ�ȣ�"+endPoint.getLatitude());
								popWinodw.dismiss();
							}
						},null);
			}
			@Override
			public void onLocatFailure() {
				LogUtils.i(taskName,"��ȡ����λ��ʧ��");
				MainActivity.showMessage(taskName+":��ȡ����λ��ʧ�ܣ�����ȡ��");
			}
		});
		return false;
	}

	@Override
	protected boolean doInFail() {
//		MainActivity.showMessage("��ȡ�Ӷ�λ��ʧ��");
		if(popWinodw!=null)
		{
			popWinodw.dismiss();
		}
		return false;
	}

	
}
