package com.runcom.wgcwgc.audio;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab2Fragment extends Fragment
{

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState )
	{
		View view = inflater.inflate(R.layout.fragment_tab2 ,null);
		return view;
	}

}
