package unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import geometries.Cylinder;
import geometries.Tube;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
/**
 * test class for {@link Cylinder} 
 */
public class CylinderTest {

	/**
	 * test the function {@link Cylinder#getNormal(Point3D)}
	 */
	@Test
	public void testGetNormal() {
		Ray ray = new Ray(new Point3D(1, 2, 3), new Vector(1, 2, 3));
		Cylinder cyl = new Cylinder(ray, 5, 5);
		// EP:
		// TC01: Point on the upper surface of the cylinder
		Point3D p1 = new Point3D(-0.427522848526584, 6.059072009028762, 7.005888588112922);
		Vector exp1 = new Vector(1, 2, 3).normalize();
		assertEquals("TC01: Point on the upper surface of the cylinder", exp1, cyl.getNormal(p1));

		// TC02: Point on the lower surface of the cylinder
		Point3D p2 = new Point3D(-2.691294614083774, 2.338786171309268, 4.004574090488413);
		Vector exp2 = new Vector(1, 2, 3).normalize().scale(-1);
		assertEquals("TC02: Point on the lower surface of the cylinder", exp2, cyl.getNormal(p2));

		// BVA:
		// TC03: point on the edge of the upper surface of the cylinder
		Point3D p3 = new Point3D(-2.436305430192589, 5.977308361351596, 7.729991880453034);
		Vector exp3 = new Vector(1, 2, 3).normalize();
		assertEquals("TC03: point on the edge of the upper surface of the cylinder ", exp3, cyl.getNormal(p3));

		// TC04: point on the edge of the lower surface of the cylinder
		Point3D p4 = new Point3D(-1.595386804260603, -1.105786247402948, 5.935653099688833);
		Vector exp4 = new Vector(1, 2, 3).normalize().scale(-1);
		assertEquals("TC04: point on the edge of the lower surface of the cylinder", exp4, cyl.getNormal(p4));

		// TC05: point on the origin point of the ray of the cylinder
		Point3D p5 = new Point3D(1, 2, 3);
		Vector exp5 = new Vector(1, 2, 3).normalize().scale(-1);
		assertEquals("TC05: point on the origin point of the ray of the cylinder", exp5, cyl.getNormal(p5));

	}

	/**
	 * orders a list of points in ascending order according to the x and y
	 * coordinates of the points
	 * this function is to help the function testFindIntersections() to order list
	 * of tow points before comparing the list
	 * 
	 * @param l list of points
	 * @return ordered list of points
	 */
	List<Point3D> orderToComper(List<Point3D> l) {
		if (l.get(0).getX() > l.get(1).getX()) {
			return List.of(l.get(1), l.get(0));
		}
		if (l.get(0).getX() == l.get(1).getX()) {
			if (l.get(0).getY() > l.get(1).getY()) {
				return List.of(l.get(1), l.get(0));
			}
		}
		return l;
	}

