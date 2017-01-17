package com.runcom.wgcwgc.main;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.runcom.wgcwgc.audio.MainActivity01;
import com.runcom.wgcwgc.audio01.R;

public class MainWelcome extends Activity
{
	ProgressBar progressBar;
	Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_welcom);

		progressBar = (ProgressBar) findViewById(R.id.login_welcome_progressBar);
		backButton = (Button) findViewById(R.id.login_welcome_cancelButton);
		final Intent newtIntent = new Intent(this , MainActivity01.class);
		final Intent previousIntent = new Intent(this , MainActivity.class);

		final Timer timer = new Timer();
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				MainWelcome.this.startActivity(newtIntent);
				MainWelcome.this.finish();
			}
		};
		timer.schedule(task ,1000 * 5);// 打死都不能删除的跳转

		backButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v )
			{
				timer.cancel();
				MainWelcome.this.startActivity(previousIntent);
				MainWelcome.this.finish();
			}
		});
	}
}
