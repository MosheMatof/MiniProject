/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

import geometries.Plane;
import geometries.Tube;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * test class for {@link Tube} 
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
	 * orders a list of points in ascending order according to the x and y coordinates of the points 
	 * @param l list of points
	 * @return ordered list of points
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
	 * Test method for {@link geometries.Tube#findIntersections(Ray)}
	 */
	@Test
	public void testFindIntersections() {
		Ray tubRay = new Ray(new Point3D(1, 0, 0), new Vector(0,0,1));
		Tube tub = new Tube(tubRay, 1);
		List<Point3D> result;
		
		//___________________ Equivalence Partitions Tests__________________
		//TC01: the ray intersects twice
		Ray r1 = new Ray(new Point3D(4, 0, -1), new Vector(-4,1,4));
		result = tub.findIntersections(r1);
		result = orderToComper(result);				
		assertEquals("TC01: ray intersects twice", result, List.of(new Point3D(0.510958323589132, 0.872260419102717, 2.489041676410868), new Point3D(1.841982852881456, 0.539504286779636, 1.158017147118544)));
		
		//TC02: the ray intersects once
		Ray r2 = new Ray(new Point3D(0.5, 0, -1), new Vector(-0.5,0,2));
		result = tub.findIntersections(r2);
		assertEquals("TC02: ray intersects once", result, List.of(new Point3D(0,0,1)));
		
		//TC03: the ray doesn't intersects (the line of the ray also doesn't intersects)
		Ray r3 = new Ray(new Point3D (4, 0, -1), new Vector(-4,-3,1));
		result = tub.findIntersections(r3);
		assertNull("TC03: ray doesn't intersects (the line of the ray also doesn't intersects)", result);
		
		//TC04: the ray doesn't intersects (the line of the ray does intersects)
		Ray r4 = new Ray(new Point3D(-1, 0, 0) , new Vector(-1,0,1));
		result = tub.findIntersections(r4);
		assertNull("TC04: ray doesn't intersects (the line of the ray does intersects)", result);
		
		//___________________ Boundary Values Tests __________________

		
		//TC05: intersects twice - The ray starts in parallel to the O point
		Ray r5 = new Ray(new Point3D (3,0,0), new Vector(-3,0,3));
		result = tub.findIntersections(r5);
		result = orderToComper(result);
		assertEquals("TC05: intersects twice - The ray starts in in parallel to the O point", result, List.of(new Point3D(0,0,3), new Point3D(2,0,1)));
		
		//TC06: intersects twice - The ray crosses the O point
		Ray r6 = new Ray(new Point3D (4,0.553394889542981,-1.260913400857578), new Vector(-3,-0.553394889542981,1.260913400857578));
		result = tub.findIntersections(r6);
		result = orderToComper(result);
		assertEquals("TC06: intersects twice - The ray crosses the O point", result, List.of(new Point3D(0.016591420754664, -0.181404427362364, 0.413331018696252),new Point3D(1.983408579245337, 0.181404427362364, -0.413331018696252)));
		/*
		//TC07:	intersects once - The ray starts in parallel to the O point 
		Ray r7 = new Ray(new Point3D (2,1,0), new Vector(-1,0,1));
		result = tub.findIntersections(r7);
		assertEquals("TC07:	intersects once - The ray starts in parallel to the O point ",result, List.of((new Point3D(1, 1,1))));
		
		//TC08:	intersects once - The ray crosses the parallel point to O
		Ray r8 = new Ray(new Point3D (4, 1, 0.673879584707434) , new Vector(-3,0,-0.673879584707434));
		result = tub.findIntersections(r8);
		assertEquals("TC08:	intersects once - The ray crosses the parallel point to O", result,List.of(new Point3D(1,1,0)));
		*/	
		//TC09: doesn't intersects (the line of the ray also doesn't intersects) - The ray starts in parallel to the O point 
		Ray r9 = new Ray(new Point3D (2, 1, 0)  , new Vector(-4.142580432497253,2.11516536352305,0.685069779457748));
		result = tub.findIntersections(r9);
		assertNull("TC09: doesn't intersects (the line of the ray also doesn't intersects) - The ray starts in parallel to the O point ", result);
		
		//TC010: the ray doesn't intersects (the line of the ray does intersects) - The ray crosses the parallel point to O
		Ray r10 =  new Ray(new Point3D (4, -1.259368078054248, 1.25376699095548) , new Vector(-2,2.259368078054248,-1.25376699095548));
		result = tub.findIntersections(r10);
		assertNull("TC010: the ray doesn't intersects (the line of the ray does intersects) - The ray crosses the parallel point to O", result);
		
		//TC011: tangents to the tube
		Ray r11 = new Ray(new Point3D (4, 1, -1) , new Vector(-3,0,1));
		result = tub.findIntersections(r11);
		assertNull("TC011: tangents to the tube", result);
		
		//TC012: tangents to the tube - The ray starts in parallel to the O point 
		Ray r12 = new Ray(new Point3D (1, 1, 0) , new Vector(3,0.657806223218302,0.576690261428155));
		result = tub.findIntersections(r12);
		assertNull("TC012: tangents to the tube - The ray starts in parallel to the O point ", result);
		
		//TC013: tangents to the tube - The ray crosses the parallel point to O
		Ray r13 = new Ray(new Point3D (-3, 1, 0.685069779457748), new Vector(4,0,-0.685069779457748));
		result = tub.findIntersections(r13);
		assertNull("TC013: tangents to the tube - The ray crosses the parallel point to O", result);
		
		//TC014: starts inside the tube
		Ray r14 = new Ray(new Point3D (0.5, 0.5, 1) , new Vector(-2.5,-1.5,-0.314930220542252));
		result = tub.findIntersections(r14);
		assertEquals("TC014: starts inside the tube ", result, List.of(new Point3D(0.023141075423635, 0.213884645254181, 0.93992908548625)));
		
		//TC015: starts inside the tube - The ray starts at the O point
		Ray r15 = new Ray(new Point3D (1,0,0), new Vector(-3, -1, 0.685069779457748));
		result = tub.findIntersections(r15);
		assertEquals("TC015: starts inside the tube - The ray starts at the O point", result, List.of(new Point3D(0.051316701949486, -0.316227766016838, 0.216638085923572)));
		
		//TC016: starts inside the tube - The ray crosses the O point
		Ray r16 = new Ray(new Point3D (0.5, 0.5, 1) , new Vector(0.5,-0.5,-1));
		result = tub.findIntersections(r16);
		assertEquals("TC016: starts inside the tube - The ray crosses the O point", result, List.of(new Point3D(1.707106781186547, -0.707106781186547, -1.414213562373095)));
		
		//TC017: in parallel to the tube:(starts inside the tube)
		Ray r17 = new Ray(new Point3D (0.5, 0.5, 1) , new Vector(0,0,-1));
		result = tub.findIntersections(r17);
		assertNull("TC017: in parallel to the tube:(starts inside the tube)", result);
		
		//TC018: in parallel to the tube:(starts inside the tube) - The ray starts at the O point
		Ray r18 = new Ray(new Point3D (1,0,0), new Vector(0, 0, -1));
		result = tub.findIntersections(r18);
		assertNull("TC018: in parallel to the tube:(starts inside the tube) - The ray starts at the O point", result);
		
		//TC019: in parallel to the tube:(starts inside the tube)- The ray crosses the O point
		Ray r19 = new Ray(new Point3D (1, 0, -1) , new Vector(0,0,1));
		result = tub.findIntersections(r19);
		assertNull("TC019: in parallel to the tube:(starts inside the tube)- The ray crosses the O point", result);
		
		//TC020: in parallel to the tube:(starts outside of the tube )
		Ray r20 = new Ray(new Point3D (3, 1.5, 1) , new Vector(0,0,2));
		result = tub.findIntersections(r20);
		assertNull("TC020: in parallel to the tube:(starts outside of the tube)", result);
		
		//TC021: in parallel to the tube:(starts outside of the tube) - The ray starts in in parallel to the O point
		Ray r21 = new Ray(new Point3D (2, 1, 0) , new Vector(0,0,1));
		result = tub.findIntersections(r21);
		assertNull("TC021: in parallel to the tube:(starts outside of the tube) - The ray starts in in parallel to the O point", result);
		
		//TC022: in parallel to the tube:(starts outside of the tube)-  The ray crosses the parallel point to O
		Ray r22 = new Ray(new Point3D (2, 1, 1) , new Vector(0,0,-1));
		result = tub.findIntersections(r22);
		assertNull("TC022: in parallel to the tube:(starts outside of the tube)-  The ray crosses the parallel point to O", result);
		
		//TC023: in parallel to the tube:(starts on the surface of the tube)
		Ray r23 = new Ray(new Point3D (0.476513544049179, -0.852033996056524, 0.658659809039375), new Vector(0,0,1.341340190960625));
		result = tub.findIntersections(r23);
		assertNull("TC023: in parallel to the tube:(starts on the surface of the tube)", result);
		
		//TC024: in parallel to the tube:(starts on the surface of the tube) - The ray starts in in parallel to the O point
		Ray r24 = new Ray(new Point3D (1, 1, 0) , new Vector(0,0,1));
		result = tub.findIntersections(r24);
		assertNull("TC024: in parallel to the tube:(starts on the surface of the tube) - The ray starts in in parallel to the O point", result);
		
		//TC025: in parallel to the tube:(starts on the surface of the tube)-  The ray crosses the parallel point to O
		Ray r25 = new Ray(new Point3D (1, 1, 1) , new Vector(0,0,-1));
		result = tub.findIntersections(r25);
		assertNull("TC025: in parallel to the tube:(starts on the surface of the tube)-  The ray crosses the parallel point to O", result);
		
		//TC026: the ray starts on the surface of the tube:(goes inside the tube)
		Ray r26 = new Ray(new Point3D (0.476513544049179, -0.852033996056524, 2) , new Vector(3.523486455950821,1.852033996056524,-3));
		result = tub.findIntersections(r26);
		assertEquals("TC026: the ray starts on the surface of the tube:(goes inside the tube)", result, List.of(new Point3D(1.998649121751307, -0.051960866287461, 0.704009000689027)));
		
		//TC027: the ray starts on the surface of the tube:(goes inside the tube) - The ray starts in in parallel to the O point
		Ray r27 = new Ray(new Point3D (1, 1, 0), new Vector(-3,-2,0.685069779457748));
		result = tub.findIntersections(r27);
		assertEquals("TC027: the ray starts on the surface of the tube:(goes inside the tube) - The ray starts in in parallel to the O point", result, List.of(new Point3D(0.076923076923077, 0.384615384615385, 0.210790701371615)));
		
		//TC028: the ray starts on the surface of the tube:(goes inside the tube)- The ray crosses the O point
		Ray r28 = new Ray(new Point3D (0.476513544049179, -0.852033996056524, 0.658659809039375) , new Vector(0.523486455950821,0.852033996056524,-0.658659809039375));
		result = tub.findIntersections(r28);
		assertEquals("TC028: the ray starts on the surface of the tube:(goes inside the tube)- The ray crosses the O point", result, List.of(new Point3D(1.523486455950821, 0.852033996056524, -0.658659809039375)));
		
		//TC029: the ray starts on the surface of the tube:(goes outside of the tube )
		Ray r29 = new Ray(new Point3D (1.971626059014012, 0.236522306442544, 1.17801942355619) , new Vector(3.028373940985988,0.052754846500883,0.82198057644381));
		result = tub.findIntersections(r29);
		assertNull("TC020: in parallel to the tube:(starts outside of the tube)", result);
		
		//TC030: the ray starts on the surface of the tube:(goes outside of the tube ) - The ray starts in parallel to the O point
		Ray r30 = new Ray(new Point3D (2,0,0), new Vector(3, 0.289277152943427, 2));
		result = tub.findIntersections(r30);
		assertNull("TC030: the ray starts on the surface of the tube:(goes outside of the tube ) - The ray starts in parallel to the O point", result);
		
		//TC031: the ray starts on the surface of the tube:(goes outside of the tube )-  The ray crosses the parallel point to O
		Ray r31 = new Ray(new Point3D (1, 1, -1) , new Vector(1,0,1));
		result = tub.findIntersections(r31);
		assertNull("TC031: the ray starts on the surface of the tube:(goes outside of the tube )-  The ray crosses the parallel point to O", result);
	
		//TC032: vertical to the tube: intersects twice 
		Ray r32 = new Ray(new Point3D (5,0.289277152943427,2), new Vector(-8, 0, 0));
		result = tub.findIntersections(r32);
		result = orderToComper(result);
		assertEquals("TC32: vertical to the tube: intersects twice", result, List.of(new Point3D(0.042754614121884, 0.289277152943427, 2),new Point3D(1.957245385878116, 0.289277152943427, 2)));
		
		//TC33: vertical to the tube: intersects twice - The ray crosses the O point
		Ray r33 = new Ray(new Point3D (1, -2, 0) , new Vector(0,3,0));
		result = tub.findIntersections(r33);
		result = orderToComper(result);
		assertEquals("TC33: vertical to the tube: intersects twice - The ray crosses the O point", result, List.of(new Point3D(1, -1, 0), new Point3D(1, 1, 0)));
		
		/*
		//TC34: vertical to the tube: intersects once - The ray starts in parallel to the O point 
		Ray r34 = new Ray(new Point3D (1,1,0), new Vector(-4, 0.380682548478987, 0));
		result = tub.findIntersections(r34);
		assertNull("TC34:	intersects once - The ray starts in parallel to the O point ", result);
		
		//TC35: vertical to the tube:	intersects once - The ray crosses the parallel point to O
		Ray r35 = new Ray(new Point3D(-3,1,0), new Vector(4, 0, 0));
		result = tub.findIntersections(r35);
		assertEquals("TC35: vertical to the tube:	intersects once - The ray crosses the parallel point to O", result,List.of(new Point3D(1,1,0)));
		*/
		//TC36: vertical to the tube: doesn't intersects (the line of the ray also doesn't intersects) - The ray starts in parallel to the O point 
		Ray r36 = new Ray(new Point3D(-3,1,0), new Vector(8, 2, 0));
		result = tub.findIntersections(r36);
		assertNull("TC36: vertical to the tube: doesn't intersects (the line of the ray also doesn't intersects) - The ray starts in parallel to the O point ", result);
		
		//TC37: vertical to the tube: the ray doesn't intersects (the line of the ray does intersects) - The ray crosses the parallel point to O
		Ray r37 = new Ray(new Point3D (2,1,0), new Vector(3,2,0));
		result = tub.findIntersections(r37);
		assertNull("TC37: vertical to the tube: the ray doesn't intersects (the line of the ray does intersects) - The ray crosses the parallel point to O", result);
		
		//TC38: vertical to the tube: tangents to the tube
		Ray r38 = new Ray(new Point3D (-3, 1, 2) , new Vector(8,0,0));
		result = tub.findIntersections(r38);
		assertNull("TC38: vertical to the tube: tangents to the tube", result);
		
		//TC39: vertical to the tube: tangents to the tube - The ray starts in parallel to the O point 
		Ray r39 = new Ray(new Point3D(1,1,0), new Vector(-4,0,0));
		result = tub.findIntersections(r39);
		assertNull("TC39: vertical to the tube: tangents to the tube - The ray starts in parallel to the O point ", result);
		
		//TC40: vertical to the tube: tangents to the tube - The ray crosses the parallel point to O
		Ray r40 = new Ray(new Point3D(-3, 1, 0) , new Vector(4,0,0));
		result = tub.findIntersections(r40);
		assertNull("TC40: vertical to the tube: tangents to the tube - The ray crosses the parallel point to O", result);
		
		//TC41: vertical to the tube: starts inside the tube
		Ray r41 = new Ray(new Point3D(0.5, 0.5, 2) , new Vector(4.5,-0.210722847056573,0));
		result = tub.findIntersections(r41);
		assertEquals("TC41: vertical to the tube: starts inside the tube ", result, List.of(new Point3D(1.9007160894186, 0.43440824838217, 2)));
		
		//TC42: vertical to the tube: starts inside the tube - The ray starts at the O point
		Ray r42 = new Ray(new Point3D(1, 0, 0) , new Vector(-4,1,0));
		result = tub.findIntersections(r42);
		assertEquals("TC42: vertical to the tube: starts inside the tube - The ray starts at the O point", result, List.of(new Point3D(0.029857499854668, 0.242535625036333, 0)));
		
		//TC43: vertical to the tube: starts inside the tube - The ray crosses the O point
		Ray r43 = new Ray(new Point3D(0.5, 0.5, 0) , new Vector(0.5,-0.5,0));
		result = tub.findIntersections(r43);
		assertEquals("TC43: vertical to the tube: starts inside the tube - The ray crosses the O point", result, List.of(new Point3D(1.707106781186547, -0.707106781186547, 0)));
		
		//TC44: vertical to the tube: the ray starts on the surface of the tube:(goes inside the tube)
		Ray r44 = new Ray(new Point3D(0.557690001596107, 0.896862233184088, 1) , new Vector(4.065280771413088,-2.777533745622982,0));
		result = tub.findIntersections(r44);
		assertEquals("TC44: vertical to the tube: the ray starts on the surface of the tube:(goes inside the tube)", result, List.of(new Point3D(1.996291860540462, -0.086037948725113, 1)));
		
		//TC45: vertical to the tube: the ray starts on the surface of the tube:(goes inside the tube) - The ray starts in in parallel to the O point
		Ray r45 = new Ray(new Point3D(2, 0, 0) , new Vector(-5,1,0));
		result = tub.findIntersections(r45);
		assertEquals("TC45: vertical to the tube: the ray starts on the surface of the tube:(goes inside the tube) - The ray starts in in parallel to the O point", result, List.of(new Point3D(0.076923076923077, 0.384615384615385, 0)));
		
		//TC46: vertical to the tube: the ray starts on the surface of the tube:(goes inside the tube)- The ray crosses the O point
		Ray r46 = new Ray(new Point3D(2, 0, 0) , new Vector(-1,0,0));
		result = tub.findIntersections(r46);
		assertEquals("TC46: vertical to the tube: the ray starts on the surface of the tube:(goes inside the tube) - The ray crosses the O point", result, List.of(new Point3D(0,0,0)));
		
		//TC47: vertical to the tube: the ray starts on the surface of the tube:(goes outside of the tube )
		Ray r47 = new Ray(new Point3D(0.557690001596107, 0.896862233184088, 1) , new Vector(-3.557690001596107,1.788528244963473,0));
		result = tub.findIntersections(r47);
		assertNull("TC47: vertical to the tube: in parallel to the tube:(starts outside of the tube)", result);
		
		//TC48: vertical to the tube: the ray starts on the surface of the tube:(goes outside of the tube ) - The ray starts in parallel to the O point
		Ray r48 = new Ray(new Point3D(1,1,0), new Vector(-4, 1.685390478147561, 0));
		result = tub.findIntersections(r48);
		assertNull("TC48: vertical to the tube: the ray starts on the surface of the tube:(goes outside of the tube ) - The ray starts in parallel to the O point", result);
		
		
		
	}
}