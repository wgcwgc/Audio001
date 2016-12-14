package com.runcom.wgcwgc.record;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.record_draw.WaveCanvas;
import com.runcom.wgcwgc.record_util.U;
import com.runcom.wgcwgc.record_wavelibrary_utils.SamplePlayer;
import com.runcom.wgcwgc.record_wavelibrary_utils.SoundFile;
import com.runcom.wgcwgc.record_wavelibrary_view.WaveSurfaceView;
import com.runcom.wgcwgc.record_wavelibrary_view.WaveformView;
import com.runcom.wgcwgc.util.Util;
import com.umeng.analytics.MobclickAgent;

public class MyRecord extends Activity
{

	private static final int FREQUENCY = 16000;// ������Ƶ�����ʣ�44100��Ŀǰ�ı�׼������ĳЩ�豸��Ȼ֧��22050��16000��11025
	private static final int CHANNELCONGIFIGURATION = AudioFormat.CHANNEL_IN_MONO;// ���õ���������
	private static final int AUDIOENCODING = AudioFormat.ENCODING_PCM_16BIT;// ��Ƶ���ݸ�ʽ��ÿ������16λ
	public final static int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;// ��Ƶ��ȡԴ
	private int recBufSize;// ¼����Сbuffer��С
	private AudioRecord audioRecord;
	private WaveCanvas waveCanvas;
	private String mFileName = Util.recordFileName + new Random(57).toString().substring(17);// �ļ���

	WaveSurfaceView waveSfv;
	private Button switchButton;
	TextView status;
	WaveformView waveView;
	Button playButton;
	Button shareButton;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(com.runcom.wgcwgc.audio01.R.layout.recorder_main);

