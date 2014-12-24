package com.news2day.main;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.news2day.R;
import com.news2day.database.DatabaseHelper;
import com.news2day.helpers.ServiceHandler;

public class ListSiteActivity extends ActionBarActivity {
	DatabaseHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_site);
		loadMySites();
	}
	
	private void loadMySites() {
		db = new DatabaseHelper(this);

		final ListView newsList = (ListView) findViewById(R.id.news_mylist);
		final ArrayList<String> remainingNewsList = db.getUserSubscriptionList();
		
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, remainingNewsList);
		newsList.setAdapter(arrayAdapter);
		newsList.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				final String line = (String) newsList.getItemAtPosition(position).toString();
				AlertDialog.Builder builder = new AlertDialog.Builder(ListSiteActivity.this);			
				builder.setMessage("Please make a choice for " + line);
				builder.setPositiveButton("Switch", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(ListSiteActivity.this, NewsFeeds.class);
						intent.putExtra("title", line);
						setResult(RESULT_OK, intent);
						finish();
					}
				});
				builder.setNegativeButton("Delete", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						try {
							final SharedPreferences shared = PreferenceManager
									.getDefaultSharedPreferences(MainActivity
											.getContextOfApplication());
							Log.i("ListSiteActivity", "Delete status " + db.deleteSourceSite(line));
							new ServiceHandler(ListSiteActivity.this)
									.startServices(new JSONObject()
											.put("telephone", shared.getString("telephone", null))
											.put("source_title", line)
											.put("transaction_type", "DELETE_USER_SUBSCRIPTION")
											.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Intent intent = new Intent(ListSiteActivity.this, NewsFeeds.class);
						setResult(2, intent);
						finish();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();	
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED, new Intent(this, NewsFeeds.class));
		finish();
		super.onBackPressed();
	}
}
	
	
	
	
