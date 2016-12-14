package com.runcom.wgcwgc.audio;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.audioBean.MyAudio;
import com.runcom.wgcwgc.audioDownload.DownloadTask;
import com.runcom.wgcwgc.play.Play;
import com.runcom.wgcwgc.util.NetUtil;
import com.runcom.wgcwgc.util.Util;

@SuppressLint("InflateParams")
public class MyListViewAdapter extends BaseAdapter
{
	public static MyAudio myAudio;
	public static ArrayList < MyAudio > audioList;

	Bundle savedInstanceState;
	LayoutInflater inflater;
	static Context context;
	static int flag;
	

	public MyListViewAdapter(Context context , LayoutInflater inflater , Bundle savedInstanceState , ArrayList < MyAudio > audioList)
	{
		MyListViewAdapter.context = context;
		this.inflater = inflater;
		this.savedInstanceState = savedInstanceState;
		MyListViewAdapter.audioList = audioList;
	}

	public MyListViewAdapter(Context context , LayoutInflater inflater , Bundle savedInstanceState , int flag)
	{
		MyListViewAdapter.context = context;
		this.inflater = inflater;
		this.savedInstanceState = savedInstanceState;
		MyListViewAdapter.flag = flag;
	}

	@Override
	public int getCount()
	{
		return audioList.size();
	}

	@Override
	public Object getItem(int position )
	{
		return audioList.get(position);
	}

	@Override
	public long getItemId(int position )
	{
		return position;
	}

	@Override
	public View getView(final int position , View convertView , ViewGroup parent )
	{
		Holder holder;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.tab1_fragment_list_item ,null);
			holder = new Holder();
			holder.name = (TextView) convertView.findViewById(R.id.tab1_fragment_list_item_name);
			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}

		holder.name.setText(audioList.get(position).getName());

		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v )
			{
				if(NetUtil.getNetworkState(context) == NetUtil.NETWORN_NONE)
				{
					Toast.makeText(context ,"������������" ,Toast.LENGTH_SHORT).show();
				}
				else
				{
					// TODO download lyrics and get source link
					Toast.makeText(inflater.getContext() ,"�������" + audioList.get(position).getName().toString() ,Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(context , Play.class);
					String source = audioList.get(position).getSource();
//					source = "http://abv.cn/music/�춹.mp3";// ǧǧ�ڸ� �춹 �������.mp3
					intent.putExtra("source" ,source);
					String lyric = audioList.get(position).getLyric();
					Log.d("LOG" , "audio: " + source + "\nlyric: " + lyric);
					lyric = "http://abv.cn/music/����_�춹.lrc";
					intent.putExtra("lyric" ,lyric);
					context.startActivity(intent);
				}
			}
		});

		ImageButton imageButton_share = (ImageButton) convertView.findViewById(R.id.tab1_fragment_list_item_share);
		final ImageButton imageButton_download = (ImageButton) convertView.findViewById(R.id.tab1_fragement_list_item_download);

		imageButton_share.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v )
			{
				if(NetUtil.getNetworkState(context) == NetUtil.NETWORN_NONE)
				{
					Toast.makeText(context ,"������������" ,Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(inflater.getContext() ,"���ڷ���" + audioList.get(position).getName().toString() + "..." ,Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/*");
					intent.putExtra(Intent.EXTRA_SUBJECT ,"Share");
					String url = ("www.baidu.com").toString();
					intent.putExtra(Intent.EXTRA_STREAM ,url);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(Intent.createChooser(intent ,"����"));

				}
			}
		});

		imageButton_download.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v )
			{
				if(NetUtil.getNetworkState(context) == NetUtil.NETWORN_NONE)
				{
					Toast.makeText(context ,"������������" ,Toast.LENGTH_SHORT).show();
				}
				else
				{

					// TODO download musics
					Toast.makeText(inflater.getContext() ,"��������" + audioList.get(position).getName().toString() + "..." ,Toast.LENGTH_SHORT).show();
					String urlString = audioList.get(position).getLink().toString();
					urlString = "http://abv.cn/music/�춹.mp3";// ǧǧ�ڸ� �춹 �������.mp3
					String fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
					try
					{
						fileName = URLEncoder.encode(fileName ,"UTF-8");
					}
					catch(UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}

					urlString = urlString.substring(0 ,urlString.lastIndexOf("/") + 1) + fileName;
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
					{
						File saveDir = new File(Util.musicsPath);

						try
						{
							download(URLDecoder.decode(fileName.substring(0 ,fileName.lastIndexOf(".")) + " " ,"UTF-8") ,urlString ,saveDir);
						}
						catch(UnsupportedEncodingException e)
						{
							e.printStackTrace();
						}
						imageButton_download.setEnabled(false);

					}
					else
					{
						Toast.makeText(context ,"����SD��" ,Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		return convertView;
	}

	class Holder
	{
		TextView id , name , data , source , link , other;
		ImageButton share , download;
	}

	public static void initData(int flag )
	{
		audioList = new ArrayList < MyAudio >();
		if(1 == flag)
		{
			for(int i = 0 ; i < 17 ; i ++ )
			{
				// TODO
				// new InitData(flag).initData();
				myAudio = new MyAudio();
				myAudio.setId("ID" + i);
				myAudio.setLyric("Data" + i);
				myAudio.setLink("Link" + i);
				myAudio.setName("�춹" + i);
				myAudio.setSource("source" + i);
				audioList.add(myAudio);
			}
		}
		else
			if(2 == flag)
			{
				for(int i = 17 ; i < 34 ; i ++ )
				{
					// TODO
					// new InitData(flag).initData();
					myAudio = new MyAudio();
					myAudio.setId("ID" + i);
					myAudio.setLyric("Data" + i);
					myAudio.setLink("Link" + i);
					myAudio.setName("�춹" + i);
					myAudio.setSource("source" + i);
					audioList.add(myAudio);
				}

			}
			else
				if(3 == flag)
				{
					for(int i = 34 ; i < 51 ; i ++ )
					{
						// TODO
						// new InitData(flag).initData();
						myAudio = new MyAudio();
						myAudio.setId("ID" + i);
						myAudio.setLyric("Data" + i);
						myAudio.setLink("Link" + i);
						myAudio.setName("�춹" + i);
						myAudio.setSource("source" + i);
						audioList.add(myAudio);
					}

				}
	}

	public void download(String filename , String path , File savDir )
	{
		DownloadTask task = new DownloadTask(filename , handler , context , path , savDir);
		new Thread(task).start();
	}

	public Handler handler = new UIHandler();

	static class UIHandler extends Handler
	{
		public void handleMessage(Message msg )
		{
			switch(msg.what)
			{
				case 1: // ���½���
					Toast.makeText(context ,msg.getData().get("filename") + "���سɹ�" ,Toast.LENGTH_LONG).show();
					break;
				case -1: // ����ʧ��
					Toast.makeText(context ,"�����쳣" ,Toast.LENGTH_LONG).show();
					break;
			}
		}
	}

}
