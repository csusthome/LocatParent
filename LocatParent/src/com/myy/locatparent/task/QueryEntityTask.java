package com.myy.locatparent.task;

import java.util.ArrayList;
import java.util.List;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Entity;
import com.ms.utils.bean.User;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.task.AbstrctTask.MaxLimiter;
import com.myy.locatparent.utils.EntityUtils;

/**
 * 解绑子端任务，不能调用两次(未完成)
 * 使用了特殊的单例模式
 * @author lenovo-Myy
 *
 */
public class QueryEntityTask extends ListMapTask{

	private User user;
	private List<Entity> entity_list = null;
	private static MaxLimiter limiter;
	//单例模式
	static
	{
		limiter = new MaxLimiter();
	}
	
	public QueryEntityTask(User user)
	{
		setMaxLimiter(limiter);
		setTaskName("查询实体");
		this.user = user;
	}
	
	@Override
	protected boolean process() {
		try {
			if(user==null||user.getId()==null)
			{
				MainActivity.showMessage(taskName+":User异常");
				throw new IllegalArgumentException("Task argument is illegal->user");
			}
			entity_list = DataExchangeUtils.getEntitiesByUser(user.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(entity_list==null)
		{
			return false;
		}
		return true;
	}

	@Override
	protected boolean doInSuccess() {
		List<Item> list = new ArrayList<Item>();
		Item item;
		if(entity_list.size()==0)
		{
			MainActivity.showMessage("无实体信息");
			return true;
		}
		for(Entity entity:entity_list)
		{
			item = EntityUtils.getItemByEntity(entity);
			list.add(item);
		}
		list_item = list;
		//设置当前实体ID,先设置当前实体才能进行更新
		LocatParentApplication.setMainEntity(entity_list.get(0));
		LocatParentApplication.setMainPupillus(
				LocatParentApplication.getMainEntity().getPupillus());
		LocatParentApplication.setList_entities(entity_list);
		return super.doInSuccess();
	}
}
