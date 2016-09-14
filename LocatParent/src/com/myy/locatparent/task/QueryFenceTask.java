package com.myy.locatparent.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Fencing;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.application.LocatParentApplication;
import com.myy.locatparent.utils.FenceUtils;

/**
 * 解绑子端任务，不能调用两次(未完成)
 * @author lenovo-Myy
 *
 */
public class QueryFenceTask extends ListMapTask{
	private static final boolean DEBUG = true;
	private Entity entity;
	private List<Fencing> fence_list = null;
	private static MaxLimiter limiter;
	//单例模式
	static
	{
		limiter = new MaxLimiter();
	}
	
	public QueryFenceTask(Entity entity) {
		setTaskName("查询围栏");
		setMaxLimiter(limiter);
		this.entity = entity;
	}
	

	@Override
	protected boolean process() throws IOException {
		if(entity!=null)
		{
			fence_list = DataExchangeUtils.getFencings(entity.getId(),null);
		}
		else
		{
			return false;
		}
		List<Item> list = new ArrayList<Item>();
		Item item;
		for(Fencing fence:fence_list)
		{
			item = FenceUtils.getItemByFence(fence);
			list.add(item);
		}
		list_item = list;
		return true;
	}

	@Override
	protected boolean doInSuccess() {
		super.doInSuccess();
		if(frgm_map!=null)
		{
			frgm_map.showFences(fence_list);
		}
		if(fence_list.size()>0)
		{
			LocatParentApplication.setMainFence(fence_list.get(0));
		}
		LocatParentApplication.setList_fences(fence_list);
		return super.doInSuccess();
	}

	@Override
	protected boolean doInFail() {
		return super.doInFail();
	}
	
	

}
