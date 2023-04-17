import java.util.ArrayList;

public abstract class Player {

	protected String name;
	protected ArrayList<Cards> hand;

	public Player(String name, ArrayList<Cards> hand) {
		this.name = name;
		this.hand = hand;
	}

}
