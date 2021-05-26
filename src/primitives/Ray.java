/**
 * 
 */
package primitives;

import java.util.List;
import java.util.stream.Collectors;

import geometries.Intersectable.GeoPoint;

/**
 * 
 * represents a ray by point and vector
 */
public class Ray {

	private Point3D origin;
	private Vector dir;

	/**
	 * ray constructor by point and vector
	 * 
	 * @param p0  the origin point
	 * @param dir the ray direction
	 */
	public Ray(Point3D p0, Vector dir) {
		this.origin = p0;
		this.dir = dir.normalized();
	}

	private static final double DELTA = 0.1;
	
	/**
	 * ray constructor move the origin by delta vector
	 * @param p0  the origin point
	 * @param v the ray direction - must be normalized
	 * @param n the vector to move the origin by
	 */
	public Ray(Point3D p0, Vector v, Vector n) {
		this.dir = v;
		this.origin = p0.add(n.scale(v.dotProduct(n) > 0 ? DELTA : -DELTA));
	}

	/**
	 * get the starting point of the ray
	 * 
	 * @return the origin point
	 */
	public Point3D getOrigin() {
		return origin;
	}

	/**
	 * get the direction vector of the ray
	 * 
	 * @return the direction vector
	 */
	public Vector getDir() {
		return dir;
	}

	/**
	 * find a point on the ray by a given scalar(origin + scalar*dir)
	 * 
	 * @param t a scalar to progress to
	 * @return a 3d point on the ray
	 */
	public Point3D getPoint(double t) {
		if (Util.alignZero(t) <= 0)
			throw new IllegalArgumentException("point must be on the ray, not including the ray head");
		return origin.add(dir.scale(t));
	}

	/**
	 * finds from a list of points the closest point to the origin of this ray
	 * 
	 * @param points the list of points to choose from
	 * @return the closest point to the origin of this ray
	 */
	public Point3D findClosestPoint(List<Point3D> points) {
		if (points == null) // if the list is empty or null
			return null;
		return findClosestGeoPoint( //
				points.stream().map(p -> new GeoPoint(null, p)).collect(Collectors.toList()) //
				).point;
	}
	
	/**
	 * finds from a list of GeoPoints the GeoPoint with the closest point to the origin of this ray
	 * @param gpoints the list of GeoPoints to choose from
	 * @return the GeoPoint with the closest point to the origin of this ray
	 */
	public GeoPoint findClosestGeoPoint(List<GeoPoint> gpoints) {
		if (gpoints == null) // if the list is empty or null
			return null;
		GeoPoint closestPoint = null;
		double smallestDist = Double.POSITIVE_INFINITY;

		// find the GeoPoint with the closest point
		for (GeoPoint gpoint : gpoints) {
			double pointDist = origin.distanceSquared(gpoint.point);
			if (pointDist < smallestDist) {
				smallestDist = pointDist;
				closestPoint = gpoint;
			}
		}
		return closestPoint;
	}
	
	@Override
	public String toString() {
		return origin.toString() + " " + dir.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Ray))
			return false;
		Ray other = (Ray) obj;
		return origin.equals(other.origin) && dir.equals(other.dir);
	}
}
