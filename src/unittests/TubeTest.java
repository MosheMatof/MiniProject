/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import geometries.Tube;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 *
 */
public class TubeTest {

	/**
	 * Test method for {@link geometries.Tube#getNormal(primitives.Point3D)}.
	 */
	@Test
	public void testGetNormal() {
		Ray ray = new Ray(new Point3D(1, 2, 3), new Vector(1, 2, 3));
		Tube tube = new Tube(ray, 5);
		Vector expected = new Vector(-18,-36,-74).normalize();
		
		assertEquals(expected, tube.getNormal(new Point3D(7,14,1)));
		
		ray = new Ray(new Point3D(0, 0, 0), new Vector(0,0,1));
		tube = new Tube(ray, 5);
		Point3D p = new Point3D(3, 4, 0);
		expected = new Vector(3,4,0).normalize();
		
		assertEquals(expected, tube.getNormal(p));
	}

}
