package com.myy.locatparent.fragment;

import android.support.v4.app.Fragment;

import com.myy.locatparent.minterface.IDExisted;

/**
 * �̳���getID�ӿڵ��Զ�����Ƭ�࣬��Ϊ�ó�������Ƭ�Ļ���
 * @author lenovo-Myy
 *
 */
public class IDFragment extends Fragment implements IDExisted {

	public static String ID = "com.myy.locatparent.fragment.IDFragment";
	@Override
	/**
	 * �õ���Ƭ���ΨһID��������Ƭ�л�ʱ��Tag
	 */
	public String getID() {
		return IDFragment.ID;
	}

}
