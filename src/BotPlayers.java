
public abstract class BotPlayers extends Player {

	private int botDifficulty;

	public BotPlayers(String name, int botDifficulty) {
		super(name);
		this.setBotDifficulty(botDifficulty);

	}

	public int getBotDifficulty() {
		return botDifficulty;
	}

	public void setBotDifficulty(int botDifficulty) {
		this.botDifficulty = botDifficulty;
	}

}
