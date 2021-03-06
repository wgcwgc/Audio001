package com.runcom.wgcwgc.play;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.audioBean.LrcRead;
import com.runcom.wgcwgc.audioBean.LyricContent;
import com.runcom.wgcwgc.audioBean.LyricView;
import com.runcom.wgcwgc.storage.MySharedPreferences;
import com.runcom.wgcwgc.util.Util;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("HandlerLeak")
public class Play extends Activity implements Runnable , OnCompletionListener , OnErrorListener , OnSeekBarChangeListener , OnBufferingUpdateListener
{
	private SeekBar seekBar;
	private ImageButton btnPlay;
	private TextView tv_currTime , tv_totalTime , tv_showName;
	private List < String > play_list = new ArrayList < String >();
	public MediaPlayer mp;
	private int currIndex = 0;// 表示当前播放的音乐索引
	private boolean seekBarFlag = true;// 控制进度条线程标记

	// 定义当前播放器的状态
	private static final int IDLE = 0;
	private static final int PAUSE = 1;
	private static final int START = 2;
	private static final int CURR_TIME_VALUE = 1;

	private int currState = IDLE; // 当前播放器的状态
	// 定义线程池（同时只能有一个线程运行）
	private ExecutorService es = Executors.newSingleThreadExecutor();

	private Intent intent;
	private String source , lyricsPath , name;

	// 歌词处理
	private LrcRead mLrcRead;
	private LyricView mLyricView;
	private int index = 0;
	private int CurrentTime = 0;
	private int CountTime = 0;
	private List < LyricContent > LyricList = new ArrayList < LyricContent >();

	private final String sharedPreferencesKey = "Setting";
	private final String sharedPreferencesSubtitleFlag = "subtitleShow";

