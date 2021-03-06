package com.example.newsAPIs;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

public class TheGuardianNewsReader {

	private Logger log = Logger.getLogger(TheGuardianNewsReader.class.getName());
	private static TheGuardianNewsReader guardian;
	private DBHelper db;
	private JSONObject data;
	private final String title = "The Guardian";
	
	private TheGuardianNewsReader(){
		db = DBHelper.getDBInstance();
		data = db.getCompanyData(title);
	}
	
	public static void main(String[] args){
		TheGuardianNewsReader guardian = TheGuardianNewsReader.getInstance();
		guardian.checkForNewItems();
	}
	
	public synchronized static TheGuardianNewsReader getInstance() {
		if (guardian == null) {
			guardian = new TheGuardianNewsReader();
		}
		return guardian;
	}
	
	public void processTheGuardianCronRequest(){
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

	public List<Integer> checkForNewItems() {
		List<Integer> list = new ArrayList<Integer>();
		try {
			JSONObject json = fetchTopStories().getJSONObject("response");
			JSONArray result = json.getJSONArray("results");
			if (result.length() > db.getNewsItemCount(title, getTodaysDate())) {
				for (int i = 0; i < result.length(); i++) {
					JSONObject j = result.getJSONObject(i);
					String date = j.getString("webPublicationDate");
					date = date.substring(0, date.indexOf("T"));
					if (!db.checkIFNewsItemExist(j.getString("webTitle"), date)) {
						list.add(db.insertTheGuardianNewsItem(j, data));
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public JSONObject fetchTopStories() {
		try {
			String str = data.getString("link") + "?api-key=" + data.getString("apikey");
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
	
	public String getTodaysDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-"
				+ cal.get(Calendar.DATE);
	}
}
