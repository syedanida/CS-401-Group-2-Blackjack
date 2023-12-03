import java.util.ArrayList;

enum Type {
	LOGININFO, TABLEINFO
}

public class Message {
	
	Type type;
	
	private int tableID;
	private int tableCurrPlayers;
	private ArrayList<Player> tablePlayers;
	private ArrayList<Deck> tableDecks;
	private Dealer tableDealer;

	public Message(Type type) 
	{
		this.type = type;
	}
	
}