	@SuppressWarnings("unused")
	private final String TAG = "LOG";
	// record setting
	int record_currentVoice = 0;
	MediaRecorder myAutoRecorder;
	String outputFile = Util.audiosPath + new Random(57).toString().substring(17) + ".mp3";

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_play);
		intent = getIntent();
		source = intent.getStringExtra("source");
		lyricsPath = intent.getStringExtra("lyric");
		name = intent.getStringExtra("name");
		// lyricsPath = Util.lyricsPath + "王菲_红豆.lrc";// defaultLyric.lrc
		lyricsPath = Util.lyricsPath + lyricsPath.substring(lyricsPath.lastIndexOf("/"));
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" 播放 ");

		initPlayView();
		myAutoRecorder = new MediaRecorder();
		// 从麦克风源进行录音
		myAutoRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		// 设置输出格式
		myAutoRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		// 设置编码格式
		myAutoRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

		myAutoRecorder.setOutputFile(outputFile);
	}

	private void initLyric()
	{
		int flag = MySharedPreferences.getValue(getApplicationContext() ,sharedPreferencesKey ,sharedPreferencesSubtitleFlag ,0);
		// MySharedPreferences sp = (MySharedPreferences)
		// context.getSharedPreferences(SETTING ,Context.MODE_PRIVATE);
		// int value = ((android.content.SharedPreferences) sp).getInt(key
		// ,defValue);

		new Thread()
		{

		}.start();
		mLrcRead = new LrcRead();
		mLyricView = (LyricView) findViewById(R.id.LyricShow);
		try
		{
			if(new File(lyricsPath).exists())
			{
				mLrcRead.Read(lyricsPath ,flag);
			}
			else
			{
				String defaultLyricPath = Util.lyricsPath + "defaultLyric.lrc";
				File defaultLyricPathFile = new File(defaultLyricPath);
				if( !defaultLyricPathFile.exists() || !defaultLyricPathFile.getParentFile().exists())
				{
					defaultLyricPathFile.getParentFile().mkdirs();

					defaultLyricPathFile.createNewFile();
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(defaultLyricPathFile , false));
					bufferedWriter.write("[00:00.00] NO LYRICS\r\n");
					bufferedWriter.flush();
					bufferedWriter.close();
				}
				mLrcRead.Read(defaultLyricPath ,flag);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		LyricList = mLrcRead.GetLyricContent();
		mLyricView.setSentenceEntities(LyricList);
		mHandler.post(mRunnable);
	}

	Handler mHandler = new Handler();

	Runnable mRunnable = new Runnable()
	{
		public void run()
		{
			mLyricView.SetIndex(Index());
			mLyricView.invalidate();
			mHandler.postDelayed(mRunnable ,1000);
		}
	};

	public int Index()
	{
		if(mp.isPlaying())
		{
			CurrentTime = mp.getCurrentPosition();
			CountTime = mp.getDuration();
		}
		if(CurrentTime < CountTime)
		{
			for(int i = 0 ; i < LyricList.size() ; i ++ )
			{
				if(i < LyricList.size() - 1)
				{
					if(CurrentTime < LyricList.get(i).getLyricTime() && i == 0)
					{
						index = i;
					}
					if(CurrentTime > LyricList.get(i).getLyricTime() && CurrentTime < LyricList.get(i + 1).getLyricTime())
					{
						index = i;
					}
				}

				if(i == LyricList.size() - 1 && CurrentTime > LyricList.get(i).getLyricTime())
				{
					index = i;
				}
			}
		}

		return index;
	}

	void initPlayView()
	{
		btnPlay = (ImageButton) findViewById(R.id.media_play);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		seekBar.setOnSeekBarChangeListener(this);
		tv_currTime = (TextView) findViewById(R.id.textView1_curr_time);
		tv_totalTime = (TextView) findViewById(R.id.textView1_total_time);
		tv_showName = (TextView) findViewById(R.id.tv_showName);
		mp = new MediaPlayer();
		mp.setOnCompletionListener(this);
		mp.setOnErrorListener(this);
		mp.setOnBufferingUpdateListener(this);
		mp.setLooping(true);
		play_list.add(source);
		Log.d("LOG" ,"source: " + source);
		initLyric();
		start();
	}

	public Handler hander = new Handler()
	{
		public void handleMessage(Message msg )
		{
			switch(msg.what)
			{
				case CURR_TIME_VALUE:
					tv_currTime.setText(msg.obj.toString());
					break;
				default:
					break;
			}
		};
	};

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
				tv_showName.setText(name);
				btnPlay.setImageResource(R.drawable.play_start);
				currState = PAUSE;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(this ,"播放完毕" ,Toast.LENGTH_SHORT).show();
		}
	}

	public void record(View view )
	{

	}

	// 播放按钮
	public void play(View v )
	{
		switch(currState)
		{
			case IDLE:
				start();
				break;
			case PAUSE:
				mp.pause();
				btnPlay.setImageResource(R.drawable.play_pause);
				currState = START;
				break;
			case START:
				mp.start();
				btnPlay.setImageResource(R.drawable.play_start);
				currState = PAUSE;
		}
	}

	// 快退按钮
	public void previous(View v )
	{
		mp.seekTo(mp.getCurrentPosition() - 7000);
	}

	// 快进按钮
	public void next(View v )
	{
		mp.seekTo(mp.getCurrentPosition() + 7000);
	}

	// 监听器，当当前歌曲播放完时触发，播放下一首
	public void onCompletion(MediaPlayer mp )
	{
		if(play_list.size() > 0)
		{
			start();
		}
		else
		{
			initSeekBar();
			Toast.makeText(this ,"播放完毕" ,Toast.LENGTH_SHORT).show();
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

	@Override
	public void onBufferingUpdate(MediaPlayer mp , int percent )
	{
		seekBar.setSecondaryProgress(percent * mp.getDuration() / 100);
		// Log.d("LOG" , "percent: " + percent);
	}

	public void onStartTrackingTouch(SeekBar seekBar )
	{
	}

	public void onStopTrackingTouch(SeekBar seekBar )
	{
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
		getMenuInflater().inflate(R.menu.main ,menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item )
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				mp.stop();
				onBackPressed();
				break;
			// TODO 录音 + 调节音量 + 恢复
			case R.id.play_audio_start:
				recordStart();
				break;
			case R.id.play_audio_stop:
				recordStop();
				break;
			case R.id.play_audio_paly:
				recordPlay();
				break;
			case R.id.play_audio_share:
				recordShare();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void recordStart()
	{
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		record_currentVoice = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC ,0 ,0);

		try
		{
			myAutoRecorder.prepare();
			myAutoRecorder.start();
		}
		catch(Exception e)
		{
			Log.d("LOG" ,e.toString());
			e.printStackTrace();
		}
		Toast.makeText(getApplicationContext() ,"Recording..." ,Toast.LENGTH_LONG).show();

		// MenuItem menuItem = new MenuItem();

	}

	
	public void recordStop()
	{
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC ,record_currentVoice ,0);

		myAutoRecorder.stop();
		myAutoRecorder.release();
		myAutoRecorder = null;
		Toast.makeText(getApplicationContext() ,"Record successfully!!!\n文件保存在:" + outputFile ,Toast.LENGTH_LONG).show();

	}

	public void recordPlay()
	{
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC ,record_currentVoice ,0);
		mp.pause();
		btnPlay.setImageResource(R.drawable.play_pause);
		currState = START;

		MediaPlayer m = new MediaPlayer();
		try
		{
			m.setDataSource(outputFile);
			m.prepare();
			m.start();
			Toast.makeText(getApplicationContext() ,"Your record is playing." ,Toast.LENGTH_LONG).show();
		}
		catch(Exception e)
		{
			Log.d("LOG" ,e.toString());
			e.printStackTrace();
		}
	}

	public void recordShare()
	{
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC ,record_currentVoice ,0);

		Intent intent = new Intent(Intent.ACTION_SEND);

		intent.setType("audio/*");
		intent.putExtra(Intent.EXTRA_SUBJECT ,"Share");
		String url = outputFile.toString();
		Uri uri = Uri.parse(url);
		intent.putExtra(Intent.EXTRA_STREAM ,uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent ,"分享"));
	}

	// 重写按返回键退出播放
	@Override
	public boolean onKeyDown(int keyCode , KeyEvent event )
	{
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			mp.stop();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode ,event);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		MobclickAgent.onPageStart("PlayScreen");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		MobclickAgent.onPageEnd("PlayScreen");
		MobclickAgent.onPause(this);
	}

}
