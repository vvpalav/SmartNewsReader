package com.example.server;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.example.helpers.DBHelper;

public class RequestHandler {

	private DBHelper db;
	private HttpServletResponse resp;
	private static final Logger log = Logger.getLogger(RequestHandler.class.getName());

	public RequestHandler(HttpServletResponse resp) {
		this.resp = resp;
		this.db = DBHelper.getDBInstance();
	}

	public void handleIncoming(String input) {
		JSONObject response = new JSONObject();
		try {
			JSONObject object = new JSONObject(input);
			log.info("Received ... " + object.toString());
			String action = object.getString("transaction_type");
			response.put("transaction_type", action + "_RESPONSE");
			switch (action) {
			case "NEW_USER_REGISTRATION":
				String msg = registerNewUser(object);
				if (msg == null) {
					response.put("response", "Success");
				} else {
					response.put("response", "Failure");
					response.put("message", msg);
				}
				break;
			case "EXISTING_USER_REGISTRATION":
				if (registerExistingUser(object)) {
					response.put("response", "Success");
				} else {
					response.put("response", "Failure");
				}
				break;
			case "USER_PASSCODE_AUTH":
				if (db.autheticateUserCode(object)) {
					if (object.getString("TYPE_USER").equals("NEW_USER")) {
						if (db.insertUser(object)) {
							response.put("response", "Success");
							response.put("email_id", object.get("email_id"));
							response.put("telephone", object.get("telephone"));
							response.put("name", object.get("name"));
							response.put("source_list", db.getSitesList());
						} else {
							response.put("response", "Failure");
						}
					} else if (object.getString("TYPE_USER").equals(
							"EXISTING_USER")) {
						response.put("response", "Success");
						response.put("email_id", object.get("email_id"));
						response.put("telephone", object.get("telephone"));
						response.put("name", object.get("name"));
						response.put("source_list", db.getSitesList());
					}
				} else {
					response.put("response", "Failure");
				}
				break;
			case "ADD_USER_SUBSCRIPTION":
				db.addUserSubscription(object);
				response.put("data", db.getNewsItemsFromTitle(object.getString("source_title")));
				break;
			default:
				response.put("message", "Unknown Transaction");
				log.info("Unknown transaction type: "
						+ object.getString("transaction_type"));
			}
		} catch (JSONException ex) {
			log.severe(ex.toString());
		} finally {
			log.info("Sending ... " + response.toString());
			resp.setContentType("text/json");
			try {
				resp.getWriter().println(response.toString());
			} catch (IOException e) {
				log.severe(e.toString());
			}
		}
	}

	private String registerNewUser(JSONObject object) {
		try {
			if (!db.checkIfUserExist(object.getString("telephone"))) {
				int authenticate = new Random().nextInt((9999 - 1000) + 1) + 1000;
				String body = "Thanks for registring with Smart News Reader.\n"
						+ "Your one time registration code is " + authenticate
						+ ".\n\nThanks,\nSmart News Reader Team";
				RequestHandler.sendEmail(
						object.getString("email_id"),
						body,
						"Authentication of New User "
								+ object.getString("name"));
				db.saveUserCode(object.getString("telephone"), authenticate);
				return null;
			} else {
				JSONObject user = db
						.getUsersInfo(object.getString("telephone"));
				return user.getString("telephone") + " already registered "
						+ " with " + user.getString("email_id")
						+ " Continue to get code on registered email";
			}
		} catch (JSONException ex) {
			log.severe(ex.toString());
		}
		return null;
	}

	private boolean registerExistingUser(JSONObject object) {
		try {
			if (db.checkIfUserExist(object.getString("telephone"))) {
				JSONObject user = db
						.getUsersInfo(object.getString("telephone"));
				int authenticate = new Random().nextInt((9999 - 1000) + 1) + 1000;
				String body = "Thanks for registring again with Smart News Reader.\n"
						+ "Your one time registration code is "
						+ authenticate
						+ ".\n\nThanks,\nSmart News Reader Team";
				RequestHandler.sendEmail(
						user.getString("email_id"),
						body,
						"Authentication of Existing User "
								+ user.getString("name"));
				db.saveUserCode(object.getString("telephone"), authenticate);
				return true;
			}
		} catch (JSONException ex) {
			log.severe(ex.toString());
		}
		return false;
	}

	public static void sendEmail(String to, String body, String sub) {
		final String from = "smartnewsreader23@gmail.com";
		final String host = "smtp.gmail.com";
		final String username = "smartnewsreader23@gmail.com";
		final String password = "projectsnr";

		// Setup mail server
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}		
		});
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(sub);
			message.setText(body);
			Transport.send(message);
			log.info("Sent message successfully....");
		} catch (MessagingException ex) {
			log.severe(ex.toString());
		}
	}
}
