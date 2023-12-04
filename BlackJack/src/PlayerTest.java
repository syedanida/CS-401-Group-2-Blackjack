import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class PlayerTest {

	// Test depositing funds into the player's balance
	@Test
	public void testDeposit() {
		Player player = new Player("John", "password", 100.0);

		player.deposit(50.0);

		assertEquals(150.0, player.getBalance(), "Balance should be 150.0 after deposit");
	}

	// Test withdrawing funds from the player's balance with sufficient funds
	@Test
	public void testWithdrawSufficientFunds() {
		Player player = new Player("Alice", "password", 100.0);

		player.withdraw(50.0);

		assertEquals(50.0, player.getBalance(), "Balance should be 50.0 after withdrawal");
	}

	// Test withdrawing funds from the player's balance with insufficient funds
	@Test
	public void testWithdrawInsufficientFunds() {
		Player player = new Player("Bob", "password", 30.0);

		player.withdraw(50.0);

		assertEquals(30.0, player.getBalance(), "Balance should remain 30.0 due to insufficient funds");
	}

	// Test calculating the hand value for the player's hand
	@Test
	public void testCalcHandValue() {
		Player player = new Player("Mary", "password", 100.0);

		Card card1 = new Card("Jack", Suit.HEARTS, 11);
		Card card2 = new Card("Three", Suit.SPADES, 3);

		player.getPlayerHand().addAll(Arrays.asList(card1, card2));

		player.calcHandValue();

		// Verify that the hand value is calculated correctly
		assertEquals(13, player.getHandValue(), "Hand value should be 13 (Jack + Three)");
	}
}
