package unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import elements.Camera;
import geometries.*;
import primitives.*;
/**
 * Integration test of Camera and Geometries
 */
public class CameraGeometriesIntegrationTest {

	/**
	 * calculates the number of intersections points between
	 * the rays from the camera(view plane) and an intersectable object
	 * @param cam the camera
	 * @param geo an intersectable object
	 * @return the number of intersections points
	 */
	private int findNumOfIntersections(Camera cam, Intersectable geo) {
		int numOfIntersaction = 0;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				Ray r = cam.constructRayThroughPixel(3, 3, j, i);
				List<Point3D> result = geo.findIntersections(r);
				numOfIntersaction += result == null? 0: result.size();
			}
		}
		return numOfIntersaction;
	}
	/**
	 * Integration test of Camera and {@link geometries.Sphere#findIntersections(Ray)}
	 */
	@Test
	public void SphereIntegrationTest() {
		Camera cam1 = new Camera(new Point3D(0,0,0), new Vector(0,1,0), new Vector(0,0,-1));
		cam1.setDistance(1);
		cam1.setViewPlaneSize(3, 3);
		//TC01: sphere right in front of the camera intersect twice with the middle pixel
		Sphere spr1 = new Sphere(new Point3D(0,0,-3), 1);
		assertEquals("TC01: sphere right in front of the camera intersect once with the middle pixel", findNumOfIntersections(cam1, spr1), 2);
		
		//TC02: all the pixels intersecting (18 intersection) 
		Camera cam2 = new Camera(new Point3D(0,0,0.5), new Vector(0,1,0), new Vector(0,0,-1));
		cam2.setDistance(1);
		cam2.setViewPlaneSize(3, 3); 
		Sphere spr2 = new Sphere(new Point3D(0,0,-2.5), 2.5);
		assertEquals("TC02: all the pixels intersecting with the sphere (18 intersection)" , findNumOfIntersections(cam2, spr2), 18);
		
		//TC03: (10 intersection) 
		Sphere spr3 = new Sphere(new Point3D(0,0,-2), 2);
		assertEquals("TC03: (10 intersection) " , findNumOfIntersections(cam2, spr3), 10);
		
		//TC04: the camera and the viewPlane are inside the sphere (9 intersection) 
		Sphere spr4 = new Sphere(new Point3D(0,0,-1), 4);
		assertEquals("TC04: the camera and the viewPlane are inside the sphere (9 intersection)" , findNumOfIntersections(cam2, spr4), 9);
		
		//TC05:the sphere is behind the camera (0 intersection)
		Sphere spr5 = new Sphere(new Point3D(0, 0, 1), 0.5);
		assertEquals("TC05: (0 intersection)" , findNumOfIntersections(cam1, spr5), 0);
	}
	/**
	 * Integration test of Camera and {@link geometries.Plane#findIntersections(Ray)}
	 */
	@Test
	public void PlaneIntegrationTest() {
		Camera cam1 = new Camera(new Point3D(0,0,0), new Vector(0,1,0), new Vector(0,0,-1));
		cam1.setDistance(1);
		cam1.setViewPlaneSize(3, 3);
		
		//TC01: the plane is in front and parallel to the view plane (9 intersections)
		Plane p1 = new Plane(new Point3D(0, 0, -2), new Vector(0,0,-1));
		assertEquals("TC01: the plane is parallel to the view plane (9 intersections)", findNumOfIntersections(cam1, p1), 9);
		
		//TC02: the plane is in front of the view plane (9 intersections)
		Plane p2 = new Plane(new Point3D(0, 0, -5), new Vector(0,-0.5,-1));
		assertEquals("TC02: the plane is in front of the view plane (9 intersections)", findNumOfIntersections(cam1, p2), 9);
		
		//TC03: the plane is in front of the viewPlane with slope smaller then the lowest pixels Rays (6 intersections)
		Plane p3 = new Plane(new Point3D(0, 0, -5), new Vector(0,-1.5,-1));
		assertEquals("TC02: the plane is in front of the view plane (9 intersections)", findNumOfIntersections(cam1, p3), 6);
	}
	/**
	 * Integration test of Camera and {@link geometries.Triangle#findIntersections(primitives.Ray)}
	 */
	@Test 
	public void TriangleIntegrationTest() {
		Camera cam1 = new Camera(new Point3D(0,0,0), new Vector(0,1,0), new Vector(0,0,-1));
		cam1.setDistance(1);
		cam1.setViewPlaneSize(3, 3);
		
		//TC01: triangle in front of the camera parallel to the view plane (one intersection) 
		Triangle trgl1 = new Triangle(new Point3D(0, 1, -2), new Point3D(1, -1,-2), new Point3D(-1,-1,-2));
		assertEquals("TC01: triangle in front of the camera parallel to the view plane (one intersection)", findNumOfIntersections(cam1, trgl1), 1);
		
		//TC02: triangle in front of the camera parallel to the view plane (2 intersection)
		Triangle trgl2 = new Triangle(new Point3D(0, 20, -2), new Point3D(1, -1,-2), new Point3D(-1,-1,-2));
		assertEquals("TC02: triangle in front of the camera parallel to the view plane (2 intersection)", findNumOfIntersections(cam1, trgl2), 2);
		
	}

}
