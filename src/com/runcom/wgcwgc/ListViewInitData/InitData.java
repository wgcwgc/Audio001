package com.runcom.wgcwgc.ListViewInitData;

import java.util.ArrayList;

import android.util.Log;

import com.runcom.wgcwgc.audioBean.MyAudio;

public class InitData
{

	private int flag= -1;
	private MyAudio myAudio = null;
	private ArrayList < MyAudio > audioList = new ArrayList < MyAudio >();
	
	public InitData()
	{

	}

	public InitData(int flag)
	{
		this.flag = flag;
	}
	
	public InitData(int flag , ArrayList < MyAudio > audioList)
	{
		this.flag = flag;
		this.audioList = audioList;
	}
	public InitData(int flag , MyAudio myAudio , ArrayList < MyAudio > audioList)
	{
		this.flag = flag;
		this.myAudio = myAudio;
		this.audioList = audioList;
	}
	
	public ArrayList < MyAudio > initData()
	{
		if(1 == flag)
		{
			for(int i = 0 ; i < 17 ; i ++ )
            {
				myAudio.setId("ID" + i);
				myAudio.setLyric("Data" + i);
				myAudio.setLink("Link" + i);
				myAudio.setName("ºì¶¹" + i);
				myAudio.setSource("source" + i);
	            audioList.add(myAudio);
            }
			Log.d("LOG" , "flag: " + flag);
		}
		else
			if(2 == flag)
			{
//				System.out.println(flag);
				for(int i = 17 ; i < 34 ; i ++ )
				{
					myAudio.setId("ID" + i);
					myAudio.setLyric("Data" + i);
					myAudio.setLink("Link" + i);
					myAudio.setName("ºì¶¹" + i);
					myAudio.setSource("source" + i);
					audioList.add(myAudio);
				}
				Log.d("LOG" ,"flag: " + flag);
			}
			else
				if(3 == flag)
				{
					for(int i = 34 ; i < 51 ; i ++ )
					{
						myAudio.setId("ID" + i);
						myAudio.setLyric("Data" + i);
						myAudio.setLink("Link" + i);
						myAudio.setName("ºì¶¹" + i);
						myAudio.setSource("source" + i);
						audioList.add(myAudio);
					}
					Log.d("LOG" ,"flag: " + flag);
//					System.out.println(flag);
				}
				else
				{
//					System.out.println(flag);
					Log.d("LOG" ,"flag: " + flag);
				}
		return audioList;
	}

}
