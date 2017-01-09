package com.runcom.wgcwgc.audio;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.runcom.wgcwgc.audioBean.MyAudio;
import com.runcom.wgcwgc.play.Play;

public class SwipeMenu extends Tab1Fragment
{

	private SwipeMenuListView listView;
	private Context context;
	ArrayList < MyAudio > audioList = new ArrayList < MyAudio >();

	public SwipeMenu()
	{
		
	}

	public SwipeMenu(SwipeMenuListView listView , Context context , ArrayList < MyAudio > audioList)
	{
		this.listView = listView;
		this.context = context;
		this.audioList = audioList;
		run(this.listView ,this.context ,this.audioList);
	}

	private void run(SwipeMenuListView listView , final Context context , final ArrayList < MyAudio > audioList )
	{
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView < ? > arg0 , View arg1 , int arg2 , long arg3 )
			{
				Toast.makeText(context ,"您单击了" + audioList.get(arg2).getName().toString() ,Toast.LENGTH_SHORT).show();
			}

		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView < ? > arg0 , View arg1 , int arg2 , long arg3 )
			{
				Toast.makeText(context ,"您长按了" + audioList.get(arg2).getName().toString() ,Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		listView.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(int position , com.baoyz.swipemenulistview.SwipeMenu menu , int index )
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
						Log.d("LOG" ,"audio: " + source + "\nlyric: " + lyric);
						// lyric = "http://abv.cn/music/王菲_红豆.lrc";
						open_intent.putExtra("lyric" ,lyric);
						getContext().startActivity(open_intent);
						break;
					case 1:
						Toast.makeText(context ,"正在删除" + audioList.get(position).getName().toString() + "..." ,Toast.LENGTH_SHORT).show();

						break;
					case 2:
						Toast.makeText(context ,"正在分享" + audioList.get(position).getName().toString() + "..." ,Toast.LENGTH_SHORT).show();
						Intent share_intent = new Intent(Intent.ACTION_SEND);
						share_intent.setType("text/*");
						share_intent.putExtra(Intent.EXTRA_SUBJECT ,"Share");
						String url = ("www.baidu.com").toString();
						share_intent.putExtra(Intent.EXTRA_TEXT ,url);
						share_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						getContext().startActivity(Intent.createChooser(share_intent ,"分享"));
						break;
				}
				return false;
			}
		});

		listView.setMenuCreator(creator);
	}

	SwipeMenuCreator creator = new SwipeMenuCreator()
	{
		@Override
		public void create(com.baoyz.swipemenulistview.SwipeMenu menu )
		{
			SwipeMenuItem openItem = new SwipeMenuItem(context);
			openItem.setBackground(new ColorDrawable(Color.rgb(0xC9 ,0xC9 ,0xCE)));
			openItem.setWidth(90);
			openItem.setTitle("Open");
			openItem.setTitleSize(18);
			openItem.setTitleColor(Color.BLACK);
			menu.addMenuItem(openItem);

			SwipeMenuItem deleteItem = new SwipeMenuItem(context);
			deleteItem.setBackground(new ColorDrawable(Color.rgb(0xA9 ,0xA9 ,0xEF)));
			deleteItem.setWidth(90);
			deleteItem.setTitle("Delete");
			deleteItem.setTitleSize(18);
			deleteItem.setTitleColor(Color.BLACK);
			menu.addMenuItem(deleteItem);

			SwipeMenuItem shareItem = new SwipeMenuItem(context);
			shareItem.setBackground(new ColorDrawable(Color.rgb(0xF9 ,0x3F ,0x25)));
			shareItem.setWidth(90);
			shareItem.setTitle("Share");
			shareItem.setTitleSize(18);
			shareItem.setTitleColor(Color.BLACK);

			menu.addMenuItem(shareItem);
		}
	};

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

}
