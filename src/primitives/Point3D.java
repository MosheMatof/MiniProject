package primitives;

/**
 * represents a point in a 3D space
 */
public final class Point3D {

	// static fields
	/**
	 * Point3D(0, 0, 0)
	 */
	public static Point3D ZERO = new Point3D(0, 0, 0);

	// private fields
	final Coordinate x;
	final Coordinate y;
	final Coordinate z;

	/**
	 * Point3D constructor by double values
	 * 
	 * @param dx the X value
	 * @param dy the Y value
	 * @param dz the Z value
	 */
	public Point3D(double dx, double dy, double dz) {
		x = new Coordinate(dx);
		y = new Coordinate(dy);
		z = new Coordinate(dz);
	}

	// ____________________methods_________________
	/**
	 * Moves this point by a vector (performs operation this point + vector)
	 * 
	 * @param v the vector to add
	 * @return a new Point3d that its the resulting point of the operation
	 */
	public Point3D add(Vector v) {
		return new Point3D(x.coord + v.head.x.coord, y.coord + v.head.y.coord, z.coord + v.head.z.coord);
	}

	/**
	 * Subtracts another point from this point and this produces a vector from
	 * another point to this point
	 * 
	 * @param other the point to subtract
	 * @return the vector from other point to this point
	 */
	public Vector subtract(Point3D other) {
		return new Vector(new Point3D(x.coord - other.x.coord, y.coord - other.y.coord, z.coord - other.z.coord));
	}

	/**
	 * calculates the squared distance between 2 points
	 * 
	 * @param p the target point
	 * @return the squared distance from 'this' to 'p'
	 */
	public double distanceSquared(Point3D p) {
		double xDif, yDif, zDif;
		xDif = x.coord - p.x.coord;
		yDif = y.coord - p.y.coord;
		zDif = z.coord - p.z.coord;
		return xDif * xDif + yDif * yDif + zDif * zDif;
	}

	/**
	 * calculates the distance between 2 points
	 * 
	 * @param p the target point
	 * @return the distance from 'this' to 'p'
	 */
	public double distance(Point3D p) {
		return Math.sqrt(distanceSquared(p));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Point3D))
			return false;
		Point3D other = (Point3D) obj;
		return x.equals(other.x) && y.equals(other.y) && z.equals(other.z);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	/**
	 * get the X double value
	 * 
	 * @return the X double value
	 */
	public double getX() {
		return x.coord;
	}

	/**
	 * get the Y double value
	 * 
	 * @return the Y double value
	 */
	public double getY() {
		return y.coord;
	}

	/**
	 * get the Z double value
	 * 
	 * @return the Z double value
	 */
	public double getZ() {
		return z.coord;
	}
}
