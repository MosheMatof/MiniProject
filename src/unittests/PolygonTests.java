/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;


import geometries.*;
import primitives.*;

/**
 * Testing Polygons
 * 
 * @author Dan
 *
 */
public class PolygonTests {

    /**
     * Test method for
     * {@link geometries.Polygon#Polygon(primitives.Point3D, primitives.Point3D, primitives.Point3D, primitives.Point3D)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(-1, 1, 1));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct polygon");
        }

        // TC02: Wrong vertices order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(0, 1, 0),
                    new Point3D(1, 0, 0), new Point3D(-1, 1, 1));
            fail("Constructed a polygon with wrong order of vertices");
        } catch (IllegalArgumentException e) {}

        // TC03: Not in the same plane
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 2, 2));
            fail("Constructed a polygon with vertices that are not in the same plane");
        } catch (IllegalArgumentException e) {}

        // TC04: Concave quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0.5, 0.25, 0.5));
            fail("Constructed a concave polygon");
        } catch (IllegalArgumentException e) {}

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 0.5, 0.5));
            fail("Constructed a polygon with vertix on a side");
        } catch (IllegalArgumentException e) {}

        // TC11: Last point = first point
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 0, 1));
            fail("Constructed a polygon with vertice on a side");
        } catch (IllegalArgumentException e) {}

        // TC12: Colocated points
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 1, 0));
            fail("Constructed a polygon with vertice on a side");
        } catch (IllegalArgumentException e) {}
    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Polygon pl = new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0),
                new Point3D(-1, 1, 1));
        double sqrt3 = Math.sqrt(1d / 3);
        Vector n = pl.getNormal(new Point3D(0, 0, 1));
        Vector expected = new Vector(sqrt3, sqrt3, sqrt3);
        assertTrue("Bad normal to trinagle", expected.equals(n) || expected.scale(-1).equals(n));
    }
    
    /**
     * Test methods for {link {@link geometries.Polygon#findIntersections(Ray)}
     */
    @Test
    public void testFindIntersection() {
    	Polygon poly = new Polygon(new Point3D(1, 1, 1), new Point3D(-0.534185323603096, -1.537474653089115, 2.003289329486019), new Point3D(-2, -2, 1), new Point3D(-1, 1, -1));
		
		//___________________ Equivalence Partitions Tests__________________
		
		//TC01: inside polygon
		Ray r1 = new Ray(new Point3D(-2,2,2), new Vector(2,-2,-2));
		assertEquals("TC01: inside triangle", poly.findIntersections(r1), List.of(new Point3D(-1d/3, 1d/3, 1d/3)));
		
		//TC02: Outside against edge
		Ray r2 = new Ray(new Point3D(-2, -2, 2) , new Vector(4,-2,-2));
		assertNull("TC02: Outside against edge", poly.findIntersections(r2));
		
		//TC03: Outside against vertex
		Ray r3 = new Ray(new Point3D(-4, -3, 2), new Vector(2,-1,-2));
		assertNull("TC03: Outside against vertex", poly.findIntersections(r3));
		
		//___________________ Boundary Values Tests __________________
		
		//TC04: ray intersect the plane of the Polygon on the edge of the polygon
		Ray r4 = new Ray(new Point3D(0, 0, -2), new Vector(-1.432240833943874, -0.296722501831622, 1.864481667887748));
		assertNull("TC04: ray intersect the plane on the edge of the polygon", poly.findIntersections(r4));
		
		//TC05: ray intersect the plane of the polygon on the vertex of the polygon
		Ray r5 = new Ray(new Point3D(3,0,0), new Vector(-2,1,1));
		assertNull("TC04: ray intersect the plane of the polygon on the edge of the polygon", poly.findIntersections(r5));
		
		//TC06: ray intersect the plane of the polygon on the edge's continuation of the polygon
		Ray r6 = new Ray(new Point3D(2, 0, 0), new Vector(-0.357842405287136, -0.850748166105454, 3.492905760818318));
		assertNull("TC06: ray intersect the plane of the polygon on the edge's continuation of the polygon", poly.findIntersections(r6));
		
    }

}
