
public class Bank {

	private double balance;
	private Player[] playerAccounts;
	private String[] transactionHistory;
	private String transactionType;

	public Bank(double balance, Player[] playerAccounts, String[] transactionHistory, String transactionType) {
		this.balance = balance;
		this.playerAccounts = playerAccounts;
		this.transactionHistory = transactionHistory;
		this.transactionType = transactionType;
	}

	// Method to get all player accpunts in the bank
	public Player[] getAllAccounts() {
		return playerAccounts;
	}

	// Method to add a new playr account to the bank
	public void addAccount(Player account) {
		// Check if the array is full and resize if needed
		if (playerAccounts.length == getNumberOfAccounts()) {
			resizeAccountsArray();
		}

		// Add the new account to the array
		playerAccounts[getNumberOfAccounts()] = account;

		// Increment the counter for the number of accounts
		// incrementNumOfAccounts();

	}

	// Method to delete a player from the bank
	public void deleteAccount(Player account) {

		int index = findAccountIndex(account);

		if (index != -1) {
			playerAccounts[index] = null;

			shiftAccounts(index);

			// decrementNumOfAccounts();
		}
	}

	// Method to find the index of an account
	private int findAccountIndex(Player account) {
		for (int i = 0; i < playerAccounts.length; i++) {
			if (playerAccounts[i] != null && playerAccounts[i].equals(account)) {
				return i;
			}
		}
		// If account not found
		return -1;
	}

	// Method to get Number Of Accounts
	private int getNumberOfAccounts() {
		int count = 0;
		for (int i = 0; i < playerAccounts.length; i++) {
			Player account = playerAccounts[i];

			if (account != null) {
				count++;
			}
		}
		return count;
	}

	// Method to resize the array
	private void resizeAccountsArray() {
		int newCapacity = playerAccounts.length * 2;
		Player[] newPlayerAccount = new Player[newCapacity];

		System.arraycopy(playerAccounts, 0, newPlayerAccount, 0, playerAccounts.length);

		playerAccounts = newPlayerAccount;
	}

	// Method to shuft accounts
	private void shiftAccounts(int startIndex) {
		for (int i = startIndex; i < playerAccounts.length - 1; i++) {
			playerAccounts[i] = playerAccounts[i + 1];
		}
		playerAccounts[playerAccounts.length - 1] = null;
	}
}
