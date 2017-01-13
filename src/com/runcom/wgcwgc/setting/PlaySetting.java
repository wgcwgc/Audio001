package com.runcom.wgcwgc.setting;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.runcom.wgcwgc.audio01.R;
import com.umeng.analytics.MobclickAgent;

public class PlaySetting extends Activity
{

	private Switch English_switch , Chinese_switch , All_swith;
	public int flag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_and_help);

		ActionBar actionbar = getActionBar();
		// ��ʾ���ؼ�ͷĬ���ǲ���ʾ��
		actionbar.setDisplayHomeAsUpEnabled(false);
		// ��ʾ���ķ��ؼ�ͷ�����ҷ��ؼ�ͷ��titleһ�����ã����ؼ�ͷ������ʾ
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		// ��ʾ����
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" ���� ");

		InitSwitch();

	}

	private void InitSwitch()
	{
		English_switch = (Switch) findViewById(R.id.EnglishSwitch);
		Chinese_switch = (Switch) findViewById(R.id.ChineseSwitch);
		All_swith = (Switch) findViewById(R.id.AllSwitch);

		English_switch.setEnabled(true);
		Chinese_switch.setEnabled(false);
		All_swith.setEnabled(false);

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
				}
				else
				{
					English_switch.setEnabled(true);
					Chinese_switch.setEnabled(true);

				}
			}
		});

	}

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

	// ��д�����ؼ��˳�����
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
