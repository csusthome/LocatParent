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

import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.EntityListDialogActivity;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.listen.ButtonEanbleTextWather;
import com.myy.locatparent.task.BindChildTask;

public class AddChildDialog extends Dialog implements android.view.View.OnClickListener{

	private View view = null;
	private Button but_add;
	private EditText et_desc,et_imei,et_check;
	private Context context;
	public AddChildDialog(Context context,String title) {
		super(context);
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.addchild_layout,null);
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
		et_imei = (EditText)findViewById(R.id.et_imei);
		et_check = (EditText)findViewById(R.id.et_checkcode);
		List<EditText> et_list = new LinkedList<EditText>();
		et_list.add(et_desc);
		et_list.add(et_imei);
		et_list.add(et_check);
		//只有当三个文本框都有内容是点击按钮才可用
		ButtonEanbleTextWather text_wather = new ButtonEanbleTextWather
				(et_list,but_add);
		et_desc.addTextChangedListener(text_wather);
		et_imei.addTextChangedListener(text_wather);
		et_check.addTextChangedListener(text_wather);
	}
	
	
	private void addChild(View v)
	{
		String desc = et_desc.getText().toString();
		String IMEI = et_imei.getText().toString();
		String checkCode = et_check.getText().toString();
		BindChildTask addTask = new BindChildTask(LocatParentApplication.getUser()
				,desc , IMEI, checkCode);
		LoadingPopupWindow pop=null;
		if(context instanceof EntityListDialogActivity)
		{
			EntityListDialogActivity activity = 
					(EntityListDialogActivity)context;
			//显示加载对话框
			pop = activity.showLoadingPopupWindow(v,Gravity.TOP);
		}
		else if(context instanceof MainActivity)
		{
			pop = MainActivity.showLoadingPopupWindow(v, Gravity.CENTER);
		}
		//绑定用于显示进度的加载浮动窗口
		addTask.bindLoadingPopupWindow(pop);
		addTask.execute();
	}
	
	
	@Override
	public void onClick(View v) {

		int id = v.getId();
		if (id == R.id.but_add) {
			addChild(v);
		}
	}

	
	

}
