package com.myy.locatparent.utils;

import android.util.Log;

public class LogUtils {

	private static final int VERBOSE = 0;
	private static final int DEBUG = 1;
	private static final int INFO = 2;
	private static final int WARNING = 3;
	private static final int ERROR = 4;
	private static final int NOTHING = 5;
	
	private static final int LEVEL = DEBUG;
	
	
	public static void v(String tag,String msg)
	{
		if(LEVEL<=VERBOSE)
		{
			Log.v(tag, msg);
		}
	}
	
	public static void d(String tag,String msg)
	{
		if(LEVEL<=DEBUG)
		{
			Log.d(tag, msg);
		}
	}
	
	public static void i(String tag,String msg)
	{
		if(LEVEL<=INFO)
		{
			Log.i(tag, msg);
		}
	}
	
	public static void w(String tag,String msg)
	{
		if(LEVEL<=WARNING)
		{
			Log.w(tag, msg);
		}
	}
	
	public static void e(String tag,String msg)
	{
		if(LEVEL<=ERROR)
		{
			Log.e(tag, msg);
		}
	}
}
