import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Table {
	
	private int id; // might be needed for keeping track of each game
	
	private ArrayList<Player> players; 
	
	private ArrayList<Deck> decks; 
	
	private Dealer dealer; 
	
    private int currentPlayers;
    
 //   private ArrayList<ObjectOutputStream> outputsToClients; 
	
    
    
    
	public void addPlayer(Player newPlayer) { // adds a play to join the next available round
		if(currentPlayers < 7) {
			players.add(newPlayer); 
		}
		// either update currentPlayers here or wait until the player can join the round
	}
	
	
	
	
	public void removePlayer(Player player) {
		
	}

	
	
	
	
	public void BlackjackGame() {// runs the game 
		// Accept wagers from each player (pre-conditon: All players have minimum required balance) 
		
		while(currentPlayers > 0) {
			RoundOfBlackJack();
			if(currentPlayers < players.size()) {// wait until the end of a round to increase # of active players
				currentPlayers++; 
			}
		}
		
	}
	
	
	
	
	
	public void RoundOfBlackJack() {
		for(int i = 0; i < currentPlayers; i++) {// check the balances for minimum wager required
			 // if balance is low, notify player
			 // Otherwise, accept a wager amount 
		}
		
		// deal cards to each player
		dealHands(); // NOTE: Consider the value of Ace cards when drawn 1/11, maybe have two handValues or ask player to choose
		
		// Each player is given a turn to hit/stand
		for(int i = 0; i < currentPlayers; i++) {
			playerTurn(players.get(i)); 
	
		}
		
		// dealer's turn to play their hand; reveals face-down card, hits if hand value is below 17, stands otherwise
		// NOTE: Consider making a function for each player/dealer to play their hand <-- NO, diff behavior for players 
		// and dealers so two diff functions, playerTurn(), dealerTurn()
		dealerTurn();
		
		
		// Pay winnings to players, take wagers from losers
		distributeWinnings(); 
		
	}
	
	
	
	
	
	public void drawCard(CardPlayer player) { //NOTE: Consider the value of aces, give player a choice or handle automatically?
		
	// if half of total cards have been drawn, set up 3 new decks before continuing
		
		// if(remaining cards < 52*decks.size()), get 3 new decks
		
		// choose random deck and random card from said deck
		Random random = new Random(); 
		int deckNum = random.nextInt(decks.size()); // for one out of x total decks
		int cardNum = random.nextInt(decks.get(deckNum).getNumCards()); // for a random card in deck 0-51 (52 total)
		
		// adds the card from to the player hand
		player.getPlayerHand().add(decks.get(deckNum).getCards()[cardNum]); 
		
		// removes the drawn card from the deck
		decks.get(deckNum).remove(cardNum); 
		
		// update handValue
		player.calcHandValue();
		
		// send updates to client
		
	}
	
	
	
	
	public void dealHands() {// distribute hands to each player as well as the dealer
		// will call drawCards() in a loop 
		
		for(int i = 0; i < 2; i++) {// deals two cards to each player
			for(int j = 0; j < currentPlayers; j++) {
				drawCard(players.get(j));
			}
			drawCard(dealer);
		}
	}
	
	
	
	
	
	public void playerTurn(CardPlayer player) throws IOException, ClassNotFoundException { // Each player will hit/stand using this method

		// send update to the clients
		ObjectOutputStream objOutStream = null;
		ObjectInputStream objInStream = null;
		try {
			if(player instanceof Player) {
				objOutStream = new ObjectOutputStream(((Player) player).getSocket().getOutputStream());
				objInStream = new ObjectInputStream(((Player) player).getSocket().getInputStream());	
			}
			
			boolean endTurn = false; 
			while(!endTurn) {
				player.setCurrMove( (MOVE) objInStream.readObject());
				
				if(player.getCurrMove() == MOVE.hit) {
					drawCard(player); 
				}
				
				if(player.getHandValue() > 21) {
					endTurn = true;  
				}
				
				if(player.getCurrMove() == MOVE.stand) {
					endTurn = true; 
				}
				sendGameState(); // send update after every move
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(objOutStream != null) {
					objOutStream.close(); 
				}
				
				if(objInStream != null) {
					objInStream.close(); 
				}
				
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	public void dealerTurn() {// dealer 
		// similar to playerTurn()
	}
	
	
	
	
	// Compares hands to dealer, rewards winners, takes wagers of losers
	public void distributeWinnings() { 
		for(int i = 0; i < currentPlayers; i++) {
			/* Win/Loss Conditions:
			 * 
			 * All players with hand values greater than the dealer WIN 
			 * Players with 21 automatically WIN
			 * Players that bust automatically lose
			 * Players with a hand value less than the dealer LOSE
			 * If the dealer has 21, all players LOSE except those who also have 21
			 * Players that have the same hand value as the dealer keep their wager and gain no winnings (unless value is 21)
			 */
			int playerBalance = players.get(i).getBalance(); 
			int wager = players.get(i).getCurrWager(); 
			int handValue = players.get(i).getHandValue(); 
			
			if(handValue > 21 || handValue < dealer.getHandValue()) {// Loss conditions
				players.get(i).setBalance(playerBalance - wager); 
			}
			
			
			
		}
	}
	
	
	
	
	// sends updated information through network to the GUI on client side
	public void sendGameState() throws IOException {
		ObjectOutputStream objOutStream = null; 
		for(int i = 0; i < currentPlayers; i++) {
			try {
				Player player = players.get(i); 
				objOutStream = new ObjectOutputStream(player.getSocket().getOutputStream());
				
				// Send the status of player after every move
				objOutStream.writeObject("Hand Value: " + Integer.toString(player.getHandValue()));
				objOutStream.writeObject("Current Move: " + player.getCurrMove().name());
				objOutStream.writeObject("Wager: $" + Integer.toString(player.getCurrWager()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if(objOutStream != null) {
					objOutStream.close(); 
				}
			}
		}
	}
}