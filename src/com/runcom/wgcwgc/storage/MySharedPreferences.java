package com.runcom.wgcwgc.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences操作工具类
 * 
 */
public class MySharedPreferences
{

	public static void putValue(Context context , String sharedPreferencesKey , String key , int value )
	{
		Editor editor = context.getSharedPreferences(sharedPreferencesKey ,Context.MODE_PRIVATE).edit();
		editor.putInt(key ,value);
		editor.commit();
	}

	public static void putValue(Context context , String sharedPreferencesKey , String key , boolean value )
	{
		Editor editor = context.getSharedPreferences(sharedPreferencesKey ,Context.MODE_PRIVATE).edit();
		editor.putBoolean(key ,value);
		editor.commit();
	}

	public static void putValue(Context context , String sharedPreferencesKey , String key , String value )
	{
		Editor editor = context.getSharedPreferences(sharedPreferencesKey ,Context.MODE_PRIVATE).edit();
		editor.putString(key ,value);
		editor.commit();
	}

	public static int getValue(Context context , String sharedPreferencesKey , String key , int defValue )
	{
		SharedPreferences sp = context.getSharedPreferences(sharedPreferencesKey ,Context.MODE_PRIVATE);
		int value = sp.getInt(key ,defValue);
		return value;
	}

	public static boolean getValue(Context context , String sharedPreferencesKey , String key , boolean defValue )
	{
		SharedPreferences sp = context.getSharedPreferences(sharedPreferencesKey ,Context.MODE_PRIVATE);
		boolean value = sp.getBoolean(key ,defValue);
		return value;
	}

	public static String getValue(Context context , String sharedPreferencesKey , String key , String defValue )
	{
		SharedPreferences sp = context.getSharedPreferences(sharedPreferencesKey ,Context.MODE_PRIVATE);
		String value = sp.getString(key ,defValue);
		return value;
	}
}
