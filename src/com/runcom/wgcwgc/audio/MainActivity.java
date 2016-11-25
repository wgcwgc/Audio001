package com.runcom.wgcwgc.audio;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.play.PlayLocaleAudio;
import com.runcom.wgcwgc.record.MyRecord;

@SuppressLint(
{ "HandlerLeak", "InflateParams" })
public class MainActivity extends FragmentActivity implements OnClickListener , OnPageChangeListener
{

	private ViewPager viewPager_top;
	private ArrayList < View > viewPager_list = new ArrayList < View >();
	// 底部点的布局
	private LinearLayout pointLayout;
	// 底部的点
	private ImageView [] dots;
	// 当前选中的索引
	private int viewPager_currentIndex;
	//
	private boolean viewPagerFlag = true;
	// 自增int
	private AtomicInteger what = new AtomicInteger(0);

	// Fragment_pagerView control
	// 三个textview
	private TextView tab1Tv , tab2Tv , tab3Tv;
	// 指示器
	private ImageView cursorImg;
	// viewpager
	private ViewPager viewPager;
	// fragment对象集合
	private ArrayList < Fragment > fragmentsList;
	// 记录当前选中的tab的index
	int currentIndex = 0;
	// 指示器的偏移量
	private int offset = 0;
	// 左margin
	int leftMargin = 0;
	// 屏幕宽度
	private int screenWidth = 0;
	// 屏幕宽度的三分之一
	private int screen1_3;
	//
	private LinearLayout.LayoutParams lp;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);

		initViewPager_top();
		initDots();
		loopPlay();

		initMiddle();

	}

	/**
	 * 初始化操作
	 */
	private void initMiddle()
	{
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screen1_3 = screenWidth / 3;

		cursorImg = (ImageView) findViewById(R.id.cursor);
		lp = (LayoutParams) cursorImg.getLayoutParams();
		leftMargin = lp.leftMargin;

		tab1Tv = (TextView) findViewById(R.id.tab1_tv);
		tab2Tv = (TextView) findViewById(R.id.tab2_tv);
		tab3Tv = (TextView) findViewById(R.id.tab3_tv);

		tab1Tv.setOnClickListener(this);
		tab2Tv.setOnClickListener(this);
		tab3Tv.setOnClickListener(this);

		initViewPager_middle();

	}

	/**
	 * 初始化viewpager
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void initViewPager_middle()
	{
		viewPager = (ViewPager) findViewById(R.id.third_vp);
		fragmentsList = new ArrayList < Fragment >();
		Fragment fragment = new Tab1Fragment();
		fragmentsList.add(fragment);
		fragment = new Tab2Fragment();
		fragmentsList.add(fragment);
		fragment = new Tab3Fragment();
		fragmentsList.add(fragment);

		viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager() , fragmentsList));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(this);

	}

	@Override
	public void onPageScrolled(int position , float positionOffset , int positionOffsetPixels )
	{
		offset = (screen1_3 - cursorImg.getLayoutParams().width) / 2;
		Log.d("LOG" ,position + "--" + positionOffset + "--" + positionOffsetPixels);
		// final float scale = getResources().getDisplayMetrics().density;
		if(position == 0)
		{// 0<->1
			lp.leftMargin = (int) (positionOffsetPixels / 3) + offset;
		}
		else
			if(position == 1)
			{// 1<->2
				lp.leftMargin = (int) (positionOffsetPixels / 3) + screen1_3 + offset;
			}
		cursorImg.setLayoutParams(lp);
		currentIndex = position;

	}

	@Override
	public void onClick(View v )
	{
		switch(v.getId())
		{
			case R.id.tab1_tv:
				viewPager.setCurrentItem(0);
				break;
			case R.id.tab2_tv:
				viewPager.setCurrentItem(1);
				break;
			case R.id.tab3_tv:
				viewPager.setCurrentItem(2);
				break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0 )
	{
	}

	@Override
	public void onPageSelected(int arg0 )
	{
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void initViewPager_top()
	{
		viewPager_top = (ViewPager) findViewById(R.id.main_viewPager);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view1 = inflater.inflate(R.layout.first_layout1 ,null);
		View view2 = inflater.inflate(R.layout.first_layout2 ,null);
		View view3 = inflater.inflate(R.layout.first_layout3 ,null);
		viewPager_list.add(view1);
		viewPager_list.add(view2);
		viewPager_list.add(view3);

		viewPager_top.setAdapter(pagerAdapter);
		viewPager_top.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int arg0 )
			{
				setDots(arg0);
			}

			@Override
			public void onPageScrolled(int arg0 , float arg1 , int arg2 )
			{
			}

			@Override
			public void onPageScrollStateChanged(int arg0 )
			{
			}
		});
	}

	public void first_layout01_onClick(View view )
	{
		Log.d("LOG" ,"first_layout01_onClick");
		Toast.makeText(this ,"first_layout01_onClick" ,Toast.LENGTH_SHORT).show();
	}

	public void first_layout02_onClick(View view )
	{
		Log.d("LOG" ,"first_layout02_onClick");
		Toast.makeText(this ,"first_layout02_onClick" ,Toast.LENGTH_SHORT).show();
	}

	public void first_layout03_onClick(View view )
	{
		Log.d("LOG" ,"first_layout03_onClick");
		Toast.makeText(this ,"first_layout03_onclick" ,Toast.LENGTH_SHORT).show();
	}

	/**
	 * 初始化底部的点
	 */
	private void initDots()
	{
		pointLayout = (LinearLayout) findViewById(R.id.point_layout);
		dots = new ImageView [viewPager_list.size()];
		for(int i = 0 ; i < viewPager_list.size() ; i ++ )
		{
			dots[i] = (ImageView) pointLayout.getChildAt(i);
		}
		viewPager_currentIndex = 0;
		dots[viewPager_currentIndex].setBackgroundResource(R.drawable.dian_down);
	}

	/**
	 * 当滚动的时候更换点的背景图
	 */
	private void setDots(int position )
	{
		if(position < 0 || position > viewPager_list.size() - 1 || viewPager_currentIndex == position)
		{
			return;
		}
		dots[position].setBackgroundResource(R.drawable.dian_down);
		dots[viewPager_currentIndex].setBackgroundResource(R.drawable.dian);
		viewPager_currentIndex = position;
	}

	private PagerAdapter pagerAdapter = new PagerAdapter()
	{
		@Override
		public boolean isViewFromObject(View arg0 , Object arg1 )
		{
			return arg0 == arg1;
		}

		@Override
		public int getCount()
		{
			return viewPager_list.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container , int position )
		{
			container.addView(viewPager_list.get(position));
			return viewPager_list.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container , int position , Object object )
		{
			container.removeView(viewPager_list.get(position));
		}

	};

	private final Handler viewHandler = new Handler()
	{
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(android.os.Message msg )
		{
			viewPager_top.setCurrentItem(msg.what);
			setDots(msg.what);
		};
	};

	/**
	 * 循环播放图片
	 */
	private void loopPlay()
	{
		/**
		 * 开辟线程来控制图片轮播-左右轮播
		 */
		new Thread(new Runnable()
		{
			public void run()
			{
				while(true)
				{
					viewHandler.sendEmptyMessage(what.get());

					if(what.get() >= viewPager_list.size() - 1)
					{
						// what.getAndAdd( -3);
						viewPagerFlag = false;
					}
					if(what.get() < 1)
					{
						viewPagerFlag = true;
					}
					if(viewPagerFlag)
					{
						what.incrementAndGet();
					}
					else
					{
						what.decrementAndGet();
					}

					try
					{
						Thread.sleep(2000);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu )
	{
		getMenuInflater().inflate(R.menu.media_menu ,menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item )
	{
		switch(item.getItemId())
		{
			case R.id.item3_record:
				Intent intent = new Intent(this , MyRecord.class);
				startActivity(intent);
				break;

			case R.id.item_playLocalAudio:
				startActivity(new Intent(this , PlayLocaleAudio.class));
				break;

		}
		return super.onOptionsItemSelected(item);
	}

}
