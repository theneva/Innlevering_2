/**
 * @author Theneva
 * @author Mads
 * @version 1.0
 * Due Date 2012.10.22
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import management.AccountMaintenance;
import db.DBConnection;

public class AccountMaintenanceClient {
	/**
	 * Connects to the database "lehmar11" @ mysql.nith.no; Resets the tables by
	 * dropping and re-creating them; Inserts data into the update table and
	 * runs updateAccounts() : void in order to fill the accounts table; Inserts
	 * more data into the update table and runs updateAccounts() : void again in
	 * order to update the existing accounts and insert a new account; Closes
	 * the connection.
	 * 
	 * @param args
	 *            the arguments.
	 */
	public static void main(String[] args) {
		// For keeping track of the time taken to run the main method.
		long startTime = System.currentTimeMillis();

		try {
			// Using Martin's database @ mysql.nith.no for simplicity
			DBConnection.connect("lehmar11", "lehmar11");
			Connection connection = DBConnection.getConnection();

			// Make sure the tables exist and are empty on running the program
			String sql = "DROP TABLE IF EXISTS accounts";
			connection.createStatement().executeUpdate(sql);
			sql = "CREATE TABLE accounts ( accountNumber VARCHAR(8) NOT NULL PRIMARY KEY, balance FLOAT(15, 4) NOT NULL DEFAULT 0, interestRate FLOAT(8, 4) NOT NULL DEFAULT 0 );";
			connection.createStatement().executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS accountUpdate";
			connection.createStatement().executeUpdate(sql);
			sql = "CREATE TABLE accountUpdate ( accountNumber VARCHAR(8) NOT NULL, strUpdate VARCHAR(10) NOT NULL, strValue FLOAT(15, 4) );";
			connection.createStatement().executeUpdate(sql);

			// Insert data
			sql = "INSERT INTO accountUpdate () VALUES ( '11111111', '1000', 3.50 ), ( '22222222', '2000', 2.50 ), ('33333333', '3000', 1.50 );";
			connection.createStatement().executeUpdate(sql);

			System.out.println("Values have been inserted.");

			// Update the accounts based on the first set of inserts
			AccountMaintenance.updateAccounts("accounts", "accountUpdate");

			// Display the results of the first update
			sql = "SELECT * FROM accounts";
			ResultSet resultSet = connection.createStatement().executeQuery(sql);

			System.out.println("Accounts after updateAccounts has been run for the first time:");
			while (resultSet.next()) {
				System.out.println(resultSet.getString("accountNumber") + ", "
						+ resultSet.getFloat("balance") + ", "
						+ resultSet.getFloat("interestRate"));
			}

			System.out.println(); // line break

			// Insert data for second update
			sql = "INSERT INTO accountUpdate () VALUES ( '11111111', 'b+', 1000 ), ( '22222222', 'b', 4000 ), ( '22222222', 'i+', 0.1 ), ( '33333333', 'i', 4.5 ), ( '44444444', '3000', 1.5 );";
			connection.createStatement().executeUpdate(sql);

			// Update the accounts again based on the second set of inserts
			AccountMaintenance.updateAccounts("accounts", "accountUpdate");

			// Display the results of the second update
			sql = "SELECT * FROM accounts";
			resultSet = connection.createStatement().executeQuery(sql);

			System.out.println("Accounts after updateAccounts has been run for the second time:");
			while (resultSet.next()) {
				System.out.println(resultSet.getString("accountNumber") + ", "
						+ resultSet.getFloat("balance") + ", "
						+ resultSet.getFloat("interestRate"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}

		System.out.println("Run time: " + (System.currentTimeMillis() - startTime) + " ms.");
	}
}