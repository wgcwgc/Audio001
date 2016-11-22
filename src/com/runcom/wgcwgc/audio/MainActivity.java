package com.runcom.wgcwgc.audio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.play.Control;
import com.runcom.wgcwgc.record.Mainactivity;

@SuppressLint("HandlerLeak")
public class MainActivity extends FragmentActivity implements OnCompletionListener , OnErrorListener , OnSeekBarChangeListener , OnItemClickListener , Runnable , OnPageChangeListener , OnClickListener
{

	private ViewPager viewPager_top;
	private ArrayList < View > viewPager_list = new ArrayList < View >();
	// �ײ���Ĳ���
	private LinearLayout pointLayout;
	// �ײ��ĵ�
	private ImageView [] dots;
	// ��ǰѡ�е�����
	private int viewPager_currentIndex;
	//
	private boolean viewPagerFlag = true;
	// ����int
	private AtomicInteger what = new AtomicInteger(0);

	// Fragment_pagerView control
	// ����textview
	private TextView tab1Tv , tab2Tv , tab3Tv;
	// ָʾ��
	private ImageView cursorImg;
	// viewpager
	private ViewPager viewPager;
	// fragment���󼯺�
	private ArrayList < Fragment > fragmentsList;
	// ��¼��ǰѡ�е�tab��index
	int currentIndex = 0;
	// ָʾ����ƫ����
	private int offset = 0;
	// ��margin
	int leftMargin = 0;
	// ��Ļ���
	private int screenWidth = 0;
	// ��Ļ��ȵ�����֮һ
	private int screen1_3;
	//
	private LinearLayout.LayoutParams lp;

	// Audio play control

	protected static final int SEARCH_MUSIC_SUCCESS = 0;// �����ɹ����
	private SeekBar seekBar;
	private ListView listView , listView01;
	private ImageButton btnPlay;
	private TextView tv_currTime , tv_totalTime , tv_showName;
	private List < String > play_list = new ArrayList < String >(); // �����б�
	String [] ext =
	{ ".mp3", ".wav" };
	File file = Environment.getExternalStorageDirectory();// sd����Ŀ¼
	private ProgressDialog pd; // �������Ի���
	private MusicListAdapter ma;// ������
	private MediaPlayer mp;
	private int currIndex = 0;// ��ʾ��ǰ���ŵ���������
	private boolean seekBarFlag = true;// ���ƽ������̱߳��

	// ���嵱ǰ��������״̬
	private static final int IDLE = 0;
	private static final int PAUSE = 1;
	private static final int START = 2;
	private static final int CURR_TIME_VALUE = 1;

