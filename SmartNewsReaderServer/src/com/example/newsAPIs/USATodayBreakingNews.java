package com.example.newsAPIs;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.logging.Logger;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.example.helpers.DBHelper;

public class USATodayBreakingNews {

	private Logger log = Logger.getLogger(USATodayBreakingNews.class.getName());
	private static USATodayBreakingNews usaBreaking;
	private DBHelper db;
	private JSONObject data;
	private final String title = "USA Today Breaking News";
	
	private USATodayBreakingNews(){
		db = DBHelper.getDBInstance();
		data = db.getCompanyData(title);
	}
	
	public static void main(String[] args){
		USATodayBreakingNews usaBreaking = USATodayBreakingNews.getInstance();
		usaBreaking.fetchTopStories();
	}
	
	public synchronized static USATodayBreakingNews getInstance() {
		if (usaBreaking == null) {
			usaBreaking = new USATodayBreakingNews();
		}
		return usaBreaking;
	}
	
	public JSONObject fetchTopStories() {
		try {
			String str = data.getString("link") + "?expired=true?api_key="
					+ data.getString("apikey");
			URL url = new URL(str);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection
					.setRequestProperty("Content-Type", "application/json");
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			int result = urlConnection.getResponseCode();
			log.info(new Date().toString() + "Received response code: "
					+ result);
			InputStreamReader in = new InputStreamReader(
					urlConnection.getInputStream());
			int charCode = -1;
			StringBuilder response = new StringBuilder();
			while ((charCode = in.read()) != -1) {
				response.append((char) charCode);
			}
			in.close();
			System.out.println(response.toString());
			return new JSONObject(response.toString());
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
