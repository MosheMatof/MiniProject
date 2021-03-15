package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * represents 
 *
 */
public class Sphere implements Geometry{

	protected Point3D center;
	protected double radius;
	
	/**
	 * 
	 * @param center the center of the sphere
	 * @param radius the radius of the sphere
	 */
	public Sphere(Point3D center, double radius) {
		this.radius = radius;
		this.center = center;	
	}
	@Override
	public Vector getNormal(Point3D point) {
		return null;
	}
	/**
	 * 
	 * @return the center of the sphere
	 */
	public Point3D getCenter() {
		return center;
	}
	/**
	 * 
	 * @return the radius of the sphere
	 */
	public double getRadius() {
		return radius;
	}
	
}
