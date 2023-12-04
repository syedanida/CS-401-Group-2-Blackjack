import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class DeckTest {

	@Test
	void testDeckInitialization() {
		Deck deck = new Deck();

		assertNotNull(deck);
		assertEquals(52, deck.getCards().size());

		for (int i = 0; i < deck.getCards().size(); i++) {
			assertEquals(i + 1, deck.getCards().get(i).getCardValue());
		}
	}

	@Test
	void testShuffle() {
		Deck deck = new Deck();
		ArrayList<Card> originalOrder = new ArrayList<>(deck.getCards());

		deck.shuffle();

		assertEquals(52, deck.getCards().size());

		assertNotEquals(originalOrder, deck.getCards());
	}

}
