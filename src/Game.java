import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game implements Misti {

	private Scanner scanner = new Scanner(System.in);
	private ArrayList<Cards> deck = new ArrayList<>();
	private ArrayList<Cards> board = new ArrayList<>();
	private ArrayList<Cards> thrownCards = new ArrayList<>();

	private HumanPlayer humanPlayer;
	private boolean spectatorMode = false;
	private boolean isValid = false;
	private boolean gameOver;

	private BotPlayers[] botPlayers;
	private int maximumBotPlayerNumber = 3;
	private int minimumBotPlayerNumber = 1;
	private int botPlayerNumber;
	private int playerNumber;
	private int round;
	private int boardCardsSum;

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

		for (String harf : misti) {
			System.out.println(harf.trim());
		}

		new Deck();
		deck = Deck.getDeck();
		Deck.shuffleDeck(deck);
		Deck.cutDeck(deck);

		inputPlayers();

		if (!gameOver) {
			int isHumanPlayerIn = spectatorMode ? 0 : 1;
			playerNumber = botPlayers.length + isHumanPlayerIn;
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
						botPlayerTurn();

					} else {
						System.out.println("Spectate mode");
						checkBoardStatus();
						botPlayerTurn();
					}
				}

			}
		}

		new Scoreboard(humanPlayer, botPlayers);
	}

	private void botPlayerTurn() {
		for (BotPlayers botPlayer : botPlayers) {
			checkBoardStatus();
			if (botPlayer.getBotDifficulty() == BotConstants.NOVICE_BOT_LEVEL)
				compare(noviceBotPlayCard(botPlayer), botPlayer);
			else if (botPlayer.getBotDifficulty() == BotConstants.REGULAR_BOT_LEVEL)
				compare(regularBotPlayCard(botPlayer), botPlayer);
			else if (botPlayer.getBotDifficulty() == BotConstants.EXPERT_BOT_LEVEL)
				compare(expertBotPlayCard(botPlayer), botPlayer);
		}
	}

	private Cards expertBotPlayCard(BotPlayers expertBot) {
		getBotHand(expertBot);

		boolean canTakeBoard = false;
		boolean isJokerPlayable = false;
		boolean isCardsNumHigher = false;
		int boardCardsMaxPotentialPoint = 0;

		Cards playedCard = null;
		Cards playedJokerCard = null;

		if (!board.isEmpty()) {
			for (int i = 0; i < expertBot.getHand().size(); i++) {

				Cards currentCard = expertBot.getHand().get(i);
				if (currentCard.getRank().equals(board.get(board.size() - 1).getRank())) {
					playedCard = currentCard;

					int potentialPoint = currentCard.getPoint() + boardCardsSum;
					if (potentialPoint > boardCardsMaxPotentialPoint) {

						isCardsNumHigher = true;
						canTakeBoard = true;
						boardCardsMaxPotentialPoint += potentialPoint;
					}

				}

				if (currentCard.getRank().equalsIgnoreCase("J")) {
					playedJokerCard = currentCard;
					int potentialPoint = currentCard.getPoint() + boardCardsSum;
					if (potentialPoint + boardCardsSum > 0) {
						isCardsNumHigher = true;
						isJokerPlayable = true;

					}
				}
			}
		} else {
			ArrayList<Cards> mostPlayedCards = new ArrayList<>();
			int[] mostThrownCards = new int[expertBot.getHand().size()];

			for (Cards thrownCard : thrownCards) {
				for (int j = 0; j < expertBot.getHand().size(); j++) {
					if (thrownCard == expertBot.getHand().get(j)) {
						mostThrownCards[j]++;
					}
				}
			}
			for (int i = 0; i < mostThrownCards.length; i++) {
				if (mostThrownCards[i] == 3) {
					playedCard = expertBot.getHand().get(i);
					mostPlayedCards.add(playedCard);
				}
			}
			playedCard = getLowestMostPlayedCard(mostPlayedCards);

			if (playedCard != null) {
				handOrganizer(playedCard, expertBot);
				return playedCard;
			}

		}

		boardCardsMaxPotentialPoint = 0;
		if (isCardsNumHigher) {
			if (canTakeBoard) {
				handOrganizer(playedCard, expertBot);
				return playedCard;

			} else if (isJokerPlayable) {
				handOrganizer(playedJokerCard, expertBot);
				return playedJokerCard;

			}
		} else {
			int lowestCardPoint = Integer.MAX_VALUE;

			for (int i = 0; i < expertBot.getHand().size(); i++) {
				Cards currentCard = expertBot.getHand().get(i);
				if (!board.isEmpty()) {
					if ((currentCard.getPoint() < lowestCardPoint)
							&& !(currentCard.getRank().equals(board.get(board.size() - 1).getRank()))) {
						lowestCardPoint = currentCard.getPoint();
						playedCard = currentCard;
					}
				} else {
					if (lowestCardPoint > currentCard.getPoint()) {
						lowestCardPoint = currentCard.getPoint();
						playedCard = currentCard;
					}

				}

			}

			handOrganizer(playedCard, expertBot);
			return playedCard;

		}
		return null;

	}

	private Cards getLowestMostPlayedCard(ArrayList<Cards> mostPlayedCards) {
		int maxCardPoint = Integer.MAX_VALUE;
		Cards lowestMostPlayedCard = null;
		for (int i = 0; i < mostPlayedCards.size(); i++) {
			if (maxCardPoint > mostPlayedCards.get(i).getPoint())
				lowestMostPlayedCard = mostPlayedCards.get(i);
		}
		return lowestMostPlayedCard;
	}

	private Cards regularBotPlayCard(BotPlayers regularBot) {
		getBotHand(regularBot);

		boolean canTakeBoard = false;
		boolean isJokerPlayable = false;
		boolean isCardsNumHigher = false;
		int boardCardsMaxPotentialPoint = 0;

		Cards playedCard = null;
		Cards playedJokerCard = null;

		if (!board.isEmpty()) {
			for (int i = 0; i < regularBot.getHand().size(); i++) {

				if (regularBot.getHand().get(i).getRank().equals(board.get(board.size() - 1).getRank())) {
					playedCard = regularBot.getHand().get(i);
					if ((playedCard.getPoint() + boardCardsSum) > boardCardsMaxPotentialPoint) {
						playedCard = regularBot.getHand().get(i);

						isCardsNumHigher = true;
						canTakeBoard = true;
						boardCardsMaxPotentialPoint += playedCard.getPoint() + boardCardsSum;

					}

				}

				if (regularBot.getHand().get(i).getRank().equalsIgnoreCase("J")) {
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
				handOrganizer(playedCard, regularBot);
				return playedCard;

			} else if (isJokerPlayable) {
				handOrganizer(playedJokerCard, regularBot);
				return playedJokerCard;

			}
		} else {
			int lowestCardPoint = Integer.MAX_VALUE;
			for (int i = 0; i < regularBot.getHand().size(); i++) {
				if (!board.isEmpty()) {
					if ((lowestCardPoint > regularBot.getHand().get(i).getPoint())
							&& !(regularBot.getHand().get(i).getRank().equals(board.get(board.size() - 1).getRank()))) {
						lowestCardPoint = regularBot.getHand().get(i).getPoint();
						playedCard = regularBot.getHand().get(i);
					}
				} else {
					if (lowestCardPoint > regularBot.getHand().get(i).getPoint()) {
						lowestCardPoint = regularBot.getHand().get(i).getPoint();
						playedCard = regularBot.getHand().get(i);
					}

				}

			}
			handOrganizer(playedCard, regularBot);
			return playedCard;

		}
		return null;

	}

	private void handOrganizer(Cards playedCard, Player player) {
		int index = -1;
		for (int i = 0; i < player.getHand().size(); i++) {
			if (playedCard == player.getHand().get(i)) {
				index = i;
				break;
			}

		}
		board.add(playedCard);
		thrownCards.add(playedCard);
		player.getHand().remove(index);

	}

	private Cards noviceBotPlayCard(BotPlayers noviceBot) {
		getBotHand(noviceBot);
		Random random = new Random();
		int noviceBotCardChoice = random.nextInt(noviceBot.getHand().size());
		Cards playedCard = noviceBot.getHand().get(noviceBotCardChoice);
		handOrganizer(playedCard, noviceBot);
		return playedCard;

	}

	private void getBotHand(BotPlayers botPlayer) {
		System.out.println(botPlayer.getName() + "'s cards are:");

		for (int i = 0; i < botPlayer.getHand().size(); i++) {
			System.out.print((i + 1) + ") " + botPlayer.getHand().get(i) + ", ");
		}

		System.out.println();
	}

	private void compare(Cards playedCard, Player player) {
		System.out.println(player.getName() + " is Playing: " + playedCard);
		if (board.size() != 1) {
			if (playedCard.getRank().equalsIgnoreCase(board.get(board.size() - 2).getRank())) {
				System.out.println(player.getName() + " Collecting all cards on board...");
				if (board.size() == 2) {
					System.out.println(player.getName() + " made a Misti!");
					player.setMistiNumber(player.getMistiNumber() + 1);
				}

				player.getCollectedCards().addAll(board);
				board.clear();
			} else if (playedCard.getRank().equalsIgnoreCase("J")) {
				System.out.println(player.getName() + " Collecting all cards on board...");
				player.getCollectedCards().addAll(board);
				board.clear();
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
		handOrganizer(playedCard, humanPlayer);
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
					implementBotNames();
				}

			} catch (Exception e) {
				System.err.println("Please enter a valid value!");
			}
		} while (!isValid);

	}

	private void implementBotNames() {
		int noviceBotCounter = 0;
		int regularBotCounter = 0;
		int expertBotCounter = 0;
		for (BotPlayers botPlayer : botPlayers) {
			if (botPlayer.getBotDifficulty() == BotConstants.NOVICE_BOT_LEVEL)
				botPlayer.setName(botPlayer.getName() + " " + ++noviceBotCounter);
			else if (botPlayer.getBotDifficulty() == BotConstants.REGULAR_BOT_LEVEL)
				botPlayer.setName(botPlayer.getName() + " " + ++regularBotCounter);
			else if (botPlayer.getBotDifficulty() == BotConstants.EXPERT_BOT_LEVEL)
				botPlayer.setName(botPlayer.getName() + " " + ++expertBotCounter);
		}

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
		System.out.println(humanPlayer.getName() + " Cards are: ");
		for (int i = 0; i < humanPlayer.getHand().size(); i++) {
			System.out.print((i + 1) + ")" + humanPlayer.getHand().get(i) + "  ");
		}
		System.out.println();

	}

	@Override
	public void checkBoardStatus() {
		if (deck.isEmpty())
			gameOver = true;
		System.out.println();
		System.out.println("Board cards are: ");

		boardCardsSum = 0;
		for (int i = 0; i < board.size(); i++) {
			System.out.print(board.get(i) + ", ");
			boardCardsSum += board.get(i).getPoint();
		}
		System.out.println();

		System.out.println("Board cards sum: " + boardCardsSum);
		Cards lastCard = board.isEmpty() ? null : board.get(board.size() - 1);
		// System.out.println("Remained deck cards: " + deck.size());
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
