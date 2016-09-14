package com.myy.locatparent.task;

import java.util.ArrayList;
import java.util.List;

import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.ImgTextListViewAdapter.Item;
import com.myy.locatparent.fragment.ActionListFragment;
import com.myy.locatparent.fragment.MapFragment;

/**
 * ��չ��ActionList��Map��ͼ�����ܵ��첽������
 * @author lenovo-Myy
 *
 */
public abstract class ListMapTask extends AbstrctTask {
	
	//���ڸ��µ�ͼ�Ľ���
	protected MapFragment frgm_map;
	//����ִ�к���µ��б�
	protected ActionListFragment list_frgm;
	//���ڸ����б�������б�
	protected List<Item> list_item = new ArrayList<Item>();

	protected ListMapTask()
	{
		setTaskName("�б��ͼ����");
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
	 * ���������Item�б�ʱ����List��ͼ���߼�
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
				MainActivity.showMessage(taskName+":����Ϣ,���Ժ�����");
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
	 * ��д��̨�Ĳ�ѯ�߼��������ڸú����ڸ���UI,
	 * ������Item�б����ڸ���List��ͼ,null���޸�
	 * @return
	 */
	@Override
	protected abstract boolean process()throws Exception;
	
}
