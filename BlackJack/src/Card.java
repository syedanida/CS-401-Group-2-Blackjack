// Enum representing the four suits in a standard deck of cards

import java.awt.Image;
import javax.swing.ImageIcon;

enum Suit {
	CLUBS, DIAMONDS, HEARTS, SPADES
}

public class Card {

	private boolean isDrawn; // Flag if the card has been drawn
	private String rank; // Rank of the card
	private Suit suit; // Suit of the card
	private int value; // Values of the card
	private ImageIcon cardFront; 
	private ImageIcon cardBack; 
	private int width = 50;
	private int height = 75;

	// Constructor to initialize a Card object with rank, suit and value
	public Card(String rank, Suit suit, int cardNum) {
		this.isDrawn = false;
		this.rank = rank;
		this.suit = suit;
		
		// Ensures J, Q, K, do not exceed value 10
		if (cardNum > 10) {
			this.value = 10;
		} else if (cardNum == 1) {
			this.value = 11; 			// default Ace value
		} else {
			this.value = cardNum;
		}
		
		this.cardFront = loadFrontsideImage(); 
		
		// setting up the backside of the cards
		ImageIcon img = new ImageIcon("data/cardImages/Back Red 1.png"); // path for the image
		Image resizedImage = img.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // rescaled size
		this.cardBack = new ImageIcon(resizedImage);// Assign image for back of the card

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
	
	public ImageIcon getCardFront() {
		return cardFront;
	}
	
	public ImageIcon getCardBack() {
		return cardBack;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private ImageIcon loadFrontsideImage() {
		String imagePath = generatePath(); // the path for the image
		ImageIcon img = new ImageIcon(imagePath); 
		Image resizedImage = img.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); 

		return new ImageIcon(resizedImage);  // consider a default image
	}
	
	private String generatePath() {
//		System.out.println("data/cardImages/" + this.rank + "_of_" + this.suit.name().toLowerCase() + ".png");
		return "data/cardImages/" + this.rank + "_of_" + this.suit.name().toLowerCase() + ".png";
	}
}