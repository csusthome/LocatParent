package com.myy.locatparent.utils;

import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Pupillus;
import com.ms.utils.bean.User;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.fragment.EntityListFragment;

public class EntityUtils {
	
	public static Entity getEntityByItem(Item item)
	{
		Entity entity = new Entity();
		entity.setAlias(item.getMain_desc());
		entity.setId(item.getItem_id());
		Pupillus pup = new Pupillus();
		pup.setMeid(item.getVice_desc());
		pup.setId(item.getItem_childId());
		entity.setPupillus(pup);
		return entity;
	}
	
	public static Item getItemByEntity(Entity entity)
	{
		Item item;
		String main_desc;
		String vice_desc;
		main_desc = entity.getAlias();
		vice_desc = "IMEI:"+entity.getPupillus().getMeid();
		item = new Item(entity.getId()
				,EntityListFragment.VISIBLE_IMG,
				EntityListFragment.ITEM_IMG,
				main_desc, vice_desc);
		item.setItem_childId(entity.getPupillus().getId());
		
		return item;
	}
}
