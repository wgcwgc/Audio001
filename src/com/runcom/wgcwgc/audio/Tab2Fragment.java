package com.runcom.wgcwgc.audio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

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

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.audioBean.MyAudio;
import com.runcom.wgcwgc.play.Play;
import com.runcom.wgcwgc.util.NetUtil;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;
import com.umeng.analytics.MobclickAgent;

public class Tab2Fragment extends Fragment
{
	String audio , lyric;

	SwipeMenuListView listView;
	MyListViewAdapter adapter;

	MyAudio myAudio = new MyAudio();
	ArrayList < MyAudio > audioList02 = new ArrayList < MyAudio >();
	private View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState )
	{
		// if(rootView == null)
		// {
		rootView = inflater.inflate(R.layout.fragment_tab2 ,container ,false);
		// }
		//
		// ViewGroup parent = (ViewGroup) rootView.getParent();
		// if(parent != null)
		// {
		// parent.removeView(rootView);
		// }

		listView = (SwipeMenuListView) rootView.findViewById(R.id.fragment_tab2_listView);

		if(NetUtil.getNetworkState(inflater.getContext()) == NetUtil.NETWORN_NONE)
		{
			// Toast.makeText(getContext() ,"请检查网络连接"
			// ,Toast.LENGTH_SHORT).show();
			TextView emptyView = new TextView(getContext());
			emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT , LayoutParams.FILL_PARENT));
			emptyView.setText("\n\n\n\n\n\n\n\tThis appears when the network can not connect\n\t\tPlease cheak your network state!!!");
			// emptyView.setVisibility(View.GONE);
			((ViewGroup) listView.getParent()).addView(emptyView);
			listView.setEmptyView(emptyView);
			return rootView;
		}

		// audioList02.clear();
		// for(int i = 0 ; i < 17 ; i ++ )
		// {
		// myAudio = new MyAudio();
		// myAudio.setLyric("2_" + i);
		// myAudio.setName("2_" + i);
		// myAudio.setSource("2");
		// audioList02.add(myAudio);
		// }
		new GetThread_getList2().start();
		adapter = new MyListViewAdapter(inflater , audioList02);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		// new SwipeMenu( listView , getContext() , audioList02);

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
				Toast.makeText(getContext() ,"您单击了" + audioList02.get(arg2).getName().toString() ,Toast.LENGTH_SHORT).show();
			}

		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView < ? > arg0 , View arg1 , int arg2 , long arg3 )
			{
				Toast.makeText(getContext() ,"您长按了" + audioList02.get(arg2).getName().toString() ,Toast.LENGTH_SHORT).show();
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
			public void create(com.baoyz.swipemenulistview.SwipeMenu menu )
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
			public boolean onMenuItemClick(int position , com.baoyz.swipemenulistview.SwipeMenu menu , int index )
			{
				// String s = (String) adapter.getItem(position);
				switch(index)
				{
					case 0:
						Toast.makeText(getContext() ,"您点击了" + audioList02.get(position).getName().toString() ,Toast.LENGTH_SHORT).show();
						Intent open_intent = new Intent(getContext() , Play.class);
						String source = audioList02.get(position).getSource();
						// source = "http://abv.cn/music/红豆.mp3";// 千千阙歌 红豆
						// 光辉岁月.mp3
						open_intent.putExtra("source" ,source);
						String lyric = audioList02.get(position).getLyric();
						Log.d("LOG" ,"audio: " + source + "\nlyric: " + lyric);
						// lyric = "http://abv.cn/music/王菲_红豆.lrc";
						open_intent.putExtra("lyric" ,lyric);
						getContext().startActivity(open_intent);
						break;
					case 1:
						Toast.makeText(getContext() ,"正在分享" + audioList02.get(position).getName().toString() + "..." ,Toast.LENGTH_SHORT).show();
						Intent share_intent = new Intent(Intent.ACTION_SEND);
						share_intent.setType("text/*");
						share_intent.putExtra(Intent.EXTRA_SUBJECT ,"Share");
						String url = ("www.baidu.com").toString();
						share_intent.putExtra(Intent.EXTRA_TEXT ,url);
						share_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						getContext().startActivity(Intent.createChooser(share_intent ,"分享"));
						break;
					case 2:
						Toast.makeText(getContext() ,"正在删除" + audioList02.get(position).getName().toString() + "..." ,Toast.LENGTH_SHORT).show();

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

	class GetThread_getList2 extends Thread
	{

		public GetThread_getList2()
		{

		}

		@Override
		public void run()
		{

			String url = "http://172.16.0.63:8080/wgc/List00.jsp?type=1";
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
					}
					JSONObject jsonObject = new JSONObject(returnLine);
					String audio = jsonObject.getString("audio");
					String lyric = jsonObject.getString("lyric");
					audioList02.clear();
					for(int i = 0 ; i < 17 ; i ++ )
					{
						myAudio = new MyAudio();
						myAudio.setLyric(lyric);
						myAudio.setName(audio.substring(audio.lastIndexOf("/") + 1 ,audio.lastIndexOf(".")) + "_" + i);
						myAudio.setSource(audio);
						audioList02.add(myAudio);
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

		// new GetThread_getList2().start();
		// adapter.notifyDataSetChanged();
		MobclickAgent.onPageStart("Tab2Fragment");
	}

	@Override
	public void onPause()
	{
		super.onPause();
		MobclickAgent.onPageEnd("Tab2Fragment");
	}

}
