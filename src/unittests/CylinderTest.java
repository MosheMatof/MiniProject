package unittests;

import static org.junit.Assert.*;

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
	
	

}
