package test;

import org.junit.*;

import location.XY;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class XYTest {

	private XY loc;

	@Before
	public void setup() {
		loc = new XY(1,1);
	}

	@Test
	public void testPlus() {
		assertEquals(new XY(2,2), loc.plus(loc));
	}

	@Test
	public void testMinus() {
		assertEquals(new XY(0, 0), loc.minus(loc));
	}

	@Test
	public void testTimes() {
		int factor = 5;
		assertEquals(new XY(factor, factor), loc.times(factor));
	}

	@Test
	public void testLength() {
		assertEquals(Math.sqrt(2), loc.length(), 1/Double.MAX_VALUE);
	}

	@Test
	public void testDistanceFrom() {
		XY test1 = new XY(1, 0);
		assertEquals(1, loc.distanceFrom(test1), 1/Double.MAX_VALUE);

		XY test2 = new XY(1, 2);
		assertEquals(1, loc.distanceFrom(test2), 1/Double.MAX_VALUE);

		XY test3 = new XY(0, 1);
		assertEquals(1, loc.distanceFrom(test3), 1/Double.MAX_VALUE);

		XY test4 = new XY(2, 1);
		assertEquals(1, loc.distanceFrom(test4), 1/Double.MAX_VALUE);

		XY test5 = new XY(4, 5);
		assertEquals(5, loc.distanceFrom(test5), 1/Double.MAX_VALUE);
	}

	@Test
	public void testHashCode() {
		assertEquals(993, loc.hashCode());
	}

	@Test
	public void testEquals() {
		assertTrue(loc.equals(loc));

		XY test1 = new XY(2, 2);
		assertFalse(loc.equals(test1));

		XY test2 = new XY(loc.x, loc.y);
		assertTrue(loc.equals(test2));
	}

	@Test
	public void testToString() {
		String string = loc.x + " " + loc.y;
		assertEquals(string, loc.toString());
	}
}