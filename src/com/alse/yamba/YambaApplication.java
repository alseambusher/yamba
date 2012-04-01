package com.alse.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener{
	private boolean serviceRunning;
	private static final String TAG = YambaApplication.class.getSimpleName();
	public Twitter twitter;private SharedPreferences prefs;
	public boolean isServiceRunning(){
		return serviceRunning;
	}
	public void setServiceRunning(boolean serviceRunning){
		this.serviceRunning=serviceRunning;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		Log.i("alse2", "onCreated");
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i("alse2","onTerminated");
	}
	public synchronized Twitter getTwitter(){
		if(this.twitter==null){
			String username=this.prefs.getString("username", "");
			String password=this.prefs.getString("password", "");
			String apiroot=this.prefs.getString("apiroot", "http://yamba.marakana.com/api");
			Log.i("userinfo",username+" "+password+" "+apiroot);
			if((!TextUtils.isEmpty(username))&&(!TextUtils.isEmpty(password))&&(!TextUtils.isEmpty(apiroot))){
				this.twitter=new Twitter(username,password);
				this.twitter.setAPIRootUrl(apiroot);
			}
		}
		return this.twitter;
		
	}
	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		this.twitter=null;
		
	}

}
