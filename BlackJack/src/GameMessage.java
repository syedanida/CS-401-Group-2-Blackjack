
public class GameMessage {
	private MessageType type; 
	private String text; 
	
	public GameMessage(MessageType type) {
		this.type = type;
		this.text = null; 
	}
	
	public GameMessage(MessageType type, String text) {
		this.type = type; 
		this.text = text; 
	}
}
