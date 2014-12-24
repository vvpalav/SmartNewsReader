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


public class News_Detail_Activity extends Activity implements TextToSpeech.OnInitListener{
	public String webURL = null;
	protected static final int TIME = 200;
	Button btn_speak;
	TextView titleview;
	TextView abtract;
	TextView date ;
	Button webV;
	/** Called when the activity is first created. */

	private TextToSpeech tts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		
		Bundle bundle = getIntent().getExtras();
		String jsonString = bundle.getString("json");
		titleview = (TextView) findViewById(R.id.news_title1);
		abtract = (TextView) findViewById(R.id.news_abstract1);
		date = (TextView) findViewById(R.id.news_date1);
		/** Called when the activity is first created. */
		
		
		// Text bto speech 
		tts = new TextToSpeech(this, this);
		btn_speak = (Button)findViewById(R.id.play_now);
		btn_speak.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				speakOut();
			}
		});
		

		
		JSONObject json;
		try {
			json = new JSONObject(jsonString);
			System.out.println("jsonString" + jsonString);
		
		 titleview = (TextView) findViewById(R.id.news_title1);
		 abtract = (TextView) findViewById(R.id.news_abstract1);
		 date = (TextView) findViewById(R.id.news_date1);
		
		titleview.setText(json.getString("title"));
		abtract.setText(json.getString("abstract"));
		date.setText(json.getString("datetime"));
		webURL = json.getString("url");
		
		if(webURL==null)
		{
			webV = (Button)findViewById(R.id.go_to_site);
			webV.setEnabled(false);
		}
		
		System.out.println("webURL : " + webURL);
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

			 tts.setPitch(-15); // set pitch level

			 tts.setSpeechRate(-10); // set speech speed rate

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
		Log.d("Text.....",text);
		
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	
	}
	
	
	public void pause(int duration){
		Log.d("Pause.....",".......");
		//Toast.makeText(MainActivity.this, "Pausing", Toast.LENGTH_SHORT);
	    tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
	}
	
	
	
}
