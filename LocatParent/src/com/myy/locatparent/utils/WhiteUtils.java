package com.myy.locatparent.utils;

import java.io.UnsupportedEncodingException;

import com.ms.utils.bean.WhiteNum;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.fragment.AnomalyListFrgment;

public class WhiteUtils {
	
	public static Item getItemByWhite(WhiteNum white)
	{
		Item item ;
		String main_desc = white.getNote();
		try {
			byte[] chars = main_desc.getBytes("utf-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String vice_desc = "手机号："+white.getPhone_number();
		item = new Item(white.getId(),AnomalyListFrgment.VISIBLE_IMG,AnomalyListFrgment.ITEM_IMG
				, main_desc, vice_desc);
		return item;
	}
	
	/**
	 * 能够得到白名单ID和备注
	 * @param item
	 * @return
	 */
	public static WhiteNum getWhiteByItem(Item item)
	{
		WhiteNum white = new WhiteNum();
		white.setId(item.getItem_id());
		white.setNote(item.getMain_desc());
		return white;
	}
}
