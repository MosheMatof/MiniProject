package geometries;

import java.util.List;

import primitives.*;
import static primitives.Util.*;

/**
 * represents a plane in a space
 */
public class Plane implements Geometry {

	private Point3D pivot;
	private Vector normal;

	/**
	 * Plane constructor by point and vector
	 * 
	 * @param pivot  a point on the plane
	 * @param normal the normal of the plane
	 */
	public Plane(Point3D pivot, Vector normal) {
		this.pivot = pivot;
		this.normal = normal.normalize();
	}

	/**
	 * Plane constructor by 3 points
	 * 
	 * @param p1 1st point
	 * @param p2 2nd point
	 * @param p3 3rd point
	 */
	public Plane(Point3D p1, Point3D p2, Point3D p3) {
		Vector v1 = p1.subtract(p2);
		Vector v2 = p3.subtract(p2);
		this.normal = v1.crossProduct(v2).normalized();
		this.pivot = p1;
	}

	@Override
	public Vector getNormal(Point3D point) {
		return normal;
	}

	/**
	 * get the normalize vector orthogonal to the plane direction
	 * 
	 * @return the normal of the plane
	 */
	public Vector getNormal() {
		return normal;
	}

	@Override
	public String toString() {
		return "pivot: " + pivot.toString() + ", normal: " + normal;
	}

	/**
	 * get the representing point of the plane
	 * 
	 * @return the point on the plane
	 */
	public Point3D getPivot() {
		return pivot;
	}

	@Override
	public List<Point3D> findIntersections(Ray ray) {
		Vector u;
		try {
			u = this.pivot.subtract(ray.getOrigin());
		} catch (IllegalArgumentException e) {
			// if the ray start at the represented point of the plane
			return null;
		}

		// numerator
		double num = this.getNormal().dotProduct(u);
		// denominator
		double denom = this.getNormal().dotProduct(ray.getDir());
		if (isZero(denom))
			return null;

		double scalar = alignZero(num / denom);
		return scalar <= 0 ? null : List.of(ray.getPoint(scalar));
	}

}