		ActionBar actionbar = getActionBar();
		// ��ʾ���ؼ�ͷĬ���ǲ���ʾ��
		actionbar.setDisplayHomeAsUpEnabled(false);
		// ��ʾ���ķ��ؼ�ͷ�����ҷ��ؼ�ͷ��titleһ�����ã����ؼ�ͷ������ʾ
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		// ��ʾ����
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" ¼�� ");

		waveSfv = (WaveSurfaceView) findViewById(R.id.wavesfv);
		switchButton = (Button) findViewById(R.id.switchButton);
		status = (TextView) findViewById(com.runcom.wgcwgc.audio01.R.id.status);
		waveView = (WaveformView) findViewById(R.id.waveview);
		playButton = (Button) findViewById(R.id.play);
		shareButton = (Button) findViewById(R.id.shareAudio);

		U.createDirectory();
		if(waveSfv != null)
		{
			waveSfv.setLine_off(42);
			// ���surfaceView��ɫ����Ч��
			waveSfv.setZOrderOnTop(true);
			waveSfv.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		}
		waveView.setLine_offset(42);

		switchButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v )
			{
				if(waveCanvas == null || !waveCanvas.isRecording)
				{
					status.setText("¼����...");
					switchButton.setText("ֹͣ¼��");
					waveSfv.setVisibility(View.VISIBLE);
					waveView.setVisibility(View.INVISIBLE);
					initAudio();
				}
				else
				{
					status.setText("ֹͣ¼��");
					switchButton.setText("��ʼ¼��");
					waveCanvas.Stop();
					waveCanvas = null;
					initWaveView();
				}
			}
		});

		playButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v )
			{
				onPlay(0);
			}
		});

		shareButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v )
			{
				Intent intent = new Intent(Intent.ACTION_SEND);

				// intent.setType("image/*");
				// intent.setType("media/*");
				// intent.setType("text/plain");

				intent.setType("audio/*");
				intent.putExtra(Intent.EXTRA_SUBJECT ,"Share");
				// intent.putExtra(Intent.EXTRA_TEXT
				// ,"I have successfully share my message through my app");
				String url = (U.DATA_DIRECTORY + mFileName + ".wav").toString();
				File file = new File(url);
				// Uri uri = Uri.parse(url);
				Uri uri = Uri.fromFile(file);

				// intent.setPackage("com.runcom.wgcwgc.audio");
				// public int getFft (byte[] fft);
				intent.putExtra(Intent.EXTRA_STREAM ,uri);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent ,"����"));
			}
		});
	}

	private void initWaveView()
	{
		loadFromFile();
	}

	File mFile;
	Thread mLoadSoundFileThread;
	SoundFile mSoundFile;
	boolean mLoadingKeepGoing;
	SamplePlayer mPlayer;

	/**
	 * ����wav�ļ���ʾ����
	 */
	private void loadFromFile()
	{
		try
		{
			Thread.sleep(300);// ���ļ�д����ɺ������벨�� �ʵ���������
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		mFile = new File(U.DATA_DIRECTORY + mFileName + ".wav");
		mLoadingKeepGoing = true;
		// Load the sound file in a background thread
		mLoadSoundFileThread = new Thread()
		{
			public void run()
			{
				try
				{
					mSoundFile = SoundFile.create(mFile.getAbsolutePath() ,null);
					if(mSoundFile == null)
					{
						return;
					}
					mPlayer = new SamplePlayer(mSoundFile);
				}
				catch(final Exception e)
				{
					e.printStackTrace();
					return;
				}
				if(mLoadingKeepGoing)
				{
					Runnable runnable = new Runnable()
					{
						public void run()
						{
							finishOpeningSoundFile();
							waveSfv.setVisibility(View.INVISIBLE);
							waveView.setVisibility(View.VISIBLE);
						}
					};
					MyRecord.this.runOnUiThread(runnable);
				}
			}
		};
		mLoadSoundFileThread.start();
	}

	float mDensity;

	/**
	 * waveview���벨�����
	 */
	private void finishOpeningSoundFile()
	{
		waveView.setSoundFile(mSoundFile);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mDensity = metrics.density;
		waveView.recomputeHeights(mDensity);
	}

	private void initAudio()
	{
		recBufSize = AudioRecord.getMinBufferSize(FREQUENCY ,CHANNELCONGIFIGURATION ,AUDIOENCODING);// ¼�����
		audioRecord = new AudioRecord(AUDIO_SOURCE ,// ָ����Ƶ��Դ������Ϊ��˷�
		FREQUENCY , // 16000HZ����Ƶ��
		CHANNELCONGIFIGURATION ,// ¼��ͨ��
		AUDIO_SOURCE ,// ¼�Ʊ����ʽ
		recBufSize);// ¼�ƻ�������С //���޸�
		waveCanvas = new WaveCanvas();
		waveCanvas.baseLine = waveSfv.getHeight() / 2;
		waveCanvas.Start(audioRecord ,recBufSize ,waveSfv ,mFileName ,U.DATA_DIRECTORY ,new Handler.Callback()
		{
			@Override
			public boolean handleMessage(Message msg )
			{
				return true;
			}
		});

	}

	private int mPlayStartMsec;
	private int mPlayEndMsec;
	private final int UPDATE_WAV = 100;

	/**
	 * ������Ƶ��@param startPosition ��ʼ���ŵ�ʱ��
	 */
	private synchronized void onPlay(int startPosition )
	{
		if(mPlayer == null)
			return;
		if(mPlayer != null && mPlayer.isPlaying())
		{
			mPlayer.pause();
			updateTime.removeMessages(UPDATE_WAV);
		}
		mPlayStartMsec = waveView.pixelsToMillisecs(startPosition);
		mPlayEndMsec = waveView.pixelsToMillisecsTotal();
		mPlayer.setOnCompletionListener(new SamplePlayer.OnCompletionListener()
		{
			@Override
			public void onCompletion()
			{
				waveView.setPlayback( -1);
				updateDisplay();
				updateTime.removeMessages(UPDATE_WAV);
				Toast.makeText(getApplicationContext() ,"�������" ,Toast.LENGTH_LONG).show();
			}
		});
		mPlayer.seekTo(mPlayStartMsec);
		mPlayer.start();
		Message msg = new Message();
		msg.what = UPDATE_WAV;
		updateTime.sendMessage(msg);
	}

	@SuppressLint("HandlerLeak")
	Handler updateTime = new Handler()
	{
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg )
		{
			updateDisplay();
			updateTime.sendMessageDelayed(new Message() ,10);
		}

		;
	};

	/**
	 * ����upd ateview �еĲ��Ž���
	 */
	private void updateDisplay()
	{
		int now = mPlayer.getCurrentPosition();// nullpointer2`1
		int frames = waveView.millisecsToPixels(now);
		waveView.setPlayback(frames);// ͨ��������µ�ǰ���ŵ�λ��
		if(now >= mPlayEndMsec)
		{
			waveView.setPlayFinish(1);
			if(mPlayer != null && mPlayer.isPlaying())
			{
				mPlayer.pause();
				updateTime.removeMessages(UPDATE_WAV);
			}
		}
		else
		{
			waveView.setPlayFinish(0);
		}
		waveView.invalidate();// ˢ�������ͼ
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
		// getMenuInflater().inflate(R.menu.main ,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item )
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		MobclickAgent.onPageStart("MyRecordScreen");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		MobclickAgent.onPageEnd("MyRecordScreen");
		MobclickAgent.onPause(this);
	}

}
