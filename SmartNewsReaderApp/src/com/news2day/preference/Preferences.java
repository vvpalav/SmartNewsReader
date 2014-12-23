package com.news2day.preference;

import com.news2day.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;

public class Preferences extends PreferenceActivity{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		   View view = new View(this);
		   
		   SharedPreferences prefs = PreferenceManager
				    .getDefaultSharedPreferences(Preferences.this);
	}
	
	
}
