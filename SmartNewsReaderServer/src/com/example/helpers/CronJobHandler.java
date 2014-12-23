package com.example.helpers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

public class CronJobHandler {

	public static boolean flag = true;
	private final AWSSQSHandler sqsHandler;
	private DBHelper db;
	public final int milisec = 1000;
	public final int min = 10;
	public final int sec = 60;

	public CronJobHandler() {
		sqsHandler = AWSSQSHandler.getSQSHandler();
		db = DBHelper.getDBInstance();
	}
	
	public static void main(String[] args){
		CronJobHandler cron = new CronJobHandler();
		cron.startCronMonitoring();
	}

	public void startCronMonitoring() {
		while (flag) {
			try {
				Thread.sleep(sec * min * milisec);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				List<Integer> list = db.getCronEntry(cal.get(Calendar.DAY_OF_WEEK),
						cal.get(Calendar.HOUR_OF_DAY) * 100,
						cal.get(Calendar.HOUR_OF_DAY) * 100 + min);
				for (Integer id : list){
					sqsHandler.sendMessageToQueue(new JSONObject().put("sourceId", id).toString());
				}
			} catch (InterruptedException | JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
