/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import geometries.Plane;
import primitives.Point3D;
import primitives.Vector;

/**
 * test class for {@link Point3D} 
 */
public class Point3DTest {

	/**
	 * Test method for {@link primitives.Point3D#add(primitives.Vector)}.
	 */
	@Test
	public void testAdd() {
		//__________________Logic test____________________
		Point3D p1 = new Point3D(1,  2, 3);
		Vector v1 = new Vector(2, 4 , 6);
		Point3D expected = new Point3D(3,  6, 9);
		
		assertEquals(expected, p1.add(v1));
	}

	/**
	 * Test method for {@link primitives.Point3D#subtract(primitives.Point3D)}.
	 */
	@Test
	public void testSubtract() {
		//__________________Logic test____________________
		Point3D p1 = new Point3D(1,  2, 3);	
		Point3D p2 = new Point3D(3,  6, 9);
		Vector expected = new Vector(-2, -4 , -6);
		
		assertEquals(expected, p1.subtract(p2));
	}

	/**
	 * Test method for {@link primitives.Point3D#distanceSquared(primitives.Point3D)}.
	 */
	@Test
	public void testDistanceSquerd() {
		//__________logic test________________
		
		Point3D p0 = new Point3D(2 ,6 ,9);
		Point3D p1 = new Point3D(0, -9, -10);
		
		double expected = 590;
		assertEquals(expected, p0.distanceSquared(p1), 0.0001);
	}
	

	/**
	 * Test method for {@link primitives.Point3D#distance(primitives.Point3D)}.
	 */
	@Test
	public void testDistance() {
		//__________logic test________________
		
		Point3D p0 = new Point3D(2 ,6 ,9);
		Point3D p1 = new Point3D(0, -9, -10);
		
		double expected = 24.28992;
		assertEquals(expected, p0.distance(p1), 0.0001);	}

	/**
	 * Test method for {@link primitives.Point3D#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		
	}

}
