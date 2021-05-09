package geometries;

import java.util.List;

import primitives.*;
import static primitives.Util.*;

/**
 * represents sphere by point and radius
 */
public class Sphere implements Geometry {

	private Point3D center;
	private double radius;
	private double radiusSquared;

	/**
	 * Sphere constructor by point and radius
	 * 
	 * @param center the center of the sphere
	 * @param radius the radius of the sphere
	 */
	public Sphere(Point3D center, double radius) {
		this.radius = radius;
		this.radiusSquared = radius * radius;
		this.center = center;
	}

	@Override
	public Vector getNormal(Point3D point) {
		return point.subtract(center).normalize();
	}

	/**
	 * get the point in the center of the sphere
	 * 
	 * @return the center point of the sphere
	 */
	public Point3D getCenter() {
		return center;
	}

	/**
	 * get the radius length of the sphere
	 * 
	 * @return the radius of the sphere
	 */
	public double getRadius() {
		return radius;
	}

	@Override
	public List<Point3D> findIntersections(Ray ray) {
		// if the origin point of the ray is in the center
		if (ray.getOrigin().equals(center)) {
			return List.of(ray.getPoint(radius));
		}

		// vector from the origin point of the ray to the center
		Vector u = center.subtract(ray.getOrigin());

		// the length from the origin point of the ray to the middle point between the
		// intersection of the line of the ray
		double tm = u.dotProduct(ray.getDir());

		// the length Squared between the center and the line of the ray
		double dSqr = u.lengthSquared() - tm * tm;
		// the half length between the intersection of the line of the ray
		double thSqr = radiusSquared - dSqr;

		// if the length between the center and the line of the ray is bigger
		if (alignZero(thSqr) <= 0)
			return null;

		// the half length between the intersection of the line of the ray
		double th = Math.sqrt(thSqr);

		// the distance between the origin of the ray and the most positive intersection
		// point
		double t2 = alignZero(tm + th);
		if (t2 <= 0)
			return null;
		Point3D p2 = ray.getPoint(t2);

		// the distance between the origin of the ray and the most negative intersection
		// point
		double t1 = alignZero(tm - th);

		return t1 <= 0 ? List.of(p2) : List.of(ray.getPoint(t1), p2);
	}

}
