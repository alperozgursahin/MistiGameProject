
import java.util.ArrayList;
import java.util.Arrays;

public final class ExpertBot extends BotPlayers {

	public ExpertBot() {
		super(BotConstants.EXPERT_BOT_NAME, BotConstants.EXPERT_BOT_LEVEL);

	}

	@Override
	public Cards botPlayCard() {
		boolean canTakeBoard = false;
		int boardCardsMaxPotentialPoint = 0;
		boolean canMakePisti = false;

		Cards playedCard = null;

		ArrayList<Cards> mostPlayedCards = new ArrayList<>();
		int[] mostThrownCardsNumber = new int[this.getHand().size()];

		checkCollectedCards(mostThrownCardsNumber);

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
						playedCard = currentCard;
						canTakeBoard = true;
						boardCardsMaxPotentialPoint = playedCard.getPoint() + Game.getBoardCardsSum();
						if (Game.getBoard().size() == 1)
							canMakePisti = true;
						continue;
					}

				}

				if (currentCard.getRank().equalsIgnoreCase("J") && !canMakePisti) {

					if ((currentCard.getPoint() + Game.getBoardCardsSum()) > boardCardsMaxPotentialPoint) {
						playedCard = this.getHand().get(i);
						boardCardsMaxPotentialPoint = playedCard.getPoint() + Game.getBoardCardsSum();
						canTakeBoard = true;
					}

				}

			}
		} else {
			for (int i = 0; i < mostThrownCardsNumber.length; i++) {
				if (mostThrownCardsNumber[i] > 2) {
					playedCard = this.getHand().get(i);
					mostPlayedCards.add(playedCard);
				}
			}
			if (mostPlayedCards.size() > 1)
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

			playedCard = getMostThrownCard(playedCard, mostThrownCardsNumber);
		}

		handOrganizer(playedCard);
		return playedCard;
	}

	private Cards getMostThrownCard(Cards playedCard, int[] mostThrownCardsNumber) {
		int howManyThrownCards = -1;
		Cards mostThrownCard = playedCard;

		for (Cards card : this.getHand()) {
			if (card.getPoint() == playedCard.getPoint()
					&& mostThrownCardsNumber[getHand().indexOf(card)] > howManyThrownCards
					&& !card.getRank().equalsIgnoreCase("J") && (Game.getBoard().isEmpty() || !card.getRank()
							.equalsIgnoreCase(Game.getBoard().get(Game.getBoard().size() - 1).getRank()))) {
				howManyThrownCards = mostThrownCardsNumber[getHand().indexOf(card)];
				mostThrownCard = card;
			}
		}

		return mostThrownCard;
	}

	private Cards getLowestMostPlayedCard(ArrayList<Cards> mostPlayedCards) {
		int maxCardPoint = Integer.MAX_VALUE;
		Cards lowestMostPlayedCard = null;
		for (Cards card : mostPlayedCards) {
			if (card.getPoint() < maxCardPoint) {
				maxCardPoint = card.getPoint();
				lowestMostPlayedCard = card;
			}
		}
		return lowestMostPlayedCard;

	}

	private void checkCollectedCards(int[] mostThrownCardsNumber) {
		ArrayList<Player> players = new ArrayList<>();
		players.addAll(Arrays.asList(Game.getBotPlayers()));
		players.add(Game.getHumanPlayer());

		for (int i = 0; i < this.getHand().size(); i++) {
			for (Player player : players) {
				if (player != null) {
					for (Cards card : player.getCollectedCards()) {
						if (this.getHand().get(i).getRank().equalsIgnoreCase(card.getRank())) {
							mostThrownCardsNumber[i]++;
						}
					}
				}
			}

			if (!Game.getBoard().isEmpty()) {
				for (Cards card : Game.getBoard()) {
					if (this.getHand().get(i).getRank().equalsIgnoreCase(card.getRank())) {
						mostThrownCardsNumber[i]++;
					}
				}
			}

			if (Game.isVerboseness()) {
				System.out.println(
						this.getHand().get(i).toString() + " => Thrown " + mostThrownCardsNumber[i] + " Times");
			}
		}

		System.out.println();
	}

}
