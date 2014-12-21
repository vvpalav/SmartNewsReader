//package com.news2day.main;
//
//import java.util.ArrayList;
//
//import android.app.FragmentManager;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.content.res.TypedArray;
//import android.os.Bundle;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//
//import com.news2day.R;
//import com.news2day.fragments.HomeFragment;
//import com.news2day.models.NewsSourceList;
//import com.news2day.preference.Preferences;
//
//public class NewsFeed extends ActionBarActivity {
////    private DrawerLayout mDrawerLayout;
////    private ListView mDrawerList;
////    private ActionBarDrawerToggle mDrawerToggle;
//// 
////    // nav drawer title
////    private CharSequence mDrawerTitle;
//// 
////    // used to store app title
////    private CharSequence mTitle;
//// 
////    // slide menu items
////    private String[] navMenuTitles;
////    private TypedArray navMenuIcons;
//// 
////    private ArrayList<NewsSourceList> newsSourceList;
////    private NewsSourceListAdapter adapter;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_news_feed);
//		
////			mTitle = mDrawerTitle = getTitle();
////	       
////	        // load slide menu items
////	        navMenuTitles = getResources().getStringArray(R.array.news_list_items);
////	 
////	        // nav drawer icons from resources
////	        navMenuIcons = getResources().obtainTypedArray(R.array.news_list_icons);
////	 
////	        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
////	        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
////	 
////	        newsSourceList = new ArrayList<NewsSourceList>();
////	 
////	        // adding nav drawer items to array
////	        // Home
////	        newsSourceList.add(new NewsSourceList(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
////	        // Find People
////	        newsSourceList.add(new NewsSourceList(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
////	        // Photos
////	        newsSourceList.add(new NewsSourceList(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
////	        // Communities, Will add a counter here
////	        newsSourceList.add(new NewsSourceList(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
////	        // Pages
////	        newsSourceList.add(new NewsSourceList(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
////	        // What's hot, We  will add a counter here
////	        newsSourceList.add(new NewsSourceList(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
////	         
////	        System.out.println(newsSourceList.size());
////	        System.out.println(newsSourceList.get(0).getSourceImage()+ "  "+ newsSourceList.get(0).getSourceTitle());
////	        System.out.println(getApplicationContext());
////	        
////	        // Recycle the typed array
////	        navMenuIcons.recycle();
////	 
////	        // setting the nav drawer list adapter
////	        adapter = new NewsSourceListAdapter(getApplicationContext(),newsSourceList);
////	        System.out.println("adapter" + adapter);
////	        mDrawerList.setAdapter(adapter);
////	        
////	        // enabling action bar app icon and behaving it as toggle button
////	        getActionBar().setDisplayHomeAsUpEnabled(true);
////	        getActionBar().setHomeButtonEnabled(true);
////	 
////	        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
////	                R.string.app_name, // nav drawer open - description for accessibility
////	                R.string.app_name // nav drawer close - description for accessibility
////	        ){
////	            public void onDrawerClosed(View view) {
////	                getActionBar().setTitle(mTitle);
////	                // calling onPrepareOptionsMenu() to show action bar icons
////	                invalidateOptionsMenu();
////	            }
////	 
////	            public void onDrawerOpened(View drawerView) {
////	                getActionBar().setTitle(mDrawerTitle);
////	                // calling onPrepareOptionsMenu() to hide action bar icons
////	                invalidateOptionsMenu();
////	            }
////	        };
////	        mDrawerLayout.setDrawerListener(mDrawerToggle);
////	 
////	        if (savedInstanceState == null) {
////	            // on first time display view for first nav item
////	            displayView(0);
////	        }
//		
//		
//
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.news_feed, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
////		if (mDrawerToggle.onOptionsItemSelected(item)) {
////			return true;
////		}
//		
//		switch(item.getItemId()){
//		case R.id.aboutUs:
//			Intent aIntent = new Intent(this,AboutUs.class);
//			startActivity(aIntent);
//			break;
//		case R.id.preferences:
//			Intent pIntent = new Intent(this,Preferences.class);
//			startActivity(pIntent);
//			break;
//		case R.id.exit:
//			finish();
//			break;
//		}
//			
//
//		return super.onOptionsItemSelected(item);
//	}
//	
//	public void signUpUser(View V){
//		//Intent intent = new Intent(this,SignUpActivity.class);
//		//startActivity(intent);
//	}
//	
//	public void signInUser(View V){
//		//Intent intent = new Intent(this,SignUpActivity.class);
//		//startActivity(intent);
//	}
//	
//	/***
//     * Called when invalidateOptionsMenu() is triggered
//     */
////    @Override
////    public boolean onPrepareOptionsMenu(Menu menu) {
////        // if nav drawer is opened, hide the action items
////        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
////        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
////        return super.onPrepareOptionsMenu(menu);
////    }
// 
//    /**
//     * Diplaying fragment view for selected nav drawer list item
//     * */
////    private void displayView(int position) {
////        // update the main content by replacing fragments
////        HomeFragment fragment = null;
////        switch (position) {
////        case 0:
////            fragment = new HomeFragment();
////            break;
////        case 1:
////            fragment = new HomeFragment();
////            break;
////        case 2:
////            fragment = new HomeFragment();
////            break;
////        case 3:
////            fragment = new HomeFragment();
////            break;
////        case 4:
////            fragment = new HomeFragment();
////            break;
////        case 5:
////            fragment = new HomeFragment();
////            break;
//// 
////        default:
////            break;
////        }
//// 
////        if (fragment != null) {
////            FragmentManager fragmentManager = getFragmentManager();
////            fragmentManager.beginTransaction()
////                    .replace(R.id.frame_container, fragment).commit();
//// 
////            // update selected item and title, then close the drawer
////            mDrawerList.setItemChecked(position, true);
////            mDrawerList.setSelection(position);
////            setTitle(navMenuTitles[position]);
////            mDrawerLayout.closeDrawer(mDrawerList);
////        } else {
////            // error in creating fragment
////            Log.e("NewsFeeds", "Error in creating fragment");
////        }
////    }
// 
////    @Override
////    public void setTitle(CharSequence title) {
////        mTitle = title;
////        getActionBar().setTitle(mTitle);
////    }
//// 
////    /**
////     * When using the ActionBarDrawerToggle, you must call it during
////     * onPostCreate() and onConfigurationChanged()...
////     */
//// 
////    @Override
////    protected void onPostCreate(Bundle savedInstanceState) {
////        super.onPostCreate(savedInstanceState);
////        // Sync the toggle state after onRestoreInstanceState has occurred.
////        mDrawerToggle.syncState();
////    }
//// 
////    @Override
////    public void onConfigurationChanged(Configuration newConfig) {
////        super.onConfigurationChanged(newConfig);
////        // Pass any configuration change to the drawer toggls
////        mDrawerToggle.onConfigurationChanged(newConfig);
////    }
//
//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_news_feed,
//					container, false);
//			return rootView;
//		}
//	}
//
//}
