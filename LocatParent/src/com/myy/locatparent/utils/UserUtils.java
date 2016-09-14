package com.myy.locatparent.utils;

import com.ms.utils.bean.User;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;

public class UserUtils {
	
	public static User getUserByItem(Item item)
	{
		User user = new User();
		user.setId(item.getItem_id());
		user.setPhone_number(item.getMain_desc());
		return user;
	}
	
//	public static Item getItemByUser(User user)
//	{
//		
//	}
}
