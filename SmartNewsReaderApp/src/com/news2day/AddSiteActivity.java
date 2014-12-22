package com.news2day;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.news2day.database.DatabaseHelper;
import com.news2day.helpers.ServiceHandler;
import com.news2day.main.MainActivity;

import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddSiteActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_site);
	loadRemainingSite();
	
	}

	private void loadRemainingSite() {
		DatabaseHelper db = new DatabaseHelper(this);
		
		ListView newsList=(ListView)findViewById(R.id.news_list);
		final ArrayList<String> remainingNewsList = db.getRemainingListOfSourceTitle();
		Log.d("remaining list",remainingNewsList.get(0));
		ArrayAdapter<String> arrayAdapter =      
		         new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, remainingNewsList);
		         // Set The Adapter
		         newsList.setAdapter(arrayAdapter); 
		         
		         // register onClickListener to handle click events on each item
		         newsList.setOnItemClickListener(new OnItemClickListener()
		            {
		                     // argument position gives the index of item which is clicked
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
								String title = remainingNewsList.get(position);
								SharedPreferences shared = PreferenceManager
										.getDefaultSharedPreferences(MainActivity
												.getContextOfApplication());
								
								try {
									new ServiceHandler(AddSiteActivity.this).startServices(new JSONObject()
											.put("telephone",shared.getString("telephone", null))
											.put("source_title",title)
											.put("transaction_type", "ADD_USER_SUBSCRIPTION").toString());
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
		            });
				
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_site, menu);
		return true;
	}
	


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
