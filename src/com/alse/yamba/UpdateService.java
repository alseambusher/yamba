package com.alse.yamba;

import java.util.List;

import winterwell.jtwitter.Status;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.TwitterList;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class UpdateService extends Service {
	static final int DELAY = 30000;
	private boolean runFlag = false;
	private Updater updater;
	private YambaApplication yamba;
	DbHelper dbhelper;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		this.yamba=(YambaApplication) getApplication();
		this.updater=new Updater();
		dbhelper=new DbHelper(this);
		Log.d("updater","logged");
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		this.runFlag=true;
		this.updater.start();
		this.yamba.setServiceRunning(true);
		Log.d("updater","logged");
		return START_STICKY;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.updater.interrupt();
		this.updater=null;
		this.yamba.setServiceRunning(false);
		Log.d("updater","destroyed");
	}
	private class Updater extends Thread{
		List<Status> timeline;
		public Updater() {
			super("Updater-service updater");
		}
		@Override
		public void run() {
			UpdateService updateServ =UpdateService.this;
			while(updateServ.runFlag){
				Log.d("updater","updater running");
				try{
					try{
						timeline=yamba.getTwitter().getFriendsTimeline();
						//timeline=yamba.getTwitter().getPublicTimeline();
					}
					catch(TwitterException e){
						Log.e("service","failed to get from timeline",e);
					}
					SQLiteDatabase db = dbhelper.getWritableDatabase();
					ContentValues values=new ContentValues();
					for(Status status:timeline){
						values.clear();
						values.put(DbHelper.C_ID,status.id.toString());
						values.put(DbHelper.C_CREATED_AT, status.createdAt.getTime());
						values.put(DbHelper.C_SOURCE, status.source);
						values.put(DbHelper.C_TEXT, status.text);
						values.put(DbHelper.C_USER, status.user.name);
						try{
						db.insertOrThrow(DbHelper.TABLE, null, values);
						}
						catch (SQLException e) {
							Log.e("updater","cant insert to db");
						}
						Log.d("status update",String.format("%s,%s",status.user.name,status.text));
					}
					db.close();
					Log.d("updater","updater ran");
					Thread.sleep(DELAY);
				}
				catch(InterruptedException e){
					updateServ.runFlag=false;
				}
			}
			super.run();
		}
		
	}

}
