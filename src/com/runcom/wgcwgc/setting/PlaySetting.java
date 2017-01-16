package com.runcom.wgcwgc.setting;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.storage.MySharedPreferences;
import com.umeng.analytics.MobclickAgent;

public class PlaySetting extends Activity
{

	private Switch English_switch , Chinese_switch , All_swith;
	private int flag = 0;
	private final String sharedPreferencesSubtitleFlag = "subtitleShow";

	TextView setting_me_textView , setting_log_textView ,
	        setting_account_textView , setting_mark_textView;
	TextView setting_clearCache_textView , setting_opinion_textView ,
	        setting_checkUpdate_textView , setting_aboutUs_textView;
	ImageView setting_me_detail , setting_account_detail , setting_mark_detail ,
	        setting_log_detail;
	ImageView setting_clearCache_detail , setting_opinion_detail ,
	        setting_checkUpdate_detail , setting_aboutUs_detail;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_and_help);

		ActionBar actionbar = getActionBar();
		// 显示返回箭头默认是不显示的
		actionbar.setDisplayHomeAsUpEnabled(false);
		// int i;
		// 显示左侧的返回箭头，并且返回箭头和title一起设置，返回箭头才能显示
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		// 显示标题
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" 设置 ");

		InitSwitch();

	}

	private void InitSwitch()
	{
		// me
		setting_me_textView = (TextView) findViewById(R.id.setting_me_textView);
		setting_me_textView.setOnClickListener(listener);
		setting_me_detail = (ImageView) findViewById(R.id.setting_me_detail);
		setting_me_detail.setOnClickListener(listener);

		// account
		setting_account_textView = (TextView) findViewById(R.id.setting_account_textView);
		setting_account_textView.setOnClickListener(listener);
		setting_account_detail = (ImageView) findViewById(R.id.setting_account_detail);
		setting_account_detail.setOnClickListener(listener);

		setting_mark_textView = (TextView) findViewById(R.id.setting_mark_textView);
		setting_mark_textView.setOnClickListener(listener);
		setting_mark_detail = (ImageView) findViewById(R.id.setting_mark_detail);
		setting_mark_detail.setOnClickListener(listener);

		setting_log_textView = (TextView) findViewById(R.id.setting_log_textView);
		setting_log_textView.setOnClickListener(listener);
		setting_log_detail = (ImageView) findViewById(R.id.setting_log_detail);
		setting_log_detail.setOnClickListener(listener);

		// other setting
		setting_clearCache_textView = (TextView) findViewById(R.id.setting_clearCache_textView);
		setting_clearCache_textView.setOnClickListener(listener);
		setting_clearCache_detail = (ImageView) findViewById(R.id.setting_clearCache_detail);
		setting_clearCache_detail.setOnClickListener(listener);

		setting_opinion_textView = (TextView) findViewById(R.id.setting_opinion_textView);
		setting_opinion_textView.setOnClickListener(listener);
		setting_opinion_detail = (ImageView) findViewById(R.id.setting_opinion_detail);
		setting_opinion_detail.setOnClickListener(listener);

		setting_checkUpdate_textView = (TextView) findViewById(R.id.setting_checkUpdate_textView);
		setting_checkUpdate_textView.setOnClickListener(listener);
		setting_checkUpdate_detail = (ImageView) findViewById(R.id.setting_checkUpdate_detail);
		setting_checkUpdate_detail.setOnClickListener(listener);

		setting_aboutUs_textView = (TextView) findViewById(R.id.setting_aboutUs_textView);
		setting_aboutUs_textView.setOnClickListener(listener);
		setting_aboutUs_detail = (ImageView) findViewById(R.id.setting_aboutUs_detail);
		setting_aboutUs_detail.setOnClickListener(listener);

		// subtitleShow_switch
		English_switch = (Switch) findViewById(R.id.EnglishSwitch);
		Chinese_switch = (Switch) findViewById(R.id.ChineseSwitch);
		All_swith = (Switch) findViewById(R.id.AllSwitch);

		flag = MySharedPreferences.getValue(getApplicationContext() ,sharedPreferencesSubtitleFlag ,0);
		if(1 == flag)
		{
			English_switch.setEnabled(false);
			Chinese_switch.setEnabled(true);
			Chinese_switch.setChecked(true);
			All_swith.setEnabled(false);
		}
		else
			if(2 == flag)
			{
				English_switch.setEnabled(false);
				Chinese_switch.setEnabled(false);
				All_swith.setEnabled(true);
				All_swith.setChecked(true);
			}
			else
			{
				English_switch.setEnabled(true);
				English_switch.setChecked(true);
				Chinese_switch.setEnabled(false);
				All_swith.setEnabled(false);
			}

		English_switch.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView , boolean isChecked )
			{
				if(isChecked)
				{
					Chinese_switch.setEnabled(false);
					All_swith.setEnabled(false);
					flag = 0;
					MySharedPreferences.putValue(getApplicationContext() ,sharedPreferencesSubtitleFlag ,flag);
				}
				else
				{
					Chinese_switch.setEnabled(true);
					All_swith.setEnabled(true);
				}
			}
		});
		Chinese_switch.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView , boolean isChecked )
			{
				if(isChecked)
				{
					English_switch.setEnabled(false);
					All_swith.setEnabled(false);
					flag = 1;
					MySharedPreferences.putValue(getApplicationContext() ,sharedPreferencesSubtitleFlag ,flag);
				}
				else
				{
					English_switch.setEnabled(true);
					All_swith.setEnabled(true);

				}
			}
		});
		All_swith.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView , boolean isChecked )
			{
				if(isChecked)
				{
					English_switch.setEnabled(false);
					Chinese_switch.setEnabled(false);
					flag = 2;
					MySharedPreferences.putValue(PlaySetting.this ,"subtitleShow" ,flag);
				}
				else
				{
					English_switch.setEnabled(true);
					Chinese_switch.setEnabled(true);
				}
			}
		});

	}

	/**
	 * 
	 */
	OnClickListener listener = new OnClickListener()
	{
		@Override
		public void onClick(View v )
		{
			switch(v.getId())
			{
				case R.id.setting_me_textView:
					Toast.makeText(getApplicationContext() ,"me..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_me_detail:
					Toast.makeText(getApplicationContext() ,"me_detail..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_account_textView:
					Toast.makeText(getApplicationContext() ,"account..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_account_detail:
					Toast.makeText(getApplicationContext() ,"account_detail..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_mark_textView:
					Toast.makeText(getApplicationContext() ,"mark..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_mark_detail:
					Toast.makeText(getApplicationContext() ,"mark_detail..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_log_textView:
					Toast.makeText(getApplicationContext() ,"log..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_log_detail:
					Toast.makeText(getApplicationContext() ,"log_detail..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_clearCache_textView:
					Toast.makeText(getApplicationContext() ,"clearCache..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_clearCache_detail:
					Toast.makeText(getApplicationContext() ,"clearCache_detail..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_opinion_textView:
					Toast.makeText(getApplicationContext() ,"opinion..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_opinion_detail:
					Toast.makeText(getApplicationContext() ,"opinion_detail..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_checkUpdate_textView:
					Toast.makeText(getApplicationContext() ,"checkUpdate..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_checkUpdate_detail:
					Toast.makeText(getApplicationContext() ,"checkUpdate_detail..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_aboutUs_textView:
					Toast.makeText(getApplicationContext() ,"aboutUs..." ,Toast.LENGTH_SHORT).show();
					break;
				case R.id.setting_aboutUs_detail:
					Toast.makeText(getApplicationContext() ,"aboutUs_detail..." ,Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu )
	{
		// getMenuInflater().inflate(R.menu.audio_menu_local ,menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item )
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				finish();
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
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode ,event);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		// new MyAudio().setOther(String.valueOf(flag));
		MobclickAgent.onPageStart("PlaySettingScreen");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		MobclickAgent.onPageEnd("PlaySettingScreen");
		MobclickAgent.onPause(this);
	}
}
