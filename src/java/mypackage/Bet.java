package mypackage;

/**
 * Represents a bet from a player.
 */
public class Bet {
	/**
	 * Either a number between 1 and 36, EVEN or ODD
	 */
	private String betChoice;
	/**
	 * How much the player wants to bet
	 */
	private double multiple;

	/**
	 * Constructor with the bet choice and multiple.
	 * 
	 * @param betChoice
	 *            the belt choice
	 * @param multiple
	 *            how much the player wants to bet
	 */
	public Bet(String betChoice, double multiple) {
		this.betChoice = betChoice;
		this.multiple = multiple;
	}

	/**
	 * @return the bet choice
	 */
	public String getBetChoice() {
		return betChoice;
	}

	/**
	 * @param betChoice
	 *            the bet choice to set
	 */
	public void setBetChoice(String betChoice) {
		this.betChoice = betChoice;
	}

	/**
	 * @return the multiple
	 */
	public double getMultiple() {
		return multiple;
	}

	/**
	 * @param multiple
	 *            the multiple to set
	 */
	public void setMultiple(double multiple) {
		this.multiple = multiple;
	}
}
