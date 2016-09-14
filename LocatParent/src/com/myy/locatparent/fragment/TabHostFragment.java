package com.myy.locatparent.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.myy.locatparent.R;
import com.myy.locatparent.activity.MainActivity;
import com.myy.locatparent.adapter.ContentPagerAdapter;
import com.myy.locatparent.utils.LogUtils;

public class TabHostFragment extends Fragment{
	
	//���ڱ��棨����Χ����Ϣ�������е��м���Ƭ
	public static final int FENCE_IMG = R.drawable.fance,WHITE_IMG= R.drawable.user;
	public static final int	WARING_IMG = R.drawable.attention,VISIBLE_IMG=R.drawable.visible;
	private static final boolean DEBUG = false;
	private RadioGroup group;
	private RadioButton roundnamelist;
	private RadioButton whitenamelist;
	private RadioButton historywarn;
	private ViewPager pager = null;
	private ArrayList<Fragment> frgm_list = new ArrayList<Fragment>();
	private View view = null;
	//���ڴ��TabHost��ListView��������������
	private List<OnItemClickListener> listener_list = null;
	private ActionListFragment whitelist_frgm,warnlist_frgm;
	private FenceListFrgment fencelist_frgm;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.buttonfragment,container, false);
		initRadioGroup();
		initViewPager();
		LogUtils.d("TabHostFragment","onCreateView");
		return view;
	}
	
	/**
	 * ����������������������������TabHost�е�ҳ������һ�£�Ĭ�ϣ�3��
	 * @param lis_list ����Ӧ�Ŀ�������Ϊ��
	 */
	public void setOnItemClikListeners(List<OnItemClickListener> lis_list)
	{
		listener_list = lis_list;
	}
	
	private void initViewPager()
	{
		pager = (ViewPager)view.findViewById(R.id.vp_list);
    	initContentFragmets();
    	pager.setAdapter(new ContentPagerAdapter(
    			this.getChildFragmentManager(),frgm_list));
	
	}
	
	private void initContentFragmets()
    {
		if(fencelist_frgm!=null&&whitelist_frgm!=null&&warnlist_frgm!=null)
		{
			return;
		}
		if(DEBUG==true)
		{
			fencelist_frgm = new FenceListFrgment
					(FenceListFrgment.getTestFanceListData());
	    	whitelist_frgm = 
	    			new WhiteListFrgment(WhiteListFrgment.getTestWhiteListData());
	    	warnlist_frgm = 
	    			new AnomalyListFrgment(AnomalyListFrgment.getTestWarnListData());
		}
		else
		{
			fencelist_frgm = new FenceListFrgment();
	    	whitelist_frgm = new WhiteListFrgment();
	    	warnlist_frgm = new AnomalyListFrgment();
		}
		fencelist_frgm.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					fencelist_frgm.setCurFence(position);
					MainActivity.showFragment(FenceWarningFragment.ID);
			}
		});
    	//�ڸ���Ƭ������Ȼ������ʾ������£����ٴε���creatVIew����������Ҫ�ȼ��
    	if(frgm_list.size()<=0)
    	{
    		frgm_list.add(fencelist_frgm);
        	frgm_list.add(whitelist_frgm);
        	frgm_list.add(warnlist_frgm);
    	}
    }
	
	
	
	
	
	private void initRadioGroup(){
		group = (RadioGroup)view.findViewById(R.id.rg_group);
		roundnamelist=(RadioButton)view.findViewById(R.id.roundnamelist);
		whitenamelist=(RadioButton)view.findViewById(R.id.whitenamelist);
		historywarn=(RadioButton)view.findViewById(R.id.historywarn);
		group.setOnCheckedChangeListener(listener);
		//��ʼѡ�е�һ���Ӱ�ť
		if(!roundnamelist.isChecked()&&!whitenamelist.isChecked()&&!historywarn.isChecked()){
			roundnamelist.setChecked(true);
		}
	}	
		private RadioGroup.OnCheckedChangeListener listener=new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// Ϊ��ť������õ���¼�
			int pos = 0;
			if (checkedId == R.id.roundnamelist) {
				pos = 0;
			} else if (checkedId == R.id.whitenamelist) {
				pos = 1;
			} else if (checkedId == R.id.historywarn) {
				pos = 2;
			}
			pager.setCurrentItem(pos);
		}
		};
}
