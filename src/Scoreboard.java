import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Scoreboard {

	private ArrayList<Player> players = new ArrayList<>();
	private Player highestScoredPlayer = null;
	private static final String filePath = "HighScores.txt";

	public Scoreboard(HumanPlayer humanPlayer, BotPlayers[] botPlayers) {

		scoreCalculator(humanPlayer, botPlayers);

	}

	private void scoreCalculator(HumanPlayer humanPlayer, BotPlayers[] botPlayers) {

		if (humanPlayer != null)
			players.add(humanPlayer);
		if (botPlayers == null)
			return;
		System.out.println();
		System.out.println("Scores are calculating..");
		for (BotPlayers botPlayer : botPlayers) {
			players.add(botPlayer);
		}

		for (int i = 0; i < players.size(); i++) {
			for (Cards card : players.get(i).getCollectedCards()) {
				players.get(i).setScore(players.get(i).getScore() + card.getPoint());
			}
			players.get(i).setMistiScore(players.get(i).getMistiScore() * 5);
			players.get(i).setScore(players.get(i).getScore() + players.get(i).getMistiScore());
		}
		System.out.println();
		System.out.println("**********SCOREBOARD**********");
		System.out.println("-----------------------------");
		for (int i = 0; i < players.size(); i++) {

			System.out
					.println(players.get(i).getName() + " Score is: " + players.get(i).getScore() + " Misti Number is: "
							+ players.get(i).getMistiNumber() + " Misti Score is : " + players.get(i).getMistiScore());

		}
		System.out.println("-----------------------------");
		int lowestScore = Integer.MIN_VALUE;
		for (Player player : players) {

			if (player.getScore() > lowestScore) {
				lowestScore = player.getScore();
				highestScoredPlayer = player;
			}

		}

		players.clear();
		players.add(highestScoredPlayer);

		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				String name = parts[0].trim();
				int point = Integer.parseInt(parts[1].trim());
				Player player = new Player(name);
				player.setScore(point);
				players.add(player);
			}

			br.close();
		} catch (IOException e) {
			try {
				File newFile = new File(filePath);
				newFile.createNewFile();
				System.out.println("New file created: " + filePath);
			} catch (IOException ex) {

				System.out.println("Could not create new file.");
			}
		}
		writeScores();
	}

	private void writeScores() {

		Collections.sort(players, Comparator.comparingInt(Player::getScore).reversed());

		for (int i = players.size(); i > 10; i--) {
			if (highestScoredPlayer.getScore() > players.get(players.size() - 1).getScore()) {
				System.out.println(highestScoredPlayer.getName() + " IS ENTERING THE HIGH SCORE LIST!!!");
				System.out.println(players.get(players.size() - 1).getName() + " is removing from " + filePath);
			}
			players.remove(players.size() - 1);

		}
		File file = new File(filePath);
		try {
			FileWriter fileWriter = new FileWriter(file);
			System.out.println();
			System.out.println(filePath + " is Arranging..");
			System.out.println();
			for (Player player : players) {
				fileWriter.write(player.getName() + ", " + player.getScore() + "\n");
				System.out.println("Name: " + player.getName() + " | Score: " + player.getScore());
			}

			fileWriter.close();
			System.out.println();
			System.out.println("File created and players written.");
		} catch (IOException e) {
			System.out.println("File creation error: " + e.getMessage());
		}

	}

}