	/**
	 * Test method for
	 * {@link geometries.cylinder#findIntersections(primitives.Point3D)}.
	 */
	@Test
	public void testFindIntersections() {
		Ray Ray = new Ray(new Point3D(1, 0, 0), new Vector(0, 0, 1));
		Cylinder cylinder = new Cylinder(Ray, 1, 10);
		List<Point3D> result;

		// ___________________ Equivalence Partitions Tests__________________
		// TC01: the ray doesn't intersects (the ray intersects the infinite Cylinder)
		Ray r1 = new Ray(new Point3D(13.687417314738514, -2.118211413364442, 2.024900777686218),
				new Vector(-14.936792746070182, 2.761267399379779, 10.87584870471578));
		result = cylinder.findIntersections(r1);
		assertNull("TC01: the ray doesn't intersects (the ray intersects the infinite Cylinder)", result);

		// ___________________ Boundary Values Tests __________________

		// TC02: intersects twice - The ray is parallel to the cylinder
		Ray r2 = new Ray(new Point3D(3, 0, 0), new Vector(-3, 0, 3));
		result = cylinder.findIntersections(r2);
		result = orderToComper(result);
		assertEquals("TC02: intersects twice - The ray is parallel to the cylinder", result,
				List.of(new Point3D(0, 0, 3), new Point3D(2, 0, 1)));

		// TC03: intersects twice - The ray crosses the body and the cap
		Ray r6 = new Ray(new Point3D(1.589735223254593, -8.187906987570473, 0),
				new Vector (-0.326768606810759,8.28282097928758,10));
		result = cylinder.findIntersections(r6);
		result = orderToComper(result);
		assertEquals("TC03: intersects twice - The ray crosses the body and the cap", result,
				List.of(new Point3D(1.262966616443833, 0.094913991717107, 10),
				new Point3D(1.304291643237488, -0.952578918439761, 8.735342810406891)));
		/*
		 * //TC04: intersects once - The ray starts inside the cylinder and parallel
		 * cylinder Ray r7 = new Ray(new Point3D (2,1,0), new Vector(-1,0,1)); result =
		 * tub.findIntersections(r7);
		 * assertEquals("TC04:	intersects once - The ray starts inside the cylinder and parallel to the cylinder "
		 * ,result, List.of((new Point3D(1, 1,1))));
		 * 
		 * //TC05: intersects once - The ray starts inside the cylinder and parallel to
		 * the cylinder and cross the O point Ray r8 = new Ray(new Point3D (4, 1,
		 * 0.673879584707434) , new Vector(-3,0,-0.673879584707434)); result =
		 * tub.findIntersections(r8);
		 * assertEquals("TC05:	intersects once - The ray starts inside the cylinder and parallel to the cylinder and cross the O point"
		 * , result,List.of(new Point3D(1,1,0)));
		 * 
		 * //TC06: intersects once - The ray starts at the O point and parallel to the
		 * cylinder Ray r8 = new Ray(new Point3D (4, 1, 0.673879584707434) , new
		 * Vector(-3,0,-0.673879584707434)); result = tub.findIntersections(r8);
		 * assertEquals("TC06: intersects once - The ray starts at the O point and parallel to the cylinder "
		 * , result,List.of(new Point3D(1,1,0)));
		 * 
		 * //TC06: intersects once - The ray starts inside the cylinder and intersects
		 * the carve Ray r8 = new Ray(new Point3D (4, 1, 0.673879584707434) , new
		 * Vector(-3,0,-0.673879584707434)); result = tub.findIntersections(r8);
		 * assertEquals("TC06:	intersects once - The ray starts inside the cylinder and parallel to the cylinder and start at the O point"
		 * , result,List.of(new Point3D(1,1,0)));
		 * 
		 * //TC07: intersects once - The ray starts at the cap and intersects the cap
		 * Ray r8 = new Ray(new Point3D (4, 1, 0.673879584707434) , new
		 * Vector(-3,0,-0.673879584707434)); result = tub.findIntersections(r8);
		 * assertEquals("TC07:	intersects once - The ray starts at the cap and intersects the cap"
		 * , result,List.of(new Point3D(1,1,0)));
		 * 
		 * //TC08: doesn't intersects - tangents to the carve Ray r9 = new Ray(new
		 * Point3D (2, 1, 0) , new
		 * Vector(-4.142580432497253,2.11516536352305,0.685069779457748)); result =
		 * cylinder.findIntersections(r9);
		 * assertNull("TC08: doesn't intersects - tangents to the carve", result);
		 * 
		 * //TC09: doesn't intersects - tangents to the cap Ray r9 = new Ray(new Point3D
		 * (2, 1, 0) , new
		 * Vector(-4.142580432497253,2.11516536352305,0.685069779457748)); result =
		 * cylinder.findIntersections(r9);
		 * assertNull("TC09: doesn't intersects - tangents to the cap", result);
		 * 
		 * //TC010: doesn't intersects - starts at the cap Ray r9 = new Ray(new Point3D
		 * (2, 1, 0) , new
		 * Vector(-4.142580432497253,2.11516536352305,0.685069779457748)); result =
		 * cylinder.findIntersections(r9);
		 * assertNull("TC010: doesn't intersects - starts at the cap", result);
		 */

	}

}
