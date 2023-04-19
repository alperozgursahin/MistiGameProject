import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CardsReader {

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

		// Read in each line and create a card object with its associated points
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] parts = line.split(" ");
			String suit = parts[0];
			String rank = parts[1];
			int points = Integer.parseInt(parts[2]);

		}

	}

	private String fileInput(Scanner scanner) {

		String fileName = scanner.nextLine();
		return fileName;
	}

}
