/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import geometries.Intersectable.Boundary;
import primitives.*;

/**
 *
 */
public class bounderyTest {

	@Test
	public void isIntersectTest() {
		Boundary b = new Boundary(-5, 5, -5, 5, -5, 5);
		// TC01: ray intersect the boundary
		Ray r = new Ray(new Point3D(-6, -6, -6), new Vector(1, 1, 1));
		assertTrue("TC01: ray intersect the boundary", b.isIntersect(r));

		// TC02: ray dosen't intersect the boundary
		r = new Ray(new Point3D(6, 6, 6), new Vector(1, -1, -1));
		assertFalse("TC02: ray dosen't intersect the boundary", b.isIntersect(r));

		// TC03: ray start after the boundary (no intersection)
		r = new Ray(new Point3D(6, 6, 6), new Vector(1, 1, 1));
		assertFalse("TC03: ray start after the boundary", b.isIntersect(r));

		// TC04: ray start inside the boundary (intersect)
		r = new Ray(new Point3D(0, 0, 0), new Vector(1, 1, 1));
		assertTrue("TC04: ray start inside the boundary (intersect)", b.isIntersect(r));

		// TC05: ray starts at one of the border of the boundary and goes outside from the boundary (no intersections)
		r = new Ray(new Point3D(5, 0, 0), new Vector(1, 1, 1));
		assertFalse("TC05: ray starts at one of the border of the boundary "
				+ "and goes outside from the boundary (no intersections)", b.isIntersect(r));
	}

}
