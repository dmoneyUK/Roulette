package mypackage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * The casino game Roulette.
 */
public class Roulette {
	/**
	 * The enum type that represents a player has won or lost.
	 */
	private enum OUTCOME {
		WIN, LOSE
	}

	/**
	 * A map from player name to a {@link Player}.
	 */
	private Map<String, Player> players;

	/**
	 * The random number.
	 */
	private int randomNumber;

	/**
	 * The round number
	 */
	private int roundNumber = 1;

	/**
	 * Indicates whether the current round has ended.
	 */
	private boolean endofRound;

	/**
	 * Constructor.
	 */
	public Roulette() {
		this.players = new HashMap<>();
	}

	/**
	 * Adds a bet for an existing player.
	 * 
	 * @param name
	 *            the player name
	 * @param bet
	 *            the {@link Bet} to add
	 */
	public void addBetForPlayer(String name, Bet bet) {
		this.players.get(name).addBet(bet);
	}

	/**
	 * Adds a player to the game.
	 * 
	 * @param name
	 *            the name of the player to add.
	 */
	public void addPlayer(String name) {
		this.players.put(name, new Player(name));
	}

	/**
	 * Starts the game.
	 * 
	 * @throws NumberFormatException
	 *             thrown if the third input from console is not a valid double
	 * @throws InterruptedException
	 *             thrown if the main thread is interrupted
	 */
	public void startGame() throws NumberFormatException, InterruptedException {
		// creates a task to generate a random number and prints out game
		// results every 30 seconds
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		Runnable randomGenerator = () -> {
			// generate a random number
			Random random = new Random();
			this.randomNumber = random.nextInt(36) + 1;
			this.endofRound = true;
			System.out.printf("The ball landed on number %s.\n", this.randomNumber);
			System.out.println("Round " + this.roundNumber++);
			// print out the game results
			this.printGameResult();
			this.clearBets();
			this.endofRound = false;
			Thread.currentThread().notify();
		};
		executor.scheduleAtFixedRate(randomGenerator, 30, 30, TimeUnit.SECONDS);
		// the game accepts the bets from players all the time
		while (true) {
			if (this.endofRound) {
				Thread.currentThread().wait();
			}
			String[] infos = System.console().readLine().split(" ");
			if (infos.length < 3) {
				System.err.println(
						"Each line should contain the player's name, what they want to bet on (either a number from 1-36, EVEN or ODD), and how much they want to bet.");
				continue;
			} else {
				String playername = infos[0];
				String betChoice = infos[1];
				double multiple;
				if (!this.players.containsKey(playername)) {
					System.err.println("Player with name '" + playername + "' does not exist.");
					continue;
				} else if (!this.isValidBetChoice(betChoice)) {
					System.err.println("Bet on either a number from 1-36, EVEN or ODD.");
					continue;
				} else {
					try {
						multiple = Double.valueOf(infos[2]);
					} catch (NumberFormatException ex) {
						System.err.println("The bet should be a float.");
						continue;
					}
				}
				this.addBetForPlayer(infos[0], new Bet(infos[1], multiple));
			}
		}
	}

	/**
	 * Reads player info from the given file.
	 * 
	 * @param path
	 *            the file path
	 * @throws IOException
	 *             thrown if an I/O error occurs opening the file
	 */
	public void readPlayerInfoFromFile(String path) throws IOException {
		// read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(path))) {
			stream.forEach(line -> this.addPlayer(line));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw e;
		}
		this.players.keySet().stream().forEach(key -> System.out.println(key));
	}

	/**
	 * Entry point of the application with one param for the file that contains
	 * player info.
	 * 
	 * @param args
	 *            the first element of which is the file containing player info
	 * @throws NumberFormatException
	 *             thrown from startGame method
	 * @throws InterruptedException
	 *             thrown from startGame method
	 * @throws Exception
	 *             thrown from startGame method
	 */
	public static void main(String[] args) throws NumberFormatException, IOException, InterruptedException {
		Roulette game = new Roulette();
		game.readPlayerInfoFromFile(args[0]);
		game.startGame();
	}

	/**
	 * Checks whether the given bet is valid.
	 * 
	 * @param choice
	 *            the bet choice to check
	 * @return true if valid, false otherwise
	 */
	private boolean isValidBetChoice(String choice) {
		if ("EVEN".equals(choice) || "ODD".equals(choice)) {
			return true;
		} else {
			try {
				int number = Integer.valueOf(choice);
				if (number > 0 && number <= 36) {
					return true;
				} else {
					return false;
				}
			} catch (NumberFormatException ex) {
				return false;
			}
		}
	}

	/**
	 * Prints out the game result to console.
	 */
	private void printGameResult() {
		System.out.format("%-50s%10s%10s%10s\n", "Player", "Bet", "Outcome", "Winnings");
		for (Entry<String, Player> entry : this.players.entrySet()) {
			Player player = entry.getValue();
			for (Bet bet : player.getBets()) {
				double winnings = 0.0;
				if (String.valueOf(this.randomNumber).equals(bet.getBetChoice())) {
					winnings = 36 * bet.getMultiple();
				} else if ("EVEN".equals(bet.getBetChoice()) && this.randomNumber % 2 == 0) {
					winnings = 2 * bet.getMultiple();
				} else if ("ODD".equals(bet.getBetChoice()) && this.randomNumber % 2 != 0) {
					winnings = 2 * bet.getMultiple();
				}
				System.out.format("%-50s%10s%10s%.1f\n", entry.getKey(), bet.getBetChoice(), (winnings > 0 ? OUTCOME.WIN
						: OUTCOME.LOSE), winnings);
			}
		}
	}

	/**
	 * Clears bets of players after each round.
	 */
	private void clearBets() {
		this.players.values().stream().forEach(player -> player.setBets(new ArrayList<Bet>()));
	}
}
