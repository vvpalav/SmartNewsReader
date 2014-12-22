package com.news2day.main;

import com.news2day.R;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemClickListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NewsFeeds1 extends ActionBarActivity{
	private DrawerLayout drawerLayout;
	private ListView listView;
	private String[] news_lists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feed);
		news_lists=getResources().getStringArray(R.array.news_list_items);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		listView =(ListView) findViewById(R.id.drawerList);
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,news_lists));
//		listView.setOnItemClickListener(new OnItemClickListener(){
//			@Override
//			public void onItemClick(AdapterViewCompat<?> arg0, View arg1,
//					int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//	    });

	}


	public void onItemClick(AdapterViewCompat<?> arg0, View arg1, int position,
			long arg3) {
			Toast.makeText(this,news_lists[position]+" was selected", Toast.LENGTH_SHORT).show();
		
	}
	
}
