package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * represents sphere by point and radius
 */
public class Sphere implements Geometry{

	protected Point3D center;
	protected double radius;
	
	/**
	 * Sphere constructor by point and radius
	 * @param center the center of the sphere
	 * @param radius the radius of the sphere
	 */
	public Sphere(Point3D center, double radius) {
		this.radius = radius;
		this.center = center;	
	}
	@Override
	public Vector getNormal(Point3D point) {
		return point.subtract(center).normalize();
	}
	/**
	 * get the point in the center of the sphere
	 * @return the center point of the sphere
	 */
	public Point3D getCenter() {
		return center;
	}
	/**
	 * get the radius length of the sphere
	 * @return the radius of the sphere
	 */
	public double getRadius() {
		return radius;
	}
	@Override
	public List<Point3D> findIntersections(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
