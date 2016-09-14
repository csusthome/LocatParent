package com.myy.locatparent.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myy.locatparent.R;
import com.myy.locatparent.dailog.DateDialog;
import com.myy.locatparent.dailog.DateDialog.CallBack;
import com.myy.locatparent.utils.DateUtils;

public class DateFragment extends Fragment implements OnClickListener {
	private String date_format = "yyyy-MM-dd";
	private ImageView imgv_left = null;
	private ImageView imgv_right = null;
	private TextView tv_date = null;
	private TextView tv_desc = null;
	private String currentDate = null;// 记录TextView文本框显示的时间
	private DateDialog date_dlg = null;
	private SimpleDateFormat sdf = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.datefragment, container, false);
		init(view);
		sdf = new SimpleDateFormat(date_format);
		return view;
	}

	private void init(View view) {
		imgv_left = (ImageView) view.findViewById(R.id.left);// 查看前一天的按钮
		imgv_right = (ImageView) view.findViewById(R.id.right);// 查看后一天的按钮
		tv_desc = (TextView) view.findViewById(R.id.tv_desc);
		tv_date = (TextView) view.findViewById(R.id.date);
		currentDate = DateUtils.getCurrentDate();
		tv_date.setText(currentDate);
		tv_date.setOnClickListener(this);
		imgv_left.setOnClickListener(this);
		imgv_right.setOnClickListener(this);
	}

	/**
	 * 获取前一天
	 */
	private void prevousday() {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = sdf.parse(currentDate);
		} catch (java.text.ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		c.setTime(date);
		int Day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, Day - 1);
		currentDate =sdf.format(c.getTime());
		tv_date.setText(currentDate);
	}

	/**
	 * 获取后一天
	 */
	private void nextday() {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = sdf.parse(currentDate);
		} catch (java.text.ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		c.setTime(date);
		int Day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, Day + 1);
		currentDate = sdf.format(c.getTime());
		tv_date.setText(currentDate);
	}

	private void showDateDialog() {
		if (date_dlg == null) {
			date_dlg = new DateDialog(getActivity(), new CallBack() {
				@Override
				public void execute() {
					// 在这里写日期选择确定后的方法
					//Error
					GregorianCalendar calendar = date_dlg.getCalendar();
					currentDate = calendar.get(Calendar.YEAR)+"-"+
							calendar.get(Calendar.MONTH)+"-"+
							calendar.get(Calendar.DAY_OF_MONTH);
					tv_date.setText(currentDate);
				}
			}, "查询日期");
		}
		date_dlg.show();
	}
	
	public Date getDate() throws Exception
	{
		Date date = sdf.parse(currentDate);
		return date;
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.left) {
			prevousday();
		} else if (id == R.id.right) {
			nextday();
		} else if (id == R.id.date) {
			showDateDialog();
		}
	}
}
