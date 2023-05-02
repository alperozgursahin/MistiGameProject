public class RegularBot extends BotPlayers {

	public RegularBot() {
		super(BotConstants.REGULAR_BOT_NAME, BotConstants.REGULAR_BOT_LEVEL);

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

				if (this.getHand().get(i).getRank().equals(Game.getBoard().get(Game.getBoard().size() - 1).getRank())) {
					playedCard = this.getHand().get(i);
					if ((playedCard.getPoint() + Game.getBoardCardsSum()) > boardCardsMaxPotentialPoint) {
						playedCard = this.getHand().get(i);

						isCardsNumHigher = true;
						canTakeBoard = true;
						boardCardsMaxPotentialPoint += playedCard.getPoint() + Game.getBoardCardsSum();

					}

				}

				if (this.getHand().get(i).getRank().equalsIgnoreCase("J")) {
					playedJokerCard = this.getHand().get(i);
					if ((playedJokerCard.getPoint() + Game.getBoardCardsSum()) > 0) {
						isCardsNumHigher = true;
						isJokerPlayable = true;

					}

				}

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
				if (!Game.getBoard().isEmpty()) {
					if ((lowestCardPoint > this.getHand().get(i).getPoint()) && !(this.getHand().get(i).getRank()
							.equals(Game.getBoard().get(Game.getBoard().size() - 1).getRank()))) {
						lowestCardPoint = this.getHand().get(i).getPoint();
						playedCard = this.getHand().get(i);
					}
				} else {
					if (lowestCardPoint > this.getHand().get(i).getPoint()) {
						lowestCardPoint = this.getHand().get(i).getPoint();
						playedCard = this.getHand().get(i);
					}

				}

			}
			handOrganizer(playedCard);
			return playedCard;

		}
		return null;
	}

}
