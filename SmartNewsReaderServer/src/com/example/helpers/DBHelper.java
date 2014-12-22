package com.example.helpers;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.sql.PreparedStatement;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.mysql.jdbc.Connection;

public class DBHelper {
	private final String dbURL = "jdbc:mysql://tweetmap.cjimqvmene65.us-west-2.rds.amazonaws.com:3306/tweetrecorder";
	private Connection conn;
	private static DBHelper db;
	private final Logger log = Logger.getLogger(DBHelper.class.getName());
	
	private DBHelper() {
		try {
			System.out.println("Connecting to database");
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = (Connection) DriverManager.getConnection(dbURL, "edge", "edge_123");
			System.out.println("Connected to database");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("Cannot connect the database!");
			e.printStackTrace();
		}
	}

	public static synchronized DBHelper getDBInstance() {
		try {
			if (db == null || (db != null && db.conn.isClosed())) {
				db = new DBHelper();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return db;
	}

	public boolean checkIfUserExist(String telephone) {
		try {
			String sql = "select count(*) from user_info "
					+ "where telephone = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, telephone);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return (rs.getInt(1) > 0);
		} catch (SQLException ex) {
			log.severe(ex.toString());
		}
		return false;
	}

	public boolean insertUser(JSONObject object) {
		boolean flag = false;
		try {
			log.info("Inserting User ... " + object.getString("name"));
			String SQL = "insert into user_info (name, telephone, email_id, gcm_id)"
					+ " values (?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(SQL);
			stmt.setString(1, object.getString("name"));
			stmt.setString(2, object.getString("telephone"));
			stmt.setString(3, object.getString("email_id"));
			stmt.setString(4, object.getString("gcm_id"));
			flag = (stmt.executeUpdate() > 0);
		} catch (SQLException | JSONException ex) {
			log.severe(ex.toString());
		}
		return flag;
	}

	public void saveUserCode(String telephone, int auth) {
		try {
			String SQL = "delete from user_auth_codes where telephone = ?";
			PreparedStatement stmt = conn.prepareStatement(SQL);
			stmt.setString(1, telephone);
			stmt.executeUpdate();
			SQL = "insert into user_auth_codes (telephone, code) values (?, ?)";
			stmt = conn.prepareStatement(SQL);
			stmt.setString(1, telephone);
			stmt.setInt(2, auth);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			log.severe(ex.toString());
		}
	}

	public boolean autheticateUserCode(JSONObject object) {
		try {
			String SQL = "Select code from user_auth_codes where telephone = ?";
			PreparedStatement stmt = conn.prepareStatement(SQL);
			stmt.setString(1, object.getString("telephone"));
			ResultSet rs = stmt.executeQuery();
			rs.next();
			boolean flag = (rs.getInt("code") == object.getInt("code"));

			if (flag) {
				SQL = "delete from user_auth_codes where telephone = ?";
				stmt = conn.prepareStatement(SQL);
				stmt.setString(1, object.getString("telephone"));
				stmt.executeUpdate();
			}
			return flag;
		} catch (SQLException | JSONException ex) {
			log.severe(ex.toString());
		}
		return false;
	}

	public JSONObject getUsersInfo(String telephone) {
		JSONObject object = new JSONObject();
		try {
			String SQL = "select name, telephone, email_id, gcm_id from user_info "
					+ "where telephone = ?";
			PreparedStatement stmt = conn.prepareStatement(SQL);
			stmt.setString(1, telephone);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			object.put("name", rs.getObject(1));
			object.put("telephone", rs.getObject(2));
			object.put("email_id", rs.getObject(3));
			object.put("gcm_id", rs.getObject(4));
		} catch (SQLException | JSONException ex) {
			log.severe(ex.toString());
		}
		return object;
	}
	
	public List<Integer> getCronEntry(int day, int timeTo, int timeFrom){
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select source_id from cron_job where time >= ? "
				+ " and time <= ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, timeTo);
			stmt.setInt(2, timeFrom);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public JSONObject getCompanyData(String title){
		JSONObject json = new JSONObject();
		String sql = "select * from news_sites_info where title = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, title);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()){
				json.put("source_id", rs.getString(1));
				json.put("title", rs.getString(2));
				json.put("apikey", rs.getString(3));
				json.put("link", rs.getString(4));
			}
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public int insertNYTimesNewsItem(JSONObject json, JSONObject data){	
		System.out.println("Inserting: " + json.toString());
		
		String sql = "insert into news_item_info values (?, ?, ?, ?, ?, ?, ?)";
		try {
			int count = getNewsItemCount(null, null);
			String date = json.getString("published_date");
			date = date.substring(0, date.indexOf("T"));
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, count+1);
			stmt.setString(2, data.getString("source_id"));
			stmt.setString(3, json.getString("title"));
			stmt.setString(4, json.getString("abstract"));
			stmt.setString(5, "");
			stmt.setString(6, date);
			stmt.setString(7, json.getString("url"));
			stmt.executeUpdate();
			return count + 1;
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int getNewsItemCount(String title, String date){
		String sql = "select count(*) from news_item_info ";
		if(title != null && title.length() > 0){
			sql += " a, news_sites_info b where a.source_id = b.source_id and "
					+ " b.title = ? ";
		}
		if(date != null && date.length() > 0){
			sql += " and a.datetime = ?";
		}
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			if(title != null && title.length() > 0){
				stmt.setString(1, title);
			}
			if(date != null && date.length() > 0){
				stmt.setObject(2, Date.valueOf(date));
			}
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public boolean checkIFNewsItemExist(String title, String date){
		String sql = "select count(*) from news_item_info "
				+ " where title like '%"+title+"%' and datetime = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setDate(1, Date.valueOf(date));
			ResultSet rs = stmt.executeQuery();
			rs.next();
			if(rs.getInt(1) > 0){
				System.out.println("This item already exists");
				return true;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public JSONArray getGCMListForNewsSource(String source_id){
		JSONArray list = new JSONArray();
		String sql = "select gcm_id from user_info u, user_subscription_details b, "
				+ " u.telephone = b.telephone and b.source_id = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, source_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				list.put(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public JSONObject getNewsItemInfo(int item_id){
		String sql = "select title, abstract, text, url, datetime from news_item_info where item_id = ?";
		JSONObject json = new JSONObject();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, item_id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			json.put("title", rs.getString(1));
			json.put("abstract", rs.getString(2));
			json.put("text", rs.getString(3));
			json.put("url", rs.getString(4));
			json.put("datetime", rs.getString(5));
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
