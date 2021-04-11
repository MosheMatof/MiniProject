package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

import geometries.Geometries;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import geometries.Tube;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * tests the class {@link geometries.Geometries} 
 */
public class GeometriesTest {

	/**
	 * test for {@link Geometries#findIntersections(primitives.Ray)}
	 */
	@Test
	public void testFindIntersections() {
		Geometries gmtrs = new Geometries();
		
		//___________________ Equivalence Partitions Tests__________________
		
		//TC01: empty Collection 
		Ray r1 = new Ray(new Point3D(1, -1,-2), new Vector(5,4,3));
		assertNull("TC01: empty Collection",gmtrs.findIntersections(r1));
		
		Triangle trngl = new Triangle(new Point3D(1, 1, 1) , new Point3D(-2, -2, 1), new Point3D(-1, 1, -1));
		Sphere spr = new Sphere(new Point3D(0,0,1), 4);
		Plane pln = new Plane(new Point3D(1, 2, 3), new Point3D(2, 2, 2), new Point3D(5, 6, 7));
		gmtrs.add(trngl, spr, pln);
		
		//TC02: non component is intersecting
		Ray r2 = new Ray(new Point3D(8, 0, 0), new Vector(-8,-6,0));
		assertNull("TC02: non component is intersecting", gmtrs.findIntersections(r2));
		
		//TC03: only one component intersecting
		Ray r3 = new Ray(new Point3D(0,0,4), new Vector(0,-5,-5));
		List<Point3D> result = gmtrs.findIntersections(r3);
		assertEquals("TC03: only one component intersecting: worng number of intersactions points", result.size(), 1);
		
		//TC04: all components are intersecting
		Ray r4 = new Ray(new Point3D(0,5,1), new Vector(1,-10,-1));
		assertEquals("TC04: all components are intersecting: worng number of intersactions points", gmtrs.findIntersections(r4).size(),4);
		
		//___________________ Boundary Values Tests __________________
		
		//TC05: some of the components are intersect
		Ray r5 = new Ray(new Point3D(1,-5,-1), new Vector(1,5,-1));
		assertEquals("TC05: some of the components are intersect", gmtrs.findIntersections(r5).size(),3);
	}

}
