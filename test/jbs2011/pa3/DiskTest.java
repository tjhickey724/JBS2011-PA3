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
public class DiskTest {
	Disk adisk;
	Disk originDisk, originDiskPrime;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test isStatic	 */
	@Test
	public final void testIsStatic() {
		adisk = new Disk(12.0f, 12.0f, 3.0f);
		originDisk = new Disk(0.0f, 0.0f, 1.0f);
		originDiskPrime = new Disk(0.0f, 1.0f, 1.0f);

		assertFalse(adisk.isStatic);
		assertFalse(originDisk.isStatic);
	}
	
	@Test
	public final void testBasicProps() {
		adisk = new Disk(12.0f, 12.0f, 3.0f);
		originDisk = new Disk(0.0f, 0.0f, 1.0f);
		originDiskPrime = new Disk(0.0f, 1.0f, 1.0f);

		assertEquals("R is 1.0", 1.0, originDisk.r, 0.0f);
		assertEquals("x is 0.0", 0.0, originDisk.x, 0.01f);
		assertEquals("y is 0.0", 0.0, originDisk.y, 0.01f);		
	}
	
	/**
	 * Test methods for testInside.
	 */
	@Test
	public final void testInside() {
		adisk = new Disk(12.0f, 12.0f, 3.0f);
		originDisk = new Disk(0.0f, 0.0f, 1.0f);
		originDiskPrime = new Disk(0.0f, 1.0f, 1.0f);

		assertEquals("0.0 should be inside the origin disk", true, originDisk.inside(0.0f, 0.0f));
	}
	
	@Test
	public final void testInsidePrime() {
		adisk = new Disk(12.0f, 12.0f, 3.0f);
		originDisk = new Disk(0.0f, 0.0f, 1.0f);
		originDiskPrime = new Disk(0.0f, 1.0f, 1.0f);

		assertEquals("0.0 should be inside the origin disk", true, originDiskPrime.inside(0.0f, 0.0f));
	}
	
}
