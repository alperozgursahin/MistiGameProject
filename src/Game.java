import java.util.ArrayList;
import java.util.Scanner;

public class Game {

	HumanPlayer humanPlayer;
	Scanner scanner = new Scanner(System.in);
	boolean spectatorMode = false;
	boolean isValid = false;
	int maxBotPlayerNumber = 3;
	int minimumBotPlayerNumber = 1;
	ArrayList<BotPlayers> botPlayers = new ArrayList<>();

	ArrayList<Cards> deck = new ArrayList<>();
	ArrayList<Cards> board = new ArrayList<>();

	ArrayList<Cards> playerHand;
	ArrayList<Cards> botHand1;
	ArrayList<Cards> botHand2;
	ArrayList<Cards> botHand3;

	public Game() {

		startGame();
		// Check Status

	}

	private void startGame() {

		System.out.println("Welcome to the Misti Game!");
//		new CardsReader(scanner);

		new Deck();
		deck = Deck.getDeck();
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
		dealCards(botPlayers, humanPlayer, deck);
		checkBotStatus();
		checkPlayerStatus(spectatorMode, humanPlayer);
		checkBoardStatus();

	}

	private void inputPlayers() {

		while (!isValid) {
			System.out.println("What would you like to do ?\nType:\n0 => PLAY \n1 => SPECTATE \n2 => EXIT");
			try {
				int playChoice = Integer.parseInt(scanner.nextLine());
				if (playChoice < 0 || playChoice > 2) {
					isValid = false;
					System.out.println("Please enter a valid value!");
				} else {
					isValid = true;
					switch (playChoice) {
					case 0:
						System.out.println("Game is starting...");
						humanPlayer = new HumanPlayer(humanNameInput(), playerHand = new ArrayList<>());
						// botPlayers.add(humanPlayer);
						inputBotLevel();
						break;
					case 1:
						System.out.println("Spectator Mode on.");
						maxBotPlayerNumber = 4;
						minimumBotPlayerNumber = 2;
						spectatorMode = true;
						inputBotLevel();
						break;
					case 2:
						System.out.println("GoodByee!");
						break;
					}
				}

			} catch (Exception e) {
				System.err.println("Please enter a valid value!");
			}

		}

	}

	private void inputBotLevel() {
		int botCounter = 0;
		System.out.println(
				"How many bot players do you want in the game (max bot player number is: " + maxBotPlayerNumber + ")");
		isValid = false;
		while (!isValid) {
			try {
				int botPlayersNumberChoice = Integer.parseInt(scanner.nextLine());

				if (botPlayersNumberChoice < minimumBotPlayerNumber || botPlayersNumberChoice > maxBotPlayerNumber) {
					isValid = false;
					System.out.println("Please enter a valid value!");
				} else {
					isValid = true;
					for (int i = 0; i < botPlayersNumberChoice; i++) {
						System.out.println("What difficulty level do you want for " + (i + 1)
								+ ". bot ? \nNovice Bot => " + BotDifficulty.NOVICEBOTLEVEL + "\nRegular Bot => "
								+ BotDifficulty.REGULARBOTLEVEL + "\nExpert Bot => " + BotDifficulty.EXPERTBOTLEVEL);
						boolean flag = false;
						while (!flag) {
							try {
								int botDifficultyLevelChoice = Integer.parseInt(scanner.nextLine());
								if (botDifficultyLevelChoice < 0 || botDifficultyLevelChoice > 2) {
									flag = false;
									System.out.println("Please enter a valid value!");
								} else {
									flag = true;
									botPlayers.add(addBot(botDifficultyLevelChoice));
									System.out.println(botPlayers.get(botCounter++).getName() + " has been added.");
								}

							} catch (Exception e) {
								System.err.println("Please enter a valid value!");
							}
						}
					}
				}

			} catch (Exception e) {
				System.err.println("Please enter a valid value!");
			}
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

	private void dealCards(ArrayList<BotPlayers> botPlayers, HumanPlayer humanPlayer, ArrayList<Cards> deck) {
		for (int i = 0; i < 4; i++) {
			board.add(deck.get(0));
			deck.remove(0);
		}

		for (int i = 0; i < 4; i++) {
			for (BotPlayers player : botPlayers) {
				player.getHand().add(deck.get(0));
				deck.remove(0);

			}
			if (!spectatorMode) {
				humanPlayer.getHand().add(deck.get(0));
				deck.remove(0);
			}

		}

	}

	private void checkPlayerStatus(boolean spectatorMode, HumanPlayer humanPlayer) {
		if (!spectatorMode) {
			System.out.println(humanPlayer.getName() + " Cards are: ");
			for (int i = 0; i < humanPlayer.getHand().size(); i++) {
				System.out.print(humanPlayer.getHand().get(i) + ", ");
			}
		}
	}

	private void checkBotStatus() {
		for (BotPlayers botPlayer : botPlayers) {
			System.out.println(botPlayer.getName() + " cards are:");
			for (int i = 0; i < botPlayer.getHand().size(); i++) {
				System.out.print(botPlayer.getHand().get(i) + ", ");
			}
			System.out.println();
			System.out.println("******************");
		}
	}

	private void checkBoardStatus() {
		System.out.println("Board cards are: ");
		for (int i = 0; i < board.size(); i++) {
			System.out.print(board.get(i) + ", ");
		}
		System.out.println("Remained deck cards :" + deck.size());
	}

}
