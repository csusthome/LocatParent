package com.myy.locatparent.listen;

import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 用于检测编辑文本框是否有内容，以此来判断按钮是否可用
 * @author lenovo-Myy
 *
 */
public class ButtonEanbleTextWather implements TextWatcher {

	
	//设置为可用的按钮
		private Button but_confirm;
		private List<EditText> et_list;
		public ButtonEanbleTextWather(List<EditText> et_list , Button but)
		{
			this.et_list = et_list;
			this.but_confirm = but;
		}

		/**
		 * 判断是否所有文本框内都有内容
		 * @return 若所有文本框内都有内容返回true
		 */
		private boolean checkList()
		{
			for(EditText et:et_list)
			{
				String text = et.getText().toString(); 
				if(text==null||text.trim().isEmpty())
				{
					return false;
				}
			}
			return true;
		}
		
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		String text = s.toString();
		if(text==null||text.isEmpty())
		{
			but_confirm.setEnabled(false);
		}
		else
		{
			if(checkList()==true)
			{
				but_confirm.setEnabled(true);
			}
			else
			{
				but_confirm.setEnabled(false);
			}
		}
		
	}

}
