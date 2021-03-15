/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import geometries.Sphere;
import primitives.Point3D;
import primitives.Vector;

/**
 *
 */
public class SphereTest {

	/**
	 * Test method for {@link geometries.Sphere#getNormal(primitives.Point3D)}.
	 */
	@Test
	public void testGetNormal() {
		Sphere sphere = new Sphere(new Point3D(1, 8, 23),11);
		Point3D p = new Point3D(7, 14, 30);
		Vector expected = new Vector(6, 6, 7).normalized();
		
		assertEquals(expected, sphere.getNormal(p));
	}

}
