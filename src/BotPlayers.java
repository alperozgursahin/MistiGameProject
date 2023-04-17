import java.util.ArrayList;

public abstract class BotPlayers extends Player {

	private int botDifficulty;

	public BotPlayers(String name, ArrayList<Cards> hand, int botDifficulty) {
		super(name, hand);
		this.setBotDifficulty(botDifficulty);
	}

	public int getBotDifficulty() {
		return botDifficulty;
	}

	public void setBotDifficulty(int botDifficulty) {
		this.botDifficulty = botDifficulty;
	}

}
