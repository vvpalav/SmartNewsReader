package com.news2day.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.news2day.main.MainActivity;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "SmartNewsReaderDB";

	// Table Names
	public static final String TABLE_NEWS_ITEM_INFO = "news_item_info";

	// NEWS_ITEM_INFO Table - column names
	public static final String NEWS_ITEM_INFO_ITEM_ID = "item_id";
	public static final String NEWS_ITEM_INFO_SOURCE_TITLE = "source_title";
	public static final String NEWS_ITEM_INFO_TITLE = "title";
	public static final String NEWS_ITEM_INFO_ABSTRACT = "abstract";
	public static final String NEWS_ITEM_INFO_URL = "url";
	public static final String NEWS_ITEM_INFO_DATETIME = "datetime";
	public static final String NEWS_ITEM_INFO_ARCHIVE = "archive_on_date";

	// table create statement NEWS_ITEM_INFO
	private static final String CREATE_TABLE_NEWS_ITEM_INFO = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NEWS_ITEM_INFO + "("
			+ NEWS_ITEM_INFO_ITEM_ID 		+ " INTEGER PRIMARY KEY,"
			+ NEWS_ITEM_INFO_SOURCE_TITLE 	+ " VARCHAR(50),"
			+ NEWS_ITEM_INFO_TITLE 			+ " TEXT,"
			+ NEWS_ITEM_INFO_ABSTRACT 		+ " TEXT,"
			+ NEWS_ITEM_INFO_URL 			+ " TEXT,"
			+ NEWS_ITEM_INFO_ARCHIVE        + " DATE,"
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
			SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MainActivity
							.getContextOfApplication());
			Calendar cal = Calendar.getInstance();
			cal.setTime(java.sql.Date.valueOf(object.getString("datetime")));
			cal.add(Calendar.DATE, shared.getInt("archieve", 10));
			String time = cal.get(Calendar.YEAR)  + "-" + cal.get(Calendar.MONTH)+1 + "-" + cal.get(Calendar.DATE);
			Log.i("DataBase Helper", "Inserting title.. " + object.getString("title")
					+ " archieve date: " + time);
			
			ContentValues values = new ContentValues();
			values.put(NEWS_ITEM_INFO_ITEM_ID, object.getString("item_id"));
			values.put(NEWS_ITEM_INFO_SOURCE_TITLE, object.getString("source_title"));
			values.put(NEWS_ITEM_INFO_TITLE, object.getString("title"));
			values.put(NEWS_ITEM_INFO_ABSTRACT, object.getString("abstract"));
			values.put(NEWS_ITEM_INFO_URL, object.getString("url"));
			values.put(NEWS_ITEM_INFO_ARCHIVE, time);
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
		String sql = "select item_id, source_title, title, abstract, url, datetime" +
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
				json.put("url",cursor.getString(4));
				json.put("datetime",cursor.getString(5));
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
	
	public void archieveNewsItems(){
		SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String str = parse.format(new Date()).toString();
		int val = this.getWritableDatabase().delete(TABLE_NEWS_ITEM_INFO, "archive_on_date = ?", new String[]{str});
		Log.i("Deleting news archieves", "deleting items for date.... " + str + " result: " + val);
	
	}
	
	public void updateArchieve(int no){
		String sql = "select datetime, item_id from news_item_info";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		try {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				Calendar cal = Calendar.getInstance();
				
				cal.setTime(java.sql.Date.valueOf(cursor.getString(0)));
				cal.add(Calendar.DATE, no);
				int mon= cal.get(Calendar.MONTH)+1;
				String time = cal.get(Calendar.YEAR)  + "-" + mon + "-" + cal.get(Calendar.DATE);
				ContentValues content = new ContentValues();
				content.put(NEWS_ITEM_INFO_ARCHIVE, time);
				db.update(TABLE_NEWS_ITEM_INFO, content, 
						"item_id=?", new String[]{cursor.getString(1)});
			}
		} finally {
			cursor.close();
		}
		
	}
}
