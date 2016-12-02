package com.runcom.wgcwgc.audioList_ReadAndWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.runcom.wgcwgc.util.Util;

public class Print
{

	private String filePath , contentsString;
	private boolean flag;
	private String defaultSavePath = Util.cachePath + Util.localAudioListCacheName;

	private List < String > contentsList;

	public Print()
	{

	}

	public Print(String filePath , String contents , boolean flag)
	{
		this.filePath = filePath;
		this.contentsString = contents;
		this.flag = flag;
	}

	public Print(ArrayList < String > contents)
	{
		this.filePath = defaultSavePath;
		this.contentsList = contents;
	}

	public void printerList()
	{
		File filepath = new File(filePath);
		if( !filepath.exists() || !filepath.getParentFile().exists())
		{
			filepath.getParentFile().mkdirs();
			try
			{
				filepath.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		BufferedWriter bufferedWriter = null;
		try
		{
			bufferedWriter = new BufferedWriter(new FileWriter(filepath , false));
			for(int i = 0 ; i < contentsList.size() ; i ++ )
			{
				bufferedWriter.write(contentsList.get(i) + "\n\r");
			}
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bufferedWriter.flush();
				bufferedWriter.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	public void printerString()
	{
		File filepath = new File(filePath);
		if( !filepath.exists() || !filepath.getParentFile().exists())
		{
			filepath.getParentFile().mkdirs();
			try
			{
				filepath.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		BufferedWriter bufferedWriter = null;
		try
		{
			if(flag)
			{
				bufferedWriter = new BufferedWriter(new FileWriter(filepath , true));
			}
			else
			{
				bufferedWriter = new BufferedWriter(new FileWriter(filepath , false));
			}
			bufferedWriter.write(contentsString + "\n\r");
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bufferedWriter.flush();
				bufferedWriter.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

	}

}
