package com.news2day.main;

import com.news2day.R;
import com.news2day.preference.Preferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class NewsFeeds extends ActionBarActivity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feed);
	}
	
	public static class PlaceholderFragment extends Fragment{
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml
		
		switch(item.getItemId()){
		case R.id.aboutUs:
			Intent aIntent = new Intent(this,AboutUs.class);
			startActivity(aIntent);
			break;
		case R.id.preferences:
			Intent pIntent = new Intent(this,Preferences.class);
			startActivity(pIntent);
			break;
		case R.id.exit:
			finish();
			break;
		}
			

		return super.onOptionsItemSelected(item);
	}
	

}
