
public abstract class BotPlayers extends Player {

	private int botDifficulty;

	public BotPlayers(String name, int botDifficulty) {
		super(name);
		this.setBotDifficulty(botDifficulty);
		this.setType("BOT");

	}
	
	public abstract Cards botPlayCard();

	public int getBotDifficulty() {
		return botDifficulty;
	}

	public void setBotDifficulty(int botDifficulty) {
		this.botDifficulty = botDifficulty;
	}

}
