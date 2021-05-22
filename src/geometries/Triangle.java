package geometries;

import java.util.List;

import primitives.*;
import static primitives.Util.*;

/**
 * represents a triangle by 3 points. inherits from polygon class.
 */
public final class Triangle extends Polygon {

	/**
	 * Triangle constructor by 3 points
	 * 
	 * @param p1 1st vertex
	 * @param p2 2nd vertex
	 * @param p3 3rd vertex
	 */
	public Triangle(Point3D p1, Point3D p2, Point3D p3) {
		super(p1, p2, p3);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	
	@Override
	public List<GeoPoint> findGeoIntersections(Ray ray, double maxDist) {
		List<GeoPoint> intersections = this.plane.findGeoIntersections(ray, maxDist);
		if (intersections == null)
			return null;

		Point3D origin = ray.getOrigin();
		Vector v1 = this.vertices.get(0).subtract(origin);
		Vector v2 = this.vertices.get(1).subtract(origin);
		Vector v3 = this.vertices.get(2).subtract(origin);

		Vector dir = ray.getDir();
		double s1 = dir.dotProduct(v1.crossProduct(v2));
		if (isZero(s1))
			return null;

		double s2 = dir.dotProduct(v2.crossProduct(v3));
		if (alignZero(s1 * s2) <= 0)
			return null;

		double s3 = Util.alignZero(dir.dotProduct(v3.crossProduct(v1)));
		if (alignZero(s1 * s3) <= 0)
			return null;
		intersections.get(0).geometry = this;
		return intersections;
	}

}
