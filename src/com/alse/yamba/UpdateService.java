package com.alse.yamba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("updater","logged");
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d("updater","logged");
		return START_STICKY;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("updater","destroyed");
	}

}
