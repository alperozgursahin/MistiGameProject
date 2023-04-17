import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {

	public static void cutDeck(ArrayList<Cards> cards) {
		Random rand = new Random();
		int cutIndex = rand.nextInt(cards.size());
		System.out.println("Deck is Cutting");

		ArrayList<Cards> bottom = new ArrayList<>(cards.subList(0, cutIndex));
		ArrayList<Cards> top = new ArrayList<>(cards.subList(cutIndex, cards.size()));
		cards.clear();
		cards.addAll(top);
		cards.addAll(bottom);
	}

	public static void shuffleDeck(ArrayList<Cards> cards) {
		System.out.println("Deck is shuffling");
		Collections.shuffle(cards);

	}
}
