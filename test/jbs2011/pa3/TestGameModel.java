/**
 * 
 */
package jbs2011.pa3;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author pitosalas
 *
 */
public class TestGameModel {
	
	GameModel theModel;
	Disk aDisk;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		theModel = new GameModel();
		aDisk = new Disk(10.0f, 10.0f, 1.0f);
	}
	
	@Test
	public void testConstructor() {
		assertEquals("Level over begins true", true, theModel.levelOver);
	}
	
	@Test
	public void testAddDisk() {
		assertEquals(1.0, theModel.addDisk(1.0f, 2.0f, 0.4f).x, 0.1);
	}
	
	@Test
	public void testTouching() {
		Disk d = new Disk(10.0f, 10.0f, 1.0f);
		assertNull(theModel.touchingDisk(0.0f, 0.0f));
		theModel.addDisk(d);
		assertEquals(d, theModel.touchingDisk(0.0f, 0.0f));
	}
	
	@Test
	public void testAddingWrongObject() {
		theModel.addSquare(null);
		assertNull(theModel.touchingSquare(0.0f, 0.0f));
	}

}
