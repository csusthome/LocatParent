package com.myy.locatparent.task;

import java.util.ArrayList;
import java.util.List;

import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.fragment.ActionListFragment;
import com.myy.locatparent.fragment.MapFragment;

/**
 * 拓展了ActionList与Map视图处理功能的异步任务类
 * @author lenovo-Myy
 *
 */
public abstract class ListMapTask extends AbstrctTask {
	
	//用于更新地图的界面
	protected MapFragment frgm_map;
	//任务执行后更新的列表
	protected ActionListFragment list_frgm;
	//用于更新列表的数据列表
	protected List<Item> list_item = new ArrayList<Item>();

	protected ListMapTask()
	{
		setTaskName("列表地图任务");
	}
	
	public final void bindMapFragment(MapFragment frgm)
	{
		frgm_map = frgm;
	}
	
	public final void bindActionListFragment(ActionListFragment frgm)
	{
		list_frgm = frgm;
	}
	
	
	@Override
	protected final void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(list_frgm!=null)
		{
			list_frgm.stopRefresh();
		}
	}

	/**
	 * 添加了若有Item列表时更新List视图的逻辑
	 */
	@Override
	protected boolean doInSuccess()
	{
		if(list_frgm!=null)
		{
			if(list_item.size()>0)
			{
				list_frgm.updateItems(list_item);
			}
			else
			{
				MainActivity.showMessage(taskName+":无信息,请稍后重试");
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected boolean doInFail() {
		return false;
	}

	/**
	 * 重写后台的查询逻辑，请勿在该函数内更新UI,
	 * 若设置Item列表将用于更新List视图,null则不修改
	 * @return
	 */
	@Override
	protected abstract boolean process()throws Exception;
	
}
