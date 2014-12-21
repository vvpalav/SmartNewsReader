package com.news2day.database;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "SmartNewsReader";

	// Table Names
	public static final String TABLE_NEWS_SITES_INFO = "news_sites_info";
	public static final String TABLE_NEWS_ITEM_INFO = "news_item_info";
	public static final String TABLE_BEST_PLACES = "best_places";
	public static final String TABLE_CHECKLIST = "checklist";
	public static final String TABLE_EXPENSES = "expenses";
//---------------------------------------------------------------------------------------
	// NEWS SITE Table - column names
	public static final String NEWS_SITES_INFO_SOURCE_ID = "news_sites_info_sourceid";
	public static final String NEWS_SITES_INFO_TITLE = "news_sites_info_title";
	public static final String NEWS_SITES_INFO_APIKEY = "news_sites_info_apikey";
	
	// NEWS ITME Table - column names
		public static final String NEWS_ITEM_INFO_ITEM_ID = "news_item_info_itemid";
		public static final String NEWS_ITEM_INFO_SOURCE_ID = "news_item_info_sourceid";
		public static final String NEWS_ITEM_INFO_TITLE = "news_item_info_title";
		public static final String NEWS_ITEM_INFO_ABSTRACT = "news_item_info_abstract";
		public static final String NEWS_ITEM_INFO_TEXT = "news_item_info_text";
		public static final String NEWS_ITEM_INFO_DATETIME = "news_item_info_title";
	
   //------------------------------------------------------------------------------------
		
	
	
	
	
	// create statement NEWS SITES INFO
	
	private static final String CREATE_TABLE_NEWS_SITES_INFO = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NEWS_SITES_INFO + "("
			+ NEWS_SITES_INFO_SOURCE_ID + " INTEGER PRIMARY KEY,"
			+ NEWS_SITES_INFO_TITLE + " TEXT PRIMARY KEY,"
			+ NEWS_SITES_INFO_APIKEY + " TEXT" + ")";
			
			

	// table create statement NEWS ITEM INFO
	private static final String CREATE_TABLE_NEWS_ITEM_INFO = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NEWS_ITEM_INFO + "("
			+ NEWS_ITEM_INFO_ITEM_ID + " INTEGER PRIMARY KEY,"
			+ NEWS_ITEM_INFO_SOURCE_ID + " INTEGER REFERENCES " + TABLE_NEWS_SITES_INFO + "(" + NEWS_ITEM_INFO_SOURCE_ID + "),"
			+ NEWS_ITEM_INFO_TITLE + " TEXT,"
			+ NEWS_ITEM_INFO_ABSTRACT + " TEXT,"
			+ NEWS_ITEM_INFO_TEXT + " TEXT,"
			+ NEWS_ITEM_INFO_DATETIME + " DATETIME" + ")";

		public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_NEWS_SITES_INFO);
		db.execSQL(CREATE_TABLE_NEWS_ITEM_INFO);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS_SITES_INFO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS_ITEM_INFO);
		onCreate(db);
	}

	/*
	 * Creating a trip
	 */
	public long insertNewsSiteInfo(JSONObject object) {
		try {
			ContentValues values = new ContentValues();
			values.put(NEWS_SITES_INFO_SOURCE_ID, object.getLong("news_sites_info_sourceid"));
			values.put(NEWS_SITES_INFO_TITLE, object.getString("news_sites_info_title"));
			values.put(NEWS_SITES_INFO_APIKEY, object.getString("news_sites_info_apikey"));
			
			return this.getWritableDatabase().insert(TABLE_NEWS_SITES_INFO, null, values);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	
	
	public long insertNewsitemInfo(JSONObject object) {
		try {
			ContentValues values = new ContentValues();
			values.put(NEWS_ITEM_INFO_ITEM_ID, object.getLong("news_item_info_itemid"));
			values.put(NEWS_ITEM_INFO_SOURCE_ID, object.getString("news_item_info_sourceid"));
			values.put(NEWS_ITEM_INFO_TITLE, object.getString("news_item_info_title"));
			values.put(NEWS_ITEM_INFO_ABSTRACT, object.getLong("news_item_info_abstract"));
			values.put(NEWS_ITEM_INFO_TEXT, object.getString("news_item_info_text"));
			values.put(NEWS_ITEM_INFO_DATETIME, object.getString("news_item_info_title"));
			
			return this.getWritableDatabase().insert(TABLE_NEWS_ITEM_INFO, null, values);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}


	public void deleteNewsItem(long sourceID) {
		this.getWritableDatabase().delete(TABLE_NEWS_ITEM_INFO, NEWS_ITEM_INFO_SOURCE_ID + "=" + sourceID, null);
		this.getWritableDatabase().delete(TABLE_NEWS_SITES_INFO, NEWS_SITES_INFO_SOURCE_ID + "=" + sourceID, null);
		
	}
	
//	public String getUserName(String phoneNumber) {
//		Log.i("dbhelper_tel", phoneNumber);
//		String selectQuery = "SELECT  distinct("+KEY_NAME+") FROM "
//				+ TABLE_TRIP_MEMBERS + " where " + KEY_TELEPHONE + " = " + phoneNumber;
//		Log.i("executing", selectQuery);
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cursor = db.rawQuery(selectQuery, null);
//		String userName = "";
//		try {
//			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//				userName = cursor.getString(0);
//			}
//			Log.i("dbhelper_name", userName);
//			return userName;
//		} finally {
//			cursor.close();
//		}
//	}
//	
}
