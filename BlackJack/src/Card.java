// Enum representing the four suits in a standard deck of cards
enum Suit {
	CLUB, DIAMOND, HEART, SPADE
}

public class Card {

	private boolean isDrawn; // Flag ifthe card has been drawn
	private String rank; // Rank of the card
	private Suit suit; // Suit of the card
	private int value; // Values of the card

	// Constructor to initialize a Card object with rank, suit and value
	public Card(String rank, Suit suit, int value) {
		this.isDrawn = false;
		this.rank = rank;
		this.suit = suit;
		this.value = value;
	}

	// Method to check if the card is currently present
	public boolean isCardPresent() {
		return !isDrawn;
	}

	// Method to retrieve the rank of the card
	public String getCardRank() {
		return rank;
	}

	// Method to retrieve the suit of the card
	public Suit getCardSuit() {
		return suit;
	}

	// Method to retrieve the numeric value of the card
	public int getCardValue() {
		return value;
	}
}
