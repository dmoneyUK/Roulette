package mypackage;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RouletteTest {
	private Roulette roulette;

	@Before
	public void setUp() {
		this.roulette = new Roulette();
	}

	@Test
	public void testRoulette() {
		Assert.assertNotNull(this.roulette);
	}

	@Test
	public void testAddBetForPlayer() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddPlayer() {
		fail("Not yet implemented");
	}

	@Test
	public void testStartGame() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadPlayerInfo() {
		fail("Not yet implemented");
	}

}
