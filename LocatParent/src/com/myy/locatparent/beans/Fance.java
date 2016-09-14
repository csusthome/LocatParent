package com.myy.locatparent.beans;

import android.graphics.Color;

public abstract class Fance {
	public static enum FENCETYPE
	{ CRICLE}
	
	public static enum FENCECOLOR
	{ 
		NEW,OLD,MAIN;
		private static int [] colors = {Color.rgb(0xff, 0xe6, 0x00),Color.rgb(0x00, 0x99, 0xcc),
			Color.rgb(0x00, 0x33, 0x66)};
		public int vlaue()
		{
			return colors[this.ordinal()];
		}
	}
	
	public String clientId="";
	public FENCETYPE type = FENCETYPE.CRICLE;
	//Ä¬ÈÏÎªÀ¶É«
	public int color = FENCECOLOR.OLD.vlaue();
}
