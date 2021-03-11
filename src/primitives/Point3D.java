package primitives;

/**
 * represents a point in a 3D space
 */
public final class Point3D {
	
	//static fields
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
	 * 
	 * @param v the vector 
	 * @return a new Point3d that its the sum of 'this' + 'v'
	 */
	public Point3D add(Vector v) {
		return new Point3D(x.coord + v.head.x.coord, y.coord + v.head.y.coord, z.coord + v.head.z.coord);
	}
	
	public Vector subtract(Point3D v) {
		return new Vector(new Point3D(x.coord - v.x.coord, y.coord - v.y.coord, z.coord - v.z.coord));
	}
	
	/**
	 * (this.x + p.x)^2 + (this.y + p.y)^2 + (this.z + p.z)^2
	 */
	public double distanceSquerd(Point3D p) {
		double x_dif, y_dif, z_dif;
		x_dif = x.coord - p.x.coord;
		y_dif = y.coord - p.y.coord;
		z_dif = z.coord - p.z.coord;
		double dist = x_dif * x_dif + y_dif * y_dif + z_dif * z_dif;
		return Util.alignZero(dist);
	}
	
	/**
	 * sqrt((this.x + p.x)^2 + (this.y + p.y)^2 + (this.z + p.z)^2)
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
}
