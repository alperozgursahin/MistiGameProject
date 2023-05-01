import java.util.ArrayList;

public class Player {

	private String name;
	private ArrayList<Cards> hand;
	private ArrayList<Cards> collectedCards;
	private int mistiNumber;
	private int score;
	private String type;
	

	public Player(String name) {
		this.setName(name);
		this.hand = new ArrayList<Cards>();
		this.collectedCards = new ArrayList<Cards>();
		this.mistiNumber = 0;
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

	public int getMistiNumber() {
		return mistiNumber;
	}

	public void setMistiNumber(int mistiNumber) {
		this.mistiNumber = mistiNumber;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
