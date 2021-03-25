package primitives;

/**
 * represents a vector in 3d space by 3d point
 *
 */
public final class Vector {
	//___________private fields__________________
	Point3D head;
	
	//___________ctors___________________________
	/**
	 * vector constructor by coordinates (cx, cy, cz)
	 * @throws IllegalArgumentException - if cx = cy = cz = 0
	 */
	public Vector(Coordinate cx, Coordinate cy, Coordinate cz){
		head = new Point3D(cx, cy, cz);
		if(head.equals(Point3D.ZERO))
			throw new IllegalArgumentException("the zero vector is not alow");
	}
	/**
	 * vector constructor by double values (dx, dy, dz)
	 * @throws IllegalArgumentException if dx = dy = dz = 0
	 */
	public Vector(double dx, double dy, double dz) {
		head = new Point3D(dx, dy, dz);
		if(head.equals(Point3D.ZERO))
			throw new IllegalArgumentException("the zero vector is not alow");
	}
	/**
	 * vector constructor by 3d point
	 * @throws IllegalArgumentException if point = {@link Point3D#ZERO}
	 */
	public Vector(Point3D p) {
		if(p.equals(Point3D.ZERO))
			throw new IllegalArgumentException("the zero vector is not alow");
		head = new Point3D(p.x,  p.y,  p.z);
	}
	
	//____________methods________________________
	/**
	 * get the point represents the head of the vector
	 * @return the head of the vector
	 */
	public Point3D getHead() {
		return head;
	}
	
	/**
	 * performs an adding operation on vectors (this vector + v)
	 * @param v the vector to add to this
	 * @return a new vector that its the sum of 'this' and 'v'
	 * @throws IllegalArgumentException if this addition will cause a zero vector
	 */
	public Vector add(Vector v) {
		try {
			return new Vector(head.add(v));
		}
		catch (IllegalArgumentException e) {//if this operation causing a creation of zero vector
			throw new IllegalArgumentException("if 'this' = -1 * 'v' then 'this' + 'v' will cause a creation of a zero vector", e);
		}
	}
	
	/**
	 * performs a subtracting operation on vectors (this vector - v)
	 * @param v the vector to subtract from this vector
	 * @return a new vector that its 'this' - 'v'
	 * @throws IllegalArgumentException if this subtraction will cause a zero vector
	 */
	public Vector subtract(Vector v) {
		try {
			return head.subtract(v.head);
		}
		catch (IllegalArgumentException e) {//if this operation causing a creation of zero vector
			throw new IllegalArgumentException("if 'this' = 'v' then 'this' - 'v' will cause a criation of a zero vector");
		}
	}
	
	/**
	 * Multiplying a vector by scalar (this vector * scalar)
	 * @return a new vector equal to this multiply by 'scalar'
	 */
	public Vector scale(double scalar){
		if(scalar == 0)
			throw new IllegalArgumentException("scalar = 0 will cause a criation of a zero vector");
		return new Vector(scalar * head.x.coord, scalar * head.y.coord, scalar * head.z.coord);
	}
	
	/**
	 * performs a dot product on vectors (this vector * v)
	 * @return the dot product of this and v
	 */
	public double dotProduct(Vector v) {
		return v.head.x.coord * this.head.x.coord + v.head.y.coord * this.head.y.coord + v.head.z.coord * this.head.z.coord;
	}
	
	/**
	 * performs a cross product on vectors (this vector X v) 
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
	 * calculates the squared length of the vector
	 * @return the Squared length of the vector
	 */
	public double lengthSquared() {
		return this.head.distanceSquerd(Point3D.ZERO);
	}
	/**
	 * calculates the length of the vector
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
		double length = this.head.distance(Point3D.ZERO);
		double px = head.x.coord/length;
		double py = head.y.coord/length;
		double pz = head.z.coord/length;
		this.head = new Point3D(px, py, pz);
		
		return this;
	}
	/**
	 * Generates a new normalized vector of this vector
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
