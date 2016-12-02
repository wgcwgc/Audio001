package com.runcom.wgcwgc.play;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.audioList_ReadAndWriter.Print;
import com.runcom.wgcwgc.audioList_ReadAndWriter.Read;

@SuppressLint("HandlerLeak")
public class PlayLocaleAudio extends Activity implements Runnable , OnCompletionListener , OnErrorListener , OnItemClickListener , OnSeekBarChangeListener
{
	protected static final int SEARCH_MUSIC_SUCCESS = 0;// 搜索成功标记
	protected static final int SEARCH_MUSIC_SUCCESS_FIRST = -1;// 首次搜索app目录
	private SeekBar seekBar;
	private ListView listView;
	private ImageButton btnPlay;
	private TextView tv_currTime , tv_totalTime , tv_showName;
	private List < String > play_list = new ArrayList < String >(); // 播放列表
	String [] ext =
	{ ".mp3", ".wav" };
	File file = Environment.getExternalStorageDirectory();// sd卡根目录
	String filePath = "/&abc_record/";
	private ProgressDialog pd; // 进度条对话框
	private MusicListAdapter ma;// 适配器
	private MediaPlayer mp;
	private int currIndex = 0;// 表示当前播放的音乐索引
	private boolean seekBarFlag = true;// 控制进度条线程标记

	// 定义当前播放器的状态
	private static final int IDLE = 0;
	private static final int PAUSE = 1;
	private static final int START = 2;
	private static final int CURR_TIME_VALUE = 1;

