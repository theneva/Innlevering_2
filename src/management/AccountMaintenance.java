package management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

import db.DBConnection;

public final class AccountMaintenance {

	/*
	 * Updates the table Accounts
	 */
	public static void updateAccounts(String tableName, String tableUpdate) throws SQLException {
		Connection connection = DBConnection.getConnection();
		
		// 1. get all info from tableName in a map
		Map<String, Account> accounts = getAccounts(tableName);
		
		// Feedback on how many rows were fetched.
		System.out.println(accounts.size() == 1 ? "1 row has" : (accounts.size() + " rows have") + " been fetched.");
		
		// 2. get all info from tableUpdate
		String sql = "SELECT * FROM " + tableUpdate + ";";
		ResultSet resultSet = connection.createStatement().executeQuery(sql);
		
		sql = "UPDATE " + tableName + " SET balance = ? WHERE accountNumber = ?;";
		PreparedStatement updateBalance = connection.prepareStatement(sql);
		sql = "UPDATE " + tableName + " SET interestRate = ? WHERE accountNumber = ?;";
		PreparedStatement updateInterestRate = connection.prepareStatement(sql);
		
		// "Decipher" info from tableUpdate and update tableName
		while (resultSet.next()) {
			String accountNumber = resultSet.getString("accountNumber");
			String strUpdate = resultSet.getString("strUpdate");
			float strValue = resultSet.getFloat("strValue");
			
			if (accounts.containsKey(accountNumber)) {
				Account account = accounts.get(accountNumber);
				
				// Requires JDK 7
				switch (strUpdate) {
				case "b" : 
					updateBalance.setFloat(1, strValue);
					updateBalance.setString(2, accountNumber);
					updateBalance.executeUpdate();
					break;
				case "b+" : 
					updateBalance.setFloat(1, account.getBalance() + strValue);
					updateBalance.setString(2, accountNumber);
					updateBalance.executeUpdate();
					break;
				case "b-" : 
					updateBalance.setFloat(1, account.getBalance() - strValue);
					updateBalance.setString(2, accountNumber);
					updateBalance.executeUpdate();
					break;
				case "i" : 
					updateInterestRate.setFloat(1, strValue);
					updateInterestRate.setString(2, accountNumber);
					updateInterestRate.executeUpdate();
					break;
				case "i+" :
					updateInterestRate.setFloat(1, account.getInterestRate() + strValue);
					updateInterestRate.setString(2, accountNumber);
					updateInterestRate.executeUpdate();
					break;
				case "i-" : 
					updateInterestRate.setFloat(1, account.getInterestRate() - strValue);
					updateInterestRate.setString(2, accountNumber);
					updateInterestRate.executeUpdate();
					break;
				default : 
					try {
						Integer.parseInt(strUpdate); // will fail here if not an integer
						System.out.println("The account already exists; invalid operator: " + strUpdate);
					} catch (NumberFormatException e) {
						System.out.println("Invalid operator: " + strUpdate);
					}
					break;
				}
			} else {
				// Account does not exist yet
				try {
					// Check if the balance is an integer
					int balance = Integer.parseInt(strUpdate);
					
					// strValue = interest rate in this case
					sql = "INSERT INTO " + tableName + " () VALUES ( " + accountNumber + ", " + balance + ", " + strValue + " );";
					connection.createStatement().executeUpdate(sql);	
				} catch (NumberFormatException e) {
					System.out.println("Invalid balance: " + strUpdate);
				}
			}
		}
		
		// Delete data from tableUpdate
		sql = "DELETE FROM " + tableUpdate + ";";
		connection.createStatement().executeUpdate(sql);
	}

	/*
	 * Gets all accounts from the table
	 */
	public static Map<String, Account> getAccounts(String tableName) throws SQLException {
		
		String sql = "SELECT * FROM " + tableName + " ORDER BY accountNumber ASC;";
		ResultSet resultSet = DBConnection.getConnection().createStatement().executeQuery(sql);
		
		Map<String, Account> accounts = new TreeMap<String, Account>();
	
		while (resultSet.next()) {
			final String accountNumber = resultSet.getString("accountNumber");
			final float balance = resultSet.getFloat("balance");
			final float interestRate = resultSet.getFloat("interestRate");
			
			accounts.put(accountNumber, new Account(accountNumber, balance, interestRate));
		}
		
		return accounts;
	}

	/*
	 * Gets a single account based on account number.
	 */
	public static Account getAccount(String tableName, String accountNumber) {
		try {
			
			// Attempt finding the account in the database
			Statement statement = DBConnection.getConnection().createStatement();
			String sql = "SELECT * FROM " + tableName + " WHERE accountNumber = '" + accountNumber + "';";
			ResultSet resultSet = statement.executeQuery(sql);
			
			float balance = resultSet.getFloat("balance");
			float interestRate = resultSet.getFloat("interestRate");
			return new Account(accountNumber, balance, interestRate);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}