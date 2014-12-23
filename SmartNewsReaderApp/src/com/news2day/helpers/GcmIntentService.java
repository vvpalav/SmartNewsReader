package com.news2day.helpers;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.news2day.R;
import com.news2day.database.DatabaseHelper;
import com.news2day.main.MainActivity;
//import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private static final String TAG = "In GCM Intent Sevices";
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Bundle extras = arg0.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(arg0);
		Log.i(TAG, "This is to verify that it reaches here");
		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: - " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server:  "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				Log.i(TAG, extras.toString());
				sendNotification("Received " + extras.toString());
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(arg0);
	}

	private void sendNotification(String string) {
		Log.i(TAG, "In Send Notification function");
		String str = string.substring(string.indexOf("message="));
		Log.i("In send notification", str);
		str = str.substring(str.indexOf("{"));
		str = str.substring(0, str.indexOf("}")+1);
		DatabaseHelper db = new DatabaseHelper(
				MainActivity.getContextOfApplication());
		JSONObject json;
		try {
			Log.i("GCM Message", str);
			json = new JSONObject(str);
			db.insertNewsitemInfo(json);

			mNotificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, MainActivity.class), 0);
			NotificationCompat.Builder mBuilder;
			mBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(json.getString("source_title"))
					.setStyle(
							new NotificationCompat.BigTextStyle().bigText(json
									.getString("title")))
					.setContentText(json.getString("title"))
					.setAutoCancel(true);
			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
