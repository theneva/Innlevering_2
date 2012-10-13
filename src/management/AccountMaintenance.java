package management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import db.DBConnection;

public final class AccountMaintenance {

	/*
	 * Updates the table Accounts
	 */
	public static void updateAccounts(String tableName, String tableUpdate) throws SQLException {
		
		// 1. get all info from tableName in a map
		Map<String, Account> accounts = getAccounts(tableName);
		
		System.out.println(accounts.size() + " rows have been fetched.");
		
		// 2. get all info from tableUpdate
		String sql = "SELECT * FROM " + tableUpdate + ";";
		ResultSet resultSet = DBConnection.connection.createStatement().executeQuery(sql);
		
		// "Decipher" info from tableUpdate
		while (resultSet.next()) {
			String accountNumber = resultSet.getString("accountNumber");
			String strUpdate = resultSet.getString("strUpdate");
			float strValue = resultSet.getFloat("strValue");
			
			if (accounts.containsKey(accountNumber)) {
				Account account = accounts.get(accountNumber);
				
				// Requires JDK 7
				switch (strUpdate) {
				case "b" : 
					account.setBalance(strValue);
					break;
				case "b+" : 
					account.setBalance(account.getBalance() + strValue);
					break;
				case "b-" : 
					account.setBalance(account.getBalance() - strValue);
					break;
				case "i" : 
					account.setInterestRate(strValue);
					break;
				case "i+" :
					account.setInterestRate(account.getInterestRate() + strValue);
					break;
				case "i-" : 
					account.setInterestRate(account.getInterestRate() - strValue);
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
				
				accounts.put(accountNumber, account);
				
			} else {
				// Account does not exist yet
				try {
					// Check if the balance is an integer
					int balance = Integer.parseInt(strUpdate);
					
					// strValue = interest rate in this case
					Account account = new Account(accountNumber, balance, strValue);
					
					// Add the account to the map
					accounts.put(accountNumber, account);
					
				} catch (NumberFormatException e) {
					System.out.println("Invalid balance: " + strUpdate);
				}
			}
		}
		
		// 3. 
		
		// Drop existing data in database
		sql = "DELETE FROM accounts;";
		DBConnection.connection.createStatement().executeUpdate(sql);
		
		Iterator<Entry<String, Account>> iterator = accounts.entrySet().iterator();
		while (iterator.hasNext()) {
			
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) iterator.next();
			
			Account account = (Account) pairs.getValue();
			
			sql = "INSERT INTO " + tableName + " () VALUES ( " + account.getAccountNumber() + ", " + account.getBalance() + ", " + account.getInterestRate() + " );";
			DBConnection.connection.createStatement().executeUpdate(sql);
			
			iterator.remove();
		}
		
		
		sql = "DELETE FROM accountUpdate;";
		DBConnection.connection.createStatement().executeUpdate(sql);
		
		// 4. 
		
	}

	/*
	 * Gets all accounts from the table
	 */
	public static Map<String, Account> getAccounts(String tableName) throws SQLException {
		
		String sql = "SELECT * FROM " + tableName + ";";
		ResultSet resultSet = DBConnection.connection.createStatement().executeQuery(sql);
		
		Map<String, Account> accounts = new HashMap<String, Account>();
	
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
			Statement statement = DBConnection.connection.createStatement();
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