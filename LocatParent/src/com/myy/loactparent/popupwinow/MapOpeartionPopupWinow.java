package com.myy.loactparent.popupwinow;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.Pupillus;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.dailog.DateDialog;
import com.myy.locatparent.fragment.FenceListFrgment;
import com.myy.locatparent.fragment.MapFragment;
import com.myy.locatparent.listen.PopWindowClickListener;
import com.myy.locatparent.listen.PopWindowClickListener.CallBack;
import com.myy.locatparent.task.DownRealTraceThread;
import com.myy.locatparent.task.QueryHistoryTraceTask;
import com.myy.locatparent.utils.ITraceFilter;

public class MapOpeartionPopupWinow extends PopupWindow {
	private DateDialog dlg_date = null;
	private LayoutInflater inflater;
	private Activity activity;
	private View view;
	private ViewGroup vg_realtrace, vg_histrace, vg_fance;
	private MapFragment map_frgm;
	public PopWindowClickListener real_popListener,his_popListener,fence_popListener;
	private boolean fenceVisible = false,realTraceVisible = false,hisTraceVisible=false;
	private List<ITraceFilter> realFilters = new ArrayList<ITraceFilter>();
	private List<ITraceFilter> historyFilters = new ArrayList<ITraceFilter>();
	
	public MapOpeartionPopupWinow(Activity activity,MapFragment map_frgm) {

		this.map_frgm = map_frgm;
		this.activity = activity;
		inflater = LayoutInflater.from(activity);
		view = inflater.inflate(R.layout.operation_pop, null);
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		// ʹ��MeasureSpec�������ȷ�ĵõ�popwindow�Ŀ��
		initViewGroup();

		// �����˾���Ŀ�ߺ�������Ϸ���ʾ
		this.setContentView(view);
		this.setWidth(view.getMeasuredWidth());
		this.setHeight(view.getMeasuredHeight());
		// ����һ���յı������ܹ���������ط�
		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);
		setFocusable(true);
		setOutsideTouchable(true);
	}

	
	
	private void initViewGroup() {
		// ���õ���������ʵʱ�켣��ť�ļ����¼�
		vg_realtrace = (ViewGroup) view.findViewById(R.id.rl_positive);
		real_popListener = new PopWindowClickListener(vg_realtrace);
		real_popListener.setCallBack(new CallBack() {
			@Override
			public void onCallBack(boolean is_pred) {
				//���°�ť
				if(is_pred==true)
				{
					map_frgm.setRealTraceVisible(true);
					if(MainActivity.downRealTraceThread==null||
							MainActivity.downRealTraceThread.isAlive()==false)
					{
						if(LocatParentApplication.getCurPupullus()==null)
						{
							MainActivity.showMessage("��ǰ��ѡ�����Ӷ˻�δͬ����ϣ�" +
									"���Ժ�����");
							return;
						}
						MainActivity.downRealTraceThread = new DownRealTraceThread
								(LocatParentApplication.getCurPupullus().getId());
						MainActivity.downRealTraceThread.start();
						MainActivity.showMessage("ʵʱ�켣��ѯ�����ɹ�");
					}
					else
					{
						MainActivity.showMessage("ʵʱ�켣��ѯ�Ѿ�����");
					}
					
				}
				else
				{
					map_frgm.setRealTraceVisible(false);
					if(MainActivity.downRealTraceThread!=null&&
							MainActivity.downRealTraceThread.isAlive()==true)
					{
						MainActivity.downRealTraceThread.stopThread();
						MainActivity.showMessage("ʵʱ�켣��ѯ�ر�");
					}
				}
			}
		});
		vg_realtrace.setOnClickListener(real_popListener);

		// ���õ�����������ʷ�켣��ť�ļ����¼�
		vg_histrace = (ViewGroup) view.findViewById(R.id.rl_history);
		his_popListener = new PopWindowClickListener(vg_histrace);
		his_popListener.setCallBack(new CallBack() {
			@Override
			public void onCallBack(boolean is_pred) {
				try {
					//���°�ť
					if(is_pred==true)
					{
						showHisDateSelectDialog();
						map_frgm.setHisTraceVisible(true);
					}
					else
					{
						map_frgm.setHisTraceVisible(false);
						MainActivity.showMessage("�����ʷ�켣");
					}
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				finally
				{
//					map_frgm.updateMarket();
				}
			}
		});
		vg_histrace.setOnClickListener(his_popListener);
		// ���õ���������Χ����ť�ļ����¼�
		vg_fance = (ViewGroup) view.findViewById(R.id.rl_fance);
		fence_popListener = new PopWindowClickListener(vg_fance);
		fence_popListener.setCallBack(new CallBack() {
			@Override
			public void onCallBack(boolean is_pred) {
				//is_pred��ʾ���ڰ�ť��״̬
				if(is_pred==true)
				{
					Fencing mainFence = LocatParentApplication.getMainFence();
					if(mainFence!=null)
					{
						map_frgm.showFences(mainFence.getId());
						MainActivity.showMessage("��ʾΧ��");
					}
					else
					{
						FenceListFrgment.updateFenceData(null,null);
						MainActivity.showMessage("Χ���������ڲ�ѯ�У����Ժ�����");
					}
					
				}
				else
				{
					map_frgm.setFenceVisible(false);
					MainActivity.showMessage("����Χ��");
				}
			}
		});
		vg_fance.setOnClickListener(fence_popListener);
	}

	/**
	 * ��ʾ��ʷ�켣��ѯ������
	 * 
	 * @throws ParseException
	 */
	private void showHisDateSelectDialog() throws ParseException {
		if (dlg_date == null) {
			dlg_date = new DateDialog(activity, new DateDialog.CallBack() {
				public void execute() {
					Pupillus pup = LocatParentApplication.getCurPupullus();
					if(pup==null||pup.getId()==null)
					{
						MainActivity.showMessage("��ʵ����Ϣ��δ��ʼ����ɣ����Ժ�����");
						return ;
					}
					MainActivity.showMessage("���ڲ�ѯ��ʷ�켣");
					Date date;
					try {
						date = dlg_date.getDate();
						QueryHistoryTraceTask task = QueryHistoryTraceTask.getInstance(date
								,pup);
						if(task!=null)
						{
							task.bindMapFragment(map_frgm);
							task.execute();
						}
						else
						{
							MainActivity.showMessage("��ʷ�켣�Ѿ��ڲ�ѯ��,���Ե�");
						}
					} catch (ParseException e) {
						MainActivity.showMessage("MapOperationPopupWinow ��ѯ��ʷ�켣�쳣"+e.getMessage());
						e.printStackTrace();
					}
				}
			}, "ѡ����Ҫ��ѯ������");
		}
		dlg_date.setCancelable(true);
		dlg_date.show();
	}
	
	
	
	public boolean isFenceVisible() {
		return fence_popListener.getIsChecked();
	}


	public void setFenceVisible(boolean fenceVisible) {
		fence_popListener.setViewChecked(fenceVisible);
	}



	public boolean isRealTraceVisible() {
		return real_popListener.getIsChecked();
	}



	public void setRealTraceVisible(boolean realTraceVisible) {
		real_popListener.setViewChecked(realTraceVisible);
	}



	public boolean isHisTraceVisible() {
		return his_popListener.getIsChecked();
	}



	public void setHisTraceVisible(boolean hisTraceVisible) {
		his_popListener.setViewChecked(hisTraceVisible);
	}


	

}
