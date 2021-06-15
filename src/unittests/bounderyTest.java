/**
 * 
 */
package unittests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import elements.*;
import geometries.*;

import geometries.Intersectable.Boundary;
import primitives.*;
import renderer.*;

import scene.Scene;

/**
 * tests the class {@link Geometries.Geometry.Boundary}
 */
public class bounderyTest {

	@Test
	public void isIntersectTest() {
		Boundary b = new Boundary(-5, 5, -5, 5, -5, 5);
		// TC01: ray intersect the boundary
		Ray r = new Ray(new Point3D(-6, -6, -6), new Vector(1, 1, 1));
		assertTrue("TC01: ray intersect the boundary", b.isIntersect(r,Double.POSITIVE_INFINITY));

		// TC02: ray dosen't intersect the boundary
		r = new Ray(new Point3D(6, 6, 6), new Vector(1, -1, -1));
		assertFalse("TC02: ray dosen't intersect the boundary", b.isIntersect(r,Double.POSITIVE_INFINITY));

		// TC03: ray start after the boundary (no intersection)
		r = new Ray(new Point3D(6, 6, 6), new Vector(1, 1, 1));
		assertFalse("TC03: ray start after the boundary", b.isIntersect(r,Double.POSITIVE_INFINITY));

		// TC04: ray start inside the boundary (intersect)
		r = new Ray(new Point3D(0, 0, 0), new Vector(1, 1, 1));
		assertTrue("TC04: ray start inside the boundary (intersect)", b.isIntersect(r,Double.POSITIVE_INFINITY));

		// TC05: ray starts at one of the border of the boundary and goes outside from
		// the boundary (no intersections)
		r = new Ray(new Point3D(5, 0, 0), new Vector(1, 1, 1));
		assertFalse("TC05: ray starts at one of the border of the boundary "
				+ "and goes outside from the boundary (no intersections)", b.isIntersect(r,Double.POSITIVE_INFINITY));
	}

	@Test
	public void generatesBoundary() {
		Scene scene = new Scene("Boundary test").setBackground(new Color(50, 120, 255));

		Camera camera = new Camera(new Point3D(40, 20, -120), new Point3D(0, 2, 0)).setViewPlaneSize(3200, 1800)
				.setDistance(10000); //

		scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

		Geometry sphere = new Sphere(new Point3D(0, 5.1, 10), 5)
				.setMaterial(new Material().setkR(0.3).setKd(0.2).setKs(0.8).setShininess(100));
		Geometry triangle = new Triangle(new Point3D(5, 8, -5), new Point3D(7, 1, -15),new Point3D(1, 2, -18))
				.setMaterial(new Material().setkR(0.3).setKd(0.2).setKs(0.8).setShininess(100));
		Geometry cylinder = new Cylinder(new Ray(new Point3D(-10, 3, 0), new Vector(0, 1, 1)), 2, 10)
				.setEmission(new Color(2, 20, 2))
				.setMaterial(new Material().setKd(0.8).setkR(0.2).setKs(1).setShininess(1));
		Geometry floor = new Plane(Point3D.ZERO, new Vector(0, 1, 0)).setEmission(new Color(2, 2, 2))
				.setMaterial(new Material().setKd(0.8).setkR(0.2).setKs(1).setShininess(1));
		// List<Geometry> lamp = List.of(
		// new Cylinder(axis, radius, height)
		// )

		scene.geometries = cubesByBoundary(sphere,cylinder,triangle);
		scene.geometries.add(floor);

		scene.lights.addAll(List.of(
				new SpotLight(new Color(0, 255, 40), new Point3D(10, 5, -3), new Vector(-22, -3, 5)).setKB(3)
						.setKl(0.0000001).setKq(0.0000005),
				new SpotLight(new Color(40, 80, 150), new Point3D(10, 10, -8), new Vector(-15, -10, 8)).setKl(0.000001)
						.setKq(0.0000005),
//				new SpotLight(new Color(30, 200, 70), new Point3D(2, 5, 8), new Vector(-2, -5, -8)).setKB(40)
//						.setKl(0.0001).setKq(0.00005),
				new PointLight(new Color(255, 255, 0), new Point3D(0, 200, -300)).setKl(0.0001).setKq(0.00005))
//                ,new SpotLight(new Color(30,3,200), new Point3D(10, -30, 30), new Vector(-1,-3,-3))
//            	.setKB(40).setKl(0.0001).setKq(0.00005))
		);
		ImageWriter imageWriter = new ImageWriter("Boundary test", 1024, 576);
		Render render = new Render() //
				.setImageWriter(imageWriter) //
				.setCamera(camera) //
				.setRayTracer(new BasicRayTracer(scene)).setMultithreading(3).setDebugPrint();

		render.renderImage();
		render.writeToImage();
	}

	/**
	 * generates cubes for each geometry by boundary
	 * 
	 * @param geomets the geometries
	 * @return instance of geometries with the "geometries" and their cubes
	 */
	private Geometries cubesByBoundary(Geometry... geomets) {
		Geometries geometries = new Geometries(geomets);
		for (Geometry g : geomets) {
			double maxX = g.getBoundary().maxX, minX = g.getBoundary().minX;
			double maxY = g.getBoundary().maxY, minY = g.getBoundary().minY;
			double maxZ = g.getBoundary().maxZ, minZ = g.getBoundary().minZ;

			Point3D xyz = new Point3D(minX, minY, minZ);
			Point3D xYz = new Point3D(minX, maxY, minZ);
			Point3D xyZ = new Point3D(minX, minY, maxZ);
			Point3D xYZ = new Point3D(minX, maxY, maxZ);
			Point3D Xyz = new Point3D(maxX, minY, minZ);
			Point3D XYz = new Point3D(maxX, maxY, minZ);
			Point3D XyZ = new Point3D(maxX, minY, maxZ);
			Point3D XYZ = new Point3D(maxX, maxY, maxZ);

			List<Geometry> temp = new LinkedList<>(
					List.of(new Polygon(xyz, xyZ, xYZ, xYz), new Polygon(xyz, xYz, XYz, Xyz)//
							, new Polygon(xyz, xyZ, XyZ, Xyz), new Polygon(xyz, xYz, xYZ, xyZ)//
							, new Polygon(XyZ, XYZ, xYZ, xyZ), new Polygon(XyZ, XYZ, XYz, Xyz)));//
			for (Geometry pol : temp) {
				pol.setEmission(new Color(0, 0, 5))
						.setMaterial(new Material().setkT(1).setKs(0.8).setKd(0.2).setShininess(100));
			}
			temp.forEach(pol -> geometries.add(pol));
		}
		return geometries;
	}

}
