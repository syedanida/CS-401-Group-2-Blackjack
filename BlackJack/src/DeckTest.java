import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class DeckTest {

	@Test
	public void testDeckInitialization() {
		Deck deck = new Deck();

		// Ensure that the deck is not null
		assertNotNull(deck);

		// Check that the deck has the expected number of cards (52)
		assertEquals(52, deck.getCards().size());

		// Retrieve the cards from the deck
		ArrayList<Card> cards = deck.getCards();

		// Loop through each card in the deck
		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);

			int cardValue = card.getCardValue();

			// Ensure that the card value is within the valid range (1 to 13)
			assertTrue(cardValue >= 1 && cardValue <= 13);
		}
	}

	@Test
	public void testShuffle() {
		// Create a new deck
		Deck deck = new Deck();

		// Create a copy of the original order of cards in the deck
		ArrayList<Card> originalOrder = new ArrayList<>(deck.getCards());

		deck.shuffle();

		// Check that the deck still has the expected number of cards (52)
		assertEquals(52, deck.getCards().size());

		// Ensure that the order of cards after shuffling is not equal to the original
		// order
		assertNotEquals(originalOrder, deck.getCards());
	}
}
