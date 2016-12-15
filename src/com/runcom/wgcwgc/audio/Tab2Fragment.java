package com.runcom.wgcwgc.audio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.audioBean.MyAudio;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;
import com.umeng.analytics.MobclickAgent;

public class Tab2Fragment extends Fragment
{
	ListView listView;
	SimpleAdapter adapter;

	MyAudio myAudio = new MyAudio();
	ArrayList < MyAudio > audioList = new ArrayList < MyAudio >();

	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState )
	{
		View view = inflater.inflate(R.layout.fragment_tab2 ,container ,false);
		listView = (ListView) view.findViewById(R.id.fragment_tab2_listView);

		new GetThread_getList2().start();

		listView.setAdapter(new MyListViewAdapter(getContext() , inflater , savedInstanceState , audioList));

		return view;
	}

	class GetThread_getList2 extends Thread
	{

		public GetThread_getList2()
		{

		}

		@Override
		public void run()
		{

			String url = "http://172.16.0.63:8080/wgc/List00.jsp?type=1";
			HttpGet httpGet = new HttpGet(url);
			try
			{
				// HttpClient httpClient = new DefaultHttpClient();
				HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();
				HttpResponse response = httpClient.execute(httpGet);
				if(response.getStatusLine().getStatusCode() == 200)
				{
					HttpEntity entity = response.getEntity();

					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String line = "";
					String returnLine = "";
					while((line = reader.readLine()) != null)
					{
						returnLine += line;
					}
					JSONObject jsonObject = new JSONObject(returnLine);
					String audio = jsonObject.getString("audio");
					String lyric = jsonObject.getString("lyric");
					audioList.clear();
					for(int i = 0 ; i < 17 ; i ++ )
					{
						myAudio = new MyAudio();
						myAudio.setLyric(lyric);
						myAudio.setName(audio.substring(audio.lastIndexOf("/") + 1 ,audio.lastIndexOf(".")) + "_" + i);
						myAudio.setSource(audio);
						audioList.add(myAudio);
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Log.d("LOG" ,"bug");
			}
		};
	}

	@Override
	public void onResume()
	{
		super.onResume();
		MobclickAgent.onPageStart("Tab2Fragment");
	}

	@Override
	public void onPause()
	{
		super.onPause();
		MobclickAgent.onPageEnd("Tab2Fragment");
	}

}
