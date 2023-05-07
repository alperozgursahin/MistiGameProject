import java.util.ArrayList;
import java.util.Scanner;

public class Game implements Misti {

	private Scanner scanner = new Scanner(System.in);
	private ArrayList<Cards> deck = new ArrayList<>();
	private static ArrayList<Cards> board = new ArrayList<>();

	private static HumanPlayer humanPlayer;
	private boolean spectatorMode = false;
	private boolean isValid = false;
	private boolean gameOver;
	private static Player lastWinner;

	private static BotPlayers[] botPlayers;

	public static BotPlayers[] getBotPlayers() {
		return botPlayers;
	}

	public static void setBotPlayers(BotPlayers[] botPlayers) {
		Game.botPlayers = botPlayers;
	}

	private int maximumBotPlayerNumber = 3;
	private int minimumBotPlayerNumber = 1;
	private int botPlayerNumber;
	private int playerNumber;
	private int round;
	private static int boardCardsSum;

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
						humanPlayer.playCard();
						botPlayerTurn();

					} else {
						checkBoardStatus();
						botPlayerTurn();
					}
				}

			}
			addLastCardsToPlayer(lastWinner);
			new Scoreboard(humanPlayer, botPlayers);
		}

	}

	private void addLastCardsToPlayer(Player lastWinner) {
		if (!board.isEmpty()) {
			System.out.println("Last Board Cards added to " + lastWinner.getName() + " Collected Cards.");
			lastWinner.getCollectedCards().addAll(board);
			board.clear();
		}

	}

	private void botPlayerTurn() {
		for (BotPlayers botPlayer : botPlayers) {
			getBotHand(botPlayer);
			System.out.println();
			compare(botPlayer.botPlayCard(), botPlayer);

		}
	}

	protected static Cards getLowestMostPlayedCard(ArrayList<Cards> mostPlayedCards) {
		int maxCardPoint = Integer.MAX_VALUE;
		Cards lowestMostPlayedCard = null;
		for (int i = 0; i < mostPlayedCards.size(); i++) {
			if (maxCardPoint > mostPlayedCards.get(i).getPoint()) {
				lowestMostPlayedCard = mostPlayedCards.get(i);
				maxCardPoint = mostPlayedCards.get(i).getPoint();
			}

		}
		return lowestMostPlayedCard;
	}

	private static void getBotHand(BotPlayers botPlayer) {
		System.out.println(botPlayer.getName() + "'s cards are:");

		for (int i = 0; i < botPlayer.getHand().size(); i++) {
			System.out.print((i + 1) + ") " + botPlayer.getHand().get(i) + ", ");
		}

		System.out.println();
	}

	protected static void compare(Cards playedCard, Player player) {
		System.out.println(player.getName() + " is Playing: " + playedCard);
		System.out.println("\n---------------------------------------------------\n");
		if (getBoard().size() != 1) {
			if (playedCard.getRank().equalsIgnoreCase(getBoard().get(getBoard().size() - 2).getRank())) {
				System.out.println(player.getName() + " Collecting all cards on board...");
				if (getBoard().size() == 2) {
					System.out.println(
							player.getName() + " made a Pisti!\n---------------------------------------------------");
					player.setMistiNumber(player.getMistiNumber() + 1);
					player.setMistiScore(player.getMistiScore() + board.get(0).getPoint() + playedCard.getPoint());

				}
				lastWinner = player;
				player.getCollectedCards().addAll(getBoard());
				player.setScore(player.getScore() - playedCard.getPoint() - board.get(0).getPoint());
				getBoard().clear();
			} else if (playedCard.getRank().equalsIgnoreCase("J")) {
				lastWinner = player;
				System.out.println(player.getName() + " Collecting all cards on board...");
				player.getCollectedCards().addAll(getBoard());
				getBoard().clear();
			}
		}
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
					System.out.println("GAME IS STARTING...");
					humanPlayer = new HumanPlayer();
					inputBotLevel();
					break;
				case 1:
					System.out.println("WELCOME TO THE SPECTATOR MODE. YOU CAN WATCH THE BOTS.");
					maximumBotPlayerNumber = 4;
					minimumBotPlayerNumber = 2;
					spectatorMode = true;
					inputBotLevel();
					break;
				case 2:
					System.out.println("GOODBYEEE!");
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
		System.out.println("How many bot players do you want in the game ?");
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
		System.out.println();
	}

	@Override
	public void dealCardsToBoard() {
		for (int i = 0; i < 4; i++) {
			getBoard().add(deck.get(0));
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

	protected static void checkPlayerStatus() {
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
		System.out.println("██████████████████████████  B O A R D  ██████████████████████████\n");

		boardCardsSum = 0;
		for (int i = 0; i < getBoard().size(); i++) {
			System.out.print(getBoard().get(i) + ", ");
			boardCardsSum += getBoard().get(i).getPoint();
		}
		System.out.println();
		System.out.println();

		System.out.println("Board cards sum: " + boardCardsSum);
		Cards lastCard = getBoard().isEmpty() ? null : getBoard().get(getBoard().size() - 1);
		// System.out.println("Remained deck cards: " + deck.size());
		System.out.println("The last board card is: " + lastCard + "\n");
		System.out.println("██████████████████████████████████████████████████████████████████\n");

	}

	public static ArrayList<Cards> getBoard() {
		return board;
	}

	public static void setBoard(ArrayList<Cards> board) {
		Game.board = board;
	}

	public static HumanPlayer getHumanPlayer() {
		return humanPlayer;
	}

	public static void setHumanPlayer(HumanPlayer humanPlayer) {
		Game.humanPlayer = humanPlayer;
	}

	public static int getBoardCardsSum() {
		int sum = 0;
		for (Cards card : board) {
			sum += card.getPoint();
		}
		return sum;
	}

	public static void setBoardCardsSum(int boardCardsSum) {
		Game.boardCardsSum = boardCardsSum;
	}

	public Player getLastWinner() {
		return lastWinner;
	}

	public void setLastWinner(Player lastWinner) {
		Game.lastWinner = lastWinner;
	}
}
