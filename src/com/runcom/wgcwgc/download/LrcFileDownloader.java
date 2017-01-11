package com.runcom.wgcwgc.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.runcom.wgcwgc.util.Util;

public class LrcFileDownloader extends Thread
{
	private String lyric;

	public LrcFileDownloader(String lyric)
	{
		this.lyric = lyric;
	}

	@Override
	public void run()
	{

		FileOutputStream fos = null;
		HttpURLConnection conn = null;
		try
		{
			// �ĵ�ַ�������� ���������κ���Դ
			URL url = new URL(lyric);
			// �˴���������url����Ҫ���ص���ҳ

			// 2 �������Ӷ���
			conn = (HttpURLConnection) url.openConnection();// һ������������
			conn.setReadTimeout(3000);// ���ÿͻ������ӳ�ʱ������������ָ��ʱ�� ����˻�û����Ӧ �Ͳ�Ҫ����

			// �жϷ���������ķ����Ƿ��Ѿ������� �ͻ���
			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
			{

				// ��������ֽ�����������
				InputStream is = conn.getInputStream();// ���ǲ����ļ�����

				// �����ڴ浽Ӳ�̵�����
				fos = new FileOutputStream(new File(Util.lyricsPath + lyric.substring(lyric.lastIndexOf("/")) + 1));

				// ������ д�ļ�
				byte [] b = new byte [1024];
				int len = 0;
				while((len = is.read(b)) != -1)
				{ // �ȶ����ڴ�
					fos.write(b ,0 ,len);
				}
				fos.flush();
				// System.out.println("���سɹ�");
				Log.d("" ,lyric + "���سɹ�");
			}

		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(fos != null)
			{
				try
				{
					fos.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

}
