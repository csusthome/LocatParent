package com.myy.locatparent.utils;

import java.util.List;

import com.ms.utils.bean.Point;

/**
 * �켣������,���ڸ��켣����
 * @author lenovo-Myy
 */
public interface ITraceFilter {
	public List<Point> dofilter(List<Point> trace);
}
