package com.myy.locatparent.utils;

import java.util.List;

import com.ms.utils.bean.Point;

/**
 * 轨迹过滤器,用于给轨迹过滤
 * @author lenovo-Myy
 */
public interface ITraceFilter {
	public List<Point> dofilter(List<Point> trace);
}
