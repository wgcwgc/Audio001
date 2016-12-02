package com.runcom.wgcwgc.util;

import android.os.Environment;

public class Util
{

	private static final String rootPath = Environment.getExternalStorageDirectory().toString();
	public static final String appPath = rootPath + "/&abc_record/";
	public static final String articlesPath = appPath + "articles/";
	public static final String audiosPath = appPath + "audios/";
	public static final String cachePath = appPath + "cache/";
	public static final String lyricsPath = appPath + "lyrics/";
	public static final String musicsPath = appPath + "musics/";

	public static final String localAudioListCacheName = "localAudioList.log";
	public static final String recordFileName = "wgcwgcRecord_";

}
