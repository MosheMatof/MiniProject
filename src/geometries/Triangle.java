package geometries;

import primitives.Point3D;

/**
 * represents a triangle by 3 points. inherits from polygon class.
 */
public final class Triangle extends Polygon{

	/**
	 * Triangle constructor by 3 points
	 * @param p1
	 * @param p2
	 * @param p3
	 */
	public Triangle(Point3D p1, Point3D p2, Point3D p3) {
		super(p1,p2,p3);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
