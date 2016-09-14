package com.myy.locatparent.fragment;

import android.support.v4.app.Fragment;

import com.myy.locatparent.minterface.IDExisted;

/**
 * 继承了getID接口的自定义碎片类，作为该程序中碎片的基类
 * @author lenovo-Myy
 *
 */
public class IDFragment extends Fragment implements IDExisted {

	public static String ID = "com.myy.locatparent.fragment.IDFragment";
	@Override
	/**
	 * 得到碎片类的唯一ID，用于碎片切换时的Tag
	 */
	public String getID() {
		return IDFragment.ID;
	}

}
