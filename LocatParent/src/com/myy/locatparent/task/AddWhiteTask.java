package com.myy.locatparent.task;

import java.io.IOException;

import android.R.string;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.WhiteNum;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.task.AbstrctTask.MaxLimiter;
import com.myy.locatparent.utils.WhiteUtils;

/**
 * 绑定子端网络任务，不能调用两次
 * 
 * @author lenovo-Myy
 * 
 */
public class AddWhiteTask extends ListMapTask {

	private WhiteNum white;
	private static MaxLimiter limiter;
	private String str;
	//单例模式
	static
	{
		limiter = new MaxLimiter();
	}
	
	public AddWhiteTask(WhiteNum white) {
		setMaxLimiter(limiter);
		setTaskName("添加白名单");
		this.white = white;
	}

	@Override
	protected boolean process() {
		try {
			// 添加白名单
			str = DataExchangeUtils.addWhiteNum(white);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (str.trim().equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean doInSuccess() {
		
		if(list_frgm!=null)
		{
			Item item = WhiteUtils.getItemByWhite(white);
			list_frgm.addItem(item);
		}
		return super.doInSuccess();
	}

	@Override
	protected boolean doInFail() {
		if(str.trim().equals("eixsted"))
		{
			MainActivity.showMessage("白名单已经存在");
		}
		return super.doInFail();
	}
	
	
	

}
