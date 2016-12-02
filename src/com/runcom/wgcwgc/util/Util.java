package com.runcom.wgcwgc.util;

import android.os.Environment;

public class Util
{

	public static final String rootPath = Environment.getExternalStorageDirectory() + "/&abc_record/";
	public static final String articlesPath = rootPath + "articles/";
	public static final String audiosPath = rootPath + "audios/";
	public static final String cachePath = rootPath + "cache/";
	public static final String lyricsPath = rootPath + "lyrics/";
	public static final String musicsPath = rootPath + "musics/";

	public static final String localAudioListCacheName = "localAudioList.log";
	public static final String recordFileName = "wgcwgcRecord_";

}
