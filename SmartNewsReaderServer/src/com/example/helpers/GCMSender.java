package com.example.helpers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

public class GCMSender {
	private static final String serverURL = "https://android.googleapis.com/gcm/send";
	private static final String authKey = "AIzaSyCTnUS6Dpp5m7cy4Y3nsgmS6ccbylMn-C8";
	private static Logger log = Logger.getLogger(GCMSender.class.getName());

	public static void sendGCMNotification(String[] regIds, String message) {
		try {
			JSONArray array = new JSONArray();
			for (String id : regIds) {
				array.put(id);
			}
			StringBuilder response = new StringBuilder();
			JSONObject msg = new JSONObject();
			msg.put("registration_ids", array);
			msg.put("data", new JSONObject().put("message", message));
			log.info("GCM Message: " + msg.toString());
			URL url = new URL(serverURL);
			byte[] bytes = msg.toString().getBytes();
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Content-Type", "application/json");
			urlConnection.setRequestProperty("Authorization", "key=" + authKey);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			urlConnection.setFixedLengthStreamingMode(bytes.length);
			OutputStream out = urlConnection.getOutputStream();
			out.write(bytes);
			out.close();
			int result = urlConnection.getResponseCode();
			log.info("Received response code: " + result);
			InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
			int charCode = -1;
			while ((charCode = in.read()) != -1) {
				response.append((char) charCode);
			}
			in.close();
			log.info("received GCM response " + response.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
