package com.runcom.wgcwgc.main;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.storage.MySharedPreferences;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity
{
	private final String SharedPreferencesKey = "Login";
	private final String SharedPreferencesRememberPassword = "rememberPassword";
	private final String SharedPreferencesAutoLogin = "autoLogin";
	private final String SharedPreferencesAccount = "account";
	private final String SharedPreferencesPassword = "password";

	EditText editText_account , editText_password;
	String account , password;
	CheckBox rememberPassword , autoLogin;
	TextView textView_cancel;

	// private String TAG = "LOG";

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		editText_account = (EditText) findViewById(R.id.main_editText_account);
		editText_password = (EditText) findViewById(R.id.main_editText_password);

		rememberPassword = (CheckBox) findViewById(R.id.main_checkBox_rememberPassword);
		autoLogin = (CheckBox) findViewById(R.id.main_checkBox_auto_login);

		textView_cancel = (TextView) findViewById(R.id.main_textView_cancel);
		textView_cancel.setEnabled(false);
		initLogin();
	}

	private void initLogin()
	{
		editText_account.setText(MySharedPreferences.getValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesAccount ,""));
		if(MySharedPreferences.getValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesRememberPassword ,false))
		{
			editText_password.setText(MySharedPreferences.getValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesPassword ,""));
			rememberPassword.setChecked(true);
		}

		if(MySharedPreferences.getValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesAutoLogin ,false))
		{
			rememberPassword.setChecked(true);
			autoLogin.setChecked(true);

			if(judge(editText_account.getText().toString() ,editText_password.getText().toString()))
			{
				login();
			}
		}

		rememberPassword.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView , boolean isChecked )
			{
				MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesAccount ,editText_account.getText().toString());
				if(isChecked)
				{
					MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesRememberPassword ,true);
					MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesPassword ,editText_password.getText().toString());
					rememberPassword.setChecked(true);
				}
				else
				{
					MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesRememberPassword ,false);
					MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesPassword ,"");
					rememberPassword.setChecked(false);
				}
			}
		});

		autoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView , boolean isChecked )
			{
				MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesAccount ,editText_account.getText().toString());
				if(isChecked)
				{
					MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesPassword ,editText_password.getText().toString());
					MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesRememberPassword ,true);
					MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesAutoLogin ,true);
					autoLogin.setChecked(true);
					rememberPassword.setChecked(true);
				}
				else
				{
					MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesPassword ,"");
					MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesRememberPassword ,false);
					MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesAutoLogin ,false);
					autoLogin.setChecked(false);
					rememberPassword.setChecked(false);
				}
			}
		});

	}

	private void login()
	{
		final Intent intent = new Intent();
		intent.setClass(MainActivity.this ,MainWelcome.class);
		final TextView textView_hintShow = (TextView) findViewById(R.id.main_textView_show);
		textView_hintShow.setText("登录成功!!! 5秒后自动跳转...");
		textView_cancel.setText("取消");
		textView_cancel.setEnabled(true);
		final Timer timer = new Timer();
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				MainActivity.this.startActivity(intent);
				MainActivity.this.finish();
			}
		};
		timer.schedule(task ,1000 * 5);// 打死都不能删除的跳转
		textView_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v )
			{
				timer.cancel();
				textView_hintShow.setText("");
				textView_cancel.setText("");
				textView_cancel.setEnabled(false);
			}
		});
	}

	public void login(View view )
	{
		account = editText_account.getText().toString();
		password = editText_password.getText().toString();
		MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesAccount ,account);
		if(autoLogin.isChecked())
		{
			MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesPassword ,password);
			MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesRememberPassword ,true);
			MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesAutoLogin ,true);
		}
		else
		{
			MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesPassword ,"");
			MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesRememberPassword ,false);
			MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesAutoLogin ,false);
		}
		if(rememberPassword.isChecked())
		{
			MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesPassword ,password);
			MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesRememberPassword ,true);
		}
		else
		{
			MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesPassword ,"");
			MySharedPreferences.putValue(getApplicationContext() ,SharedPreferencesKey ,SharedPreferencesRememberPassword ,false);
		}
		if(judge(account ,password))
		{
			login();
			// Intent intent = new Intent();
			// intent.setClass(MainActivity.this ,MainWelcome.class);
			// startActivity(intent);
			// this.finish();
		}
		else
		{
			Toast.makeText(getApplicationContext() ,"账号或者密码错误!" ,Toast.LENGTH_SHORT).show();
		}
	}

	private boolean judge(String account , String password )
	{
		// TODO Auto-generated method stub
		if("123456".equals(account) && "123456".equals(password))
			return true;
		else
			return false;
	}

	public void register(View view )
	{
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext() ,"注册账号..." ,Toast.LENGTH_SHORT).show();
		// Intent intent = new Intent();
		// intent.setClass(MainActivity.this , MainActivity01.class);
		// startActivity(intent);
		// this.finish();

		// AudioManager mAudioManager = (AudioManager)
		// getSystemService(Context.AUDIO_SERVICE);
		// // 通话音量
		// int max =
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
		// int current =
		// mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
		// Log.d(TAG ,"VIOCE_CALL" + " max : " + max + " current : " + current);
		// // 系统音量
		// max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
		// current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
		// Log.d(TAG ,"SYSTEM" + " max : " + max + " current : " + current);
		// // 铃声音量
		// max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
		// current = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
		// Log.d(TAG ,"RING" + " max : " + max + " current : " + current);
		// // 音乐音量
		// max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		// Log.d(TAG ,"MUSIC" + " max : " + max + " current : " + current);
		// mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC ,0 ,0);
		// current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		// Log.d(TAG ,"MUSIC" + " max : " + max + " current : " + current);
		// // 提示声音音量
		// max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
		// current = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
		// Log.d(TAG ,"ALARM" + " max : " + max + " current : " + current);
	}

	@Override
	public boolean onKeyDown(int keyCode , KeyEvent event )
	{
		switch(keyCode)
		{
			case KeyEvent.KEYCODE_VOLUME_UP:
				((AudioManager) getSystemService(Service.AUDIO_SERVICE)).adjustStreamVolume(AudioManager.STREAM_MUSIC ,AudioManager.ADJUST_RAISE ,AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				((AudioManager) getSystemService(Service.AUDIO_SERVICE)).adjustStreamVolume(AudioManager.STREAM_MUSIC ,AudioManager.ADJUST_LOWER ,AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
				return true;
			default:
				break;
		}
		return super.onKeyDown(keyCode ,event);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		MobclickAgent.onPageStart("MainActivityScreen");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		MobclickAgent.onPageEnd("MainActivityScreen");
		MobclickAgent.onPause(this);
	}
}
