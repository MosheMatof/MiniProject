/**
 * 
 */
package primitives;

/**
 * 
 *represents a ray in the space
 */
public class Ray {

	private Point3D origin;
	private Vector dir;
	
	/**
	 * this.origin <- p0
	 * this.dir <- dir
	 */
	public Ray(Point3D p0, Vector dir) {
		this.origin = p0;
		dir.normalize();
		this.dir = dir;
	}
	
	/*
	public Ray(Ray other) {
		this.dir.head.x = new Coordinate(other.dir.head.x.coord);
		this.dir.head.y = new Coordinate(other.dir.head.y.coord);
		this.dir.head.z = new Coordinate(other.dir.head.z.coord);
	}
	*/
	
	public Point3D getOrigin() {
		return origin;
	}
	
	public Vector getDir() {
		return dir;
	}
	
	@Override
	public String toString() {
		return origin.toString() + " " + dir.toString();
	}
}
