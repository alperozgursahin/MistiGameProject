
public final class RegularBot extends BotPlayers {

	public RegularBot() {
		super(BotConstants.REGULAR_BOT_NAME, BotConstants.REGULAR_BOT_LEVEL);

	}

	@Override
	public Cards botPlayCard() {
		boolean canTakeBoard = false;
		boolean canMakePisti = false;
		int boardCardsMaxPotentialPoint = 0;

		Cards playedCard = null;

		if (!Game.getBoard().isEmpty()) {
			for (int i = 0; i < this.getHand().size(); i++) {
				Cards currentCard = this.getHand().get(i);
				if (currentCard.getRank().equals(Game.getBoard().get(Game.getBoard().size() - 1).getRank())) {

					if ((currentCard.getPoint() + Game.getBoardCardsSum()) > boardCardsMaxPotentialPoint) {
						playedCard = this.getHand().get(i);
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

		}
		handOrganizer(playedCard);
		return playedCard;
	}

}