	private int currState = IDLE; // ��ǰ��������״̬
	// �����̳߳أ�ͬʱֻ����һ���߳����У�
	ExecutorService es = Executors.newSingleThreadExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);

		initViewPager_top();
		initDots();
		loopPlay();

		initMiddle();

		mp = new MediaPlayer();
		mp.setOnCompletionListener(this);
		mp.setOnErrorListener(this);

		initPlayView();

	}

	/**
	 * ��ʼ������
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
		//
		tab1Tv.setOnClickListener(this);
		tab2Tv.setOnClickListener(this);
		tab3Tv.setOnClickListener(this);

		initViewPager_middle();

	}

	/**
	 * ��ʼ��viewpager
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

		viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager() , fragmentsList));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(this);

		listView01 = (ListView) getLayoutInflater().inflate(R.layout.fragment_tab1 ,null).findViewById(R.id.fragment_tab1_listView);
		listView01.setOnItemClickListener(this);
		// TODO
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
		Toast.makeText(this ,arg0 + "��ѡ����" ,Toast.LENGTH_SHORT).show();

	}

	private void initPlayView()
	{
		btnPlay = (ImageButton) findViewById(R.id.media_play);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		seekBar.setOnSeekBarChangeListener(this);
		listView = (ListView) findViewById(R.id.main_listView);
		listView.setOnItemClickListener(this);
		tv_currTime = (TextView) findViewById(R.id.textView1_curr_time);
		tv_totalTime = (TextView) findViewById(R.id.textView1_total_time);
		tv_showName = (TextView) findViewById(R.id.tv_showName);

		// search(file ,ext);
		// hander.sendEmptyMessage(SEARCH_MUSIC_SUCCESS);
	}

	public Handler hander = new Handler()
	{
		public void handleMessage(android.os.Message msg )
		{
			switch(msg.what)
			{
				case SEARCH_MUSIC_SUCCESS:
					// ���������ļ�����
					ma = new MusicListAdapter();
					listView.setAdapter(ma);
					// listView01.setAdapter(ma);
					pd.dismiss();
					break;
				case CURR_TIME_VALUE:
					// ���õ�ǰʱ��
					tv_currTime.setText(msg.obj.toString());
					break;
				default:
					break;
			}
		};
	};

	// ���������ļ�
	private void search(File file , String [] ext )
	{
		if(file != null)
		{
			if(file.isDirectory())
			{
				// System.out.println("2:" + file.toString());
				File [] listFile = file.listFiles();
				if(listFile != null)
				{
					for(int i = 0 ; i < listFile.length ; i ++ )
					{
						search(listFile[i] ,ext);
					}
				}
			}
			else
			{
				String filename = file.getAbsolutePath();
				for(int i = 0 ; i < ext.length ; i ++ )
				{
					if(filename.endsWith(ext[i]))
					{
						// list.add(filename.substring(filename.lastIndexOf("/")));
						play_list.add(filename);
						// System.out.println("3:" + filename);
						break;
					}
				}
			}
		}
	}

	@SuppressLint("InflateParams")
	class MusicListAdapter extends BaseAdapter
	{

		public int getCount()
		{
			return play_list.size();
		}

		public Object getItem(int position )
		{
			return play_list.get(position);
		}

		public long getItemId(int position )
		{
			return position;
		}

		public View getView(int position , View convertView , ViewGroup parent )
		{
			if(convertView == null)
			{
				convertView = getLayoutInflater().inflate(R.layout.list_item ,null);
			}
			TextView tv_music_name = (TextView) convertView.findViewById(R.id.textView1_music_name);
			String name = play_list.get(position).toString();
			name = name.substring(name.lastIndexOf("/") + 1);
			// tv_music_name.setText(list.get(position));
			tv_music_name.setText(name);
			// Toast.makeText(MainActivity.this ,"�������"
			// ,Toast.LENGTH_SHORT).show();
			return convertView;
		}

	}

	private void play()
	{
		switch(currState)
		{
			case IDLE:
				start();
				break;
			case PAUSE:
				mp.pause();
				btnPlay.setImageResource(R.drawable.ic_media_play);
				currState = START;
				break;
			case START:
				mp.start();
				btnPlay.setImageResource(R.drawable.ic_media_pause);
				currState = PAUSE;
		}
	}

	// ��һ��
	private void previous()
	{
		if((currIndex - 1) >= 0 && play_list.size() > 0)
		{
			currIndex -- ;
			start();
		}
		else
			if(play_list.size() <= 0)
			{
				Toast.makeText(this ,"�����б�Ϊ��" ,Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(this ,"��ǰ�Ѿ��ǵ�һ�׸�����" ,Toast.LENGTH_SHORT).show();
			}
	}

	// ��һ��
	private void next()
	{
		if(currIndex + 1 < play_list.size() && play_list.size() > 1)
		{
			currIndex ++ ;
			start();
		}
		else
			if(currIndex == play_list.size())
			{
				Toast.makeText(this ,"�����б�Ϊ��" ,Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(this ,"��ǰ�Ѿ������һ�׸�����" ,Toast.LENGTH_SHORT).show();
			}
	}

	// ��ʼ����
	private void start()
	{
		if(play_list.size() > 0 && currIndex < play_list.size())
		{
			String SongPath = play_list.get(currIndex);
			mp.reset();
			try
			{
				mp.setDataSource(SongPath);
				mp.prepare();
				mp.start();
				initSeekBar();
				es.execute(this);
				tv_showName.setText(play_list.get(currIndex));
				btnPlay.setImageResource(R.drawable.ic_media_pause);
				currState = PAUSE;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(this ,"�����б�Ϊ��" ,Toast.LENGTH_SHORT).show();
		}
	}

	// ���Ű�ť
	public void play(View v )
	{
		play();
	}

	// ��һ�װ�ť
	public void previous(View v )
	{
		previous();
	}

	// ��һ�װ�ť
	public void next(View v )
	{
		next();
	}

	// ������������ǰ����������ʱ������������һ��
	public void onCompletion(MediaPlayer mp )
	{
		if(play_list.size() > 0)
		{
			next();
		}
		else
		{
			tv_showName.setText("");
			Toast.makeText(this ,"�����б�Ϊ��" ,Toast.LENGTH_SHORT).show();
		}
	}

	// �������쳣ʱ����
	public boolean onError(MediaPlayer mp , int what , int extra )
	{
		mp.reset();
		return false;
	}

	// ��ʼ��SeekBar
	private void initSeekBar()
	{
		seekBar.setMax(mp.getDuration());
		seekBar.setProgress(0);
		tv_totalTime.setText(toTime(mp.getDuration()));
	}

	private String toTime(int time )
	{
		int minute = time / 1000 / 60;
		int s = time / 1000 % 60;
		String mm = null;
		String ss = null;
		if(minute < 10)
			mm = "0" + minute;
		else
			mm = minute + "";

		if(s < 10)
			ss = "0" + s;
		else
			ss = "" + s;

		return mm + ":" + ss;
	}

	public void run()
	{
		seekBarFlag = true;
		while(seekBarFlag)
		{
			if(mp.getCurrentPosition() < seekBar.getMax())
			{
				seekBar.setProgress(mp.getCurrentPosition());
				Message msg = hander.obtainMessage(CURR_TIME_VALUE ,toTime(mp.getCurrentPosition()));
				hander.sendMessage(msg);
				try
				{
					Thread.sleep(500);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				seekBarFlag = false;
			}
		}
	}

	// SeekBar������
	public void onProgressChanged(SeekBar seekBar , int progress , boolean fromUser )
	{
		// �Ƿ����û��ı�
		if(fromUser)
		{
			mp.seekTo(progress);
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar )
	{
	}

	public void onStopTrackingTouch(SeekBar seekBar )
	{
	}

	// ListView������
	public void onItemClick(AdapterView < ? > parent , View view , int position , long id )
	{
		currIndex = position;
		start();
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
				// Log.d("LOG" ,"arg0:" + arg0);
			}

			@Override
			public void onPageScrolled(int arg0 , float arg1 , int arg2 )
			{
				// Log.d("LOG" ,"arg0:" + arg0 + "\narg1:" + arg1 + "\narg2:" +
				// arg2);
			}

			@Override
			public void onPageScrollStateChanged(int arg0 )
			{
				// Log.d("LOG" ,"arg0:" + arg0);
			}
		});
	}

	// public void main_child_buttonClick(View view )
	// {
	// // search(file ,ext);
	// // hander.sendEmptyMessage(SEARCH_MUSIC_SUCCESS);
	// Log.d("LOG" ,"�׶���Ƶ");
	// Toast.makeText(this ,"�׶���Ƶ" ,Toast.LENGTH_SHORT).show();
	// }
	//
	// public void main_primary_buttonClick(View view )
	// {
	// // search(file ,ext);
	// // hander.sendEmptyMessage(SEARCH_MUSIC_SUCCESS);
	// Log.d("LOG" ,"Сѧ��Ƶ");
	// Toast.makeText(this ,"Сѧ��Ƶ" ,Toast.LENGTH_SHORT).show();
	// }
	//
	// public void main_middle_buttonClick(View view )
	// {
	// // search(file ,ext);
	// // hander.sendEmptyMessage(SEARCH_MUSIC_SUCCESS);
	// Log.d("LOG" ,"��ѧ��Ƶ");
	// Toast.makeText(this ,"��ѧ��Ƶ" ,Toast.LENGTH_SHORT).show();
	// }

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
	 * ��ʼ���ײ��ĵ�
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
	 * ��������ʱ�������ı���ͼ
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
	 * ѭ������ͼƬ
	 */
	private void loopPlay()
	{
		/**
		 * �����߳�������ͼƬ�ֲ�-�����ֲ�
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

					// what.incrementAndGet();
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
		// �����������ֲ˵�
			case R.id.item1_search:
				play_list.clear();
				// �Ƿ����ⲿ�洢�豸
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					pd = ProgressDialog.show(this ,"����" ,"�������������ļ�..." ,true);
					// Toast.makeText(this ,"�������������ļ�"
					// ,Toast.LENGTH_LONG).show();
					new Thread(new Runnable()
					{
						// String [] ext =
						// { ".mp3", ".wav" };
						// File file =
						// Environment.getExternalStorageDirectory();// sd����Ŀ¼

						public void run()
						{
							search(file ,ext);
							hander.sendEmptyMessage(SEARCH_MUSIC_SUCCESS);
						}
					}).start();

				}
				else
				{
					Toast.makeText(this ,"������ⲿ�洢�豸..." ,Toast.LENGTH_LONG).show();
					System.out.println(Environment.getExternalStorageDirectory().toString());
				}

				break;
			// ��������б�˵�
			case R.id.item2_clear:
				if(play_list.isEmpty())
				{

				}
				else
				{
					play_list.clear();
					ma.notifyDataSetChanged();
				}
				break;

			case R.id.item3_record:
				Intent intent = new Intent(this , Mainactivity.class);
				startActivity(intent);
				break;

			case R.id.item_playLocalAudio:
				startActivity(new Intent(this , Control.class));
				break;

			// �˳��˵�
			case R.id.item3_exit:
				seekBarFlag = false;
				this.finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
