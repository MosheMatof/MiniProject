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
		//point on the upper surface of the cylinder 
		Ray ray = new Ray(new Point3D(1, 2, 3), new Vector(1, 2, 3));
		Cylinder ciy = new Cylinder(ray, 5, 5);
		Vector expected = new Vector(1, 2, 3).normalized();
		
		assertEquals("upper surface", expected, ciy.getNormal(new Point3D(2.918362587386101, 4.920333319728062, 6.649752569358125)));
		
		//point on the lower surface of the cylinder
		expected = expected.scale(-1);
		assertEquals("lower surface", expected, ciy.getNormal(new Point3D(1.249135312546304, 2.106031007159894, 2.846267557699403)));
		
	}

}
