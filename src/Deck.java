import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
	private static ArrayList<Cards> cards;

	public Deck() {
		createDeck();
	}

	private void createDeck() {
		String[] suits = { "Spades", "Diamonds", "Hearts", "Clubs" };
		String[] ranks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
		cards = new ArrayList<Cards>();
		for (String suit : suits) {
			for (String rank : ranks) {
				Cards card = new Cards(suit, rank, 1);
				cards.add(card);
			}
		}
	}
	
	public static ArrayList<Cards> getDeck() {
		return cards;
	}

	public void setDeck(ArrayList<Cards> cards) {
		Deck.cards = cards;
	}

	public static void cutDeck(ArrayList<Cards> cards) {
		Random rand = new Random();
		int cutIndex = rand.nextInt(cards.size());
		System.out.println("Deck is Cutting...");

		ArrayList<Cards> bottom = new ArrayList<>(cards.subList(0, cutIndex));
		ArrayList<Cards> top = new ArrayList<>(cards.subList(cutIndex, cards.size()));
		cards.clear();
		cards.addAll(top);
		cards.addAll(bottom);
	}

	public static void shuffleDeck(ArrayList<Cards> cards) {
		System.out.println("Deck is Shuffling...");
		Collections.shuffle(cards);

	}
}
