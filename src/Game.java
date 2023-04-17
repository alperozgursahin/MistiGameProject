import java.util.ArrayList;
import java.util.Scanner;

public class Game {

	HumanPlayer humanPlayer;
	String playChoice;
	Scanner scanner = new Scanner(System.in);
	boolean spectatorMode = false;
	int maxBotPlayerNumber = 0;
	// ArrayList<BotPlayers> botPlayers = new ArrayList<>();
	ArrayList<Player> players = new ArrayList<>();

	ArrayList<Cards> deck = new ArrayList<>();
	ArrayList<Cards> board = new ArrayList<>();

	ArrayList<Cards> playerHand;
	ArrayList<Cards> botHand1;
	ArrayList<Cards> botHand2;
	ArrayList<Cards> botHand3;

	public Game() {

		startGame();
		for (Player player : players) {
			System.out.println(player.name + " cards are:");
			for (int i = 0; i < player.hand.size(); i++) {
				System.out.print(player.hand.get(i) + ", ");
			}
			System.out.println();
			System.out.println("******************");
		}
	}

	private void startGame() {

		System.out.println("Welcome to the Misti Game!");
		new CardsReader(scanner);
		deck = CardsReader.getCards();
		// System.out.println("////////////////////////////////////");
		Deck.shuffleDeck(deck);

		/*
		 * for (Cards card : deck) { System.out.println(card.getSuit() + card.getRank()
		 * + ": " + card.getPoint() + " points"); }
		 * System.out.println("/////////////////////////////////");
		 */

		Deck.cutDeck(deck);
		/*
		 * for (Cards card : deck) { System.out.println(card.getSuit() + card.getRank()
		 * + ": " + card.getPoint() + " points"); }
		 */
		inputPlayers();
		dealCards(players, deck);

	}

	private void inputPlayers() {

		while (true) {
			System.out.println("Do you want to play or not ? \nType:\n'0' for play \n'1' for spectate \n'2' for exit.");
			playChoice = scanner.nextLine();

			try {
				Integer.parseInt(playChoice);

			} catch (Exception e) {
				System.err.println("Please enter a valid value!");

			}

			switch (Integer.parseInt(playChoice)) {
			case 0:
				System.out.println("Game is starting..");
				maxBotPlayerNumber = 3;
				humanPlayer = new HumanPlayer(humanNameInput(), playerHand = new ArrayList<>());
				players.add(humanPlayer);
				inputBotLevel();
				break;
			case 1:
				System.out.println("Spectator Mode on.");
				maxBotPlayerNumber = 4;
				spectatorMode = true;
				inputBotLevel();
				break;
			case 2:
				System.out.println("GoodByee!");
				break;
			}
			break;

		}

	}

	private void inputBotLevel() {
		System.out.println(
				"How many bot players do you want in the game (max bot player number is: " + maxBotPlayerNumber + ")");
		int botPlayersNumberChoice = scanner.nextInt();
		for (int i = 0; i < botPlayersNumberChoice; i++) {
			System.out.println("What difficulty level do you want in " + (i + 1) + ". bot ? \nNovice Bot for "
					+ BotDifficulty.NOVICEBOTLEVEL + "\nRegular Bot for " + BotDifficulty.REGULARBOTLEVEL
					+ "\nExpert Bot for " + BotDifficulty.EXPERTBOTLEVEL);
			int botDifficultyLevelChoice = scanner.nextInt();
			players.add(addBot(botDifficultyLevelChoice));
			System.out.println("Bot has been added.");

		}
	}

	private BotPlayers addBot(int botDifficultyLevelChoice) {
		switch (botDifficultyLevelChoice) {
		case 0:
			return new NoviceBot("Novice Bot", botHand1 = new ArrayList<>());
		case 1:
			return new RegularBot("Regular Bot", botHand2 = new ArrayList<>());
		case 2:
			return new ExpertBot("Expert Bot", botHand3 = new ArrayList<>());
		}
		return null;
	}

	private String humanNameInput() {
		System.out.println("Please enter your name: ");
		String name = scanner.nextLine();
		System.out.println("Welcome to the game: " + name);
		return name;

	}

	private void dealCards(ArrayList<Player> players, ArrayList<Cards> deck) {
		for (int i = 0; i < 4; i++) {
			board.add(deck.get(0));
			deck.remove(0);
		}

		for (int i = 0; i < 4; i++) {
			for (Player player : players) {
				player.hand.add(deck.get(0));
				deck.remove(0);

			}
		}

	}

}
