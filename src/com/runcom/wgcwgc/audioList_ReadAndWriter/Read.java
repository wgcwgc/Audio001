package com.runcom.wgcwgc.audioList_ReadAndWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.runcom.wgcwgc.util.Util;

public class Read
{

	private String filePath;
	private List < String > conttentsList;
	private String defaultSavePath = Util.cachePath + Util.localAudioListCacheName;

	public Read()
	{
		this.filePath = defaultSavePath;
	}

	public Read(String filePath)
	{
		this.filePath = filePath;
	}

	public ArrayList < String > reader()
	{
		File filepath = new File(filePath);
		if( !filepath.exists())
		{
			return null;
		}
		else
		{
			BufferedReader bufferedReader = null;
			try
			{
				bufferedReader = new BufferedReader(new FileReader(filepath));
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}

			String temp = null;
			try
			{
				conttentsList = new ArrayList < String >();
				while((temp = bufferedReader.readLine()) != null)
				{
					if(new File(temp).exists())
					{
						conttentsList.add(temp);
					}
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
					bufferedReader.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}

		}

		return (ArrayList < String >) conttentsList;

	}

}
