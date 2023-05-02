import java.util.ArrayList;

public class ExpertBot extends BotPlayers {

	public ExpertBot() {
		super(BotConstants.EXPERT_BOT_NAME, BotConstants.EXPERT_BOT_LEVEL);

	}

	@Override
	public Cards botPlayCard() {
		boolean canTakeBoard = false;
		boolean isJokerPlayable = false;
		boolean isCardsNumHigher = false;
		int boardCardsMaxPotentialPoint = 0;

		Cards playedCard = null;
		Cards playedJokerCard = null;

		if (!Game.getBoard().isEmpty()) {
			for (int i = 0; i < this.getHand().size(); i++) {

				Cards currentCard = this.getHand().get(i);
				if (currentCard.getRank().equals(Game.getBoard().get(Game.getBoard().size() - 1).getRank())) {
					playedCard = currentCard;

					int potentialPoint = currentCard.getPoint() + Game.getBoardCardsSum();
					if (potentialPoint > boardCardsMaxPotentialPoint) {

						isCardsNumHigher = true;
						canTakeBoard = true;
						boardCardsMaxPotentialPoint += potentialPoint;
					}

				}

				if (currentCard.getRank().equalsIgnoreCase("J")) {
					playedJokerCard = currentCard;
					int potentialPoint = currentCard.getPoint() + Game.getBoardCardsSum();
					if (potentialPoint + Game.getBoardCardsSum() > 0) {
						isCardsNumHigher = true;
						isJokerPlayable = true;

					}
				}
			}
		} else {
			ArrayList<Cards> mostPlayedCards = new ArrayList<>();
			int[] mostThrownCards = new int[this.getHand().size()];

			for (Cards thrownCard : Game.getThrownCards()) {
				for (int j = 0; j < this.getHand().size(); j++) {
					if (thrownCard == this.getHand().get(j)) {
						mostThrownCards[j]++;
					}
				}
			}
			for (int i = 0; i < mostThrownCards.length; i++) {
				if (mostThrownCards[i] == 3) {
					playedCard = this.getHand().get(i);
					mostPlayedCards.add(playedCard);
				}
			}
			playedCard = Game.getLowestMostPlayedCard(mostPlayedCards);

			if (playedCard != null) {
				handOrganizer(playedCard);
				return playedCard;
			}

		}

		boardCardsMaxPotentialPoint = 0;
		if (isCardsNumHigher) {
			if (canTakeBoard) {
				handOrganizer(playedCard);
				return playedCard;

			} else if (isJokerPlayable) {
				handOrganizer(playedJokerCard);
				return playedJokerCard;

			}
		} else {
			int lowestCardPoint = Integer.MAX_VALUE;

			for (int i = 0; i < this.getHand().size(); i++) {
				Cards currentCard = this.getHand().get(i);
				if (!Game.getBoard().isEmpty()) {
					if ((currentCard.getPoint() < lowestCardPoint) && !(currentCard.getRank()
							.equals(Game.getBoard().get(Game.getBoard().size() - 1).getRank()))) {
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

			handOrganizer(playedCard);
			return playedCard;

		}
		return null;
	}

}
