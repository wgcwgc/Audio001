package com.runcom.wgcwgc.audio;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.audio01.R;
import com.runcom.wgcwgc.audioBean.MyAudio;

@SuppressLint("InflateParams")
public class MyListViewAdapter extends BaseAdapter
{
	public static MyAudio myAudio;
	public static List < MyAudio > audioList;

	Bundle savedInstanceState;
	LayoutInflater inflater;

	public MyListViewAdapter()
	{
	}

	public MyListViewAdapter(LayoutInflater inflater , Bundle savedInstanceState)
	{
		this.inflater = inflater;
		this.savedInstanceState = savedInstanceState;
	}

	@Override
	public int getCount()
	{
		return audioList.size();
	}

	@Override
	public Object getItem(int position )
	{
		return audioList.get(position);
	}

	@Override
	public long getItemId(int position )
	{
		return position;
	}

	@Override
	public View getView(final int position , View convertView , ViewGroup parent )
	{
		Holder holder;
		if(convertView == null)
		{
			// getLayoutInflater(savedInstanceState);
			convertView = inflater.inflate(R.layout.tab1_fragment_list_item ,null);
			holder = new Holder();
			holder.name = (TextView) convertView.findViewById(R.id.tab1_fragment_list_item_name);
			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}

		holder.name.setText(audioList.get(position).getName());

		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v )
			{
				// TODO
				Toast.makeText(inflater.getContext() ,"Äúµã»÷ÁË" + audioList.get(position).getName().toString() ,Toast.LENGTH_SHORT).show();
			}
		});

		return convertView;
	}

	class Holder
	{
		TextView id , name , data , source , link , other;
	}

	public static void initData()
	{
		audioList = new ArrayList < MyAudio >();
		for(int i = 0 ; i < 17 ; i ++ )
		{
			myAudio = new MyAudio();

			myAudio.setId("ID" + i);
			myAudio.setData("Data" + i);
			myAudio.setLink("Link" + i);
			myAudio.setName("name" + i);
			myAudio.setSource("source" + i);
			myAudio.setOther("other" + i);
			audioList.add(myAudio);
		}
	}

}

/*
 * import java.util.List;
 * 
 * import android.support.v4.view.PagerAdapter; import android.view.View; import
 * android.view.ViewGroup;
 * 
 * public class MyAdapter extends PagerAdapter {
 * 
 * private List < View > list;
 * 
 * public MyAdapter(List < View > list) { super(); this.list = list; }
 * 
 * @Override public int getCount() { return Integer.MAX_VALUE; }
 * 
 * @Override public boolean isViewFromObject(View arg0 , Object arg1 ) { return
 * arg0 == arg1; }
 * 
 * @Override public Object instantiateItem(ViewGroup container , int position )
 * { View child = list.get(position % list.size()); if(child.getParent() !=
 * null) { container.removeView(child); } container.addView(child); return
 * list.get(position % list.size()); }
 * 
 * @Override public void destroyItem(ViewGroup container , int position , Object
 * object ) { container.removeView(list.get(position % list.size())); }
 * 
 * }
 */
