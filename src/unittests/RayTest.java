
package unittests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import primitives.*;

import org.junit.Test;

import geometries.Intersectable.GeoPoint;
import geometries.Sphere;

/**
 * test class for {@link Ray}
 */
public class RayTest {

	/**
	 * Test method for {@link primitives.Ray#findClosestGeoPoint(java.util.List)}.
	 */
	@Test
	public void testFindClosestPoint() {
		Ray ray = new Ray(new Point3D(0, -6, 0), new Vector(0, 6, 4));
		// this will use for a default geometry in the GeoPoints
		Sphere spr = new Sphere(new Point3D(0,0,0), 1);
		// ____________EP___________________
		// TC01: closest point is in the middle of the list
		List<GeoPoint> gpoints = List.of(new GeoPoint(spr, new Point3D(0, -2.064155224010097, 2.623896517326602)),
				new GeoPoint(spr, new Point3D(0, 0, 4)),
				new GeoPoint(spr, new Point3D(0, -3.976587309161571, 1.348941793892286)),
				new GeoPoint(spr, new Point3D(0, 1.956242772188382, 5.304161848125588)),
				new GeoPoint(spr, new Point3D(0, 5.519693402097971, 7.679795601398647)));
		assertEquals("TC01: closest point is in the middle of the list", ray.findClosestGeoPoint(gpoints),
				new GeoPoint(spr, new Point3D(0, -3.976587309161571, 1.348941793892286)));

		// ____________BVA__________________

		// TC02: empty list
		assertNull("TC02: empty list", ray.findClosestGeoPoint(new LinkedList<GeoPoint>()));

		// TC03: closest point is the first point in the list
		
		gpoints = List.of(new GeoPoint(spr, new Point3D(0, -3.976587309161571, 1.348941793892286)),
				new GeoPoint(spr, new Point3D(0, -2.064155224010097, 2.623896517326602)),
				new GeoPoint(spr, new Point3D(0, 0, 4)),
				new GeoPoint(spr, new Point3D(0, 1.956242772188382, 5.304161848125588)),
				new GeoPoint(spr, new Point3D(0, 5.519693402097971, 7.679795601398647)));
		assertEquals("TC03: closest point is the first point in the list", ray.findClosestGeoPoint(gpoints),
				new GeoPoint(spr, new Point3D(0, -3.976587309161571, 1.348941793892286)));
		
		// TC04: closest point is the last point in the list
		gpoints = List.of(new GeoPoint(spr, new Point3D(0, -2.064155224010097, 2.623896517326602)), 
				new GeoPoint(spr, new Point3D(0, 0, 4)),
				new GeoPoint(spr, new Point3D(0, 1.956242772188382, 5.304161848125588)),
				new GeoPoint(spr, new Point3D(0, 5.519693402097971, 7.679795601398647)),
				new GeoPoint(spr, new Point3D(0, -3.976587309161571, 1.348941793892286)));
		assertEquals("TC04: closest point is the last point in the list", ray.findClosestGeoPoint(gpoints),
				new GeoPoint(spr, new Point3D(0, -3.976587309161571, 1.348941793892286)));
	}

}
