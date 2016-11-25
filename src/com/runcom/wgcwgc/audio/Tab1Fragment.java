package com.runcom.wgcwgc.audio;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.runcom.wgcwgc.audio01.R;

@SuppressLint("InflateParams")
public class Tab1Fragment extends Fragment
{
	public ListView listView;
	SimpleAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState )
	{
		View view = inflater.inflate(R.layout.fragment_tab1 ,container ,false);
		listView = (ListView) view.findViewById(R.id.fragment_tab1_listView);
		MyListViewAdapter.initData();
		listView.setAdapter(new MyListViewAdapter(getContext() , inflater , savedInstanceState));
		return view;
	}

}
