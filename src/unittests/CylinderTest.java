package unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import geometries.Cylinder;
import geometries.Tube;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

public class CylinderTest {

	@Test
	public void testGetNormal() {
		Ray ray = new Ray(new Point3D(1, 2, 3), new Vector(1, 2, 3));
		Cylinder cyl = new Cylinder(ray, 5, 5);
		//EP:
		//TC01: Point on the upper surface of the cylinder
		Point3D p1 = new Point3D(-0.427522848526584, 6.059072009028762, 7.005888588112922);
		Vector exp1 = new Vector(1,2,3).normalize();
		assertEquals("TC01: Point on the upper surface of the cylinder", exp1, cyl.getNormal(p1));
		
		//TC02: Point on the lower surface of the cylinder
		Point3D p2 = new Point3D(-2.691294614083774, 2.338786171309268, 4.004574090488413);
		Vector exp2 = new Vector(1,2,3).normalize().scale(-1);
		assertEquals("TC02: Point on the lower surface of the cylinder", exp2, cyl.getNormal(p2));
		
		//BVA:
		//TC03: point on the edge of the upper surface of the cylinder 
		Point3D p3 = new Point3D(-2.436305430192589, 5.977308361351596, 7.729991880453034);
		Vector exp3 = new Vector(1,2,3).normalize();
		assertEquals("TC03: point on the edge of the upper surface of the cylinder ", exp3, cyl.getNormal(p3));
		
		//TC04: point on the edge of the lower surface of the cylinder
		Point3D p4 = new Point3D(-1.595386804260603, -1.105786247402948, 5.935653099688833);
		Vector exp4 = new Vector(1,2,3).normalize().scale(-1);
		assertEquals("TC04: point on the edge of the lower surface of the cylinder", exp4, cyl.getNormal(p4));
		
		//TC05: point on the origin point of the ray of the cylinder
		Point3D p5 = new Point3D(1,2,3);
		Vector exp5 = new Vector(1,2,3).normalize().scale(-1);
		assertEquals("TC05: point on the origin point of the ray of the cylinder", exp5, cyl.getNormal(p5));
		
	}
	
	//this function is to help the function testFindIntersections() to order list of tow points before comparing the list
		/**
		 * orders a list of points in ascending order according to the x and y coordinates of the points 
		 */
		List<Point3D> orderToComper(List<Point3D> l){
			if(l.get(0).getX() > l.get(1).getX()) {
				return List.of(l.get(1), l.get(0));
			}
			if(l.get(0).getX() == l.get(1).getX()) {
				if (l.get(0).getY() > l.get(1).getY()) {
					return List.of(l.get(1), l.get(0));
				}
			}
			return l;
		}
	/**
	 * Test method for {@link geometries.cylinder#findIntersections(primitives.Point3D)}.
	 */
	@Test
	public void testFindIntersections() {
		Ray Ray = new Ray(new Point3D(1, 0, 0), new Vector(0,0,1));
		Cylinder cylinder = new Cylinder(Ray, 1,10);
		List<Point3D> result;
		
		//___________________ Equivalence Partitions Tests__________________
				//TC01: the ray doesn't intersects (the ray intersects the infinite Cylinder)
				Ray r1 = new Ray(new Point3D(13.687417314738514, -2.118211413364442, 2.024900777686218), new Vector(-14.936792746070182, 2.761267399379779, 10.87584870471578));
				result = cylinder.findIntersections(r1);
				assertNull("TC01: the ray doesn't intersects (the ray intersects the infinite Cylinder)", result);
				
				
				//___________________ Boundary Values Tests __________________

				/*
				//TC05: intersects twice - The ray starts in parallel to the O point
				Ray r5 = new Ray(new Point3D (3,0,0), new Vector(-3,0,3));
				result = cylinder.findIntersections(r5);
				result = orderToComper(result);
				assertEquals("TC05: intersects twice - The ray starts in in parallel to the O point", result, List.of(new Point3D(0,0,3), new Point3D(2,0,1)));
				
				//TC06: intersects twice - The ray crosses the O point
				Ray r6 = new Ray(new Point3D (4,0.553394889542981,-1.260913400857578), new Vector(-3,-0.553394889542981,1.260913400857578));
				result = cylinder.findIntersections(r6);
				result = orderToComper(result);
				assertEquals("TC06: intersects twice - The ray crosses the O point", result, List.of(new Point3D(0.016591420754664, -0.181404427362364, 0.413331018696252),new Point3D(1.983408579245337, 0.181404427362364, -0.413331018696252)));
				
				//TC07:	intersects once - The ray starts in parallel to the O point 
				Ray r7 = new Ray(new Point3D (2,1,0), new Vector(-1,0,1));
				result = tub.findIntersections(r7);
				assertEquals("TC07:	intersects once - The ray starts in parallel to the O point ",result, List.of((new Point3D(1, 1,1))));
				
				//TC08:	intersects once - The ray crosses the parallel point to O
				Ray r8 = new Ray(new Point3D (4, 1, 0.673879584707434) , new Vector(-3,0,-0.673879584707434));
				result = tub.findIntersections(r8);
				assertEquals("TC08:	intersects once - The ray crosses the parallel point to O", result,List.of(new Point3D(1,1,0)));
				
				//TC09: doesn't intersects (the line of the ray also doesn't intersects) - The ray starts in parallel to the O point 
				Ray r9 = new Ray(new Point3D (2, 1, 0)  , new Vector(-4.142580432497253,2.11516536352305,0.685069779457748));
				result = cylinder.findIntersections(r9);
				assertNull("TC09: doesn't intersects (the line of the ray also doesn't intersects) - The ray starts in parallel to the O point ", result);
				
				//TC010: the ray doesn't intersects (the line of the ray does intersects) - The ray crosses the parallel point to O
				Ray r10 =  new Ray(new Point3D (4, -1.259368078054248, 1.25376699095548) , new Vector(-2,2.259368078054248,-1.25376699095548));
				result = cylinder.findIntersections(r10);
				assertNull("TC010: the ray doesn't intersects (the line of the ray does intersects) - The ray crosses the parallel point to O", result);
				*/
		
	}
	

}
