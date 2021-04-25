/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.PrimitiveIterator.OfDouble;

import org.junit.Test;

import geometries.Sphere;
import primitives.*;

/**
 *	Testing sphere Class
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
	
	/**
	 * Test the method {@link geometries.Sphere#findIntersections(primitives.Ray)}
	 */
	@Test
	public void testFindItersections() {
		
		//___________________ Equivalence Partitions Tests__________________
		
		Sphere spr = new Sphere(new Point3D(0,0,1), 16);
		
		// TC01: Ray's line is outside the sphere (0 points)		
		Ray r1 = new Ray(new Point3D(0, -30, 0), new Vector(0, 30, 40));
		assertNull("EP-T1-no intersections", spr.findIntersections(r1));
		
        // TC02: Ray starts before and crosses the sphere (2 points)
		Ray r2 = new Ray(new Point3D(-20, 0, 0), new Vector(20, 0, 20));
		List<Point3D> result = spr.findIntersections(r2);
		assertEquals("worng number of points", result.size(), 2);
		if(result.get(0).getX() > result.get(1).getX()) {
			result = List.of(result.get(1), result.get(0));
		}
		
		assertEquals("TC02: Ray starts before and crosses the sphere (2 points)", spr.findIntersections(r2) ,List.of(new Point3D(-15.644102863722253, 0, 4.355897136277747), new Point3D(-3.355897136277747, 0, 16.644102863722253)));
				
        // TC03: Ray starts inside the sphere (1 point)
		Ray r3 = new Ray(new Point3D(-10, 0, 0), new Vector(10, 0, 30));
		result = spr.findIntersections(r3);
		assertEquals("worng number of intersections", result.size(), 1);
		assertEquals("TC03: Ray starts inside the sphere (1 point)", result, List.of(new Point3D(-4.553917511674425,0,16.338247464976725)));		
		
        // TC04: Ray starts after the sphere (0 points)
		Ray r4 = new Ray(new Point3D(20, 0, 0), new Vector(new Point3D(1, 0, 0)));
		assertNull("TC04: Ray starts after the sphere (0 points)", spr.findIntersections(r4));
		
		//___________________ Boundary Values Tests __________________
		
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
		Ray r5 = new Ray(new Point3D(0,0,-15), new Vector(0,10,15));
		assertEquals("worng number of intersections", spr.findIntersections(r5).size(), 1);
		assertEquals("TC11: Ray starts at sphere and goes inside (1 points)", spr.findIntersections(r5).get(0), new Point3D(0, 14.76923076923077, 7.153846153846153));
		
        // TC12: Ray starts at sphere and goes outside (0 points)
		Ray r6 = new Ray(new Point3D(0,0,-15), new Vector(new Point3D(0,0,-15)));
		assertNull(" TC12: Ray starts at sphere and goes outside (0 points)", spr.findIntersections(r6));
		
		// **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
		Ray r7 = new Ray(new Point3D(0,-20,0), new Vector(0,20,1));
		assertEquals("worng number of intersections", spr.findIntersections(r7).size(), 2);
		result = spr.findIntersections(r7);
		if(result.get(0).getY() > result.get(1).getY()) {
			result = List.of(result.get(1), result.get(0));
		}
		assertEquals("TC13: Ray starts before the sphere (2 points)", result, List.of(new Point3D(0, -15.980037422045516, 0.200998128897724), new Point3D(0, 15.98003742204552, 1.799001871102276)));
		
        // TC14: Ray starts at sphere and goes inside (1 points)
		Ray r8 = new Ray(new Point3D(-7.454829411102433, -10.683554215012949, 10.289089717848633) , new Vector(7.454829411102433,10.683554215012949,-9.289089717848633));
		assertEquals("worng number of intersections", spr.findIntersections(r8).size(), 1);
		assertEquals("TC14: Ray starts at sphere and goes inside (1 points)", spr.findIntersections(r8).get(0), new Point3D(7.454829411102433, 10.683554215012949, -8.289089717848633));
		
        // TC15: Ray starts inside (1 points)
		Ray r9 = new Ray(new Point3D(5, 5, -5) , new Vector(-5, -5, 6));
		assertEquals("worng number of intersections", spr.findIntersections(r9).size(), 1);
		assertEquals("TC15: Ray starts inside (1 points)", spr.findIntersections(r9).get(0), new Point3D(-8.626621856275074, -8.626621856275074, 11.351946227530089));
		
        // TC16: Ray starts at the center (1 points)
		Ray r10 = new Ray(new Point3D(0,0,1) , new Vector(-7.454829411102433, -10.683554215012949, 9.289089717848633));
		assertEquals("worng number of intersections", spr.findIntersections(r10).size(), 1);
		assertEquals("TC16: Ray starts at the center (1 points)", spr.findIntersections(r10).get(0), new Point3D(-7.454829411102433, -10.683554215012949, 10.289089717848633));
		
        // TC17: Ray starts at sphere and goes outside (0 points)
		Ray r11 = new Ray(new Point3D(-7.454829411102433,-10.683554215012949,10.289089717848633), new Vector(-7.545170588897567,-4.316445784987051,4.710910282151367));
		assertNull(" TC17: Ray starts at sphere and goes outside (0 points)", spr.findIntersections(r11));
		
        // TC18: Ray starts after sphere (0 points)
		Ray r12 = new Ray(new Point3D(-15,-15, 15), new Vector(-5, -5, 5));
		assertNull("TC18: Ray starts after sphere (0 points)", spr.findIntersections(r12));
		
		// **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
		Ray r13 = new Ray(new Point3D(-15,-15, 15), new Vector(-1.102183018878883, -1.102183018878883, 16.923893530973828));
		assertNull("TC19: Ray starts before the tangent point", spr.findIntersections(r13));
		
        // TC20: Ray starts at the tangent point
		Ray r14 = new Ray(new Point3D(-1.102183018878883, -1.102183018878883, 16.923893530973828), new Vector(-13.897816981121117,-13.897816981121117,-1.923893530973828));
		assertNull("TC20: Ray starts at the tangent point", spr.findIntersections(r14));
		
		
        // TC21: Ray starts after the tangent point
		Ray r15 = new Ray(new Point3D(-15,-15, 15), new Vector(1.102183018878883, 1.102183018878883, -16.923893530973828));
		assertNull("TC21: Ray starts after the tangent point", spr.findIntersections(r15));
		
		// **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
		Ray r16 = new Ray(new Point3D(-2.169916404950832,-2.169916404950832,32.350072729933416), new Vector(-13.897816981121117,-13.897816981121117,-1.923893530973828));
		assertNull("TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line", spr.findIntersections(r16));
		
		
	}
}
