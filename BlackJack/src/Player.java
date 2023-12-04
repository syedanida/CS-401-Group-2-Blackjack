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
	}
	
	public Player(String name, String pass) {
		id = Integer.toString(idCounter++);
		displayName = name; 
		password = pass; 
		// balance = some minimum value to start off
		
	}
	
	public Player(String userID, String userPassword, String userName, int userBalance) {
		id = userID;
		password = userPassword;
		displayName = userName;
		balance = userBalance;
	}

	public void deposit(int amount) {
		balance += amount; 
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
	
	public void setPassword(String password) {
		this.password = password;
	}

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

	public MOVE getCurrMove() {
		return currMove;
	}

	public void setCurrMove(MOVE currMove) {
		this.currMove = currMove;
	}

	public ArrayList<Card> getPlayerHand(){
		return this.playerHand; 
	}
	
	public int getHandValue() {// edit this later 
		this.calcHandValue();
		return handValue;
	}
	
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

	public Object getPassword() {
		return password;
	}
	

//	public void setHandValue(int handValue) {
//		this.handValue = handValue;
//	}
	
	
	
}