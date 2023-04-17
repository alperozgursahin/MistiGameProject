import java.util.ArrayList;

public class HumanPlayer extends Player {

	public HumanPlayer(String name, ArrayList<Cards> hand) {
		super(name, hand);
	}

	public ArrayList<Cards> getHand() {
		return hand;
	}

	public void setHand(ArrayList<Cards> hand) {
		this.hand = hand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
