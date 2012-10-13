import java.sql.ResultSet;
import java.sql.SQLException;

import management.AccountMaintenance;
import db.DBConnection;


public class AccountMaintenanceClient {
	public static void main(String[] args) throws SQLException {
		
		// Using my own database @ mysql.nith.no for simplicity
		DBConnection.connect("lehmar11", "lehmar11");
		
		// Make sure the tables exist and are empty on running the program
		String sql = "DROP TABLE IF EXISTS accounts";
		DBConnection.connection.createStatement().executeUpdate(sql);
		sql = "CREATE TABLE accounts ( accountNumber VARCHAR(8) NOT NULL PRIMARY KEY, balance FLOAT(15, 4) NOT NULL DEFAULT 0, interestRate FLOAT(8, 4) NOT NULL DEFAULT 0 );";
		DBConnection.connection.createStatement().executeUpdate(sql);
		
		sql = "DROP TABLE IF EXISTS accountUpdate";
		DBConnection.connection.createStatement().executeUpdate(sql);
		sql = "CREATE TABLE accountUpdate ( accountNumber VARCHAR(8) NOT NULL, strUpdate VARCHAR(10) NOT NULL, strValue FLOAT(15, 4) );";
		DBConnection.connection.createStatement().executeUpdate(sql);
		
		// Insert data
		sql = "INSERT INTO accountUpdate () VALUES ( '11111111', '1000', 3.50 ), ( '22222222', '2000', 2.50 ), ('33333333', '3000', 1.50 );";
		DBConnection.connection.createStatement().executeUpdate(sql);
		
		System.out.println("Values have been inserted.");
		
		AccountMaintenance.updateAccounts("accounts", "accountUpdate");
		
		sql = "SELECT * FROM accounts";
		ResultSet resultSet = DBConnection.connection.createStatement().executeQuery(sql);
		
		System.out.println("Accounts after updateAccounts has been run for the first time:");
		while (resultSet.next()) {
			System.out.println(resultSet.getString("accountNumber") + ", " + resultSet.getFloat("balance") + ", " + resultSet.getFloat("interestRate"));
		}
		
		System.out.println(); // line break
		
		sql = "INSERT INTO accountUpdate () VALUES ( '11111111', 'b+', 1000 ), ( '22222222', 'b', 4000 ), ( '22222222', 'i+', 0.1 ), ( '33333333', 'i', 4.5 ), ( '44444444', '3000', 1.5 );";
		DBConnection.connection.createStatement().executeUpdate(sql);
		
		AccountMaintenance.updateAccounts("accounts", "accountUpdate");
		
		sql = "SELECT * FROM accounts";
		resultSet = DBConnection.connection.createStatement().executeQuery(sql);
		
		System.out.println("Accounts after updateAccounts has been run for the second time:");
		while (resultSet.next()) {
			System.out.println(resultSet.getString("accountNumber") + ", " + resultSet.getFloat("balance") + ", " + resultSet.getFloat("interestRate"));
		}
	}
}