package geometries;


import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

/**
 * represents a plane in a space
 */
public class Plane implements Geometry{

	private Point3D pivot;
	private Vector normal;
	
	/**
	 * Plane constructor by point and vector
	 * @param pivot a point on the plane
	 * @param normal the normal of the plane
	 */
	public Plane(Point3D pivot, Vector normal) {
		this.pivot = pivot;
		this.normal = normal.normalize();	
	}
	
	/**
	 * Plane constructor by 3 points
	 * @param p1
	 * @param p2
	 * @param p3
	 */
	public Plane(Point3D p1, Point3D p2, Point3D p3) {
		try { 
			Vector v1 = p1.subtract(p2);
			Vector v2 = p3.subtract(p2);
			this.normal = v1.crossProduct(v2).normalized();
		} catch (IllegalArgumentException e) { // if the 3 point on the same line...
			throw new IllegalArgumentException("the 3 points cannot be on the same line", e);
		}	
		this.pivot = p1;
	}

	@Override
	public Vector getNormal(Point3D point) {
		return normal;
	}
	
	/**
	 * get the normalize vector orthogonal to the plane direction
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
	 * @return the point on the plane
	 */
	public Point3D getPivot() {
		return pivot;
	}

	@Override
	public List<Point3D> findIntersections(Ray ray) {
		//if the ray start at the represented point of the plane
		if (this.pivot.equals(ray.getOrigin())) {
			return null;
		}
		
		//numerator
		double a = this.getNormal().dotProduct(this.pivot.subtract(ray.getOrigin()));
		//denominator
		double b = this.getNormal().dotProduct(ray.getDir());
		
		if (!Util.isZero(b)) {
			double scalar = a / b;
			
			if (!Util.isZero(scalar) && scalar > 0) {
				return List.of(ray.getPoint(scalar));
			}
		}
		return null;
	}
	 
}
