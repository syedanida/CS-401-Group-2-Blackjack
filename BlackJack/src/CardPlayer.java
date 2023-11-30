import java.util.ArrayList;

public interface CardPlayer {
	public ArrayList<Card> getPlayerHand(); 
	public int getHandValue(); 
	public void calcHandValue();
	public void setCurrMove(MOVE currMove);
	public MOVE getCurrMove(); 
	
	
}