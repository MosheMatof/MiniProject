package primitives;

import org.graalvm.compiler.core.common.type.ArithmeticOpTable.BinaryOp.Add;

import sun.net.www.content.text.plain;

public final class Vector {
	//___________private fields__________________
	private Point3D head;
	
	//___________ctors___________________________
	/*
	 * set head to be a new point3d ('cx', 'cy', 'cz')
	 */
	public Vector(Coordinate cx, Coordinate cy, Coordinate cz){
		head = new Point3D(cx, cy, cz);
		if(head.equals(Point3D.ZERO))
			throw new IllegalArgumentException("the zero vector is not alow");
	}
	/*
	 * set head to be a new point3d ('dx', 'dy', 'dz')
	 */
	public Vector(double dx, double dy, double dz) {
		head = new Point3D(dx, dy, dz);
		if(head.equals(Point3D.ZERO))
			throw new IllegalArgumentException("the zero vector is not alow");
	}
	/*
	 * set head to be 'p'
	 */
	public Vector(Point3D p) {
		if(p.equals(Point3D.ZERO))
			throw new IllegalArgumentException("the zero vector is not alow");
		head = new Point3D(p.x,  p.y,  p.z);
	}
	
	//____________methods________________________
	
	/*
	 * Returns a new vector that its the sum of 'this' and 'v'
	 */
	public Vector add(Vector v) {
		try {
			return new Vector(head.add(v));
		}
		catch (IllegalArgumentException e) {//if this operation causing a creation of zero vector
			throw new IllegalArgumentException("if 'this' = -1 * 'v' then 'this' + 'v' will cause a criation of a zero vector");
		}
	}
	
	/*
	 * Returns a new vector that its 'this' - 'v'
	 */
	public Vector subtruct(Vector v) {
		try {
			return add(v.scale(-1));//v - u = v + -1*u
		}
		catch (IllegalArgumentException e) {//if this operation causing a creation of zero vector
			throw new IllegalArgumentException("if 'this' = 'v' then 'this' - 'v' will cause a criation of a zero vector");
		}
	}
	
	/*
	 * returns a new vector equal to this multiply by 'scalar'
	 */
	public Vector scale(double scalar){
		if(scalar == 0)
			throw new IllegalArgumentException("scalar = 0 will cause a criation of a zero vector");
		return new Vector(scalar * head.x.coord, scalar * head.y.coord, scalar * head.z.coord);
	}
	
	/*
	 * returns the dot product of this and v
	 */
	public double dotProdact(Vector v) {
		return v.head.x.coord * this.head.x.coord + v.head.y.coord * this.head.y.coord + v.head.z.coord * this.head.z.coord;
	}
	
	/*
	 * returns the crossProduct of this and v
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
}
