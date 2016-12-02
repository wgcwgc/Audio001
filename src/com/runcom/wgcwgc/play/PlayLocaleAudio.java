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
	protected static final int SEARCH_MUSIC_SUCCESS = 0;// �����ɹ����
	protected static final int SEARCH_MUSIC_SUCCESS_FIRST = -1;// �״�����appĿ¼
	private SeekBar seekBar;
	private ListView listView;
	private ImageButton btnPlay;
	private TextView tv_currTime , tv_totalTime , tv_showName;
	private List < String > play_list = new ArrayList < String >(); // �����б�
	String [] ext =
	{ ".mp3", ".wav" };
	File file = Environment.getExternalStorageDirectory();// sd����Ŀ¼
	String filePath = "/&abc_record/";
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
		setContentView(R.layout.control_main);

		ActionBar actionbar = getActionBar();
		// ��ʾ���ؼ�ͷĬ���ǲ���ʾ��
		actionbar.setDisplayHomeAsUpEnabled(false);
		// ��ʾ���ķ��ؼ�ͷ�����ҷ��ؼ�ͷ��titleһ�����ã����ؼ�ͷ������ʾ
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		// ��ʾ����
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" ���ű�����Ƶ ");

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
			Toast.makeText(PlayLocaleAudio.this ,"������ⲿ�洢�豸..." ,Toast.LENGTH_LONG).show();
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
					// ���������ļ�����
					ma = new MusicListAdapter();
					listView.setAdapter(ma);
					pd.dismiss();
					break;
				case CURR_TIME_VALUE:
					// ���õ�ǰʱ��
					tv_currTime.setText(msg.obj.toString());
					break;
				// ��ʼ��listview
				case SEARCH_MUSIC_SUCCESS_FIRST:
					ma = new MusicListAdapter();
					listView.setAdapter(ma);
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
				Toast.makeText(PlayLocaleAudio.this ,"�����б�Ϊ��" ,Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(PlayLocaleAudio.this ,"��ǰ�Ѿ��ǵ�һ�׸�����" ,Toast.LENGTH_SHORT).show();
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
				Toast.makeText(PlayLocaleAudio.this ,"�����б�Ϊ��" ,Toast.LENGTH_SHORT).show();
			}
			else
				if(currIndex + 1 == play_list.size())
				{
					Toast.makeText(PlayLocaleAudio.this ,"��ǰ�Ѿ������һ�׸�����" ,Toast.LENGTH_SHORT).show();
					currIndex = -1;
					next();
				}
				else
				{
					currIndex = -1;
					next();
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
			Toast.makeText(PlayLocaleAudio.this ,"�����б�Ϊ��" ,Toast.LENGTH_SHORT).show();
		}
	}

	// ���Ű�ť
	public void playLocalAudio(View v )
	{
		play();
	}

	// ��һ�װ�ť
	public void previousLocalAudio(View v )
	{
		previous();
	}

	// ��һ�װ�ť
	public void nextLocalAudio(View v )
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
			tv_currTime.setText("00:00");
			Toast.makeText(PlayLocaleAudio.this ,"�����б�Ϊ��" ,Toast.LENGTH_SHORT).show();
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

			// �����������ֲ˵�
			case R.id.item_search:
				play_list.clear();
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					pd = ProgressDialog.show(PlayLocaleAudio.this ,"����" ,"�������������ļ�..." ,true);
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
					Toast.makeText(PlayLocaleAudio.this ,"������ⲿ�洢�豸..." ,Toast.LENGTH_LONG).show();
					System.out.println(Environment.getExternalStorageDirectory().toString());
				}

				break;
			// ��������б�˵�
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

	// ��д�����ؼ��˳�����
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
