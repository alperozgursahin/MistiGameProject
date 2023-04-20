import java.util.ArrayList;

public abstract class Player {

	private String name;
	private ArrayList<Cards> hand;
	private ArrayList<Cards> collectedCards;

	public Player(String name) {
		this.setName(name);
		this.hand = new ArrayList<Cards>();
		this.collectedCards = new ArrayList<Cards>();
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

	public ArrayList<Cards> getCollectedCards() {
		return collectedCards;
	}

	public void setCollectedCards(ArrayList<Cards> collectedCards) {
		this.collectedCards = collectedCards;
	}

}
