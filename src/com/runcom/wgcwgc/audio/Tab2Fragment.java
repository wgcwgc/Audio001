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
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.audioBean.MyAudio;
import com.runcom.wgcwgc.util.NetUtil;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;
import com.umeng.analytics.MobclickAgent;

public class Tab2Fragment extends Fragment
{
	ListView listView;
	SimpleAdapter adapter;

	MyAudio myAudio = new MyAudio();
	ArrayList < MyAudio > audioList02 = new ArrayList < MyAudio >();
	private View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState )
	{
		// if(rootView == null)
		// {
		rootView = inflater.inflate(R.layout.fragment_tab2 ,container ,false);
		// }
		//
		// ViewGroup parent = (ViewGroup) rootView.getParent();
		// if(parent != null)
		// {
		// parent.removeView(rootView);
		// }

		listView = (ListView) rootView.findViewById(R.id.fragment_tab2_listView);

		if(NetUtil.getNetworkState(inflater.getContext()) == NetUtil.NETWORN_NONE)
		{
			// Toast.makeText(getContext() ,"Çë¼ì²éÍøÂçÁ¬½Ó"
			// ,Toast.LENGTH_SHORT).show();
			TextView emptyView = new TextView(getContext());
			emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT , LayoutParams.FILL_PARENT));
			emptyView.setText("\n\n\n\n\n\n\n\tThis appears when the network can not connect\n\t\tPlease cheak your network state!!!");
			// emptyView.setVisibility(View.GONE);
			((ViewGroup) listView.getParent()).addView(emptyView);
			listView.setEmptyView(emptyView);
			return rootView;
		}

		new GetThread_getList2().start();

		// audioList02.clear();
		// for(int i = 0 ; i < 17 ; i ++ )
		// {
		// myAudio = new MyAudio();
		// myAudio.setLyric("2_" + i);
		// myAudio.setName("2_" + i);
		// myAudio.setSource("2");
		// audioList02.add(myAudio);
		// }

		listView.setAdapter(new MyListViewAdapter(inflater , audioList02));
		// listView.setAdapter(new MyListViewAdapter(getContext() , inflater ,
		// savedInstanceState , audioList));

		return rootView;
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
					audioList02.clear();
					for(int i = 0 ; i < 17 ; i ++ )
					{
						myAudio = new MyAudio();
						myAudio.setLyric(lyric);
						myAudio.setName(audio.substring(audio.lastIndexOf("/") + 1 ,audio.lastIndexOf(".")) + "_" + i);
						myAudio.setSource(audio);
						audioList02.add(myAudio);
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
