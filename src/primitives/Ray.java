/**
 * 
 */
package primitives;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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

	/**
	 * small number to move the start point of reflection's or transparency's ray
	 */
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
	 * generates sample of 5 rays (from center and 4 carves)
	 * @param target the targrt point to send the rays to
	 * @param radius the half width of the Square
	 * @param vUp the normal vector - orthogonal to the normal of the origin point 
	 * @return sample of 5 rays
	 */
	public List<Ray> getSempleRaysSquare(Point3D target, double radius,Vector vUp) {
		Vector vRight = vUp.crossProduct(this.dir);
		List<Ray> rays = new LinkedList<>();
		List<Point3D> points = List.of(
			target.add(vUp.scale(radius).add(vRight.scale(radius))),
			target.add(vUp.scale(-radius).add(vRight.scale(radius))),
			target.add(vUp.scale(radius).add(vRight.scale(-radius))),
			target.add(vUp.scale(-radius).add(vRight.scale(-radius)))
		);
		for (Point3D p : points) {
			rays.add(new Ray(this.origin, p.subtract(this.origin)));
		}
		rays.add(this);
		return rays; // create and return the ray
	}	
	
	/**
	 * generates sample of rays
	 * @param target the targrt point
	 * @param radius the half width of the Square
	 * @param vUp the normal vector - orthogonal to the normal of the origin point 
	 * @param n the dimnension of matrix of the random rays contracted through the pixel
	 * @return list of n*n rays
	 */
	public List<Ray> getRaysSquare(Point3D target, double radius,Vector vUp,int n) {
		Vector vRight = vUp.crossProduct(this.dir);
		List<Ray> rays = new LinkedList<>();
		double interval = (2*radius)/(n-1);
		for (double i = -radius; i < radius; i+=interval) {
			for (double j = -radius; j < radius; j+=interval) {
				double rH = ThreadLocalRandom.current().nextDouble(interval) + i;
				double rW = ThreadLocalRandom.current().nextDouble(interval) + j;
				Point3D p = target;
				if (!Util.isZero(rH)) { // to prevent a creation of a zero vector
					p = p.add(vUp.scale(rH));
				}
				if (!Util.isZero(rW)) { // to prevent a creation of a zero vector
					p = p.add(vRight.scale(rW));
				}
				rays.add(new Ray(this.origin, p.subtract(this.origin)));
			}
		}
		rays.add(this);
		return rays; // create and return the ray
	}	

	/**
	
	
	/**
	 * generates sample of 5 rays (from center and 4 carves)
	 * @param target the targrt point to send the rays to
	 * @param radius the radius of the circle
	 * @param vUp the normal vector - orthogonal to the normal of the origin point 
	 * @return sample of 5 rays
	 */
	public List<Ray> getSempleRaysCircle(Point3D target, double radius,Vector vUp) {
		Vector vRight = vUp.crossProduct(this.dir);
		List<Ray> rays = new LinkedList<>();
		List<Point3D> points = List.of(
			target.add(vUp.scale(radius)),
			target.add(vUp.scale(-radius)),
			target.add(vRight.scale(radius)),
			target.add(vRight.scale(-radius))
		);
		for (Point3D p : points) {
			rays.add(new Ray(this.origin, p.subtract(this.origin)));
		}
		rays.add(this);
		return rays; // create and return the ray
	}	

	/**
	 * generates sample of rays from given point to random points on a circle
	 * @param target the targrt point
	 * @param radius the radius of the circle
	 * @param vUp the normal vector - orthogonal to the normal of the origin point 
	 * @param n the dimnension of matrix of the random rays contracted through the pixel
	 * @return list of n rays
	 */
	public List<Ray> getRaysCircle(Point3D target, double radius,Vector vUp,int n) {
		Vector vRight = vUp.crossProduct(this.dir);
		List<Ray> rays = new LinkedList<>();
		for (int i = 0; i < n; i++) {
			Point3D p = target;
			double sn = ThreadLocalRandom.current().nextDouble(2 * radius) - radius;
			if(!Util.isZero(sn))
				p = p.add(vRight.scale(sn));
			double sv = ThreadLocalRandom.current().nextDouble(2 * radius) - radius;
			if(!Util.isZero(sv))
				p = p.add(vUp.scale(sv));
			rays.add(new Ray(this.origin, p.subtract(this.origin)));
		}
		rays.add(this);
		return rays; // create and return the ray
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
