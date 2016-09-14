package com.myy.locatparent.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ms.utils.bean.AnomalyRecord;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.fragment.AnomalyListFrgment;

public class AnomalyUtils {
	
	private static final String FOMAT = "yyyy-MM-dd HH-mm-ss";
	private static SimpleDateFormat sdf = new SimpleDateFormat(FOMAT);
	//	public static User getUserByItem(Item item)
//	{
//		User user = new User();
//		user.setId(item.getItem_id());
//		user.setPhone_number(item.getMain_desc());
//		return user;
//	}
	
	public static Item getItemByAnomaly(AnomalyRecord record)
	{
		Item item ;
		String main_desc = record.getMessage();
		Date date = record.getDate();
		String vice_desc = sdf.format(date);
		item = new Item(record.getId(),AnomalyListFrgment.VISIBLE_IMG,AnomalyListFrgment.ITEM_IMG
				, main_desc, vice_desc);
		return item;
	}
}
