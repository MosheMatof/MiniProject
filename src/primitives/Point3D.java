package primitives;

/**
 * represents a point in a 3D space
 */
public final class Point3D {
	
	//static fields
	/**
	 * Point3D(0, 0, 0)
	 */
	public static Point3D ZERO = new Point3D(0, 0, 0);
	
	//private fields
	final Coordinate x;
	final Coordinate y;
	final Coordinate z;
	
	public Point3D(double dx, double dy, double dz)
	{
		x = new Coordinate(dx);
		y = new Coordinate(dy);
		z = new Coordinate(dz);
	}
	
	public Point3D(Coordinate cx, Coordinate cy, Coordinate cz)
	{
		x = new Coordinate(cx.coord);
		y = new Coordinate(cy.coord);
		z = new Coordinate(cz.coord);
	}
	
	//____________________methods_________________
	/**
	 * Moves this point by a vector (performs operation this point + vector)
	 * @param v the vector to add
	 * @return a new Point3d that its the resulting point of the operation
	 */
	public Point3D add(Vector v) {
		return new Point3D(x.coord + v.head.x.coord, y.coord + v.head.y.coord, z.coord + v.head.z.coord);
	}
	
	/**
	 * Subtracts another point from this point and this produces a vector from another point to this point
	 * @param other the point to subtract
	 * @return the vector from other point to this point
	 */
	public Vector subtract(Point3D other) {
		try {
			return new Vector(new Point3D(x.coord - other.x.coord, y.coord - other.y.coord, z.coord - other.z.coord));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("the subtraction retuned zero vector", e);
		}
	}
	/**
	 * calculates the squared distance between 2 points
	 * @param p the target point 
	 * @return the squared distance from 'this' to 'p' 
	 */
	public double distanceSquerd(Point3D p) {
		double xDif, yDif, zDif;
		xDif = x.coord - p.x.coord;
		yDif = y.coord - p.y.coord;
		zDif = z.coord - p.z.coord;
		double dist = xDif * xDif + yDif * yDif + zDif * zDif;
		return Util.alignZero(dist);
	}
	
	/**
	 * calculates the distance between 2 points
	 * @param p the target point 
	 * @return the distance from 'this' to 'p' 
	 */
	public double distance(Point3D p)
	{
		return Math.sqrt(distanceSquerd(p));
	}
	
	@Override
	public boolean equals(Object obj) {
	if (this == obj) return true;
	if (obj == null) return false;
	if (!(obj instanceof Point3D)) return false;
	Point3D other = (Point3D)obj;
	return x.equals(other.x) && y.equals(other.y) && z.equals(other.z);
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public double getX() {
		return x.coord;
	}

	public double getY() {
		return y.coord;
	}

	public double getZ() {
		return z.coord;
	}
}
