package com.news2day.main;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.news2day.R;
import com.news2day.database.DatabaseHelper;
import com.news2day.helpers.ServiceHandler;

public class AddSiteActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_site);
		loadRemainingSite();
	}
	
	@Override
	protected void onResume() {
		loadRemainingSite();
		super.onResume();
	}
	
	private void loadRemainingSite() {
		DatabaseHelper db = new DatabaseHelper(this);
		final ArrayList<String> remainingNewsList = db.getRemainingListOfSourceTitle();
		Log.d("remaining list", remainingNewsList.get(0));
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, remainingNewsList);
		
		ListView newsList = (ListView) findViewById(R.id.news_list);
		newsList.setAdapter(arrayAdapter);
		newsList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String title = remainingNewsList.get(position);
				final SharedPreferences shared = PreferenceManager
						.getDefaultSharedPreferences(MainActivity
								.getContextOfApplication());

				try {
					new ServiceHandler(AddSiteActivity.this)
							.startServices(new JSONObject()
									.put("telephone",
											shared.getString("telephone", null))
									.put("source_title", title)
									.put("transaction_type",
											"ADD_USER_SUBSCRIPTION").toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				final ProgressDialog progress = new ProgressDialog(AddSiteActivity.this);
				progress.setTitle("Fetching News Feeds");
				progress.setMessage("Wait while fetching feeds for " + title+ "...");
				progress.show();
				new Thread(new Runnable() {
					public void run() {
						while (true) {
							if (shared.contains("received_response")) {
								shared.edit().remove("received_response").commit();
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								progress.dismiss();
								progress.cancel();
								break;
							}
						}
						
						setResult(RESULT_OK, new Intent(AddSiteActivity.this, NewsFeeds.class));
						AddSiteActivity.this.finish();
					}
				}).start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_site, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED, new Intent(this, NewsFeeds.class));
		finish();
		super.onBackPressed();
	}
}
