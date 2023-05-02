import java.util.Random;

public class NoviceBot extends BotPlayers {

	public NoviceBot() {
		super(BotConstants.NOVICE_BOT_NAME, BotConstants.NOVICE_BOT_LEVEL);
	}

	public Cards botPlayCard() {

		Random random = new Random();
		int noviceBotCardChoice = random.nextInt(this.getHand().size());
		Cards playedCard = this.getHand().get(noviceBotCardChoice);
		handOrganizer(playedCard);
		return playedCard;

	}

}
