package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * represents 
 *
 */
public class Sphere implements Geometry{

	Point3D center;
	double radius;
	
	public Sphere(Point3D center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	@Override
	public Vector getNormal() {
		return null;
	}	
}
