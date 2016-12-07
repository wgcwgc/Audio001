package com.runcom.wgcwgc.audio;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.audioBean.MyAudio;

public class Tab3Fragment extends Fragment
{
	ListView listView;
	SimpleAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState )
	{
		View view = inflater.inflate(R.layout.fragment_tab3 ,container ,false);
		listView = (ListView) view.findViewById(R.id.fragment_tab3_listView);

//		MyListViewAdapter.initData(3);
		
		MyAudio myAudio = new MyAudio();
		ArrayList < MyAudio > audioList = new ArrayList < MyAudio >() ;
		for(int i = 34 ; i < 51 ; i ++ )
		{
			// TODO
			// new InitData(flag).initData();
			myAudio = new MyAudio();
			myAudio.setId("ID" + i);
			myAudio.setLyric("Data" + i);
			myAudio.setLink("Link" + i);
			myAudio.setName("�춹" + i);
			myAudio.setSource("source" + i);
			audioList.add(myAudio);
		}
		
		listView.setAdapter(new MyListViewAdapter(getContext() , inflater , savedInstanceState , audioList));
		
		
//		listView.setAdapter(new MyListViewAdapter(getContext() , inflater , savedInstanceState , 3));

		return view;
	}

}
