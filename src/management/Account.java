package management;

public final class Account {
	private String accountNumber;
	private float balance;
	private float interestRate;
	
	/*
	 * Constructors
	 */
	public Account(String accountNumber, float balance, float interestRate) {
		this.setAccountNumber(accountNumber);
		this.setBalance(balance);
		this.setInterestRate(interestRate);
	}

	/*
	 * Get/Set
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
		
		// Make sure the accountNumber has a proper length
		if (accountNumber.length() == 8) {
			this.accountNumber = accountNumber;
			return true;
		}
		
		return false;
	}
	
	protected String getAccountNumber() {
		return this.accountNumber;
	}
	
	protected float getBalance() {
		return this.balance;
	}

	protected boolean setBalance(float balance) {
		if (this.balance < 0) return false;
		
		this.balance = balance;
		return true;
	}

	protected float getInterestRate() {
		return this.interestRate;
	}

	protected boolean setInterestRate(float interest) {
		if (interest < 0) return false;
		
		this.interestRate = interest;
		return true;
	}
}