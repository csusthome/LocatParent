package com.myy.locatparent.task;

import java.io.IOException;

import com.ms.utils.DataExchangeUtils;
import com.ms.utils.bean.Entity;
import com.ms.utils.bean.Fencing;
import com.myy.locatparent.fragment.EntityListFragment;
import com.myy.locatparent.fragment.FenceListFrgment;
import com.myy.locatparent.task.AbstrctTask.MaxLimiter;

/**
 * ����Ӷ����񣬲��ܵ�������(δ���)
 * @author lenovo-Myy
 *
 */
public class AddFanceTask extends ListMapTask {

	
	private Fencing fence;
	private static MaxLimiter limiter;
	//����ģʽ
	static
	{
		limiter = new MaxLimiter();
	}
	
	public AddFanceTask(Fencing fence) {
		setMaxLimiter(limiter);
		setTaskName("���Χ��");
		this.fence = fence;
	}

	@Override
	protected boolean process() {
		try {
			String str_result=null;
			if(fence!=null)
			{
			str_result = DataExchangeUtils.addFencings(fence);
			if(str_result==null||str_result.trim().equals("false"))
			{
				return false;
			}
			else
			{
				return true;
			}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}

	@Override
	protected boolean doInSuccess() {
		if(frgm_map!=null)
		{
			//�����Χ��
			frgm_map.showNewFence(null);
		}
		//����Χ������
		FenceListFrgment.updateFenceData(list_frgm, frgm_map);
		return super.doInSuccess();
	}
	

	@Override
	protected boolean doInFail() {
		if(frgm_map!=null)
		{
			//�����Χ��
			frgm_map.showNewFence(null);
		}
		return super.doInFail();
	}

}
