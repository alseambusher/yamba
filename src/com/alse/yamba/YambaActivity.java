package com.alse.yamba;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.os.AsyncTask;
import android.preference.PreferenceManager;


public class YambaActivity extends Activity implements OnClickListener,TextWatcher{
	EditText et;Button b;Twitter twitter;TextView tv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        et=(EditText) findViewById(R.id.editText);
        et.addTextChangedListener(this);
        b=(Button) findViewById(R.id.buttonUpdate);
        b.setOnClickListener(this);
        tv=(TextView) findViewById(R.id.textView2);
        tv.setText(Integer.toString(140));
        tv.setTextColor(Color.GREEN);
    }
    class PostToTwitter extends AsyncTask<String, Integer, String>{
    	@Override
    	protected String doInBackground(String... statuses){
    		try{
    			//winterwell.jtwitter.Status status=twitter.updateStatus(statuses[0]);
    			winterwell.jtwitter.Status status=((YambaApplication)getApplication()).getTwitter().updateStatus(statuses[0]);
    			return status.text;
    		}
    		catch(TwitterException e){
    			Log.e("errors",e.toString());
    			e.printStackTrace();
    			return "failed to post";
    		}
    	}
    	@Override
    	protected void onProgressUpdate(Integer... values){
    		super.onProgressUpdate(values);
    	}
    	@Override
    	protected void onPostExecute(String result) { //
    		Toast.makeText(YambaActivity.this, result, Toast.LENGTH_LONG).show();
    		}
    }

	public void onClick(View v) {
		try{
			//getTwitter().setStatus(et.getText().toString());
			//twitter.setStatus(et.getText().toString());
			String status=et.getText().toString();
			new PostToTwitter().execute(status);
			Log.d("alse","clicked perfect");
		}
		catch(TwitterException e){
			Log.e("alse","onClicked "+e);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		int count=140-s.length();
		tv.setText(Integer.toString(count));
		tv.setTextColor(Color.GREEN);
		if(count<10)
			tv.setTextColor(Color.YELLOW);
		if(count<0)
			tv.setTextColor(Color.RED);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
				case R.id.itemServiceStart:
				startService(new Intent(this, UpdateService.class)); //
				break;
				case R.id.itemServiceStop:
				stopService(new Intent(this, UpdateService.class)); //
				break;
				case R.id.itemPrefs:
				startActivity(new Intent(this, PrefsActivity.class));
				break;
				default:break;
		}
		return true;
	}
}