
import java.util.ArrayList;

public final class ExpertBot extends BotPlayers {

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

		ArrayList<Cards> mostPlayedCards = new ArrayList<>();
		int[] mostThrownCards = new int[this.getHand().size()];

		// Checking collected cards from bots
		for (int i = 0; i < this.getHand().size(); i++) {
			for (BotPlayers botPlayer : Game.getBotPlayers()) {
				for (int j = 0; j < botPlayer.getCollectedCards().size(); j++) {
					if (this.getHand().get(i).getRank()
							.equalsIgnoreCase(botPlayer.getCollectedCards().get(j).getRank()))
						mostThrownCards[i]++;

				}
			}
			// Checking Collected Cards From Human
			if (Game.getHumanPlayer() != null) {
				for (int j = 0; j < Game.getHumanPlayer().getCollectedCards().size(); j++) {
					if (this.getHand().get(i).getRank()
							.equalsIgnoreCase(Game.getHumanPlayer().getCollectedCards().get(j).getRank()))
						mostThrownCards[i]++;

				}

			}

			if (!(Game.getBoard().isEmpty())) {
				for (int j = 0; j < Game.getBoard().size(); j++) {
					if (this.getHand().get(i).getRank().equalsIgnoreCase(Game.getBoard().get(j).getRank()))
						mostThrownCards[i]++;

				}
			}

			System.out.println(this.getHand().get(i).getRank() + " Throwed:  " + mostThrownCards[i] + " Times");
		}

		for (int i = 0; i < mostThrownCards.length; i++) {
			if (mostThrownCards[i] == 3) {
				playedCard = this.getHand().get(i);
				mostPlayedCards.add(playedCard);
			}
		}

		if (!Game.getBoard().isEmpty()) {
			for (int i = 0; i < this.getHand().size(); i++) {

				Cards currentCard = this.getHand().get(i);
				if (currentCard.getRank().equals(Game.getBoard().get(Game.getBoard().size() - 1).getRank())) {
					playedCard = currentCard;

					int potentialPoint = currentCard.getPoint() + Game.getBoardCardsSum();
					if (potentialPoint > boardCardsMaxPotentialPoint) {

						isCardsNumHigher = true;
						canTakeBoard = true;
						boardCardsMaxPotentialPoint = potentialPoint;
					}
					boardCardsMaxPotentialPoint = 0;
				}

				if (currentCard.getRank().equalsIgnoreCase("J")) {
					playedJokerCard = currentCard;
					int potentialPoint = currentCard.getPoint() + Game.getBoardCardsSum();
					if (potentialPoint > boardCardsMaxPotentialPoint) {
						isCardsNumHigher = true;
						isJokerPlayable = true;
						boardCardsMaxPotentialPoint = potentialPoint;

					}
				}
			}
		} else {

			playedCard = Game.getLowestMostPlayedCard(mostPlayedCards);

			if (playedCard != null && !playedCard.getRank().equalsIgnoreCase("J")) {
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
			int howManyThrownCards = -1;

			for (int j = 0; j < this.getHand().size(); j++) {
				if (playedCard.getPoint() == this.getHand().get(j).getPoint()) {
					if (mostThrownCards[j] > howManyThrownCards) {
						howManyThrownCards = mostThrownCards[j];
						playedCard = this.getHand().get(j);
					}
				}
			}

			handOrganizer(playedCard);
			return playedCard;

		}
		return null;
	}

}
