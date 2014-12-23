package com.news2day.database;

import java.util.ArrayList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.news2day.main.MainActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "SmartNewsReaderDB";

	// Table Names
	public static final String TABLE_NEWS_ITEM_INFO = "news_item_info";

	// NEWS_ITEM_INFO Table - column names
	public static final String NEWS_ITEM_INFO_ITEM_ID = "item_id";
	public static final String NEWS_ITEM_INFO_SOURCE_TITLE = "source_title";
	public static final String NEWS_ITEM_INFO_TITLE = "title";
	public static final String NEWS_ITEM_INFO_ABSTRACT = "abstract";
	public static final String NEWS_ITEM_INFO_TEXT = "text";
	public static final String NEWS_ITEM_INFO_URL = "url";
	public static final String NEWS_ITEM_INFO_DATETIME = "datetime";

	// table create statement NEWS_ITEM_INFO
	private static final String CREATE_TABLE_NEWS_ITEM_INFO = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NEWS_ITEM_INFO + "("
			+ NEWS_ITEM_INFO_ITEM_ID 		+ " INTEGER PRIMARY KEY,"
			+ NEWS_ITEM_INFO_SOURCE_TITLE 	+ " VARCHAR(50),"
			+ NEWS_ITEM_INFO_TITLE 			+ " TEXT,"
			+ NEWS_ITEM_INFO_ABSTRACT 		+ " TEXT,"
			+ NEWS_ITEM_INFO_TEXT 			+ " TEXT," 
			+ NEWS_ITEM_INFO_URL 			+ " TEXT,"
			+ NEWS_ITEM_INFO_DATETIME 		+ " DATETIME" + ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_NEWS_ITEM_INFO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS_ITEM_INFO);
		onCreate(db);
	}

	public long insertNewsitemInfo(JSONObject object) {
		try {
			Log.i("DataBase Helper", "Inserting title.. " + object.getString("title"));
			ContentValues values = new ContentValues();
			values.put(NEWS_ITEM_INFO_ITEM_ID, object.getString("item_id"));
			values.put(NEWS_ITEM_INFO_SOURCE_TITLE, object.getString("source_title"));
			values.put(NEWS_ITEM_INFO_TITLE, object.getString("title"));
			values.put(NEWS_ITEM_INFO_ABSTRACT, object.getString("abstract"));
			values.put(NEWS_ITEM_INFO_TEXT, object.getString("text"));
			values.put(NEWS_ITEM_INFO_URL, object.getString("url"));
			values.put(NEWS_ITEM_INFO_DATETIME, object.getString("datetime"));
			return this.getWritableDatabase().insert(TABLE_NEWS_ITEM_INFO, null, values);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public ArrayList<String> getUserSubscriptionList(){
		ArrayList<String> list = new ArrayList<String>();
		String sql = "select distinct(source_title) from news_item_info";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		try {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				list.add(cursor.getString(0));
			}
		} finally {
			cursor.close();
		}
		return list;
	}
	
	@SuppressLint("NewApi")
	public ArrayList<String> getRemainingListOfSourceTitle(){
		ArrayList<String> list = getUserSubscriptionList();
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MainActivity
				.getContextOfApplication());
		Set<String> set = shared.getStringSet("source_list", null);
		for(int i = 0; i < list.size(); i++){
			set.remove(list.get(i));
		}
		list.clear();
		for(Object str : set.toArray()){
			list.add((String)str);
		}
		return list;
	}
	
	public JSONArray getNewsItemsForTitle(String title){
		JSONArray array = new JSONArray();
		String sql = "select item_id, source_title, title, abstract, text, url, datetime" +
				" from news_item_info where source_title = '" + title + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		try {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				JSONObject json = new JSONObject();
				json.put("item_id",cursor.getString(0));
				json.put("source_title",cursor.getString(1));
				json.put("title",cursor.getString(2));
				json.put("abstract",cursor.getString(3));
				json.put("text",cursor.getString(4));
				json.put("url",cursor.getString(5));
				json.put("datetime",cursor.getString(6));
				array.put(json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return array;
	}
	
	public boolean deleteSourceSite(String title){
		return this.getWritableDatabase().delete(TABLE_NEWS_ITEM_INFO, 
				NEWS_ITEM_INFO_SOURCE_TITLE + "='" + title+"'", null) > 0;
	}
}
