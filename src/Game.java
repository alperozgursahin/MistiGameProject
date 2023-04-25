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
	int maxBotPlayerNumber = 3;
	int minimumBotPlayerNumber = 1;
	int botPlayerNumber = 0;
	int playerNumber;
	int round;

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

//		checkBotStatus();
//		checkPlayerStatus();
//		checkBoardStatus();
		if (!gameOver) {
			playerNumber = botPlayers.length + isHumanPlayerIn();
			round = roundNumberCalculator(playerNumber);
			dealCardsToBoard();
		}

		while (!gameOver) {
			while (round > 0) {
				dealCardsToPlayers();
				round--;

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
				}

			}
		}

	}

	private void expertBotPlayCard(BotPlayers botPlayer) {
		// TODO Auto-generated method stub

	}

	private void regularBotPlayCard(BotPlayers botPlayer) {
		// TODO Auto-generated method stub

	}

	private void noviceBotPlayCard(BotPlayers noviceBot) {
		System.out.println(noviceBot.getName() + "'s turn!");
		System.out.println(noviceBot.getName() + "'s cards are:");

		for (int i = 0; i < noviceBot.getHand().size(); i++) {
			System.out.print((i + 1) + ") " + noviceBot.getHand().get(i) + ", ");
		}

		System.out.println();

		int noviceBotCardChoice = random.nextInt(noviceBot.getHand().size()) + 1;
		Cards playedCard = noviceBot.getHand().get(noviceBotCardChoice - 1);
		System.out.println(noviceBot.getName() + " is playing: " + playedCard);
		board.add(playedCard);
		noviceBot.getHand().remove(noviceBotCardChoice - 1);
		compare(playedCard, noviceBot);

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
					System.out.println("Collecting cards on board...");
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
		while (!validPlayerInput) {
			try {
				playerCardChoice = Integer.parseInt(scanner.nextLine());
				if (playerCardChoice < 1 || playerCardChoice > 4) {
					validPlayerInput = false;
					System.out.println("Please enter a valid value!");
				} else {
					validPlayerInput = true;
				}

			} catch (Exception e) {
				System.err.println("Please enter a valid value!");
			}
		}
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
						gameOver = true;
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
		for (int i = 0; i < board.size(); i++) {
			System.out.print(board.get(i) + ", ");
		}
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
