// Enum representing the four suits in a standard deck of cards

import java.awt.Image;

import javax.swing.ImageIcon;

enum Suit {
	CLUBS, DIAMONDS, HEARTS, SPADES
}

public class Card {

	private boolean isDrawn; // Flag ifthe card has been drawn
	private String rank; // Rank of the card
	private Suit suit; // Suit of the card
	private int value; // Values of the card
	private ImageIcon cardFront;
	private ImageIcon cardBack;

	// Constructor to initialize a Card object with rank, suit and value
	public Card(String rank, Suit suit, int cardNum) {
		this.isDrawn = false;
		this.rank = rank;
		this.suit = suit;

		// Ensures J, Q, K, do not exceed value 10
		if (cardNum > 10) {
			this.value = 10;
		} else {

			this.value = cardNum;
		}

		this.cardFront = loadFrontsideImage();

		// setting up the backside of the cards
		// path for the image
		ImageIcon img = new ImageIcon("C:\\Users\\dqjon\\Documents\\books\\Software Engineering\\"
				+ "HW Assignments\\BlackJackPracticeCode\\cardImages\\Back Red 1.png");

		// rescaled size
		Image resizedImage = img.getImage().getScaledInstance(75, 100, Image.SCALE_SMOOTH);

		// Assign image for back of the card
		this.cardBack = new ImageIcon(resizedImage);

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

	private ImageIcon loadFrontsideImage() {

		// the path for the image
		String imagePath = generatePath();

		ImageIcon img = new ImageIcon(imagePath);

		Image resizedImage = img.getImage().getScaledInstance(75, 100, Image.SCALE_SMOOTH);

		// consider a default image
		return new ImageIcon(resizedImage);
	}

	private String generatePath() {

		return "C:\\Users\\dqjon\\Documents\\books\\Software Engineering\\HW Assignments"
				+ "\\BlackJackPracticeCode\\cardImages" + this.rank + "_of_" + this.suit.name() + ".png";
	}
}