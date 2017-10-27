package mypackage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BetTest {
	private Bet bet;

	@Before
	public void setUp() {
		this.bet = new Bet("1", 2);
	}

	@Test
	public void testBet() {
		Assert.assertNotNull(this.bet);
		Assert.assertEquals("1", this.bet.getBetChoice());
		Assert.assertTrue(2.0 == this.bet.getMultiple());
	}

	@Test
	public void testGetBetChoice() {
		Assert.assertNotNull(this.bet);
		Assert.assertEquals("1", this.bet.getBetChoice());
	}

	@Test
	public void testSetBetChoice() {
		Assert.assertEquals("1", this.bet.getBetChoice());
		this.bet.setBetChoice("EVEN");
		Assert.assertEquals("EVEN", this.bet.getBetChoice());
	}

	@Test
	public void testGetMultiple() {
		Assert.assertNotNull(this.bet);
		Assert.assertTrue(2.0 == this.bet.getMultiple());
	}

	@Test
	public void testSetMultiple() {
		Assert.assertNotNull(this.bet);
		Assert.assertTrue(2.0 == this.bet.getMultiple());
		this.bet.setMultiple(3.0);
		Assert.assertTrue(3.0 == this.bet.getMultiple());
	}

}
