package com.runcom.wgcwgc.audioDownload;

import java.io.File;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public final class DownloadTask implements Runnable
{
	private String path;
	private File saveDir;
	private FileDownloader loader;
	private Context context;
	private Handler handler;
	private String filename;

	public DownloadTask(String filename , Handler handler , Context context , String path , File saveDir)
	{
		this.filename = filename;
		this.handler = handler;
		this.path = path;
		this.saveDir = saveDir;
		this.context = context;
	}

	/**
	 * 退出下载
	 */
	public void exit()
	{
		if(loader != null)
			loader.exit();
	}

	DownloadProgressListener downloadProgressListener = new DownloadProgressListener()
	{
		@Override
		public void onDownloadSize(int size )
		{
			Message msg = new Message();
			if(size == loader.getFileSize())
			{
				msg.what = 1;
				msg.getData().putString("filename" ,filename);
				handler.sendMessage(msg);
			}
		}
	};

	public void run()
	{
		try
		{
			loader = new FileDownloader(context , path , saveDir , 3);
			loader.download(downloadProgressListener);
		}
		catch(Exception e)
		{
			handler.sendMessage(handler.obtainMessage( -1)); // 发送一条空消息对象
		}
	}
}
