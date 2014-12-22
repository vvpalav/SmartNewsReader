package com.example.helpers;

import java.util.List;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.example.newsAPIs.NewYorkTimesReader;

public class InputRequestHandler {
	private AWSSQSHandler sqsHandler;
	public static boolean flag = true;

	public InputRequestHandler() {
		sqsHandler = AWSSQSHandler.getSQSHandler();
	}

	public void startListening(){
		while(flag){
			try {
				Thread.sleep(60 * 1000);
				List<Message> ll = sqsHandler.getMessagesFromQueue(); 
				for (Message m : ll){
					JSONObject json = new JSONObject(m.getBody());
					switch(json.getInt("sourceId")){
					case 1 :
						break;
					case 2 :
						break;
					case 3 :
						break;
					case 4 :
						break;
					case 5 : {
						new Thread(new Runnable(){
							@Override
							public void run() {
								NewYorkTimesReader nytimes = NewYorkTimesReader.getInstance();
								nytimes.processNYTimesCronRequest();
							}
						});
						
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
