package com.myy.locatparent.fragment;

import android.app.Instrumentation;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myy.locatparent.R;
import com.myy.locatparent.utils.LogUtils;

public class TitleFragment extends Fragment {

	private TextView  tv_title = null;
	private ImageButton ibut_back = null;
	private View view = null;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.title_frgm,container,false);
		initTitleTextView();
		initBackButton();
		return view;
	}
	
	private void initTitleTextView()
	{
		tv_title = (TextView)view.findViewById(R.id.tv_title);
	}
	
	private void initBackButton()
	{
		ibut_back = (ImageButton)view.findViewById(R.id.ibut_back);
		ibut_back.setOnClickListener(new OnClickListener() {
			//Ä£Äâ»ØÍË¼ü
			@Override
			public void onClick(View v) {
				onBack();
			}
		});
	}
	
	public void setTitle(String title)
	{
		tv_title.setText(title);
	}
	
	public void setBackVisible(boolean visible)
	{
		if(visible==true)
		{
			ibut_back.setVisibility(View.VISIBLE);
		}
		else
		{
			ibut_back.setVisibility(View.INVISIBLE);
		}
	}
	
    public void onBack(){  
        new Thread(){  
         public void run() {  
          try{  
           Instrumentation inst = new Instrumentation();  
           inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);  
          }  
          catch (Exception e) {  
                       LogUtils.e("Exception when onBack", e.toString());  
                   }  
         }  
        }.start();  
       }  
	
}
