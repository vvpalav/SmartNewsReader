package com.example.helpers;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.sql.PreparedStatement;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.mysql.jdbc.Connection;

public class DBHelper {
	private final String dbURL = "jdbc:mysql://db-project.ctg5kek7aepz.us-east-1.rds.amazonaws.com:3306/ProjectDBSchema";
	private Connection conn;
	private static DBHelper db;
	private static final Logger log = Logger
			.getLogger(DBHelper.class.getName());

	private DBHelper() {
		try {
			System.out.println("Connecting to database");
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = (Connection) DriverManager.getConnection(dbURL,
					"admin", "adminpassword");
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
		return list;
	}
}
