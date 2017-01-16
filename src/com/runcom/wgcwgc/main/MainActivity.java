package com.runcom.wgcwgc.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.runcom.wgcwgc.audio.MainActivity01;
import com.runcom.wgcwgc.audio01.R;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity
{

	EditText editText_account , editText_password;
	String account , password;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		editText_account = (EditText) findViewById(R.id.main_editText_account);
		editText_password = (EditText) findViewById(R.id.main_editText_password);
	}

	public void login(View view )
	{
		account = editText_account.getText().toString();
		password = editText_password.getText().toString();

		if(judge(account ,password))
		{
			Intent intent = new Intent();
			intent.setClass(MainActivity.this ,MainActivity01.class);
			startActivity(intent);
			this.finish();
		}
		else
		{
			Toast.makeText(getApplicationContext() ,"’À∫≈ªÚ’ﬂ√‹¬Î¥ÌŒÛ!" ,Toast.LENGTH_SHORT).show();
		}
	}

	private boolean judge(String account , String password )
	{
		// TODO Auto-generated method stub
		return true;
	}

	public void register(View view )
	{
		Intent intent = new Intent();
		intent.setClass(MainActivity.this ,MainActivity01.class);
		startActivity(intent);
		this.finish();
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
