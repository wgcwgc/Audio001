package com.runcom.wgcwgc.play;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

@SuppressLint("HandlerLeak")
public class Play extends Activity implements Runnable , OnCompletionListener , OnErrorListener , OnSeekBarChangeListener , OnBufferingUpdateListener
{
	private SeekBar seekBar;
	private ImageButton btnPlay;
	private TextView tv_currTime , tv_totalTime , tv_showName;
	private List < String > play_list = new ArrayList < String >();
	public MediaPlayer mp;
	private int currIndex = 0;// ��ʾ��ǰ���ŵ���������
	private boolean seekBarFlag = true;// ���ƽ������̱߳��

	// ���嵱ǰ��������״̬
	private static final int IDLE = 0;
	private static final int PAUSE = 1;
	private static final int START = 2;
	private static final int CURR_TIME_VALUE = 1;

	private int currState = IDLE; // ��ǰ��������״̬
	// �����̳߳أ�ͬʱֻ����һ���߳����У�
	private ExecutorService es = Executors.newSingleThreadExecutor();

	private Intent intent;
	private String source , lyricsPath;

	// ��ʴ���
	private LrcRead mLrcRead;
	private LyricView mLyricView;
	private int index = 0;
	private int CurrentTime = 0;
	private int CountTime = 0;
	private List < LyricContent > LyricList = new ArrayList < LyricContent >();

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_play);

		intent = getIntent();
		source = intent.getStringExtra("source");
		lyricsPath = intent.getStringExtra("lyricsPath");

		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" ���� ");

		initPlayView();
	}

	private void initLyric()
	{
		mLrcRead = new LrcRead();
		mLyricView = (LyricView) findViewById(R.id.LyricShow);
		try
		{
			mLrcRead.Read(lyricsPath);
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
				btnPlay.setImageResource(R.drawable.play_pause);
				currState = PAUSE;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(this ,"�������" ,Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * �������
	 */
	@Override
	public void onBufferingUpdate(MediaPlayer mp , int percent )
	{
		seekBar.setSecondaryProgress(percent);
	}

	// ���Ű�ť
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

	// ���˰�ť
	public void previous(View v )
	{
		mp.seekTo(mp.getCurrentPosition() - 7000);
	}

	// �����ť
	public void next(View v )
	{
		mp.seekTo(mp.getCurrentPosition() + 7000);
	}

	// ������������ǰ����������ʱ������������һ��
	public void onCompletion(MediaPlayer mp )
	{
		if(play_list.size() > 0)
		{
			start();
		}
		else
		{
			initSeekBar();
			Toast.makeText(this ,"�������" ,Toast.LENGTH_SHORT).show();
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
					Thread.sleep(10);
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
					Toast.makeText(this ,"overflow չ����ʾitemͼ���쳣" ,Toast.LENGTH_LONG).show();
				}
			}
		}

		return super.onMenuOpened(featureId ,menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu )
	{
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
		}
		return super.onOptionsItemSelected(item);
	}

	// ��д�����ؼ��˳�����
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

}