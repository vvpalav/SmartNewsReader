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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.news2day.R;
import com.news2day.database.DatabaseHelper;
import com.news2day.preference.Preferences;

public class NewsFeeds extends ActionBarActivity {
	private ArrayList<String> newsFeedsList;
	private HashMap<String, JSONObject> hashMap;
	private DatabaseHelper dbhelper;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_feed_list);
		dbhelper = new DatabaseHelper(this);
		ArrayList<String> ll = dbhelper.getUserSubscriptionList();
		if (ll.size() > 0) {
			newsFeedsList(ll.get(0));
		} else newsFeedsList(null);
	}

	public void newsFeedsList(String title) {
		hashMap = new HashMap<String, JSONObject>();
		newsFeedsList = new ArrayList<String>();
		setTitle("Smart News Reader");
		if (title != null && title.length() > 0) {
			setTitle("News Feed: " + title);
			JSONArray array = dbhelper.getNewsItemsForTitle(title);
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject json = array.getJSONObject(i);
					hashMap.put(json.getString("title"), json);
					newsFeedsList.add(json.getString("title"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, newsFeedsList);
				
		ListView newsList = (ListView) findViewById(R.id.news_list);
		newsList.setAdapter(arrayAdapter);
		newsList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startDetailActivity(arrayAdapter, position);
			}
		});
	}

	public void startDetailActivity(ArrayAdapter<String> arrayAdapter,
			int position) {
		JSONObject json = hashMap.get(arrayAdapter.getItem(position));
		Intent intent = new Intent(this, News_Detail_Activity.class);
		intent.putExtra("json", json.toString());
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.news_feed_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

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
			startActivityForResult(new Intent(this, AddSiteActivity.class), 1);
			break;
		case R.id.mysite:
			startActivityForResult(new Intent(this, ListSiteActivity.class), 2);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int request, int response, Intent intent) {
		Log.i("NewsFeeds", "onActivityResult with RequestCode: " + request + " ResponseCode: " + response);
		if(response == RESULT_CANCELED)
			return;
		
		ArrayList<String> array = dbhelper.getUserSubscriptionList();
		if (request == 2 && response == RESULT_OK) {
			String title = intent.getExtras().getString("title");
			newsFeedsList(title);
		} else if (array.size() > 0) {
			newsFeedsList(array.get(0));
		} else {
			newsFeedsList(null);
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
