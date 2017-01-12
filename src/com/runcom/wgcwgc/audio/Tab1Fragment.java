package com.runcom.wgcwgc.audio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.audioBean.MyAudio;
import com.runcom.wgcwgc.download.LrcFileDownloader;
import com.runcom.wgcwgc.play.Play;
import com.runcom.wgcwgc.util.NetUtil;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("InflateParams")
public class Tab1Fragment extends Fragment
{

	String audio , lyric , name;
	private SwipeMenuListView listView;
	MyAudio myAudio = new MyAudio();
	ArrayList < MyAudio > audioList01 = new ArrayList < MyAudio >();
	MyListViewAdapter adapter;
	private View rootView;

	// private boolean flag = true;
	@Override
	public void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		// adapter = new MyListViewAdapter(getContext() , audioList01);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState )
	{
		// if(rootView == null)
		// {
		rootView = inflater.inflate(R.layout.fragment_tab1 ,container ,false);
		// }
		//
		// ViewGroup parent = (ViewGroup) rootView.getParent();
		// if(parent != null)
		// {
		// parent.removeView(rootView);
		// }
		// rootView = inflater.inflate(R.layout.fragment_tab1 ,container
		// ,false);
		listView = (SwipeMenuListView) rootView.findViewById(R.id.fragment_tab1_listView);

		if(NetUtil.getNetworkState(inflater.getContext()) == NetUtil.NETWORN_NONE)
		{
			Toast.makeText(getContext() ,"请检查网络连接" ,Toast.LENGTH_SHORT).show();
			TextView emptyView = new TextView(getContext());
			emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT , LayoutParams.FILL_PARENT));
			emptyView.setText("\n\n\n\n\n\n\n\tThis appears when the network can not connect\n\t\tPlease cheak your network state!!!");
			// emptyView.setVisibility(View.GONE);
			((ViewGroup) listView.getParent()).addView(emptyView);
			listView.setEmptyView(emptyView);
			return rootView;
		}

		// audioList01.clear();
		// for(int i = 0 ; i < 17 ; i ++ )
		// {
		// myAudio = new MyAudio();
		// myAudio.setLyric("1_" + i);
		// myAudio.setName("1_" + i);
		// myAudio.setSource("1");
		// audioList01.add(myAudio);
		// }
		new GetThread_getList1().start();
		adapter = new MyListViewAdapter(inflater , audioList01);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView < ? > arg0 , View arg1 , int arg2 , long arg3 )
			{
				// System.out.println(flag);
				// Log.d("LOG" , flag + "item");
				// if(flag)
				// {
				// Toast.makeText(getContext() ,"您单击了" +
				// audioList01.get(arg2).getName().toString()
				// ,Toast.LENGTH_SHORT).show();
				// }
				// else
				// flag = true;
				Toast.makeText(getContext() ,"您单击了" + audioList01.get(arg2).getName().toString() ,Toast.LENGTH_SHORT).show();
			}

		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView < ? > arg0 , View arg1 , int arg2 , long arg3 )
			{
				Toast.makeText(getContext() ,"您长按了" + audioList01.get(arg2).getName().toString() ,Toast.LENGTH_SHORT).show();
				return false;
			}

		});

		listView.setOnSwipeListener(new OnSwipeListener()
		{
			@Override
			public void onSwipeStart(int arg0 )
			{
				// flag = false;
			}

			@Override
			public void onSwipeEnd(int arg0 )
			{
				// Toast.makeText(getContext() ,"arg0: " + arg0 +
				// "onSwipeEnd..." ,Toast.LENGTH_SHORT).show();
				// flag = true;
				// Log.d("LOG" , flag + "End");
			}
		});

		SwipeMenuCreator creator = new SwipeMenuCreator()
		{
			@Override
			public void create(SwipeMenu menu )
			{
				SwipeMenuItem openItem = new SwipeMenuItem(getContext());
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9 ,0xC9 ,0xCE)));
				openItem.setWidth(dp2px(90));
				openItem.setTitle("Open");
				openItem.setTitleSize(18);
				openItem.setTitleColor(Color.BLACK);
				menu.addMenuItem(openItem);

				// SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
				// deleteItem.setBackground(new ColorDrawable(Color.rgb(0xA9
				// ,0xA9 ,0xEF)));
				// deleteItem.setWidth(dp2px(90));
				// deleteItem.setTitle("Delete");
				// deleteItem.setTitleSize(18);
				// deleteItem.setTitleColor(Color.BLACK);
				// menu.addMenuItem(deleteItem);

				SwipeMenuItem shareItem = new SwipeMenuItem(getContext());
				shareItem.setBackground(new ColorDrawable(Color.rgb(0xF9 ,0x3F ,0x25)));
				shareItem.setWidth(dp2px(90));
				shareItem.setTitle("Share");
				shareItem.setTitleSize(18);
				shareItem.setTitleColor(Color.BLACK);

				menu.addMenuItem(shareItem);
			}
		};
		listView.setMenuCreator(creator);

		listView.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(int position , SwipeMenu menu , int index )
			{
				// String s = (String) adapter.getItem(position);
				switch(index)
				{
					case 0:
						Toast.makeText(getContext() ,"您点击了" + audioList01.get(position).getName().toString() ,Toast.LENGTH_SHORT).show();
						Intent open_intent = new Intent(getContext() , Play.class);
						String source = audioList01.get(position).getSource();
						// source = "http://abv.cn/music/红豆.mp3";// 千千阙歌 红豆
						// 光辉岁月.mp3
						open_intent.putExtra("source" ,source);
						String lyric = audioList01.get(position).getLyric();
						// lyric = "http://abv.cn/music/王菲_红豆.lrc";
						open_intent.putExtra("lyric" ,lyric);
						String name = audioList01.get(position).getName();
						open_intent.putExtra("name" ,name);
						Log.d("LOG" ,"audio: " + source + "\nlyric: " + lyric + "\nname: " + name);
						getContext().startActivity(open_intent);
						break;
					case 1:
						Toast.makeText(getContext() ,"正在分享" + audioList01.get(position).getName().toString() + "..." ,Toast.LENGTH_SHORT).show();
						Intent share_intent = new Intent(Intent.ACTION_SEND);
						share_intent.setType("text/*");
						share_intent.putExtra(Intent.EXTRA_SUBJECT ,"Share");
						String url = (audioList01.get(position).getSource().toString()).toString();
						share_intent.putExtra(Intent.EXTRA_TEXT ,url);
						share_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						getContext().startActivity(Intent.createChooser(share_intent ,"分享"));
						break;
					case 2:
						Toast.makeText(getContext() ,"正在删除" + audioList01.get(position).getName().toString() + "..." ,Toast.LENGTH_SHORT).show();
						
						break;
				}
				return false;
			}
		});

		return rootView;
	}

	/**
	 * 把单位 dp 转换为 px
	 * 
	 * @param dp
	 * @return
	 */
	int dp2px(int dp )
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP ,dp ,getResources().getDisplayMetrics());
	}

	class GetThread_getList1 extends Thread
	{

		public GetThread_getList1()
		{

		}

		@Override
		public void run()
		{

			String url = "http://172.16.0.63:8080/wgc/List00.jsp?type=0";
			HttpGet httpGet = new HttpGet(url);
			try
			{
				// HttpClient httpClient = new DefaultHttpClient();
				HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();
				HttpResponse response = httpClient.execute(httpGet);
				if(response.getStatusLine().getStatusCode() == 200)
				{
					HttpEntity entity = response.getEntity();

					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String line = "";
					String returnLine = "";
					while((line = reader.readLine()) != null)
					{
						returnLine += line;
						// System.out.println("*" + line + "*\n");
					}
					JSONObject jsonObject = new JSONObject(returnLine);
					audio = jsonObject.getString("audio");
					lyric = jsonObject.getString("lyric");
					name = jsonObject.getString("name");
					audioList01.clear();
					for(int i = 1 ; i < 9 ; i ++ )
					{
						myAudio = new MyAudio();
						lyric = lyric.substring(0 ,lyric.lastIndexOf("/")) + "/00" + i + ".lrc";
						myAudio.setLyric(lyric);
						// new Thread(new DownloadTask(getContext() , lyric ,
						// new File(Util.lyricsPath))).start();
						new LrcFileDownloader(lyric).start();
						myAudio.setName(name + i);
						myAudio.setSource(audio.substring(0 ,audio.lastIndexOf("/")) + "/00" + i + ".mp3");
						audioList01.add(myAudio);
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Log.d("LOG" ,"bug");
			}
		};
	}

	@Override
	public void onResume()
	{
		super.onResume();

		// new GetThread_getList1().start();
		// adapter.notifyDataSetChanged();

		MobclickAgent.onPageStart("Tab1Fragment");
	}

	@Override
	public void onPause()
	{
		super.onPause();
		MobclickAgent.onPageEnd("Tab1Fragment");
	}

}
