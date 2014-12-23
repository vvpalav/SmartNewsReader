package com.news2day.main;

//import android.R;
import com.news2day.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class BrowserView extends Activity {
	/** Called when the activity is first created. */
    String url= "";
	WebView wv ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("webUrl");
        setContentView(R.layout.browser_view);
		//setContentView(R.layout.browser_view);
		//wv = (WebView)findViewById(R.layout.webview1);
		wv.setWebViewClient(new WebViewClient());
		wv.loadUrl(url);
		
		
	}


}