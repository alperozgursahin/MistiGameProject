import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Deck {
	private static ArrayList<Cards> cards;

	public Deck() {

		createDeck();
	}

	private void createDeck() {
		int defaultPoint = 1;
		Scanner scanner = new Scanner(System.in);
		String[] suits = { "Spades", "Diamonds", "Hearts", "Clubs" };
		String[] ranks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
		cards = new ArrayList<Cards>();
		for (String suit : suits) {
			for (String rank : ranks) {
				Cards card = new Cards(suit, rank, Integer.MAX_VALUE);
				cards.add(card);
			}
		}
		do {
			try {

				BufferedReader reader = new BufferedReader(new FileReader(fileInput(scanner)));
				String line = reader.readLine();
				while (line != null) {
					if (line.startsWith("**")) {
						defaultPoint = Integer.parseInt(line.substring(2).trim());
						System.out.println("DEFAULT CARD POINT: " + defaultPoint);
					} else {

						String[] parts = line.split("\\*");
						String suitOrRank = parts[0];
						int value = Integer.parseInt(parts[1].trim());
						for (Cards card : cards) {
							if (card.getSuit().equals(suitOrRank) && card.getPoint() == Integer.MAX_VALUE) {
								card.setPoint(value);

							} else if (card.getRank().equals(suitOrRank) && card.getPoint() == Integer.MAX_VALUE) {
								card.setPoint(value);
							}

						}
					}

					line = reader.readLine();

				}

				reader.close();
				break;
			} catch (IOException e) {
				System.err.println("WRONG FILE NAME INPUT!!");
			}

		} while (true);

		for (Cards card : cards) {
			if (card.getPoint() == Integer.MAX_VALUE)
				card.setPoint(defaultPoint);
		}
		for (Cards card : cards) {
			System.out.println(card + "(" + card.getPoint() + ")");
		}
	}

	private String fileInput(Scanner scanner) {
		System.out.println("Please enter a file name which inputs card point values. Example(cards.txt)");
		String fileName = scanner.nextLine();
		return fileName;
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
