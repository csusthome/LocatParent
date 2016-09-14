package com.myy.locatparent.utils;

import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.ms.utils.bean.Fencing;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.beans.CircleFence;
import com.myy.locatparent.beans.Fance.FENCECOLOR;
import com.myy.locatparent.fragment.TabHostFragment;

public class FenceUtils {
	
	public static Item getItemByFence(Fencing fence)
	{
		Item item;
		String main_desc;
		String vice_desc;
		main_desc = fence.getName();
		vice_desc = "经度："+fence.getLongitude()+" 纬度:"+fence.getLatitude()
				+"\n半径："+fence.getRadius();
		item = new Item(fence.getId()
				,TabHostFragment.VISIBLE_IMG,
				TabHostFragment.FENCE_IMG,
				main_desc, vice_desc);
		
		return item;
	}
	
	/**
	 * 由Item得到围栏实体，只能得到围栏ID与名字
	 * @param item
	 * @return
	 */
	public static Fencing getFencingByItem(Item item)
	{
		Fencing fence = new Fencing();
		fence.setId(item.getItem_id());
		fence.setName(item.getMain_desc());
		return fence;
	}
	
	public static CircleFence getCricleFenceByFencing(Fencing fence)
	{
		CircleFence fence_cricle = null;
		LatLng latlng = new LatLng(fence.getLatitude(),fence.getLongitude());
		fence_cricle = new CircleFence(fence.getId().toString()
				,latlng,(int)fence.getRadius());
		return fence_cricle;
	}
	
	public static CircleOptions getCircleOptionByFencing(Fencing fence,FENCECOLOR color)
	{
		if(fence==null)
		{
			return null;
		}
		LatLng latlng = new LatLng(fence.getLatitude(),fence.getLongitude());
		CircleOptions option = new CircleOptions().fillColor(0x000000FF)
				.center(latlng)
                .stroke(new Stroke(5,color.vlaue()))
                .radius((int)fence.getRadius());
		return option;
	}
}
