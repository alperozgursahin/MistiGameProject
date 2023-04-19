import java.util.ArrayList;

public interface Misti {
	
	void startGame();
	
	void inputPlayers();
	
	void checkBotStatus();
	
	void checkPlayerStatus(boolean spectatorMode, HumanPlayer humanPlayer);
	
	void dealCards(BotPlayers[] botPlayers, HumanPlayer humanPlayer, ArrayList<Cards> deck);
	
	void checkBoardStatus();
	
	void inputBotLevel();
	
	void addBot(int botDifficultyLevelChoice, int botCounter);
	
	void humanPlayCard();
	
	

}
