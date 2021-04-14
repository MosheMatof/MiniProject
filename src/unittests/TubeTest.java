/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;
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
		Ray tubRay = new Ray(new Point3D(1, 0, 0), new Vector(0,0,1));
		Tube tub = new Tube(tubRay, 1);
		//EP:
		//TC01: point on the tube
		Point3D p1 = new Point3D(0, 0, 2); 
		Vector expct1 = new Vector(-1,0,0);
		assertEquals("TC01: point on the tube", tub.getNormal(p1), expct1);
		
		//BVA:
		//TC02: point on circle around the origin point of the ray
		Point3D p2 = new Point3D(2, 0, 0);
		Vector expct2 = new Vector(1,0,0);
		assertEquals("TC02: point on circle around the origin point of the ray", tub.getNormal(p2), expct2);

	}

	//this function is to help the function testFindIntersections() to order list of tow points before comparing the list
	/**
	 * orders a list of points in ascending order according to the x coordinate of the points 
	 */
	List<Point3D> orderToComper(List<Point3D> l){
		if(l.get(0).getX() > l.get(1).getX()) {
			return List.of(l.get(1), l.get(0));
		}
		return l;
	}
	
	/**
	 * Test method for {@link geometries.Tube#findIntersections(primitives.Point3D)}.
	 */
	@Test
	public void testFindIntersections() {
		Ray tubRay = new Ray(new Point3D(1, 0, 0), new Vector(0,0,1));
		Tube tub = new Tube(tubRay, 1);
		String wnop = ": worng number of points";
		
		//___________________ Equivalence Partitions Tests__________________
		//TC01: ray intersects twice
		Ray r1 = new Ray(new Point3D(4, 0, -1), new Vector(-4,1,4));
		List<Point3D> result = tub.findIntersections(r1);
		result = orderToComper(result);
		assertEquals("TC01: ray intersects twice", result, List.of(new Point3D(0.510958323589132, 0.872260419102717, 2.489041676410868), new Point3D(1.841982852881456, 0.539504286779636, 1.158017147118544)));
		
		//TC02: ray intersects once
		Ray r2 = new Ray(new Point3D(0.5, 0, -1), new Vector(-0.5,0,2));
		result = tub.findIntersections(r2);
		assertEquals("TC02: ray intersects once", result, List.of(new Point3D(0,0,1)));
		
		//TC03: ray doesn't intersects (the line of the ray also doesn't intersects)
		Ray r3 = new Ray(new Point3D (4, 0, -1), new Vector(-4,-3,1));
		result = tub.findIntersections(r3);
		assertNull("TC03: ray doesn't intersects (the line of the ray also doesn't intersects)", result);
		
		//TC04: ray doesn't intersects (the line of the ray does intersects)
		Ray r4 = new Ray(new Point3D(-1, 0, 0) , new Vector(-1,0,1));
		result = tub.findIntersections(r4);
		assertNull("TC04: ray doesn't intersects (the line of the ray does intersects)", result);
		
		//___________________ Boundary Values Tests __________________

		
		//TC05: intersects twice: (The ray starts in the/in parallel to the O point)
		Ray r5 = new Ray(new Point3D (3,0,0), new Vector(-3,0,3));
		result = tub.findIntersections(r5);
		result = orderToComper(result);
		assertEquals("TC05: intersects twice: (The ray starts in the/in parallel to the O point)", result, List.of(new Point3D(0,0,3), new Point3D(2,0,1)));
		
		//TC06: intersects twice: (2.	The ray crosses the O point/ the parallel point to O)
		Ray r5 = new Ray(new Point3D (3,0,0), new Vector(-3,0,3));
		result = tub.findIntersections(r5);
		result = orderToComper(result);
		assertEquals("TC05: intersects twice: (The ray starts in the/in parallel to the O point)", result, List.of(new Point3D(0,0,3), new Point3D(2,0,1)));
		
		//TC03: ray doesn't intersects
		Ray r3 = new Ray(new Point3D (4, 0, -1), new Vector(-4,-3,1));
		result = tub.findIntersections(r3);
		assertNull("TC03: ray doesn't intersects", result);
		
		//TC03: ray doesn't intersects
		Ray r3 = new Ray(new Point3D (4, 0, -1), new Vector(-4,-3,1));
		result = tub.findIntersections(r3);
		assertNull("TC03: ray doesn't intersects", result);
		
		//TC03: ray doesn't intersects
		Ray r3 = new Ray(new Point3D (4, 0, -1), new Vector(-4,-3,1));
		result = tub.findIntersections(r3);
		assertNull("TC03: ray doesn't intersects", result);
		
		//TC03: ray doesn't intersects
		Ray r3 = new Ray(new Point3D (4, 0, -1), new Vector(-4,-3,1));
		result = tub.findIntersections(r3);
		assertNull("TC03: ray doesn't intersects", result);
		
		//TC03: ray doesn't intersects
		Ray r3 = new Ray(new Point3D (4, 0, -1), new Vector(-4,-3,1));
		result = tub.findIntersections(r3);
		assertNull("TC03: ray doesn't intersects", result);
		
		//TC03: ray doesn't intersects
		Ray r3 = new Ray(new Point3D (4, 0, -1), new Vector(-4,-3,1));
		result = tub.findIntersections(r3);
		assertNull("TC03: ray doesn't intersects", result);
		
		//TC03: ray doesn't intersects
		Ray r3 = new Ray(new Point3D (4, 0, -1), new Vector(-4,-3,1));
		result = tub.findIntersections(r3);
		assertNull("TC03: ray doesn't intersects", result);
		
		//TC04: 
		
		
	}
}