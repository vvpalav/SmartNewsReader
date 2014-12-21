package com.news2day.main;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.news2day.R;
import com.news2day.helpers.ServiceHandler;
import com.news2day.preference.Preferences;

public class MainActivity extends Activity {
	private int progress = 0;
	public static Context contextOfApplication;
	private SharedPreferences shared;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		contextOfApplication = getApplicationContext();
		shared = PreferenceManager.getDefaultSharedPreferences(contextOfApplication);
		final ProgressBar loading = (ProgressBar) findViewById(R.id.loadingBar);
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				if (progress < 100) {
					progress += 30;
					loading.setProgress((progress > 100) ? 100 : progress);
				} else {
					timer.cancel();
					timer.purge();
					Log.i("MainActivity", "starting home screen");
					//startActivity(new Intent(MainActivity.this,HomeScreenActivity.class));
					finish();
				}
			}
		};
		
		 if (shared.contains("is_user_reg") && shared.getBoolean("is_user_reg", false)) {
			Log.i("MainActivity", "pulling data from server");
			timer.schedule(task, 10, 700);
		} else if (shared.contains("is_user_accepted") && shared.getBoolean("is_user_accepted", false)){
			Log.i("MainActivity", "starting Registration screen");
			startActivity(new Intent(this, InputRegistrationCodeActivity.class));
			finish();
		} else {
			Log.i("MainActivity", "starting login screen");
	//		startActivity(new Intent(this, LoginActivity.class));
			startActivity(new Intent(this, NewsFeeds.class));
			finish();
		}
		
	}

	public static Context getContextOfApplication() {
		return contextOfApplication;
	}
}