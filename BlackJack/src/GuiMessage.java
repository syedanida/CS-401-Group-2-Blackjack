import java.util.ArrayList;

public class GuiMessage {
	private MessageType type; 
	private String id; 
	private String displayName; 
	private ArrayList<Card> hand; 
	private double wager; 
	private double winnings; 
	
	public GuiMessage(MessageType type,Player player) {
		this.type = type;
		this.id = player.getId(); 
		this.displayName = player.getDisplayName(); 
		this.hand = player.getPlayerHand(); 
		this.wager = player.getCurrWager();
		//this.winnings = winnings; 
	}
}