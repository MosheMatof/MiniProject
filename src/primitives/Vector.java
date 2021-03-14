package primitives;

/**
 * represents a vector in 3d space
 *
 */
public final class Vector {
	//___________private fields__________________
	Point3D head;
	
	//___________ctors___________________________
	/**
	 * constructs a new vector with the coordinates (cx, cy, cz)
	 * @throws IllegalArgumentException - if cx = cy = cz = 0
	 */
	public Vector(Coordinate cx, Coordinate cy, Coordinate cz){
		head = new Point3D(cx, cy, cz);
		if(head.equals(Point3D.ZERO))
			throw new IllegalArgumentException("the zero vector is not alow");
	}
	/**
	 * constructs a new vector with the coordinates (dx, dy, dz)
	 * @throws IllegalArgumentException if dx = dy = dz = 0
	 */
	public Vector(double dx, double dy, double dz) {
		head = new Point3D(dx, dy, dz);
		if(head.equals(Point3D.ZERO))
			throw new IllegalArgumentException("the zero vector is not alow");
	}
	/**
	 * constructs a new vector with the coordinates of p
	 * @throws IllegalArgumentException - if @ = {@link Point3D#ZERO}
	 */
	public Vector(Point3D p) {
		if(p.equals(Point3D.ZERO))
			throw new IllegalArgumentException("the zero vector is not alow");
		head = new Point3D(p.x,  p.y,  p.z);
	}
	
	//____________methods________________________
	public Point3D getHead() {
		return head;
	}
	
	/**
	 * @param v the vector to add to this
	 * @return a new vector that its the sum of 'this' and 'v'
	 * @throws IllegalArgumentException if this addition will cause a zero vector
	 */
	public Vector add(Vector v) {
		try {
			return new Vector(head.add(v));
		}
		catch (IllegalArgumentException e) {//if this operation causing a creation of zero vector
			throw new IllegalArgumentException("if 'this' = -1 * 'v' then 'this' + 'v' will cause a criation of a zero vector");
		}
	}
	
	/**
	 * @param v the vector to subtract from this vector
	 * @return a new vector that its 'this' - 'v'
	 * @throws IllegalArgumentException if this subtraction will cause a zero vector
	 */
	public Vector subtract(Vector v) {
		try {
			return add(v.scale(-1));//v - u = v + -1*u
		}
		catch (IllegalArgumentException e) {//if this operation causing a creation of zero vector
			throw new IllegalArgumentException("if 'this' = 'v' then 'this' - 'v' will cause a criation of a zero vector");
		}
	}
	
	/**
	 * @return a new vector equal to this multiply by 'scalar'
	 */
	public Vector scale(double scalar){
		if(scalar == 0)
			throw new IllegalArgumentException("scalar = 0 will cause a criation of a zero vector");
		return new Vector(scalar * head.x.coord, scalar * head.y.coord, scalar * head.z.coord);
	}
	
	/**
	 * @return the dot product of this and v
	 */
	public double dotProduct(Vector v) {
		return v.head.x.coord * this.head.x.coord + v.head.y.coord * this.head.y.coord + v.head.z.coord * this.head.z.coord;
	}
	
	/**
	 * do a cross product of this and v 
	 * @param v the vector at the right side of the cross 
	 * @return the crossProduct of this and v
	 * @throws IllegalArgumentException if the product of this cross product is the zero vector 
	 */
	public Vector crossProduct(Vector v) {
		//V x U = (VyUz - VzUy, VzUx, VzUx - VxUz, VxUy - VyUx)
		double vx = head.x.coord, vy = head.y.coord, vz = head.z.coord;
		double ux = v.head.x.coord, uy = v.head.y.coord, uz = v.head.z.coord;
		try {
			return new Vector(vy*uz - vz*uy, vz*ux - vx*uz, vx*uy - vy*ux);
		}
		catch (IllegalArgumentException e) {//if the angel between this and v is PI*k then it will cause acriation of zero vector
			throw new IllegalArgumentException("if the angel between this and v is PI*k then it will cause acriation of zero vector");
		}
	}
	
	/**
	 * @return the Squared length of the vector
	 */
	public double lengthSquared() {
		return this.head.distanceSquerd(Point3D.ZERO);
	}
	/**
	 * @return the length of the vector
	 */
	public double length() {
		return Math.sqrt(lengthSquared());
	}
	/**
	 * normalize this vector 
	 * @return this vector
	 */
	public Vector normalize() {
		this.head = normalized().head;
		return this;
	}
	/**
	 * @return a new normalized vector
	 */
	public Vector normalized() {
		return scale(1 / this.length());
	}
	
	@Override
	public boolean equals(Object obj) {
	if (this == obj) return true;
	if (obj == null) return false;
	if (!(obj instanceof Vector)) return false;
	Vector other = (Vector)obj;
	return head.equals(other.head);
	}
	
	@Override
	public String toString() {
		return "-" + head.toString() + "->";
	}
}
