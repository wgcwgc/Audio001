package com.runcom.wgcwgc.ListViewInitData;

import android.util.Log;

public class InitData
{

	private int flag;

	public InitData()
	{

	}

	public InitData(int flag)
	{
		this.flag = flag;
	}

	public void initData()
	{
		if(1 == flag)
		{
			System.out.println(flag);
			Log.d("LOG" ,flag + "");
		}
		else
			if(2 == flag)
			{
				System.out.println(flag);
				Log.d("LOG" ,flag + "");
			}
			else
				if(3 == flag)
				{
					Log.d("LOG" ,flag + "");
					System.out.println(flag);
				}
				else
				{
					System.out.println(flag);
					Log.d("LOG" ,flag + "");
				}
	}

}
