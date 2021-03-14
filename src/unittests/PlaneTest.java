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
 * @author erenb
 *
 */
public class PlaneTest {

	/**
	 * Test method for {@link geometries.Plane#getNormal(primitives.Point3D)}.
	 */
	@Test
	public void testGetNormalPoint3D() {
		//_______________Logic test______________
		//check if the normal is in the right direction and if the normal is normalized
		//test 1- using the Plane(Point3d pivot, Vector normal)
		Plane p = new Plane(new Point3D(1, 2, 3), new Vector(2, 6, 9));
		Vector expected = new Vector(2/11d, 6/11d, 9/11d);
		
		assertEquals(expected, p.getNormal(null));
		
		//test 2- using the Plane(Point3d p1, Point3D p2, Point3D p3)
		Point3D p1 = new Point3D(1, 2, 4); 
		Point3D p2 = new Point3D(4, 5, 2); 
		Point3D p3 = new Point3D(3, 6, 7); 

		expected = (p1.subtract(p3).crossProduct(p1.subtract(p3))).normalize();//calculate the normalized normal of the plane
		Plane plane = new Plane(p1, p2, p3);
		
		assertEquals(expected, plane.getNormal(null));
	}

}
