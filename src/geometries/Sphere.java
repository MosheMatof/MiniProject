package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * represents a sphere
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
	
	@Override
	public String toString() {
		return "center: " + center.toString() + ", radius: " + radius;
	}

	public Point3D getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}
}
