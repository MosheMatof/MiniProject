package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

/**
 * represents a triangle by 3 points. inherits from polygon class.
 */
public final class Triangle extends Polygon{

	/**
	 * Triangle constructor by 3 points
	 * @param p1
	 * @param p2
	 * @param p3
	 */
	public Triangle(Point3D p1, Point3D p2, Point3D p3) {
		super(p1,p2,p3);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	/*
	@Override
	public List<Point3D> findIntersections(Ray ray) {
		Vector v1 = this.vertices.get(0).subtract(ray.getOrigin());
		Vector v2 = this.vertices.get(1).subtract(ray.getOrigin());
		Vector v3 = this.vertices.get(2).subtract(ray.getOrigin());
		
		Vector n1 = v1.crossProduct(v2).normalize();
		Vector n2 = v2.crossProduct(v3).normalize();
		Vector n3 = v3.crossProduct(v1).normalize();
		
		//the values of all the normals * ray
		double s1 = Util.alignZero(ray.getDir().dotProduct(n1));
		double s2 = Util.alignZero(ray.getDir().dotProduct(n2));
		double s3 = Util.alignZero(ray.getDir().dotProduct(n3));
		
		//if all the scalars have the same sign and non of theme is zero
		if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
			return this.plane.findIntersections(ray);
		}
		return null;
	}
	*/
}
