import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CardsReader {

	private static ArrayList<Cards> cards;

	public CardsReader(Scanner scanner) {
		System.out.println("Cards Taking...");
		System.out.println("Enter a file name please!");
		// Read in file
		do {
			try {
				System.out.print("Please enter a file name: ");
				scanner = new Scanner(new File(fileInput(scanner)));
			} catch (FileNotFoundException e) {
				System.err.println("Please enter a valid file name!");
			}
		} while (scanner == null);

		// Create ArrayList to hold cards
		cards = new ArrayList<>();

		// Read in each line and create a card object with its associated points
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] parts = line.split(" ");
			String suit = parts[0];
			String rank = parts[1];
			int points = Integer.parseInt(parts[2]);

			Cards card = new Cards(suit, rank, points);
			cards.add(card);
		}

		// Print out the cards with their associated points
		/*
		 * for (Cards card : cards) { System.out.println(card.getSuit() + card.getRank()
		 * + ": " + card.getPoint() + " points"); }
		 */
	}

	private String fileInput(Scanner scanner) {

		String fileName = scanner.nextLine();
		return fileName;
	}

	public static ArrayList<Cards> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Cards> cards) {
		CardsReader.cards = cards;
	}

}
