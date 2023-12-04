import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DealerTest {

	private Dealer dealer;

	@BeforeEach
	public void setUp() {
		dealer = new Dealer();
	}

	@Test
	public void testPlayerHandInitialization() {
		// Ensure that the player hand is not null after initialization
		assertNotNull(dealer.getPlayerHand());
	}

	@Test
	public void testInitialHandValue() {
		// Check that the initial hand value is 0
		assertEquals(0, dealer.getHandValue());
	}

	@Test
	public void testCalcHandValueWithNoAces() {
		// Add cards to the player's hand without Aces
		dealer.getPlayerHand().add(new Card("Jack", Suit.HEARTS, 10));
		dealer.getPlayerHand().add(new Card("King", Suit.DIAMONDS, 10));

		// Calculate the hand value
		dealer.calcHandValue();

		// Check that the hand value is the sum of card values
		assertEquals(20, dealer.getHandValue());
	}

	@Test
	public void testSetAndGetCurrMove() {
		// Set the current move to HIT
		dealer.setCurrMove(MOVE.hit);

		// Check that the current move is HIT
		assertEquals(MOVE.hit, dealer.getCurrMove());
	}
}
