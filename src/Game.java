import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game implements Misti {

	Scanner scanner = new Scanner(System.in);
	Random random = new Random();
	ArrayList<Cards> deck = new ArrayList<>();
	ArrayList<Cards> board = new ArrayList<>();

	HumanPlayer humanPlayer;
	boolean spectatorMode = false;
	boolean isValid = false;
	boolean gameOver;

	BotPlayers[] botPlayers;
	int maximumBotPlayerNumber = 3;
	int minimumBotPlayerNumber = 1;
	int botPlayerNumber;
	int playerNumber;
	int round;
	int boardCardsSum;

	public Game() {

		startGame();

	}

	@Override
	public void startGame() {

		String[] misti = { "███╗   ███╗ ██████╗  ███████  ████████╗ ██████╗    ",
				"████╗ ████║   ██╔═╝ ██╚═════╗    ██╔══╝   ██╔═╝    ",
				"██╔████╔██║   ██║    ███████╚╗   ██║      ██║      ",
				"██║╚██╔╝██║   ██╚═╗        ██║   ██║      ██╚═╗ ",
				"██║ ╚═╝ ██║ ██████║  ███████╔╝   ██║    ██████║   ",
				"╚═╝     ╚═╝ ╚═════╝  ╚══════╝    ╚═╝    ╚═════╝ " };

		// Harfleri düzenli bir şekilde çizmek için her bir satırı yazdırma
		for (String harf : misti) {
			System.out.println(harf.trim());
		}

		new Deck();
		deck = Deck.getDeck();
		Deck.shuffleDeck(deck);
		Deck.cutDeck(deck);

		inputPlayers();

		if (!gameOver) {
			playerNumber = botPlayers.length + isHumanPlayerIn();
			round = roundNumberCalculator(playerNumber);
			dealCardsToBoard();
		}
		int turn;

		while (!gameOver) {
			while (round > 0) {
				round--;
				turn = 4;
				dealCardsToPlayers();
				while (turn > 0) {
					turn--;

					if (!spectatorMode) {
						checkBoardStatus();
						humanPlayCard();
						for (BotPlayers botPlayer : botPlayers) {
							checkBoardStatus();
							if (botPlayer.getBotDifficulty() == BotConstants.NOVICE_BOT_LEVEL)
								noviceBotPlayCard(botPlayer);
							else if (botPlayer.getBotDifficulty() == BotConstants.REGULAR_BOT_LEVEL)
								regularBotPlayCard(botPlayer);
							else if (botPlayer.getBotDifficulty() == BotConstants.EXPERT_BOT_LEVEL)
								expertBotPlayCard(botPlayer);

						}

					} else {
						System.out.println("spectate döndüm");
						checkBoardStatus();
						humanPlayCard();
						for (BotPlayers botPlayer : botPlayers) {
							checkBoardStatus();
							if (botPlayer.getBotDifficulty() == BotConstants.NOVICE_BOT_LEVEL)
								noviceBotPlayCard(botPlayer);
							else if (botPlayer.getBotDifficulty() == BotConstants.REGULAR_BOT_LEVEL)
								regularBotPlayCard(botPlayer);
							else if (botPlayer.getBotDifficulty() == BotConstants.EXPERT_BOT_LEVEL)
								expertBotPlayCard(botPlayer);
						}
					}
				}

			}
		}

	}

	private void expertBotPlayCard(BotPlayers expertBot) {
		getBotHand(expertBot);
	}

	private void regularBotPlayCard(BotPlayers regularBot) {
		getBotHand(regularBot);

		boolean canTakeBoard = false;
		boolean isJokerPlayable = false;
		boolean isCardsNumHigher = false;
		int playedNormalCardIndex = 0;
		int playedJokerCardIndex = 0;
		int boardCardsMaxPotentialPoint = 0;

		Cards playedCard = null;
		Cards playedJokerCard = null;

		if (!board.isEmpty()) {
			for (int i = 0; i < regularBot.getHand().size(); i++) {

				if (regularBot.getHand().get(i).getRank().equals(board.get(board.size() - 1).getRank())) {
					playedCard = regularBot.getHand().get(i);
					playedNormalCardIndex = i;

					if ((playedCard.getPoint() + boardCardsSum) > boardCardsMaxPotentialPoint) {
						playedCard = regularBot.getHand().get(i);
						playedNormalCardIndex = i;

						isCardsNumHigher = true;
						canTakeBoard = true;
						boardCardsMaxPotentialPoint += playedCard.getPoint() + boardCardsSum;

					}

				}

				if (regularBot.getHand().get(i).getRank().equalsIgnoreCase("J")) {
					playedJokerCardIndex = i;
					playedJokerCard = regularBot.getHand().get(i);
					if ((playedJokerCard.getPoint() + boardCardsSum) > 0) {
						isCardsNumHigher = true;
						isJokerPlayable = true;

					}

				}

			}
		}
		boardCardsMaxPotentialPoint = 0;
		if (isCardsNumHigher) {
			if (canTakeBoard) {
				board.add(playedCard);
				regularBot.getHand().remove(playedNormalCardIndex);
				System.out.println(regularBot.getName() + " is playing: " + playedCard);
				compare(playedCard, regularBot);

			} else if (isJokerPlayable) {
				board.add(playedJokerCard);
				regularBot.getHand().remove(playedJokerCardIndex);
				System.out.println(regularBot.getName() + " is playing: " + playedJokerCard);
				compare(playedJokerCard, regularBot);

			}
		} else {
			int lowestCardIndex = 0;
			int lowestCardPoint = Integer.MAX_VALUE;
			for (int i = 0; i < regularBot.getHand().size(); i++) {
				if (!board.isEmpty()) {
					if ((lowestCardPoint > regularBot.getHand().get(i).getPoint())
							&& !(regularBot.getHand().get(i).getRank().equals(board.get(board.size() - 1).getRank()))) {
						lowestCardPoint = regularBot.getHand().get(i).getPoint();
						playedCard = regularBot.getHand().get(i);
						lowestCardIndex = i;
					}
				} else {
					if (lowestCardPoint > regularBot.getHand().get(i).getPoint()) {
						lowestCardPoint = regularBot.getHand().get(i).getPoint();
						playedCard = regularBot.getHand().get(i);
						lowestCardIndex = i;
					}

				}

			}
			board.add(playedCard);
			regularBot.getHand().remove(lowestCardIndex);
			System.out.println(regularBot.getName() + " is playing: " + playedCard);
			compare(playedCard, regularBot);

		}

	}

	private void noviceBotPlayCard(BotPlayers noviceBot) {
		getBotHand(noviceBot);

		int noviceBotCardChoice = random.nextInt(noviceBot.getHand().size()) + 1;
		Cards playedCard = noviceBot.getHand().get(noviceBotCardChoice - 1);
		System.out.println(noviceBot.getName() + " is playing: " + playedCard);
		board.add(playedCard);
		noviceBot.getHand().remove(noviceBotCardChoice - 1);
		compare(playedCard, noviceBot);

	}

	private void getBotHand(BotPlayers botPlayer) {
		System.out.println(botPlayer.getName() + "'s turn!");
		System.out.println(botPlayer.getName() + "'s cards are:");

		for (int i = 0; i < botPlayer.getHand().size(); i++) {
			System.out.print((i + 1) + ") " + botPlayer.getHand().get(i) + ", ");
		}

		System.out.println();
	}

	private void compare(Cards playedCard, BotPlayers botPlayer) {
		if (board.size() != 1) {
			if (playedCard.getRank().equalsIgnoreCase(board.get(board.size() - 2).getRank())
					|| playedCard.getRank().equalsIgnoreCase("J")) {
				if (board.size() == 2) {
					System.out.println(botPlayer.getName() + " made a Misti!");
					int newMistiNumber = botPlayer.getMistiNumber() + 1;
					botPlayer.setMistiNumber(newMistiNumber);
				} else {
					System.out.println(botPlayer.getName() + " Collecting all cards on board...");
					botPlayer.getCollectedCards().addAll(board);
					board.clear();
				}
			}
		}

	}

	private void compare(Cards playedCard, HumanPlayer humanPlayer) {
		if (board.size() != 1) {
			if (playedCard.getRank().equalsIgnoreCase(board.get(board.size() - 2).getRank())
					|| playedCard.getRank().equalsIgnoreCase("J")) {
				if (board.size() == 2) {
					System.out.println(humanPlayer.getName() + " made a Misti!");
					int newMistiNumber = humanPlayer.getMistiNumber() + 1;
					humanPlayer.setMistiNumber(newMistiNumber);
				} else {
					System.out.println("Collecting cards on board...");
					humanPlayer.getCollectedCards().addAll(board);
					board.clear();
				}
			}
		}
	}

	@Override
	public void humanPlayCard() {
		boolean validPlayerInput = false;
		System.out.println("Your turn!");
		checkPlayerStatus();

		System.out.println("Choose a card to play: ");
		int playerCardChoice = -1;
		do {
			try {
				playerCardChoice = Integer.parseInt(scanner.nextLine());
				if (!String.valueOf(playerCardChoice).matches("[1-4]"))
					System.out.println("Please enter a valid value!");
				else
					validPlayerInput = true;

			} catch (Exception e) {
				System.err.println("Please enter a valid value!");
			}
		} while (!validPlayerInput);

		Cards playedCard = humanPlayer.getHand().get(playerCardChoice - 1);
		System.out.println("Playing: " + playedCard);
		board.add(playedCard);
		humanPlayer.getHand().remove(playerCardChoice - 1);
		compare(playedCard, humanPlayer);

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

		do {
			System.out.println("What would you like to do?\nType:\n0 => PLAY\n1 => SPECTATE\n2 => EXIT");
			try {
				int playChoice = Integer.parseInt(scanner.nextLine());
				switch (playChoice) {
				case 0:
					System.out.println("Game is starting...");
					humanPlayer = new HumanPlayer(humanNameInput());
					inputBotLevel();
					break;
				case 1:
					System.out.println("Spectator Mode on.");
					maximumBotPlayerNumber = 4;
					minimumBotPlayerNumber = 2;
					spectatorMode = true;
					inputBotLevel();
					break;
				case 2:
					System.out.println("Goodbye!");
					gameOver = true;
					break;
				default:
					System.err.println("Please enter a valid value!");
					continue;
				}
				break;
			} catch (Exception e) {
				System.err.println("Please enter a valid value!");
			}
		} while (true);

	}

	@Override
	public void inputBotLevel() {
		System.out.println("How many bot players do you want in the game");
		System.out.println("Minimum bot player number: " + minimumBotPlayerNumber + "\nMaximum bot player number: "
				+ maximumBotPlayerNumber);
		do {
			try {
				int botPlayersNumberChoice = Integer.parseInt(scanner.nextLine());
				if (botPlayersNumberChoice < minimumBotPlayerNumber
						|| botPlayersNumberChoice > maximumBotPlayerNumber) {
					System.out.println("Please enter a valid value!");
				} else {
					isValid = true;
					botPlayers = new BotPlayers[botPlayersNumberChoice];
					for (int i = 0; i < botPlayersNumberChoice; i++) {
						System.out.println("What difficulty level do you want for " + (i + 1) + ". bot ? \n"
								+ BotConstants.NOVICE_BOT_NAME + " => " + BotConstants.NOVICE_BOT_LEVEL + "\n"
								+ BotConstants.REGULAR_BOT_NAME + " => " + BotConstants.REGULAR_BOT_LEVEL + "\n"
								+ BotConstants.EXPERT_BOT_NAME + " => " + BotConstants.EXPERT_BOT_LEVEL);
						boolean checked = false;
						do {
							try {
								int botDifficultyLevelChoice = Integer.parseInt(scanner.nextLine());
								if (!String.valueOf(botDifficultyLevelChoice).matches("[0-2]")) {
									System.out.println("Please enter a valid value!");
								} else {
									checked = true;
									addBot(botDifficultyLevelChoice);

								}

							} catch (Exception e) {
								System.err.println("Please enter a valid value!");
							}
						} while (!checked);
					}
				}

			} catch (Exception e) {
				System.err.println("Please enter a valid value!");
			}
		} while (!isValid);

	}

	@Override
	public void addBot(int botDifficultyLevelChoice) {
		BotPlayers bot = null;
		switch (botDifficultyLevelChoice) {
		case BotConstants.NOVICE_BOT_LEVEL:
			bot = new NoviceBot();
			break;
		case BotConstants.REGULAR_BOT_LEVEL:
			bot = new RegularBot();
			break;
		case BotConstants.EXPERT_BOT_LEVEL:
			bot = new ExpertBot();
			break;

		}
		botPlayers[botPlayerNumber] = bot;
		System.out.println(botPlayers[botPlayerNumber++].getName() + " has been added.");
	}

	@Override
	public void dealCardsToBoard() {
		for (int i = 0; i < 4; i++) {
			board.add(deck.get(0));
			deck.remove(0);
		}

	}

	@Override
	public void dealCardsToPlayers() {

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
		boardCardsSum = 0;
		for (int i = 0; i < board.size(); i++) {
			System.out.print(board.get(i) + ", ");
			boardCardsSum += board.get(i).getPoint();
		}

		System.out.println("Board cards sum: " + boardCardsSum);
		Cards lastCard = board.isEmpty() ? null : board.get(board.size() - 1);
		System.out.println("Remained deck cards :" + deck.size());
		System.out.println("The last board card is: " + lastCard);
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
