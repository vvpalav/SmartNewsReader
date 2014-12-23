package com.news2day.main;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.news2day.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class News_Detail_Activity extends Activity{
	public String webURL = null;
	protected static final int TIME = 200;
	Button btn_speak;
	TextView titleview = (TextView) findViewById(R.id.news_title);
	TextView abtract = (TextView) findViewById(R.id.news_abstract);
	TextView date = (TextView) findViewById(R.id.news_date);
	/** Called when the activity is first created. */

	private TextToSpeech tts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		Bundle bundle = getIntent().getExtras();
		String jsonString = bundle.getString("json");
		
		
		// Text bto speech 
		
		btn_speak = (Button)findViewById(R.id.button1);
		btn_speak.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				speakOut();
			}
		});
		

		
		JSONObject json;
		try {
			json = new JSONObject(jsonString);
		
		
		TextView titleview = (TextView) findViewById(R.id.news_title);
		TextView abtract = (TextView) findViewById(R.id.news_abstract);
		TextView date = (TextView) findViewById(R.id.news_date);
		
		titleview.setText(json.getString("title"));
		abtract.setText(json.getString("abstract"));
		date.setText(json.getString("datetime"));
		webURL = json.getString("url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void openWebView(View V){
		Intent intent = new Intent(this,BrowserView.class);
		intent.putExtra("webUrl", webURL);
		startActivity(intent);		
	}
	
	
	
	
	
	
	// TExt to speech
	
	@Override
	public void onDestroy() {
		// Don't forget to shutdown!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	public void onInit(int status) {
		// TODO Auto-generated method stub

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			// tts.setPitch(5); // set pitch level

			// tts.setSpeechRate(2); // set speech speed rate

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "Language is not supported");
			} else {
				btn_speak.setEnabled(true);
				speakOut();
			}

		} else {
			Log.e("TTS", "Initilization Failed");
		}

	}

	@SuppressWarnings("deprecation")
	private void speakOut() {

		String text = abtract.getText().toString();

		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	
	}
	
	
	public void pause(int duration){
		Log.d("Pause.....",".......");
		//Toast.makeText(MainActivity.this, "Pausing", Toast.LENGTH_SHORT);
	    tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
	}
	
	
	
}
