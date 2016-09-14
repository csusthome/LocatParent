package com.myy.locatparent.fragment;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ms.utils.bean.User;
import com.myy.loactparent.popupwinow.LoadingPopupWindow;
import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.fragment.MainFragment.MSGTYPE;
import com.myy.locatparent.listen.ButtonEanbleTextWather;
import com.myy.locatparent.task.LoginTask;

public class LoginFragment extends IDFragment implements OnClickListener{

	private static final boolean DEBUG = false;
	public static final String ID = "LoginFragment";
	private View view = null;
	private Button but_login=null;
	private TextView tv_register=null;
	private Handler handler = null;
	private EditText et_acount,et_password;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//初始化Handler
		handler = MainActivity.getHandler();
		view = inflater.inflate(R.layout.login_layout,container,false);
		initButton();
		initEditText();
		return view;
	}
	
	@Override
	public String getID() {
		return this.ID;
	}
	
	private void initButton()
	{
		but_login = (Button)view.findViewById(R.id.but_login);
		tv_register = (TextView)view.findViewById(R.id.tv_register);
		but_login.setOnClickListener(this);
		tv_register.setOnClickListener(this);
	}
	
	private void initEditText()
	{
		et_acount = (EditText)view.findViewById(R.id.et_acount);
		et_password = (EditText)view.findViewById(R.id.et_password);
		List<EditText> et_list = new LinkedList<EditText>();
		et_list.add(et_acount);
		et_list.add(et_password);
		//只有当2个文本框都有内容是点击按钮才可用
		ButtonEanbleTextWather text_wather = new ButtonEanbleTextWather
				(et_list,but_login);
		et_acount.addTextChangedListener(text_wather);
		et_password.addTextChangedListener(text_wather);
	}
	
	private void login()
	{
		if(LoginFragment.DEBUG==true)
		{
			MainActivity.showFragment(MapFragment.ID);
			return ;
		}
		User user = new User();
		String acount = et_acount.getText().toString();
		String password = et_password.getText().toString();
		user.setPhone_number(acount);
		user.setPwd(password);
		LoginTask task = new LoginTask(user);
		LoadingPopupWindow pop = MainActivity.showLoadingPopupWindow(but_login,
				Gravity.CENTER);
		task.bindLoadingPopupWindow(pop);
		task.execute();
		//清空密码框
		et_password.setText("");
	}
	
	private void register()
	{
		MainActivity.showFragment(RegisterFragment.ID);
	}
	 
	@Override
	public void onClick(View v) {
		try{
			int id = v.getId();
			if (id == R.id.but_login) {
				login();
			} else if (id == R.id.tv_register) {
				register();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
}
