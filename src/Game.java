import java.util.ArrayList;
import java.util.Scanner;

public class Game implements Misti {

	Scanner scanner = new Scanner(System.in);

	ArrayList<Cards> deck = new ArrayList<>();
	ArrayList<Cards> board = new ArrayList<>();

	HumanPlayer humanPlayer;
	boolean spectatorMode = false;
	boolean isValid = false;

	BotPlayers[] botPlayers;
	int maxBotPlayerNumber = 3;
	int minimumBotPlayerNumber = 1;
	int botPlayerNumber = 0;

	public Game() {

		startGame();

	}

	@Override
	public void startGame() {

		System.out.println("Welcome to the Misti Game!");

		new Deck();
		deck = Deck.getDeck();
		Deck.shuffleDeck(deck);
		Deck.cutDeck(deck);

		inputPlayers();
		dealCards();
		checkBotStatus();
		checkPlayerStatus();
		checkBoardStatus();

		boolean gameOver = false;
		int playerNumber = botPlayers.length + isHumanPlayerIn();
		int round = roundNumberCalculator(playerNumber);

		while (!gameOver) {
			while (round > 0) {
				round--;

				if (!spectatorMode) {
					System.out.println("The last board card is: " + board.get(board.size() - 1));
					humanPlayCard();
					checkBoardStatus();

				} else {
					System.out.println("spectate döndm");
				}

			}
		}

	}

	@Override
	public void humanPlayCard() {
		System.out.println("Your turn!");
		checkPlayerStatus();

		System.out.println("Choose a card to play: ");
		int playerCardChoice = scanner.nextInt();
		System.out.println("Throwing: " + humanPlayer.getHand().get(playerCardChoice - 1));
		board.add(humanPlayer.getHand().get(playerCardChoice - 1));
		humanPlayer.getHand().remove(playerCardChoice - 1);

	}

	private int roundNumberCalculator(int playerNumber) {
		if (playerNumber == 4)
			return 3;
		else if (playerNumber == 3)
			return 4;
		else if (playerNumber == 2)
			return 6;
		else
			return 0;
	}

	private int isHumanPlayerIn() {
		if (spectatorMode)
			return 0;
		else
			return 1;
	}

	@Override
	public void inputPlayers() {

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
						humanPlayer = new HumanPlayer(humanNameInput());
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

	@Override
	public void inputBotLevel() {
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
					botPlayers = new BotPlayers[botPlayersNumberChoice];
					for (int i = 0; i < botPlayersNumberChoice; i++) {
						System.out.println("What difficulty level do you want for " + (i + 1) + ". bot ? \n"
								+ BotConstants.NOVICE_BOT_NAME + " => " + BotConstants.NOVICE_BOT_LEVEL + "\n"
								+ BotConstants.REGULAR_BOT_NAME + " => " + BotConstants.REGULAR_BOT_LEVEL + "\n"
								+ BotConstants.EXPERT_BOT_NAME + " => " + BotConstants.EXPERT_BOT_LEVEL);
						boolean flag = false;
						while (!flag) {
							try {
								int botDifficultyLevelChoice = Integer.parseInt(scanner.nextLine());
								if (botDifficultyLevelChoice < 0 || botDifficultyLevelChoice > 2) {
									flag = false;
									System.out.println("Please enter a valid value!");
								} else {
									flag = true;
									addBot(botDifficultyLevelChoice);
									System.out.println(botPlayers[botPlayerNumber++].getName() + " has been added.");

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

	@Override
	public void addBot(int botDifficultyLevelChoice) {
		switch (botDifficultyLevelChoice) {
		case BotConstants.NOVICE_BOT_LEVEL:
			BotPlayers noviceBot = new NoviceBot();
			botPlayers[botPlayerNumber] = noviceBot;
			break;

		case BotConstants.REGULAR_BOT_LEVEL:
			BotPlayers regularBot = new RegularBot();
			botPlayers[botPlayerNumber] = regularBot;
			break;

		case BotConstants.EXPERT_BOT_LEVEL:
			BotPlayers expertBot = new ExpertBot();
			botPlayers[botPlayerNumber] = expertBot;
			break;

		}

	}

	@Override
	public void dealCards() {

		for (int i = 0; i < 4; i++) {
			board.add(deck.get(0));
			deck.remove(0);
		}
		for (int i = 0; i < 4; i++) {
			if (!spectatorMode) {
				humanPlayer.getHand().add(deck.get(0));
				deck.remove(0);
			}
			for (BotPlayers botPlayer : botPlayers) {
				botPlayer.getHand().add(deck.get(0));
				deck.remove(0);

			}
		}
	}

	@Override
	public void checkPlayerStatus() {
		if (!spectatorMode) {
			System.out.println(humanPlayer.getName() + " Cards are: ");
			for (int i = 0; i < humanPlayer.getHand().size(); i++) {
				System.out.print((i + 1) + ") " + humanPlayer.getHand().get(i) + " ");
			}
			System.out.println();
		}
	}

	@Override
	public void checkBotStatus() {
		for (BotPlayers botPlayer : botPlayers) {
			System.out.println(botPlayer.getName() + " cards are:");
			for (int i = 0; i < botPlayer.getHand().size(); i++) {
				System.out.print(botPlayer.getHand().get(i) + ", ");
			}
			System.out.println();
			System.out.println("******************");
		}
	}

	@Override
	public void checkBoardStatus() {
		System.out.println();
		System.out.println("Board cards are: ");
		for (int i = 0; i < board.size(); i++) {
			System.out.print(board.get(i) + ", ");
		}
		System.out.println("Remained deck cards :" + deck.size());
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
}
