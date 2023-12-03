import java.net.Socket;
import java.util.ArrayList;

public class Player implements CardPlayer{
	private static int idCounter = 0; 
	
	private String id; 
	
	private String displayName; 
	
	private String password;
	
	private int balance; 
	
	private int currWager; 
	
	private MOVE currMove;
	
	private ArrayList<Card> playerHand; 
	
	private int handValue; 
	
	private Socket socket; 
	
	public Player(){
		id = Integer.toString(idCounter++);
		playerHand = new ArrayList<>(); 
	}
	
	public Player(String name, String pass, int bal) {
		id = Integer.toString(idCounter++);
		displayName = name; 
		password = pass; 
		// balance = some minimum value to start off
		balance = bal; 
		playerHand = new ArrayList<>(); 
		
	}
	
	public void deposit(int amount) {
		balance += amount; 
	}
	
	@Override
	public String toString() {
		
		String hand = ""; 
		for(Card card : playerHand) {
			hand += card.getCardRank() + " of " + card.getCardSuit() + ", ";
		}
		return "\nid=" + id + "\ndisplayName=" + displayName + "\nbalance=" + balance + "\ncurrWager=" + currWager
				+ "\ncurrMove=" + currMove.name() + "\nplayerHand=" + hand + "\nhandValue=" + handValue;
	}

	public void withdraw(int amount) {
		if(amount > balance) {
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

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getCurrWager() {
		return currWager;
	}

	public void setCurrWager(int currWager) {
		this.currWager = currWager;
	}

	@Override
	public MOVE getCurrMove() {
		return currMove;
	}
	@Override
	public void setCurrMove(MOVE currMove) {
		this.currMove = currMove;
	}

	@Override
	public ArrayList<Card> getPlayerHand(){
		return this.playerHand; 
	}
	@Override
	public int getHandValue() {// edit this later 
		this.calcHandValue();
		return handValue;
	}
	@Override
	public void calcHandValue() {
		handValue = 0; 
		for(Card card : this.getPlayerHand()) {
			handValue += card.getCardValue(); 
		}
		
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	

//	public void setHandValue(int handValue) {
//		this.handValue = handValue;
//	}
	
	
	
}