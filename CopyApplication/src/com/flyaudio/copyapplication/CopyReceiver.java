package com.flyaudio.copyapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CopyReceiver extends BroadcastReceiver {
	public static final String SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE";

	private final static String TAG = "CopyReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(SECRET_CODE_ACTION)) {
			Flog.d(TAG, "onReceive---------SECRET_CODE_ACTION");

			Intent i = new Intent(Intent.ACTION_MAIN);
			i.setClass(context, CopyActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}

	}
}
