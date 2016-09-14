package com.myy.locatparent.fragment;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ms.utils.bean.User;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.listen.ButtonEanbleTextWather;
import com.myy.locatparent.task.UserRegisterTask;

public class RegisterFragment extends IDFragment{

	public static final String ID = "RegisterFragment";
	private View view = null;
	private Button but_register=null;
//	private Handler handler = null;
	private MainActivity activity = null;
	private EditText et_acount,et_password,et_confiempw,et_name;
	private LoadingPopupWindow loading_pop;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//初始化Handler
		activity = (MainActivity) getActivity();
		view = inflater.inflate(R.layout.regist_frgm,container,false);
		initButton();
		return view;
	}
	
	@Override
	public String getID() {
		return this.ID;
	}
	
	private void initButton()
	{
		but_register = (Button)view.findViewById(R.id.but_register);
		but_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkRegister()==true)
				{
					String pw = et_password.getText().toString();
					String phone = et_acount.getText().toString();
					String name = et_name.getText().toString();
					User user = new User();
					user.setPhone_number(phone);
					user.setPwd(pw);
					user.setName(name);
					UserRegisterTask task = new UserRegisterTask(user);
					task.bindLoadingPopupWindow(MainActivity.showLoadingPopupWindow(v, Gravity.CENTER));
					task.execute();
				}
			}
		});
		initEditText();
	}
	
	
	private boolean checkRegister()
	{
		String acount = et_acount.getText().toString();
		String pw = et_password.getText().toString();
		String con_pw = et_confiempw.getText().toString();
		if(et_acount.length()!=11)
		{
			MainActivity.showMessage("请输入11位号码");
			return false;
		}
		if(pw.equals(con_pw)==false)
		{
			MainActivity.showMessage("两次密码输入不一致");
			return false;
		}
		return true;
	}
	
	private void initEditText()
	{
		et_acount = (EditText)view.findViewById(R.id.et_acount);
		et_password = (EditText)view.findViewById(R.id.et_password);
		et_confiempw = (EditText)view.findViewById(R.id.et_confirmpw);
		et_name = (EditText)view.findViewById(R.id.et_desc);
		List<EditText> et_list = new LinkedList<EditText>();
		et_list.add(et_acount);
		et_list.add(et_password);
		et_list.add(et_confiempw);
		et_list.add(et_name);
		
		//只有当每个个文本框都有内容是点击按钮才可用
		ButtonEanbleTextWather text_wather = new ButtonEanbleTextWather
				(et_list,but_register);
		et_acount.addTextChangedListener(text_wather);
		et_password.addTextChangedListener(text_wather);
		et_confiempw.addTextChangedListener(text_wather);
		et_name.addTextChangedListener(text_wather);
	}

	
}
