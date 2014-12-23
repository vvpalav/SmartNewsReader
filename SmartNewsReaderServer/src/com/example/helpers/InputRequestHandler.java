package com.example.helpers;

import java.util.List;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.example.newsAPIs.FeedZillaEntertainment;
import com.example.newsAPIs.FeedZillaScience;
import com.example.newsAPIs.FeedZillaSportsRSSReader;
import com.example.newsAPIs.FeedZillaJobsFeed;
import com.example.newsAPIs.FeedZillaTechnology;
import com.example.newsAPIs.FeedZillaTopNews;
import com.example.newsAPIs.FeedZillaWordNewsRssReader;
import com.example.newsAPIs.NewYorkTimesReader;
import com.example.newsAPIs.TheGuardianNewsReader;
import com.example.newsAPIs.USATodayNewsReader;

public class InputRequestHandler {
	private AWSSQSHandler sqsHandler;
	public static boolean flag = true;

	public InputRequestHandler() {
		sqsHandler = AWSSQSHandler.getSQSHandler();
	}

	public void startListening() {
		while (flag) {
			try {
				Thread.sleep(60 * 1000);
				List<Message> ll = sqsHandler.getMessagesFromQueue();
				for (Message m : ll) {
					JSONObject json = new JSONObject(m.getBody());
					switch (json.getInt("sourceId")) {
					case 1: {
						new Thread(new Runnable() {
							@Override
							public void run() {
								USATodayNewsReader usaToday = USATodayNewsReader
										.getInstance();
								usaToday.processUSATodayCronRequest();
							}
						}).start();
					}
						break;
					case 2: {
						new Thread(new Runnable() {
							@Override
							public void run() {
								TheGuardianNewsReader theGuardian = TheGuardianNewsReader
										.getInstance();
								theGuardian.processTheGuardianCronRequest();
							}
						}).start();
					}
						break;
					case 3: {
						new Thread(new Runnable() {
							@Override
							public void run() {
								FeedZillaSportsRSSReader feedZillaWorld = FeedZillaSportsRSSReader
										.getInstance();
								feedZillaWorld
										.processfeedZillaSportsCronRequest();
							}
						}).start();
					}
						break;
					case 4: {
						new Thread(new Runnable() {
							@Override
							public void run() {
								FeedZillaWordNewsRssReader feedZillaWorld = FeedZillaWordNewsRssReader
										.getInstance();
								feedZillaWorld
										.processfeedZillaWorldNewsCronRequest();
							}
						}).start();
					}
						break;
					case 5: {
						new Thread(new Runnable() {
							@Override
							public void run() {
								NewYorkTimesReader nytimes = NewYorkTimesReader.getInstance();
								nytimes.processNYTimesCronRequest();
							}
						}).start();

					}
						break;
					case 6: {
						new Thread(new Runnable() {
							@Override
							public void run() {
								FeedZillaEntertainment feedZilla = FeedZillaEntertainment.getInstance();
								feedZilla.processFeedZillaEntertainmentCronRequest();
							}
						}).start();
					}
						break;
					case 7: {
						new Thread(new Runnable() {
							@Override
							public void run() {
								FeedZillaJobsFeed feedZilla = FeedZillaJobsFeed.getInstance();
								feedZilla.processFeedZillaJobsCronRequest();
							}
						}).start();
					}
						break;
					case 8: {
						new Thread(new Runnable() {
							@Override
							public void run() {
								FeedZillaScience feedZilla = FeedZillaScience.getInstance();
								feedZilla.processFeedZillaScienceCronRequest();
							}
						}).start();
					}
						break;
					case 9: {
						new Thread(new Runnable() {
							@Override
							public void run() {
								FeedZillaTechnology feedZilla = FeedZillaTechnology.getInstance();
								feedZilla.processFeedZillaTechnologyCronRequest();
							}
						}).start();
					}
						break;
					case 10: {
						new Thread(new Runnable() {
							@Override
							public void run() {
								FeedZillaTopNews feedZilla = FeedZillaTopNews.getInstance();
								feedZilla.processFeedZillaTopNewsCronRequest();
							}
						}).start();
					}
						break;
					default:
						System.out.println("Invalid Source Id");
					}
				}
			} catch (InterruptedException | JSONException e) {
				e.printStackTrace();
			}

		}
	}
}
