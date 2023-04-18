import java.util.ArrayList;

public abstract class Player {

	private String name;
	private ArrayList<Cards> hand;

	public Player(String name, ArrayList<Cards> hand) {
		this.setName(name);
		this.setHand(hand);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Cards> getHand() {
		return hand;
	}

	public void setHand(ArrayList<Cards> hand) {
		this.hand = hand;
	}

}
