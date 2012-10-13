package db;

/*
 * Author:		Martin Lehmann
 * Due date:	2012.10.19
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
	public static Connection connection;

	public static boolean connect(String user, String password) {
		try {

			// Load the JDBC driver
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			// Using my own database @ mysql.nith.no for simplicity.
			String serverName = "mysql.nith.no";
			String database = "lehmar11";

			String url = "jdbc:mysql://" + serverName + "/" + database;

			connection = DriverManager.getConnection(url, user, password);

			System.out.println("Connection to database has been established.");
			
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Failed
		return false;
	}

	public boolean closeConnection() {
		try {
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}