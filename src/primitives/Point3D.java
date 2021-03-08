package primitives;


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
	/*
	 * returns a new Point3d that its the sum of 'this' + 'v'
	 */
	public Point3D add(Vector v) {
		return null;
	}
	
	public Vector subsruct(Point3D v) {
		return null;
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
}
