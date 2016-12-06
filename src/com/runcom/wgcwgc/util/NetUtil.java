package com.runcom.wgcwgc.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil
{
	public static final int NETWORN_NONE = 0;
	public static final int NETWORN_WIFI = 1;
	public static final int NETWORN_MOBILE = 2;

	@SuppressWarnings("deprecation")
	public static int getNetworkState(Context context )
	{
		// ��ȡ�������ӵķ���
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Wifi��״̬
		// CONNECTING ����, CONNECTED ����, SUSPENDED��ͣ, DISCONNECTING �Ͽ�,
		// DISCONNECTED �Ͽ�, UNKNOWN δ֪
		NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if(state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)
		{
			return NETWORN_WIFI;
		}

		// 2G/3G/4G��״̬
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if(state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)
		{
			return NETWORN_MOBILE;
		}
		return NETWORN_NONE;
	}
}
