/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;


import primitives.*;
/**
 * @author erenb
 *
 */
public class VectorTest {

	/**
	 * Test method for {@link primitives.Vector#Vector(double, double, double)}.
	 */
	@Test
	public void testVectorDoubleDoubleDouble() {
		//_________________ZERO vector___________________
		 
		try {
			new Vector(0, 0, 0);
			fail("alow to create zero vector");
		} catch (IllegalArgumentException e) {
			//good
		}
	}

	/**
	 * Test method for {@link primitives.Vector#Vector(primitives.Point3D)}.
	 */
	@Test
	public void testVectorPoint3D() {
		//_________________ZERO vector___________________
		 
		Point3D p = new Point3D(0, 0, 0);
		try {
			new Vector(p);
			fail("alow to create zero vector");
		} catch (IllegalArgumentException e) {
			//good
		}	
	}

	/**
	 * Test method for {@link primitives.Vector#add(primitives.Vector)}.
	 */
	@Test
	public void testAdd() {
		//_____________logic test_______________
		
		//TADD01: simple addition
		Vector v1 = new Vector(1, 2, 3);
		Vector v2 = new Vector(1, 1, 1);
		Vector expected = new Vector(2, 3, 4);
		
		assertEquals(expected, v1.add(v2));
		
		//____________zero vector________________
		
		//TADD02: adding -v to v
		v1 = new Vector(1, 1, 1);
		v2 = new Vector(-1, -1, -1);
		
		try {
			v1.add(v2);
			fail("alow to create a zero vector");
		} catch (IllegalArgumentException e) {
			//good
		}
	}

	/**
	 * Test method for {@link primitives.Vector#subtract(primitives.Vector)}.
	 */
	@Test
	public void testSubtract() {
		//_____________logic test_______________
		
		//TSBTRCT01: simple subtraction
		Vector v1 = new Vector(1, 2, 3);
		Vector v2 = new Vector(1, 1, 1);
		Vector expected = new Vector(0, 1, 2);
		
		assertEquals(expected, v1.subtract(v2));
		
		//____________zero vector________________
		
		//TSBTRCT02: subtracting v from v
		v1 = new Vector(1, 1, 1);
		v2 = new Vector(1, 1, 1);
		
		try {
			v1.subtract(v2);
			fail("alow to create a zero vector");
		} catch (IllegalArgumentException e) {
			//good
		}
	}

	/**
	 * Test method for {@link primitives.Vector#scale(double)}.
	 */
	@Test
	public void testScale() {
		//_____________logic test_______________
		
		//TSCL01: simple scale
		Vector v1 = new Vector(1, 2, 3);
		double scalar = 3;
		Vector expected = new Vector(3, 6, 9);
		
		assertEquals(expected, v1.scale(scalar));
		
		//____________zero vector________________
		
		//TSCL02: multiply by 0 
		v1 = new Vector(1, 1, 1);
		scalar = 0;		
		try {
			v1.scale(scalar);
			fail("alow to create a zero vector");
		} catch (IllegalArgumentException e) {
			//good
		}
	}

	/**
	 * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
	 */
	@Test
	public void testDotProduct() {
		//____________logic test______________
		Vector v1 = new Vector(3, 2, 4);
		Vector v2 = new Vector(4, 2, 3);
		double expecte = 28;
		
		//TDPRTCT01: (3, 2, 4) * (4, 2, 3)
		assertEquals(expecte, v1.dotProduct(v2), 0.000001);
	}

	/**
	 * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
	 */
	@Test
	public void testCrossProduct() {
		//__________logic test________________
		
		//TCPRDCT01: (3, 2, 4) X (4, 2, 3)
		Vector v1 = new Vector(3, 2, 4);
		Vector v2 = new Vector(4, 2, 3);
		Vector expecte = new Vector(-2, 7, -2);
		
		assertEquals(expecte, v1.crossProduct(v2));
		
		//__________zero vector_______________
		
		//TCPRDCT02: (-1, 2, -3) X (1, -2, 3)
		v1 = new Vector(-1, 2, -3);
		v2 = new Vector(1, -2, 3);
		
		try {
			v1.crossProduct(v2);
			fail("alow zero vector");
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
		}
	}

	/**
	 * Test method for {@link primitives.Vector#lengthSquared()}.
	 */
	@Test
	public void testLengthSquared() {
		//__________logic test________________
		
		//TLSQR: squared length of (2, 6, 9)
		Vector vector = new Vector(2 ,6 ,9);
		double epected = 121;
		
		assertEquals(epected, vector.lengthSquared(), 0.0000001);
	}

	/**
	 * Test method for {@link primitives.Vector#length()}.
	 */
	@Test
	public void testLength() {
		//__________logic test________________
		
		//TLSQR01: squared length of (2, 6, 9)
		Vector vector = new Vector(2 ,6 ,9);
		double epected = 11;
		
		assertEquals(epected, vector.length(), 0.0000001);
	}

	/**
	 * Test method for {@link primitives.Vector#normalize()}.
	 */
	@Test
	public void testNormalize() {
		//__________logic test________________
		Vector vector = new Vector(2 ,6 ,9);
		Vector epected = new Vector(2/11d, 6/11d, 9/11d);
		
		assertEquals(epected, vector.normalize());

		//check if the vector is changing
		vector = new Vector(2 ,6 ,9);
		epected = new Vector(2/11d, 6/11d, 9/11d);
		
		vector.normalize();
		assertEquals(epected, vector);

		
	}

	/**
	 * Test method for {@link primitives.Vector#normalized()}.
	 */
	@Test
	public void testNormalized() {
		//__________logic test________________
		Vector vector = new Vector(2 ,6 ,9);
		Vector epected = new Vector(2/11d, 6/11d, 9/11d);
		
		assertEquals(epected, vector.normalized());

		//check if the vector is changing
		vector = new Vector(2 ,6 ,9);
		epected = new Vector(2/11d, 6/11d, 9/11d);
		
		vector.normalized();
		assertNotEquals(epected, vector);
	}

}
