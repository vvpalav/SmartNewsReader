package com.news2day.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.news2day.R;
import com.news2day.preference.Preferences;

public class NewsFeeds extends ActionBarActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feed);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 //Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_feed_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		 Handle action bar item clicks here. The action bar will
//		 automatically handle clicks on the Home/Up button, so long
//		 as you specify a parent activity in AndroidManifest.xml

		switch (item.getItemId()) {
		case R.id.aboutUs:
			Intent aIntent = new Intent(this, AboutUs.class);
			startActivity(aIntent);
			break;
		case R.id.preferences:
			Intent pIntent = new Intent(this, Preferences.class);
			startActivity(pIntent);
			break;
		case R.id.exit:
			finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public static class PlaceholderFragment extends Fragment {
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_news_feed,
					container, false);
			return rootView;
		}
	}
}
