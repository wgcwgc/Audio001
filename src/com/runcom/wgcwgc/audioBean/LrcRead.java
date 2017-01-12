package com.runcom.wgcwgc.audioBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LrcRead
{
	private List < LyricContent > LyricList;
	private LyricContent mLyricContent;

	public LrcRead()
	{
		mLyricContent = new LyricContent();
		LyricList = new ArrayList < LyricContent >();
	}

	public void Read(String file , int flag ) throws FileNotFoundException , IOException
	{
		String Lrc_data = "";
		File mFile = new File(file);
		FileInputStream mFileInputStream = new FileInputStream(mFile);
		InputStreamReader mInputStreamReader = new InputStreamReader(mFileInputStream , "GB2312");
		// 一行一行读取
		BufferedReader mBufferedReader = new BufferedReader(mInputStreamReader);
		while((Lrc_data = mBufferedReader.readLine()) != null)
		{
			Lrc_data = Lrc_data.replace("[" ,"");
			Lrc_data = Lrc_data.replace("]" ,"@");
			String splitLrc_data[] = Lrc_data.split("@");
			if(splitLrc_data.length > 1)
			{
				String reg = "[\u4e00-\u9fa5]";
				int code_index = -1;
				if(splitLrc_data[1].matches(".*" + reg + ".*"))
				{
					code_index = splitLrc_data[1].split(reg)[0].length();
				}

				if(0 == flag)
				{
					if(code_index != -1)
						splitLrc_data[1] = splitLrc_data[1].substring(0 ,code_index);
//					else
//						splitLrc_data[1] = splitLrc_data[1].substring(0);
				}
				else
					if(1 == flag)
					{
						splitLrc_data[1] = splitLrc_data[1].substring(code_index);
					}
					else
						if(2 == flag)
						{
							splitLrc_data[1] = splitLrc_data[1].substring(0 ,code_index) + "\n" + splitLrc_data[1].substring(code_index);
						}
				mLyricContent.setLyric(splitLrc_data[1]);
				int LyricTime = TimeStr(splitLrc_data[0]);
				mLyricContent.setLyricTime(LyricTime);
				LyricList.add(mLyricContent);
				mLyricContent = new LyricContent();
			}

		}

		mBufferedReader.close();
		mInputStreamReader.close();
	}

	public int TimeStr(String timeStr )
	{

		timeStr = timeStr.replace(":" ,".");
		timeStr = timeStr.replace("." ,"@");
		String timeData[] = timeStr.split("@");

		int minute = Integer.parseInt(timeData[0]);
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		return currentTime;
	}

	public List < LyricContent > GetLyricContent()
	{
		return LyricList;
	}
}
