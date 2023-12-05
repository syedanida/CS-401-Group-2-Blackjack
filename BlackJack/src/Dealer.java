import java.util.ArrayList;

public class Dealer implements CardPlayer {

	private MOVE currMove;
	private ArrayList<Card> playerHand;
	private int handValue;

	public Dealer() {
		playerHand = new ArrayList<>();
		handValue = 0;
	}

	public ArrayList<Card> getPlayerHand() {
		return playerHand;
	}

	public void calcHandValue() {
		handValue = 0;
		int numAces = 0;

		for (int i = 0; i < this.getPlayerHand().size(); i++) {
			Card card = this.getPlayerHand().get(i);
			handValue += card.getCardValue();

			if ("Ace".equals(card.getCardRank()) && card.getCardValue() == 11) {
				numAces++;
			}
		}
		while (handValue > 21 && numAces > 0) {
			handValue -= 10;
			numAces--;
		}
	}

	public int getHandValue() {
		this.calcHandValue();
		return handValue;
	}

	public MOVE getCurrMove() {
		return currMove;
	}

	public void setCurrMove(MOVE currMove) {
		this.currMove = currMove;
	}

	public void setHandValue(int handValue) {
		this.handValue = handValue;
	}
<<<<<<< HEAD
}
=======
}
>>>>>>> main
