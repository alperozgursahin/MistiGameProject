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
		//Check Status
		for (Player player : players) {
			System.out.println(player.getName() + " cards are:");
			for (int i = 0; i < player.getHand().size(); i++) {
				System.out.print(player.getHand().get(i) + ", ");
			}
			System.out.println();
			System.out.println("******************");
		}
		System.out.println("Board cards are: ");
		for (int i = 0; i < board.size(); i++) {
			System.out.print(board.get(i)+", ");
		}
		System.out.println("Remained deck cards :"+deck.size());
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
			System.out.println("What would you like to do ?\nType:\n0 => PLAY \n1 => SPECTATE \n2 => EXIT");
			playChoice = scanner.nextLine();

			try {
				Integer.parseInt(playChoice);

			} catch (Exception e) {
				System.err.println("Please enter a valid value!");

			}

			switch (Integer.parseInt(playChoice)) {
			case 0:
				System.out.println("Game is starting...");
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
			System.out.println("What difficulty level do you want for " + (i + 1) + ". bot ? \nNovice Bot => "
					+ BotDifficulty.NOVICEBOTLEVEL + "\nRegular Bot => " + BotDifficulty.REGULARBOTLEVEL
					+ "\nExpert Bot => " + BotDifficulty.EXPERTBOTLEVEL);
			int botDifficultyLevelChoice = scanner.nextInt();
			players.add(addBot(botDifficultyLevelChoice));
			System.out.println("Bot has been added.");

		}
	}

	private BotPlayers addBot(int botDifficultyLevelChoice) {
		switch (botDifficultyLevelChoice) {
		case 0:
			return new NoviceBot("NOVICE BOT", botHand1 = new ArrayList<>());
		case 1:
			return new RegularBot("REGULAR BOT", botHand2 = new ArrayList<>());
		case 2:
			return new ExpertBot("EXPERT BOT", botHand3 = new ArrayList<>());
		}
		return null;
	}

	private String humanNameInput() {
		System.out.println("Enter Your Name Please: ");
		String name = scanner.nextLine();
		StringBuilder sb = new StringBuilder(name);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		name = sb.toString();
		System.out.println("Welcome to Game " + name);
		return name;

	}

	private void dealCards(ArrayList<Player> players, ArrayList<Cards> deck) {
		for (int i = 0; i < 4; i++) {
			board.add(deck.get(0));
			deck.remove(0);
		}

		for (int i = 0; i < 4; i++) {
			for (Player player : players) {
				player.getHand().add(deck.get(0));
				deck.remove(0);

			}
		}

	}

}
