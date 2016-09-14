package com.myy.locatparent.beans;

import com.baidu.mapapi.model.LatLng;

public class CircleFence extends Fance {
	
	public LatLng center;
	public int radius;
	
	public CircleFence (String clientID,LatLng center,int radius,int color)
	{
		this.clientId = clientID;
		this.center = center;
		this.radius = radius;
		type = FENCETYPE.CRICLE;
		this.color = color;
	}
	
	public CircleFence (String clientID,LatLng center,int radius)
	{
		this.clientId = clientID;
		this.center = center;
		this.radius = radius;
		type = FENCETYPE.CRICLE;
	}
}
