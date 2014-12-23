package com.example.newsAPIs;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.example.helpers.DBHelper;
import com.example.helpers.GCMSender;

public class FeedZillaScience {
	private Logger log = Logger.getLogger(FeedZillaWordNewsRssReader.class.getName());
	private final String title = "FeedZilla Science";
	private static FeedZillaScience feedfZillaScience;
	private JSONObject data;
	private DBHelper db;

	private FeedZillaScience() {
		db = DBHelper.getDBInstance();
		data = db.getCompanyData(title);
	}

	public static void main(String[] args) throws JSONException {
		FeedZillaScience feedfZillaScience = FeedZillaScience.getInstance();
		feedfZillaScience.checkForNewItems();
	}
	
	public synchronized static FeedZillaScience getInstance() {
		if (feedfZillaScience == null) {
			feedfZillaScience = new FeedZillaScience();
		}
		return feedfZillaScience;
	}
	
	public void processFeedZillaScienceCronRequest(){
		try {
			List<Integer> ll = checkForNewItems();
			if(ll != null && ll.size() > 0){
				JSONArray gcm_list = db.getGCMListForNewsSource(data.getString("source_id"));
				for(Integer l : ll){
					GCMSender.sendGCMNotification(gcm_list, db.getNewsItemInfo(l).toString());
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private List<Integer> checkForNewItems() {
		List<Integer> list = new ArrayList<Integer>();
		try {
			JSONObject json = fetchTopStories();
			JSONArray result = json.getJSONArray("articles");
			if (result.length() > db.getNewsItemCount(title, getTodaysDate())) {
				for (int i = 0; i < result.length(); i++) {
					JSONObject j = result.getJSONObject(i);
					SimpleDateFormat parser = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
					Date dateStr = parser.parse(j.getString("publish_date"));
					parser = new SimpleDateFormat("yyyy-MM-dd");
					if (!db.checkIFNewsItemExist(j.getString("title"), parser.format(dateStr))) {
						list.add(db.insertFeedZillaRssItem(j, data));
					}
				}
			}
		} catch (JSONException | ParseException e) {
			e.printStackTrace();
		}
		return list;
	}

	public JSONObject fetchTopStories() {
		try {
			String str = data.getString("link");
			URL url = new URL(str);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Content-Type", "application/json");
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			int result = urlConnection.getResponseCode();
			log.info(new Date().toString() + "Received response code: " + result);
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
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getTodaysDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-"
				+ cal.get(Calendar.DATE);
	}
}
