/**
 * @author Theneva
 * @version 1.0
 * Due Date 2012.10.22
 */

package management;

public final class Account {
	private String accountNumber;
	private float balance;
	private float interestRate;

	/**
	 * No default constructor in order to avoid duplicate entries in the
	 * database.
	 * 
	 * @param accountNumber
	 *            the account number.
	 * @param balance
	 *            the balance.
	 * @param interestRate
	 *            the interest rate.
	 */
	public Account(String accountNumber, float balance, float interestRate) {
		this.setAccountNumber(accountNumber);
		this.setBalance(balance);
		this.setInterestRate(interestRate);
	}

	// get/set

	/**
	 * Gets the account number.
	 * 
	 * @return the account number.
	 */
	protected String getAccountNumber() {
		return this.accountNumber;
	}

	/**
	 * Sets the account number.
	 * 
	 * @param accountNumber
	 *            the new account number
	 * @return whether or not the account number was updated.
	 */
	protected boolean setAccountNumber(String accountNumber) {
		// Make sure the number is an actual number and above 0
		try {
			if (Integer.parseInt(accountNumber) < 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}

		// Make sure the accountNumber has a proper length (as defined by the
		// assignment)
		if (accountNumber.length() == 8) {
			this.accountNumber = accountNumber;
			return true;
		}

		return false;
	}

	/**
	 * Gets the balance.
	 * 
	 * @return the balance.
	 */
	protected float getBalance() {
		return this.balance;
	}

	/**
	 * Sets the balance.
	 * 
	 * @param balance
	 *            the new balance.
	 * @return whether or not the balance was updated.
	 */
	protected boolean setBalance(float balance) {
		if (this.balance < 0)
			return false;

		this.balance = balance;
		return true;
	}

	/**
	 * Gets the interest rate.
	 * 
	 * @return the interest rate.
	 */
	protected float getInterestRate() {
		return this.interestRate;
	}

	/**
	 * Sets the interest rate.
	 * 
	 * @param interestRate
	 *            the new interest rate.
	 * @return whether or not the interest rate was updated.
	 */
	protected boolean setInterestRate(float interestRate) {
		if (interestRate < 0)
			return false;

		this.interestRate = interestRate;
		return true;
	}
}