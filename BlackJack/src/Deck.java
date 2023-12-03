import java.util.ArrayList;

public class Deck {
	//private Card[] cards; // Array to store the deck of cards
	private ArrayList<Card> cards; 
	//private int numCards; // Total number of cards in the deck
	
	public Deck() {
	//	this.numCards = 52; // Initializing the total number of cards in a standard deck
		initializeDeck(); // Creating and initializing the deck of cards
	}

	// Getter method to access the array of cards
	public ArrayList<Card> getCards() {
		return cards;
	}
	

	private void initializeDeck() {
		// Creating a new array to hold the cards
		//Card[] newDeck = new Card[numCards];

		cards = new ArrayList<>(); 
		// Index to keep track of the position in the new deck array
		//int index = 0;

		// Obtaining all possible suit values (enum)
		Suit[] suits = Suit.values();

		// Loop to iterate over each suit and value to create a deck of cards
		for (int i = 0; i < suits.length; i++) {
			// Current suit in the iteration
			Suit suit = suits[i];

			// Loop through each value (1 to 13) to create cards for the current suit
			for (int cardNum = 1; cardNum <= 13; cardNum++) { 
				// Creating a new card
				cards.add(new Card(determineRank(cardNum), suit, cardNum));
				// The Card() constructor handles values > 10
			}
		}
		// Returning the initialized deck of cards
		//return newDeck;
	}

	private String determineRank(int value) {
		// Switch statement to determine the rank of a card based on its value
		switch (value) {
		case 1:
			return "Ace"; // ---> Check this!
		case 11:
			return "Jack";
		case 12:
			return "Queen";
		case 13:
			return "King";
		default:
			return String.valueOf(value);
		}
	}
	
}