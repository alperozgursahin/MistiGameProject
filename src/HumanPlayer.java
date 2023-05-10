import java.util.Scanner;

public final class HumanPlayer extends Player {

	public HumanPlayer() {
		super(humanNameInput());
	}

	private static String humanNameInput() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter Your Name Please: ");
		String name = scanner.nextLine();
		StringBuilder sb = new StringBuilder(name);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		name = sb.toString();
		System.out.println("Welcome to Game " + name);
		return name;
	}

	public void playCard() {
		boolean validPlayerInput = false;
		System.out.println("Your turn!");
		Game.checkPlayerStatus();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		System.out.println("Choose a card to play: ");
		int playerCardChoice = -1;
		do {
			try {
				playerCardChoice = Integer.parseInt(scanner.nextLine());
				if ((playerCardChoice < 1) || (playerCardChoice > this.getHand().size()))
					System.err.println("Please enter a valid value!");
				else
					validPlayerInput = true;

			} catch (Exception e) {
				System.err.println("Please enter a valid value!");
			}
		} while (!validPlayerInput);

		Cards playedCard = Game.getHumanPlayer().getHand().get(playerCardChoice - 1);
		handOrganizer(playedCard);
		Game.compare(playedCard, this);
	}
}
