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
	
	public DownloadTask(Handler handler , Context context , String path , File saveDir)
	{
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
			e.printStackTrace();
			handler.sendMessage(handler.obtainMessage(-1)); // 发送一条空消息对象
		}
	}
}
