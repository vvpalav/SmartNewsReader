package com.news2day.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.news2day.AddSiteActivity;
import com.news2day.R;
import com.news2day.database.DatabaseHelper;
import com.news2day.preference.Preferences;

public class NewsFeeds extends ActionBarActivity {
	ArrayList<String> newsFeedsList = new ArrayList<String>();
	HashMap<String, JSONObject> hashMap = new HashMap<String, JSONObject>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.news_feed_list);
		ListView newsList = (ListView) findViewById(R.id.news_list);
		DatabaseHelper dbhelper = new DatabaseHelper(this);
		
		JSONArray array = dbhelper.getNewsItemsForTitle("New York Times");
		for (int i = 0; i < array.length(); i++) {
			JSONObject json;
			try {
				json = array.getJSONObject(i);
				hashMap.put(json.getString("title"), json);
				newsFeedsList.add(json.getString("title"));
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, newsFeedsList);
		newsList.setAdapter(arrayAdapter);
		newsList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {	
				startDetailActivity(arrayAdapter,position);
				
			}
		});
	}
	
	public void startDetailActivity(ArrayAdapter<String> arrayAdapter, int position){
		JSONObject json = hashMap.get(arrayAdapter.getItem(position));
		Intent intent = new Intent();
		intent.putExtra("json", json.toString());
		startActivity(new Intent(this, News_Detail_Activity.class));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_feed_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml

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
			
		case R.id.site:
			startActivityForResult(new Intent(this, AddSiteActivity.class),1);
		}

		return super.onOptionsItemSelected(item);
	}

	
	@Override
	
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if(arg0==1) {
			
		}
		
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
