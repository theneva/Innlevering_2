/**
 * @author Theneva
 * @author Mads
 * @version 1.0
 * Due Date 2012.10.22
 */

package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.NotInitializedException;

public final class DBConnection {
	private static Connection connection;

	/**
	 * Gets the connection.
	 * 
	 * @return the connection.
	 * @throws NotInitializedException
	 *             if the connection has not been initialized.
	 */
	public static Connection getConnection() throws NotInitializedException {
		if (connection == null)
			throw new NotInitializedException();

		return connection;
	}

	/**
	 * Loads the JDBC driver and connects to the database using the parameters.
	 * 
	 * @param username
	 *            the username for connecting to the database.
	 * @param password
	 *            the password for connecting to the database.
	 * @return whether or not the connection was successful.
	 */
	public static boolean connect(String username, String password) {
		try {
			// Load the JDBC driver
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			// Using Martin's database @ mysql.nith.no for simplicity.
			String serverName = "mysql.nith.no";
			String database = "lehmar11";

			// Connect
			String url = "jdbc:mysql://" + serverName + "/" + database;

			connection = DriverManager.getConnection(url, username, password);

			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Closes the connection.
	 * 
	 * @return whether or not the connection was successfully closed.
	 * @throws NotInitializedException
	 *             if the connection has not been initialized.
	 */
	public static boolean closeConnection() throws NotInitializedException {
		if (connection == null)
			throw new NotInitializedException();

		try {
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}