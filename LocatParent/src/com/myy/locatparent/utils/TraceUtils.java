package com.myy.locatparent.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.ms.utils.Distance;
import com.ms.utils.bean.Fencing;
import com.ms.utils.bean.Point;

public class TraceUtils {

	public static List<Point> correctTrace(List<Point> point_list) {
		LogUtils.i("TraceUtils", "正在进行轨迹纠偏");
		List<Point> correct_list = new ArrayList<Point>();
		// 距离检测优化，从第二个到，结束前一个
		for (int i = 2; i < point_list.size() - 2; i++) {
			distanceCorrect1(point_list, correct_list, i);
		}
		return correct_list;
	}

	private static void distanceCorrect1(List<Point> src_list, List<Point> des_list,
			int i) {
		Point p1, p2, p3;
		// 只有两个点时无法进行优化
		if (src_list.size() <= 3) {
			return;
		}
		Double distance1, distance2;
		p1 = src_list.get(i - 2);
		p2 = src_list.get(i - 1);
		p3 = src_list.get(i);
		distance1 = Distance.getDistance(p1, p2);
		distance2 = Distance.getDistance(p2, p3);
		// d1的长度是d2的10倍
//		if (distance2 / distance1 > 10.0d) {
//			LogUtils.i("TraceUtils", "异常点ID:" + p3.getId());
//			des_list.remove(p3);
//		}
		if(distance1>100||distance2>100)
		{
//			LogUtils.i("TraceUtils", "异常点ID:" + p1.getId()+" "+p2.getId()+" "+p3.getId()
//					+"d1:"+distance1+" d2:"+distance2);
			des_list.add(p1);
			des_list.add(p2);
			des_list.add(p3);
		}
	}
	
	private static void distanceCorrect(List<Point> src_list, List<Point> des_list,
			int i) {
		Point p1, p2, p3, p4, p5;
		// 只有两个点时无法进行优化
		if (src_list.size() <= 5) {
			return;
		}
		Double distance1, distance2, distance3, distance4;
		p1 = src_list.get(i - 2);
		p2 = src_list.get(i - 1);
		p3 = src_list.get(i);
		p4 = src_list.get(i + 1);
		p5 = src_list.get(i + 2);
		distance1 = Distance.getDistance(p1, p2);
		distance2 = Distance.getDistance(p2, p3);
		distance3 = Distance.getDistance(p3, p4);
		distance4 = Distance.getDistance(p4, p5);
		// d1的长度是d2的10倍
		if (distance2 / distance1 > 10.0d && distance4 / distance3 > 10.0d) {
			LogUtils.i("TraceUtils", "异常点ID:" + p3.getId());
			des_list.remove(p3);
		}

	}

}
