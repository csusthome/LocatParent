package com.myy.locatparent.adapter;


import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * 自定义的VIewPager适配器
 * @author lenovo-Myy
 *
 */
public class ContentPagerAdapter extends FragmentPagerAdapter  {

	private ArrayList<Fragment> frgm_list = null;
	public ContentPagerAdapter(FragmentManager fm,ArrayList<Fragment> frgm_list) {
		super(fm);
		this.frgm_list = frgm_list;
	}

	@Override
	public Fragment getItem(int loc) {
		return frgm_list.get(loc);
	}

	@Override
	public int getCount() {
		if(frgm_list==null)
		{
			return 0;
		}
		return frgm_list.size();
	}

}
