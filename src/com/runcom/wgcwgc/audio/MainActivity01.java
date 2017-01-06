package com.runcom.wgcwgc.audio;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.play.PlayLocaleAudio;
import com.runcom.wgcwgc.record.MyRecord;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("HandlerLeak")
public class MainActivity01 extends FragmentActivity
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

	private FragmentTabHost mTabHost;
	private RadioGroup mTabRg;

	@SuppressWarnings("rawtypes")
	private final Class [] fragments =
	{ Tab1Fragment.class, Tab2Fragment.class, Tab3Fragment.class};

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViewPager_top();
		initDots();
		loopPlay();

		initEnd();

		MobclickAgent.openActivityDurationTrack(false);
	}

	private void initEnd()
	{
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this ,getSupportFragmentManager() ,R.id.realtabcontent);
		// 得到fragment的个数
		int count = fragments.length;
		for(int i = 0 ; i < count ; i ++ )
		{
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(i + "").setIndicator(i + "");
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec ,fragments[i] ,null);
		}
		mTabRg = (RadioGroup) findViewById(R.id.tab_rg_menu);
		mTabRg.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroup group , int checkedId )
			{
				switch(checkedId)
				{
					case R.id.tab_rb_1:
						mTabHost.setCurrentTab(0);
						break;

					case R.id.tab_rb_2:
						mTabHost.setCurrentTab(1);
						break;

					case R.id.tab_rb_3:
						mTabHost.setCurrentTab(2);
						break;

					default:
						break;
				}
			}
		});

		mTabHost.setCurrentTab(0);
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
		dots[viewPager_currentIndex].setBackgroundResource(R.drawable.main_point_down);
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
		dots[position].setBackgroundResource(R.drawable.main_point_down);
		dots[viewPager_currentIndex].setBackgroundResource(R.drawable.main_point);
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
						Thread.sleep(5000);
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

	// 两秒内按返回键两次退出程序
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode , KeyEvent event )
	{
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if((System.currentTimeMillis() - exitTime) > 2000)
			{
				Toast.makeText(getApplicationContext() ,"再按一次退出程序" ,Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			}
			else
			{
				MobclickAgent.onKillProcess(this);
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode ,event);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
