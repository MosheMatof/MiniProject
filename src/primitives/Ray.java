/**
 * 
 */
package primitives;

/**
 * 
 *represents a ray by point and vector
 */
public class Ray {

	private Point3D origin;
	private Vector dir;
	
	/**
	 * ray constructor by point and vector
	 * @param p0 the origin point
	 * @param dir
	 */
	public Ray(Point3D p0, Vector dir) {
		this.origin = p0;
		this.dir = dir.normalized();
	}
	
	/*
	public Ray(Ray other) {
		this.dir.head.x = new Coordinate(other.dir.head.x.coord);
		this.dir.head.y = new Coordinate(other.dir.head.y.coord);
		this.dir.head.z = new Coordinate(other.dir.head.z.coord);
	}
	*/
	/**
	 * get the starting point of the ray
	 * @return the origin point
	 */
	public Point3D getOrigin() {
		return origin;
	}
	/**
	 * get the direction vector of the ray
	 * @return the direction vector
	 */
	public Vector getDir() {
		return dir;
	}
	/**
	 * find a point on the ray by a given scalar(origin + scalar*dir) 
	 * @param t a scalar to progress to 
	 * @return a 3d point on the ray 
	 */
	public Point3D getPoint(double t) {
		if (Util.isZero(t)) {
			return origin;
		}
		return origin.add(dir.scale(t));
	}
	
	@Override
	public String toString() {
		return origin.toString() + " " + dir.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Ray)) return false;
		Ray other = (Ray)obj;
		return origin.equals(other.origin) && dir.equals(other.dir);
	}
}
