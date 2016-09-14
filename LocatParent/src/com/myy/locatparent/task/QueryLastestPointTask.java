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
 * 得到子端最后出现的点,应该使用模态显示
 * @author lenovo-Myy
 *
 */
public class QueryLastestPointTask extends AbstrctTask{

	private Pupillus queryPup;
	private Point lastPoint;
	private Date queryData;
	private List<Point> listPoint;
//	private Dialog dialog;
	//不适用父类的加载对话框，使我们自己能够控制加载对话框的生命周期
	private LoadingPopupWindow popWinodw;
	public QueryLastestPointTask(Pupillus pup,Date date,LoadingPopupWindow popWiondow)
	{
		setTaskName("获取子端位置");
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
			LogUtils.i(taskName, taskName+"：参数错误");
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
		popWinodw.setText(taskName+"中");
	}


	@Override
	protected boolean doInSuccess() {
//		if(listPoint.size()<=0)
//		{
//			LogUtils.i(taskName, taskName+"：无数据");
//			MainActivity.showMessage(taskName+":子端近期无位置信息，导航取消");
//			return;
//		}
		lastPoint = listPoint.get(0);
		
		if(popWinodw!=null)
		{
			popWinodw.setText("获取本机位置");
		}
		LocatParentApplication.startLocatService(new LocateCallBack() {
			private Point startPoint;
			private Point endPoint;
			@Override
			public void onLocatSuccess(Point point) {
				startPoint = point;
				endPoint = lastPoint;
				Entity curEntity = LocatParentApplication.getCurEntity();
				MainActivity.showConfirmDialog("开始导航","您确定要导航到"+curEntity.getAlias()+"最近的位置？"
						,new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								MainActivity.routeplanToNavi(startPoint, endPoint);
								LogUtils.i(LocateCallBack.class.getName(),"开启导航规划：起始点 经度： "+startPoint.getLongitude()+" 纬度："+startPoint.getLatitude()
										+"目标点 经度："+endPoint.getLongitude()+" 纬度："+endPoint.getLatitude());
								popWinodw.dismiss();
							}
						},null);
			}
			@Override
			public void onLocatFailure() {
				LogUtils.i(taskName,"获取本机位置失败");
				MainActivity.showMessage(taskName+":获取本机位置失败，导航取消");
			}
		});
		return false;
	}

	@Override
	protected boolean doInFail() {
//		MainActivity.showMessage("获取子端位置失败");
		if(popWinodw!=null)
		{
			popWinodw.dismiss();
		}
		return false;
	}

	
}
