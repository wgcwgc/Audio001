package com.runcom.wgcwgc.audio;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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

@SuppressLint("InflateParams") public class MyListViewAdapter extends BaseAdapter
{
	public static MyAudio myAudio;
	public static List < MyAudio > audioList;

	Bundle savedInstanceState;
	LayoutInflater inflater;
	static Context context;

	public MyListViewAdapter()
	{
	}

	@SuppressWarnings("static-access")
	public MyListViewAdapter(Context context , LayoutInflater inflater , Bundle savedInstanceState)
	{
		this.context = context;
		this.inflater = inflater;
		this.savedInstanceState = savedInstanceState;
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
				// TODO
				Toast.makeText(inflater.getContext() ,"您点击了" + audioList.get(position).getName().toString() ,Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context , Play.class);
				String contents = "I am lyric ! \n @autor : @wgcwgc \n @time : @2016.11.25.10:41:57:256 \n contents... \n contents ... \n ... \n . \n end \n";
				intent.putExtra("contents" ,contents);
				intent.putExtra("source" ,audioList.get(position).getSource());
				context.startActivity(intent);
			}
		});

		ImageButton imageButton_share = (ImageButton) convertView.findViewById(R.id.tab1_fragment_list_item_share);
		final ImageButton imageButton_download = (ImageButton) convertView.findViewById(R.id.tab1_fragement_list_item_download);

		imageButton_share.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v )
			{
				Toast.makeText(inflater.getContext() ,"正在分享" + audioList.get(position).getName().toString() + "..." ,Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("audio/*");
				intent.putExtra(Intent.EXTRA_SUBJECT ,"Share");
				String url = ("www.baidu.com").toString();
				File file = new File(url);
				Uri uri = Uri.fromFile(file);
				intent.putExtra(Intent.EXTRA_STREAM ,uri);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(Intent.createChooser(intent ,"分享"));
			}
		});

		imageButton_download.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v )
			{
				// TODO Auto-generated method stub
				Toast.makeText(inflater.getContext() ,"正在下载" + audioList.get(position).getName().toString() + "..." ,Toast.LENGTH_SHORT).show();
				String urlString = audioList.get(position).getLink().toString();
				urlString = "http://abv.cn/music/千千阙歌.mp3";// 千千阙歌 红豆 光辉岁月.mp3
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
					File saveDir = new File(Environment.getExternalStorageDirectory() + "/&abc_record/musics");
					download(urlString ,saveDir);
					imageButton_download.setEnabled(false);

				}
				else
				{
					Toast.makeText(context ,"请检查SD卡" ,Toast.LENGTH_LONG).show();
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

	public static void initData()
	{
		audioList = new ArrayList < MyAudio >();
		for(int i = 0 ; i < 17 ; i ++ )
		{
			myAudio = new MyAudio();
			myAudio.setId("ID" + i);
			myAudio.setData("Data" + i);
			myAudio.setLink("Link" + i);
			myAudio.setName("name" + i);
			myAudio.setSource("source" + i);
			myAudio.setOther("other" + i);
			audioList.add(myAudio);
		}
	}

	void download(String path , File savDir )
	{
		DownloadTask task = new DownloadTask(handler , context , path , savDir);
		new Thread(task).start();
	}

	public Handler handler = new UIHandler();

	static class UIHandler extends Handler
	{
		public void handleMessage(Message msg )
		{
			switch(msg.what)
			{
				case 1: // 更新进度
					Toast.makeText(context ,"下载成功" ,Toast.LENGTH_LONG).show();
					break;
				case -1: // 下载失败
					Toast.makeText(context ,"下载失败" ,Toast.LENGTH_LONG).show();
					break;
			}
		}
	}

}
