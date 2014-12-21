package com.example.helpers;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class AWSSQSHandler {
	private final AmazonSQS sqsHandler;
	private final static String queueName = "SmartNewsReaderWorkDistributor";
	private final static String queueRegion = "us-east-1";
	private final String queueURL;
	private static AWSSQSHandler SQSHandler;

	private AWSSQSHandler() {
		System.out.println("Initializing SQS Handler");
		AWSCredentials credentials = new ProfileCredentialsProvider("EC2").getCredentials();
		sqsHandler = new AmazonSQSClient(credentials);
		sqsHandler.setRegion(Region.getRegion(Regions.fromName(queueRegion)));
		queueURL = createMessageQueue();
	}
	
	public int getMessangeCountInQueue(){
		List<Message> ll = getMessagesFromQueue();
		return (ll == null) ? 0 : ll.size();
	}

	public static synchronized AWSSQSHandler getSQSHandler() {
		if (SQSHandler == null) {
			SQSHandler = new AWSSQSHandler();
		}
		SQSHandler.deleteAllMessagesFromQueue();
		return SQSHandler;
	}
	
	public void deleteAllMessagesFromQueue(){
		List<Message> ll = getMessagesFromQueue();
		if(ll != null){
			for (Message m : ll){
				deleteMessageFromQueue(m.getReceiptHandle());
			}
		}
	}

	public synchronized String createMessageQueue() {
		ListQueuesResult l = sqsHandler.listQueues();
		for (String s : l.getQueueUrls()) {
			if (s.contains(queueName)) {
				System.out.println("SQS Queue already exists: " + s);
				return s;
			}
		}
		System.out.println("Creating new SQS Queue");
		CreateQueueRequest request = new CreateQueueRequest(queueName);
		return sqsHandler.createQueue(request).getQueueUrl();
	}

	public synchronized boolean sendMessageToQueue(String message) {
		if (queueURL != null && message != null) {
			sqsHandler.sendMessage(new SendMessageRequest(queueURL, message));
			return true;
		}
		return false;
	}

	public synchronized List<Message> getMessagesFromQueue() {
		if (queueURL != null) {
			ReceiveMessageRequest request = new ReceiveMessageRequest(queueURL);
			List<Message> ll = sqsHandler.receiveMessage(request).getMessages();
			if(ll.size() == 0){
				return null;
			}
			return sqsHandler.receiveMessage(request).getMessages();
		}
		return null;
	}

	public synchronized boolean deleteMessageFromQueue(String messageRecieptHandle) {
		if (queueURL != null && messageRecieptHandle != null) {
			sqsHandler.deleteMessage(new DeleteMessageRequest(queueURL, messageRecieptHandle));
			return true;
		}
		return false;
	}
}