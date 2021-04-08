/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

import geometries.Plane;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
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

		
		expected = (p1.subtract(p3).crossProduct(p1.subtract(p2))).normalize();//calculate the normalized normal of the plane
		Plane plane = new Plane(p1, p2, p3);
		
		assertEquals(expected, plane.getNormal(null));
	}
	
	@Test
	public void testFindIntersections() {
		Plane pln = new Plane(new Point3D(0, 1, 0), new Vector(0,0,1));
		//EP: The Ray must be neither orthogonal nor parallel to the plane
		//TC01: Ray intersects the plane
		Ray r1 = new Ray(new Point3D(-1, 1, 1), new Vector(1,-1,-1));
		assertEquals("TC01: Ray intersects the plane",pln.findIntersections(r1), List.of(new Point3D(0, 0, 0)));
		//TC02: Ray does not intersect the plane
		Ray r2 = new Ray(new Point3D(0, 0, 1), new Vector(1,1,1));
		assertNull("TC02: Ray does not intersect the plane", pln.findIntersections(r2));
		
		//BVA:
		
		//ray is parallel to the plane
		//TC03: Ray is parallel to the plane outside the plane
		Ray r3 = new Ray(new Point3D(-3,  -2,  2), new Vector(2, 3, 0));
		assertNull("TC03: Ray is parallel to the plane outside the plane", pln.findIntersections(r3));
		
		//TC04: Ray is parallel to the plane inside the plane
		Ray r4 = new Ray(new Point3D(-3,  -2,  0), new Vector(2, 3, 0));
		assertNull("TC04: Ray is parallel to the plane inside the plane", pln.findIntersections(r4));
		
		
		//Ray is orthogonal to the plane
		//TC05: ray starts before the plane
		Ray r5 = new Ray(new Point3D(2, 2, 2), new Vector(0,0,-1));
		assertEquals("TC05: ray starts before the plane", pln.findIntersections(r5), new Point3D(2, 2, 0));
		
		//TC06: ray starts in the plane (orthogonal)
		Ray r6 = new Ray(new Point3D(-2, 1, 0), new Vector(0,0,-1));
		assertNull("TC06: ray starts in the plane", pln.findIntersections(r6));
		
		//TC07: ray starts after the plane
		Ray r7 = new Ray(new Point3D(1,1,3), new Vector(0,0,1));
		assertNull("TC07: ray starts after the plane", pln.findIntersections(r5));
		
		
		//TC08: Ray is neither orthogonal nor parallel to the plane and begins at the plane
		Ray r8 = new Ray(new Point3D(4, 3, 0), new Vector(2, 3, 3));
		assertNull("TC08: Ray is neither orthogonal nor parallel to the plane and begins at the plane", pln.findIntersections(r8));
		
		//TC09: Ray is neither orthogonal nor parallel to the plane and begins in 
		//the same point which appears as reference point in the plane
		Ray r9 = new Ray(new Point3D(0,1,0), new Vector(2, 3, 3));
		assertNull("//TC09: Ray is neither orthogonal nor parallel to the plane and begins in \r\n"
				+ "		//the same point which appears as reference point in the plane",
				pln.findIntersections(r9));
		
	}

}
