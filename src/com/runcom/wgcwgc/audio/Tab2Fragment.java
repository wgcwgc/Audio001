package com.runcom.wgcwgc.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.runcom.wgcwgc.audio01.R;

public class Tab2Fragment extends Fragment
{
	ListView listView;
	SimpleAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState )
	{
		View view = inflater.inflate(R.layout.fragment_tab2 ,container ,false);
		listView = (ListView) view.findViewById(R.id.fragment_tab2_listView);

		MyListViewAdapter.initData();
		listView.setAdapter(new MyListViewAdapter(getContext() , inflater , savedInstanceState , 2));

		// init();
		return view;
	}

	public void init()
	{
		String [] listItem =
		{ "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "a", "b", "c", "d", "e", "f", "g", "h", "i" };
		adapter = new SimpleAdapter(getActivity() , getData(listItem) , R.layout.tab2_fragment_list_item , new String []
		{ "name" } , new int []
		{ R.id.tab2_fragment_list_item_name });
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView < ? > arg0 , View arg1 , int arg2 , long arg3 )
			{
				Log.d("LOG" ,"asdf: " + arg2);
			}

		});
	}

	private List < ? extends Map < String , ? >> getData(String [] strs )
	{
		List < Map < String , Object >> list = new ArrayList < Map < String , Object >>();

		for(int i = 0 ; i < strs.length ; i ++ )
		{
			Map < String , Object > map = new HashMap < String , Object >();
			map.put("name" ,strs[i]);
			list.add(map);
		}
		return list;
	}
}