	private int currState = IDLE; // 当前播放器的状态
	// 定义线程池（同时只能有一个线程运行）
	ExecutorService es = Executors.newSingleThreadExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_main);

		ActionBar actionbar = getActionBar();
		// 显示返回箭头默认是不显示的
		actionbar.setDisplayHomeAsUpEnabled(false);
		// 显示左侧的返回箭头，并且返回箭头和title一起设置，返回箭头才能显示
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		// 显示标题
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" 播放本地音频 ");

		mp = new MediaPlayer();
		mp.setOnCompletionListener(this);
		mp.setOnErrorListener(this);

		initPlayView();
	}

	private void initPlayView()
	{
		btnPlay = (ImageButton) findViewById(R.id.media_play_control_main);
		seekBar = (SeekBar) findViewById(R.id.seekBar_control_main);
		seekBar.setOnSeekBarChangeListener(this);
		listView = (ListView) findViewById(R.id.control_main_listView);
		listView.setOnItemClickListener(this);
		tv_currTime = (TextView) findViewById(R.id.textView1_curr_time_control_main);
		tv_totalTime = (TextView) findViewById(R.id.textView1_total_time_control_main);
		tv_showName = (TextView) findViewById(R.id.tv_showName_control_main);

		initListView();
	}

	private void initListView()
	{
		play_list.clear();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			new Thread(new Runnable()
			{
				public void run()
				{
					play_list = new Read().reader();
					if(play_list == null)
					{
						play_list = new ArrayList < String >();
						search(new File(file.toString() + filePath) ,ext);
					}
					hander.sendEmptyMessage(SEARCH_MUSIC_SUCCESS_FIRST);
				}
			}).start();

		}
		else
		{
			Toast.makeText(PlayLocaleAudio.this ,"请插入外部存储设备..." ,Toast.LENGTH_LONG).show();
			System.out.println(Environment.getExternalStorageDirectory().toString());
		}
	}

	public Handler hander = new Handler()
	{
		public void handleMessage(android.os.Message msg )
		{
			switch(msg.what)
			{
				case SEARCH_MUSIC_SUCCESS:
					// 搜索音乐文件结束
					ma = new MusicListAdapter();
					listView.setAdapter(ma);
					pd.dismiss();
					break;
				case CURR_TIME_VALUE:
					// 设置当前时间
					tv_currTime.setText(msg.obj.toString());
					break;
				// 初始化listview
				case SEARCH_MUSIC_SUCCESS_FIRST:
					ma = new MusicListAdapter();
					listView.setAdapter(ma);
					break;
				default:
					break;
			}
		};
	};

	// 搜索音乐文件
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
			tv_music_name.setText(name);
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
				btnPlay.setImageResource(R.drawable.locale_play_media_play);
				currState = START;
				break;
			case START:
				mp.start();
				btnPlay.setImageResource(R.drawable.locale_play_media_pause);
				currState = PAUSE;
		}
	}

	// 上一首
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
				Toast.makeText(PlayLocaleAudio.this ,"播放列表为空" ,Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(PlayLocaleAudio.this ,"当前已经是第一首歌曲了" ,Toast.LENGTH_SHORT).show();
			}
	}

	// 下一首
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
				Toast.makeText(PlayLocaleAudio.this ,"播放列表为空" ,Toast.LENGTH_SHORT).show();
			}
			else
				if(currIndex + 1 == play_list.size())
				{
					Toast.makeText(PlayLocaleAudio.this ,"当前已经是最后一首歌曲了" ,Toast.LENGTH_SHORT).show();
					currIndex = -1;
					next();
				}
				else
				{
					currIndex = -1;
					next();
				}
	}

	// 开始播放
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
				btnPlay.setImageResource(R.drawable.locale_play_media_pause);
				currState = PAUSE;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(PlayLocaleAudio.this ,"播放列表为空" ,Toast.LENGTH_SHORT).show();
		}
	}

	// 播放按钮
	public void playLocalAudio(View v )
	{
		play();
	}

	// 上一首按钮
	public void previousLocalAudio(View v )
	{
		previous();
	}

	// 下一首按钮
	public void nextLocalAudio(View v )
	{
		next();
	}

	// 监听器，当当前歌曲播放完时触发，播放下一首
	public void onCompletion(MediaPlayer mp )
	{
		if(play_list.size() > 0)
		{
			next();
		}
		else
		{
			tv_showName.setText("");
			tv_currTime.setText("00:00");
			Toast.makeText(PlayLocaleAudio.this ,"播放列表为空" ,Toast.LENGTH_SHORT).show();
		}
	}

	// 当播放异常时触发
	public boolean onError(MediaPlayer mp , int what , int extra )
	{
		mp.reset();
		return false;
	}

	// 初始化SeekBar
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

	// SeekBar监听器
	public void onProgressChanged(SeekBar seekBar , int progress , boolean fromUser )
	{
		// 是否由用户改变
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

	// ListView监听器
	public void onItemClick(AdapterView < ? > parent , View view , int position , long id )
	{
		currIndex = position;
		start();
	}

	@Override
	public boolean onMenuOpened(int featureId , Menu menu )
	{

		if(featureId == Window.FEATURE_ACTION_BAR && menu != null)
		{
			if(menu.getClass().getSimpleName().equals("MenuBuilder"))
			{
				try
				{
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible" ,Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu ,true);
				}
				catch(Exception e)
				{
					Toast.makeText(this ,"overflow 展开显示item图标异常" ,Toast.LENGTH_LONG).show();
				}
			}
		}

		return super.onMenuOpened(featureId ,menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu )
	{
		getMenuInflater().inflate(R.menu.audio_menu_local ,menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item )
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				mp.release();
				mp.stop();
				ma.notifyDataSetChanged();
				onBackPressed();
				finish();
				break;

			// 搜索本地音乐菜单
			case R.id.item_search:
				play_list.clear();
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					pd = ProgressDialog.show(PlayLocaleAudio.this ,"搜索" ,"正在搜索音乐文件..." ,true);
					new Thread(new Runnable()
					{
						public void run()
						{
							search(file ,ext);
							new Print((ArrayList < String >) play_list).printerList();
							hander.sendEmptyMessage(SEARCH_MUSIC_SUCCESS);
						}
					}).start();
				}
				else
				{
					Toast.makeText(PlayLocaleAudio.this ,"请插入外部存储设备..." ,Toast.LENGTH_LONG).show();
					System.out.println(Environment.getExternalStorageDirectory().toString());
				}

				break;
			// 清除播放列表菜单
			case R.id.item_clear:
				if(play_list.isEmpty())
				{
				}
				else
				{
					play_list.clear();
					new Print((ArrayList < String >) play_list).printerList();
					ma.notifyDataSetChanged();
					mp.stop();
					tv_currTime.setText("00:00");
					tv_showName.setText("");
					tv_totalTime.setText("00:00");
					seekBar.setProgress(0);
					mp.seekTo(0);
					btnPlay.setImageResource(R.drawable.locale_play_media_play);
				}
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	// 重写按返回键退出播放
	@Override
	public boolean onKeyDown(int keyCode , KeyEvent event )
	{
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			mp.release();
			mp.stop();
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode ,event);
	}

	@Override
	protected void onDestroy()
	{
		new Print((ArrayList < String >) play_list).printerList();
	    super.onDestroy();
	}

}
