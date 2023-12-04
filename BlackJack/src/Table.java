import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import Message.MessageType;

public class Table {

	private int id; // might be needed for keeping track of each game
	private ArrayList<Player> players;
	private ArrayList<Deck> decks;
	private Dealer dealer;
	private int currentPlayers;
	
	// for gui input/output
	 Map<String,ObjectOutputStream> outputStreams;
	 Map<String, ObjectInputStream> inputStreams; 

	// for console
	private Scanner scanner = new Scanner(System.in);

	Table() {
		players = new ArrayList<>();
		decks = new ArrayList<>();

		// Set up list of decks
		for (int i = 0; i < 3; i++) {
			decks.add(new Deck());
		}
		dealer = new Dealer();
		currentPlayers = 0;
		
		// Set up streams for network comms
		outputStreams = new HashMap<>(); 
		inputStreams = new HashMap<>(); 

		// Shuffle the decks at the beginning of each round
		shuffleDecks();
	}

	// Adds a play to join the next available round
	public void addPlayer(Player newPlayer) {
		if (currentPlayers < 7) {
			players.add(newPlayer);

			// if this is the first player to join, automatically consider as an active
			// player
			if (currentPlayers == 0) {
				currentPlayers++;
			}
			// Create new input and output streams for network comms to client
			try {
				ObjectOutputStream outStream = new ObjectOutputStream(newPlayer.getSocket().getOutputStream()); 
				ObjectInputStream instream = new ObjectInputStream(newPlayer.getSocket().getInputStream()); 
				// add to lists of streams
				outputStreams.put(newPlayer.getId(), outStream); 
				inputStreams.put(newPlayer.getId(),instream); 
				
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void removePlayer(Player player) {
		try {
			// Maybe a goodbye before removal
			
			// Close streams
			outputStreams.get(player.getId()).close(); 
			inputStreams.get(player.getId()).close();
			
			// remove streams from map
			outputStreams.remove(player.getId()); 
			inputStreams.remove(player.getId()); 
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		players.remove(player);
		currentPlayers--;
	}

	public boolean isTableFull() {
		return players.size() >= 7;
	}

	// Runs the game
	public void BlackjackGame() throws ClassNotFoundException, IOException {
		// Consider new thread for updating status of the game periodically
		while (players.size() > 0) {
			// Play a round of blackjack
			RoundOfBlackJack();

			// Ask players if they would like to play again
			for (int i = 0; i < currentPlayers; i++) {
				String choice;
				do {
					System.out.println("\nWould you like to play the next round (Y or N)?");
					choice = scanner.nextLine();
					// clear buffer
					// scanner.nextLine();
				} while (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n"));

				// Remove players if they wish to quit
				if (choice.equalsIgnoreCase("n")) {
					removePlayer(players.get(i));
				}
			}
			// if the number of active players is less than the size of the actual
			// arrayList, there are players waiting to join
			if (currentPlayers < players.size()) {
				// Ensures the next round includes any players that were waiting
				currentPlayers++;
			}

			// Reset all player hands
			resetHands();
		}
		scanner.close();
	}

	public void RoundOfBlackJack() throws ClassNotFoundException, IOException {
		try {
			for (int i = 0; i < currentPlayers; i++) {
				// check the balances for minimum wager required
				// if balance is low, notify player
				if (players.get(i).getBalance() < 500) {
					System.out.println("\nYour balance is too low");
				}

				int wager;
//				GameMessage msg = new GameMessage(MessageType.WAGER); 
//				outputStreams.get(players.get(i).getId()).writeObject(msg);
//				
				do {
					// Players input a valid wager amount
					System.out.println("\nEnter wager amount: ");
					wager = scanner.nextInt();
					players.get(i).setCurrWager(wager);
					if (wager > players.get(i).getBalance()) {
						System.out.println("\nInsufficient balance\n");
					}
				} while (wager > players.get(i).getBalance());
				// clear buffer
				scanner.nextLine();
			}

			// deal cards to each player
			dealHands();

			// Give each player a turn
			for (int i = 0; i < currentPlayers; i++) {
				System.out.println("\n" + players.get(i).getDisplayName() + "'s Turn");
				playerTurn(players.get(i));
			}

			// Dealer's turn
			System.out.println("\nDealer's Turn");
			playerTurn(dealer);

			// Pay winnings to winners, take wagers from losers
			distributeWinnings();
		}catch(IOException e) {
			
		}
// FROM THE CONSOLE VERSION
//		for (int i = 0; i < currentPlayers; i++) {
//			// check the balances for minimum wager required
//			// if balance is low, notify player
//			if (players.get(i).getBalance() < 500) {
//				System.out.println("\nYour balance is too low");
//			}
//
//			int wager;
//
//			do {
//				// Players input a valid wager amount
//				System.out.println("\nEnter wager amount: ");
//				wager = scanner.nextInt();
//				players.get(i).setCurrWager(wager);
//				if (wager > players.get(i).getBalance()) {
//					System.out.println("\nInsufficient balance\n");
//				}
//			} while (wager > players.get(i).getBalance());
//			// clear buffer
//			scanner.nextLine();
//		}
//
//		// deal cards to each player
//		dealHands();
//
//		// Give each player a turn
//		for (int i = 0; i < currentPlayers; i++) {
//			System.out.println("\n" + players.get(i).getDisplayName() + "'s Turn");
//			playerTurn(players.get(i));
//		}
//
//		// Dealer's turn
//		System.out.println("\nDealer's Turn");
//		playerTurn(dealer);
//
//		// Pay winnings to winners, take wagers from losers
//		distributeWinnings();
	}

	public void drawCard(CardPlayer player) {
		// Checking if we need to shuffle the decks
		if (needToshuffle()) {
			shuffleDecks();
		}

		// if half of total cards have been drawn, set up 3 new decks before continuing

		// if(remaining cards < 52*decks.size()), get 3 new decks

		// choose random deck and random card from said deck
		Random random = new Random();
		int deckNum = random.nextInt(decks.size()); // for one out of x total decks
		int cardNum = random.nextInt(decks.get(deckNum).getCards().size()); // for a random card in deck 0-51 (52 total)

		// adds the card from to the player hand
		player.getPlayerHand().add(decks.get(deckNum).getCards().get(cardNum));

		// removes the drawn card from the deck
		decks.get(deckNum).getCards().remove(cardNum);

		// update handValue
		player.calcHandValue();

		// send updates to client

	}

	public boolean needToshuffle() {
		int totalCardsRemaining = 0;
		for (int i = 0; i < decks.size(); i++) {
			totalCardsRemaining += decks.get(i).getCards().size();
		}
		return totalCardsRemaining < (52 * decks.size() / 2);
	}

	public void shuffleDecks() {
		for (int i = 0; i < decks.size(); i++) {
			decks.get(i).shuffle();
		}
	}

	public void dealHands() {
		// distribute hands to each player as well as the dealer
		// will call drawCards() in a loop

		for (int i = 0; i < 2; i++) {
			// deals two cards to each player
			for (int j = 0; j < currentPlayers; j++) {
				drawCard(players.get(j));
			}
			drawCard(dealer);
		}

	}

	// Each player gets a turn
	public void playerTurn(CardPlayer participant) throws IOException, ClassNotFoundException {

		boolean endTurn = false;

		// Runs until "Bust" or "Stand"
		while (!endTurn) {
			// Display hand
			System.out.println("\nHand Value: " + participant.getHandValue());
			System.out.println("Hand size: " + participant.getPlayerHand().size());
			// if it's the dealer's turn, always hit when < 17, otherwise stand
			if (participant instanceof Dealer) {
				if (participant.getHandValue() < 17) {
					participant.setCurrMove(MOVE.hit); // NOTE: Be sure to notify GUI to flip face down card
				} else {
					participant.setCurrMove(MOVE.stand);
				}
			} else {// If it is an actual player's turn, allow freedom of choice
				System.out.println("Enter move 'hit' or 'stand': ");
				String moveInput = scanner.nextLine();
				participant.setCurrMove(MOVE.valueOf(moveInput.toLowerCase()));// set the move
			}

			// Process participant's decisions
			if (participant.getCurrMove() == MOVE.hit) {
				drawCard(participant);
			}

			// check if it is necessary to end the participant's turn
			if (participant.getHandValue() >= 21 || participant.getCurrMove() == MOVE.stand) {
				// Display hand before exit
				System.out.println("\nEnd of Turn, Final Hand Value: " + participant.getHandValue());
				endTurn = true;
			}
			// sendGameState();
		}

	}

	// Compares hands to dealer, rewards winners, takes wagers of losers
	public void distributeWinnings() {
		for (int i = 0; i < currentPlayers; i++) {
			/*
			 * Win/Loss Conditions:
			 * 
			 * All players with hand values greater than the dealer WIN Players with 21
			 * automatically WIN Players that bust automatically lose Players with a hand
			 * value less than the dealer LOSE If the dealer has 21, all players LOSE except
			 * those who also have 21 Players that have the same hand value as the dealer
			 * keep their wager and gain no winnings (unless value is 21)
			 */
			int initialBalance = players.get(i).getBalance();
			int wager = players.get(i).getCurrWager();
			int handValue = players.get(i).getHandValue();
			String playerName = players.get(i).getDisplayName();

			if (handValue > 21 || (dealer.getHandValue() < 21 && handValue < dealer.getHandValue())) {// Loss conditions
				System.out.println("\n" + players.get(i).getDisplayName() + " has lost the round.");
				System.out
						.println("\nBEFORE: \n" + playerName + "\n\tBalance: " + initialBalance + "\n\tLoss: " + wager);

				players.get(i).setBalance(initialBalance - wager);
				System.out.println("\n\nAFTER: \n" + playerName + "\n\tBalance: " + players.get(i).getBalance());

			} else if (handValue == 21 || handValue > dealer.getHandValue() || dealer.getHandValue() > 21) {// win
																											// conditions
				System.out.println("\n" + playerName + " has won the round.");
				System.out.println("\nBEFORE: \n\t" + playerName + "\n\tBalance: " + initialBalance + "\n\tWinnings: "
						+ Integer.toString(2 * wager));
				players.get(i).setBalance(initialBalance + 2 * wager);

				System.out.println("\nAFTER: \n" + playerName + "\n\tBalance: " + players.get(i).getBalance());
			} else {
				System.out.println("\n" + playerName + " has matched the dealer's hand, wager is returned");
			}

		}
	}

	// Reset all hands for the next round
	public void resetHands() {
		for (Player player : players) {
			player.getPlayerHand().clear();
		}
		dealer.getPlayerHand().clear();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

//	public void sendRealTimeUpdates() {
//		
//	}
	// NOTE FOR LATER: Nested for loops

	// sends updated information through network to the GUI on client side
	public void sendGameState() throws IOException {
		for(Player player : players) {
			for(int i = 0; i < currentPlayers; i++) {
				try {
					//outputStreams.get(players.get(i).getId()).writeObject(players.get(i).getPlayerHand());
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}