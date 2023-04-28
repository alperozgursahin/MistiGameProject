
public class Scoreboard {

	private HumanPlayer humanPlayer;
	private BotPlayers[] botPlayers;

	public Scoreboard(HumanPlayer humanPlayer, BotPlayers[] botPlayers) {
		this.setHumanPlayer(humanPlayer);
		this.setBotPlayers(botPlayers);
		scoreCalculator();
	}

	public void getScores() {
		System.out.println("**********SCOREBOARD**********");
		if (humanPlayer != null) {
			System.out.println(humanPlayer.getName() + "'s Score is: " + humanPlayer.getScore() + " Pisti Number is: "
					+ humanPlayer.getMistiNumber());
		}
		for (int i = 0; i < botPlayers.length; i++) {
			System.out.println(botPlayers[i].getName() + " Score is: " + botPlayers[i].getScore() + " Pisti Number is: "
					+ botPlayers[i].getMistiNumber());

		}
	}

	private void scoreCalculator() {
		System.out.println("Scores are calculating..");
		if (humanPlayer != null) {
			for (Cards card : humanPlayer.getCollectedCards()) {
				humanPlayer.setScore(humanPlayer.getScore() + card.getPoint());
			}
			humanPlayer.setScore(humanPlayer.getScore() + humanPlayer.getMistiNumber() * 10);
		}
		for (int i = 0; i < botPlayers.length; i++) {
			for (Cards card : botPlayers[i].getCollectedCards()) {
				botPlayers[i].setScore(botPlayers[i].getScore() + card.getPoint());
			}
			botPlayers[i].setScore(botPlayers[i].getScore() + botPlayers[i].getMistiNumber() * 10);
		}
	}

	public BotPlayers[] getBotPlayers() {
		return botPlayers;
	}

	public void setBotPlayers(BotPlayers[] botPlayers) {
		this.botPlayers = botPlayers;
	}

	public HumanPlayer getHumanPlayer() {
		return humanPlayer;
	}

	public void setHumanPlayer(HumanPlayer humanPlayer) {
		this.humanPlayer = humanPlayer;
	}

}
