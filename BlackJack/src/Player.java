import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Player implements CardPlayer {
	private static int idCounter = 0;
	private String id;
	private String displayName;
	private String password;
	private double balance;
	private double currWager;
	private MOVE currMove;
	private ArrayList<Card> playerHand;
	private int handValue;
	private double winnings; 
	private Socket socket;
	private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

	public Player() {
		id = Integer.toString(idCounter++);
		playerHand = new ArrayList<>();
	}

	public Player(String name, String pass, double bal) {
		id = Integer.toString(idCounter++);
		displayName = name;
		password = pass;
		// balance = some minimum value to start off
		balance = bal;
		playerHand = new ArrayList<>();

	}

	public Player(String userID, String pass, String name, double bal) {
		id = userID;
		password = pass;
		displayName = name;
		balance = bal;
		playerHand = new ArrayList<>();

	}

	public void deposit(double amount) {
		balance += amount;
	}

	public String toString() {
		String result = "\nid =" + id + "\ndisplayName =" + displayName + "\nbalance =" + balance + "\ncurrWager ="
				+ currWager + "\ncurrMove =" + currMove.name() + "\nplayerHand =" + "\nhandValue =" + handValue;

		for (int i = 0; i < playerHand.size(); i++) {
			Card card = playerHand.get(i);

			result += card.getCardRank() + " of " + card.getCardSuit();
			if (i < playerHand.size() - 1) {
				result += ", ";
			}
		}

		result += "\nhandValue = " + handValue;
		return result;

//		String hand = "";
//		for (Card card : playerHand) {
//			hand += card.getCardRank() + " of " + card.getCardSuit() + ", ";
//		}
//		return "\nid=" + id + "\ndisplayName=" + displayName + "\nbalance=" + balance + "\ncurrWager=" + currWager
//				+ "\ncurrMove=" + currMove.name() + "\nplayerHand=" + hand + "\nhandValue=" + handValue;
	}

	public void withdraw(double amount) {
		if (amount > balance) {
			// notify of insufficient funds
			return;
		}
		balance -= amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	// May not require setter for password

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getCurrWager() {
		return currWager;
	}

	public void setCurrWager(double currWager) {
		this.currWager = currWager;
	}

	public MOVE getCurrMove() {
		return currMove;
	}

	public void setCurrMove(MOVE currMove) {
		this.currMove = currMove;
	}

	public ArrayList<Card> getPlayerHand() {
		return this.playerHand;
	}

	public int getHandValue() {
		this.calcHandValue();
		return handValue;
	}

	public void calcHandValue() {
		handValue = 0;
		int numAces = 0;

		for (int i = 0; i < this.getPlayerHand().size(); i++) {
			Card card = this.getPlayerHand().get(i);
			handValue += card.getCardValue();

			if ("Ace".equals(card.getCardRank()) && card.getCardValue() == 11) {
				numAces++;
			}
		}
		while (handValue > 21 && numAces > 0) {
			handValue -= 10;
			numAces--;
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(Object pass) { // Not sure why this is type object but i casted it to String
		this.password = (String) pass;
	}

	public double getWinnings() {
		return winnings;
	}

	public void setWinnings(double winnings) {
		this.winnings = winnings;
	}
	
	public void setInOutObjectStream(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}
	
	public ObjectOutputStream getObjectOutStream() {
		return outputStream;
	}
	
	public ObjectInputStream getObjectInStream() {
		return inputStream;
	}
	
}