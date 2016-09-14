package com.myy.locatparent.listen;

import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * ���ڼ��༭�ı����Ƿ������ݣ��Դ����жϰ�ť�Ƿ����
 * @author lenovo-Myy
 *
 */
public class ButtonEanbleTextWather implements TextWatcher {

	
	//����Ϊ���õİ�ť
		private Button but_confirm;
		private List<EditText> et_list;
		public ButtonEanbleTextWather(List<EditText> et_list , Button but)
		{
			this.et_list = et_list;
			this.but_confirm = but;
		}

		/**
		 * �ж��Ƿ������ı����ڶ�������
		 * @return �������ı����ڶ������ݷ���true
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
