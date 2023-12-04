import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CardTest {

	@Test
	public void testIsCardPresent() {
		Card card = new Card("Ace", Suit.SPADES, 1);
		assertTrue(card.isCardPresent());
		card.setDrawn(true);
		assertFalse(card.isCardPresent());
	}

	@Test
	public void testGetCardRank() {
		Card card = new Card("King", Suit.HEARTS, 10);
		assertEquals("King", card.getCardRank());
	}

	@Test
	void testCardInitialization() {
		Card card = new Card("Ace", Suit.SPADES, 1);

		assertEquals("Ace", card.getCardRank());
		assertEquals(Suit.SPADES, card.getCardSuit());
		assertEquals(11, card.getCardValue()); // Since cardNum is 1, the default value for Ace should be 11
		assertTrue(card.isCardPresent());
	}

	@Test
	void testCardDrawn() {
		Card card = new Card("Queen", Suit.HEARTS, 12);

		assertTrue(card.isCardPresent());

		// Draw the card
		card.setDrawn(true);

		assertFalse(card.isCardPresent());
	}

	// Add more test cases as needed to cover other methods and scenarios

}
