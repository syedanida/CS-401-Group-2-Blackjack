import java.util.ArrayList;

public class Dealer implements CardPlayer{
	
	private MOVE currMove;
	
	private ArrayList<Card> playerHand;

	private int handValue; 
	
	
	public Dealer() {
		playerHand= new ArrayList<>(); 
		handValue = 0; 
	}

	@Override
	public ArrayList<Card> getPlayerHand() {
		// TODO Auto-generated method stub
		return playerHand;
	}

	@Override
	public void calcHandValue() {
		// TODO Auto-generated method stub
		handValue = 0; 
		for(Card card : playerHand) {
			handValue += card.getCardValue();
		}
	} 
	
	
	
	@Override
	public int getHandValue() {
		this.calcHandValue();
		return handValue; 
	}
	@Override
	public MOVE getCurrMove() {
		return currMove;
	}
	@Override
	public void setCurrMove(MOVE currMove) {
		this.currMove = currMove;
	}
	
	public void setHandValue(int handValue) {
		this.handValue = handValue;
	}
	
	
	
	
}










//import java.util.ArrayList;
//
//public class Dealer implements CardPlayer{
//	
//	private MOVE currMove;
//	
//	private ArrayList<Card> playerHand;
//
//	private int handValue; 
//	
//
//	@Override
//	public ArrayList<Card> getPlayerHand() {
//		// TODO Auto-generated method stub
//		return playerHand;
//	}
//
//	@Override
//	public void calcHandValue() {
//		// TODO Auto-generated method stub
//		handValue = 0; 
//		for(Card card : playerHand) {
//			handValue += card.getCardValue();
//		}
//	} 
//	
//	@Override
//	public int getHandValue() {
//		this.calcHandValue();
//		return handValue; 
//	}
//
//	public MOVE getCurrMove() {
//		return currMove;
//	}
//
//	public void setCurrMove(MOVE currMove) {
//		this.currMove = currMove;
//	}
//
//	public void setHandValue(int handValue) {
//		this.handValue = handValue;
//	}
//	
//}