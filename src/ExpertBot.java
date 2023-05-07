
import java.util.ArrayList;

public final class ExpertBot extends BotPlayers {

	public ExpertBot() {
		super(BotConstants.EXPERT_BOT_NAME, BotConstants.EXPERT_BOT_LEVEL);

	}

	@Override
	public Cards botPlayCard() {
		boolean canTakeBoard = false;
		int boardCardsMaxPotentialPoint = 0;

		Cards playedCard = null;

		ArrayList<Cards> mostPlayedCards = new ArrayList<>();
		int[] mostThrownCardsNumber = new int[this.getHand().size()];

		// Checking collected cards from bots
		for (int i = 0; i < this.getHand().size(); i++) {
			for (BotPlayers botPlayer : Game.getBotPlayers()) {
				for (int j = 0; j < botPlayer.getCollectedCards().size(); j++) {
					if (this.getHand().get(i).getRank()
							.equalsIgnoreCase(botPlayer.getCollectedCards().get(j).getRank()))
						mostThrownCardsNumber[i]++;

				}
			}
			// Checking Collected Cards From Human
			if (Game.getHumanPlayer() != null) {
				for (int j = 0; j < Game.getHumanPlayer().getCollectedCards().size(); j++) {
					if (this.getHand().get(i).getRank()
							.equalsIgnoreCase(Game.getHumanPlayer().getCollectedCards().get(j).getRank()))
						mostThrownCardsNumber[i]++;

				}

			}

			if (!(Game.getBoard().isEmpty())) {
				for (int j = 0; j < Game.getBoard().size(); j++) {
					if (this.getHand().get(i).getRank().equalsIgnoreCase(Game.getBoard().get(j).getRank()))
						mostThrownCardsNumber[i]++;

				}
			}

			System.out.println(this.getHand().get(i).getRank() + " Throwed:  " + mostThrownCardsNumber[i] + " Times");
		}

		for (int i = 0; i < mostThrownCardsNumber.length; i++) {
			if (mostThrownCardsNumber[i] == 3) {
				playedCard = this.getHand().get(i);
				mostPlayedCards.add(playedCard);
			}
		}

		if (!Game.getBoard().isEmpty()) {
			for (int i = 0; i < this.getHand().size(); i++) {
				Cards currentCard = this.getHand().get(i);
				if (currentCard.getRank().equals(Game.getBoard().get(Game.getBoard().size() - 1).getRank())) {

					if ((currentCard.getPoint() + Game.getBoardCardsSum()) > boardCardsMaxPotentialPoint) {
						playedCard = this.getHand().get(i);
						canTakeBoard = true;
						boardCardsMaxPotentialPoint = playedCard.getPoint() + Game.getBoardCardsSum();
						continue;
					}

				}

				if (currentCard.getRank().equalsIgnoreCase("J")) {

					if ((currentCard.getPoint() + Game.getBoardCardsSum()) > boardCardsMaxPotentialPoint) {
						playedCard = this.getHand().get(i);
						boardCardsMaxPotentialPoint = playedCard.getPoint() + Game.getBoardCardsSum();
						canTakeBoard = true;
					}

				}

			}
		} else {
			playedCard = getLowestMostPlayedCard(mostPlayedCards);

			if (playedCard != null && !playedCard.getRank().equalsIgnoreCase("J")) {
				handOrganizer(playedCard);
				return playedCard;
			}
		}
		if (canTakeBoard) {

			handOrganizer(playedCard);
			return playedCard;

		} else {
			int lowestCardPoint = Integer.MAX_VALUE;
			for (int i = 0; i < this.getHand().size(); i++) {
				Cards currentCard = this.getHand().get(i);
				if (this.getHand().size() == 1) {
					playedCard = this.getHand().get(0);
					break;
				}

				if (!Game.getBoard().isEmpty()) {
					if ((lowestCardPoint > this.getHand().get(i).getPoint())
							&& !currentCard.getRank().equalsIgnoreCase("J")) {
						if (!currentCard.getRank()
								.equalsIgnoreCase(Game.getBoard().get(Game.getBoard().size() - 1).getRank())) {
							lowestCardPoint = currentCard.getPoint();
							playedCard = currentCard;
						}
					}

				} else {
					if (lowestCardPoint > currentCard.getPoint() && !currentCard.getRank().equalsIgnoreCase("J")) {
						lowestCardPoint = currentCard.getPoint();
						playedCard = currentCard;
					}
				}

			}
			if (playedCard == null) {
				playedCard = this.getHand().get(0);
			}

			int howManyThrownCards = -1;

			for (int i = 0; i < this.getHand().size(); i++) {
				if (playedCard.getPoint() == this.getHand().get(i).getPoint()) {
					if (mostThrownCardsNumber[i] > howManyThrownCards) {
						howManyThrownCards = mostThrownCardsNumber[i];
						playedCard = this.getHand().get(i);
					}
				}
			}

		}

		handOrganizer(playedCard);
		return playedCard;
	}

	private Cards getLowestMostPlayedCard(ArrayList<Cards> mostPlayedCards) {
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

}
