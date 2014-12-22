package com.news2day.helpers;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.news2day.database.DatabaseHelper;
import com.news2day.main.MainActivity;

public class ClientServerRequestHandler {
	private static DatabaseHelper db;

	public static void handleRequest(String line) {
		SharedPreferences shared = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.getContextOfApplication());
		try {
			if (db == null) {
				db = new DatabaseHelper(MainActivity.getContextOfApplication());
			}
			JSONObject object = new JSONObject(line);
			String action = object.getString("transaction_type");
			if (action.equals("NEW_USER_REGISTRATION") || 
					action.equals("EXISTING_USER_REGISTRATION") ) {
				shared.edit().putString("user_login_info", line).commit();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	public static void handleServerResponse(String line) {
		try {
			Log.i("from_server", line);
			if (db == null) {
				db = new DatabaseHelper(MainActivity.getContextOfApplication());
			}
			SharedPreferences shared = PreferenceManager
					.getDefaultSharedPreferences(MainActivity
							.getContextOfApplication());
			JSONObject object = new JSONObject(line);
			String action = object.getString("transaction_type");
			if (action.equals("USER_PASSCODE_AUTH_RESPONSE")){
				boolean flag = false;
				if(object.getString("response").equalsIgnoreCase("success")) {
					flag = true;
					shared.edit().putString("telephone", object.getString("telephone"))
						.putString("email_id", object.getString("email_id"))
						.putString("name", object.getString("name")).commit();
					JSONArray ll = object.getJSONArray("source_list");
					Set<String> set = new HashSet<String>();
					for(int i = 0; i < ll.length(); i++){
						set.add(ll.getString(i));
					}
					shared.edit().putStringSet("source_list", set).commit();
				}
				shared.edit().putBoolean("is_user_reg", flag).commit();
			} else if (action.equals("NEW_USER_REGISTRATION_RESPONSE")) {
				boolean flag = true;
				if (object.getString("response").equals("Failure")) {
					shared.edit().putString("server_res", object.getString("message")).commit();
					flag = false;
				} 
				shared.edit().putBoolean("is_user_accepted", flag).commit();
			} else if (action.equals("EXISTING_USER_REGISTRATION_RESPONSE")) {
				boolean flag = true;
				if (object.getString("response").equals("Failure")) {
					flag = false;
				} 
				shared.edit().putBoolean("is_user_accepted", flag).commit();
			} else if (action.equalsIgnoreCase("ADD_USER_SUBSCRIPTION_RESPONSE")){
				JSONArray array = object.getJSONArray("data");
				for(int i = 0; i < array.length(); i++){
					db.insertNewsitemInfo(array.getJSONObject(i));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
