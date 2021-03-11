package geometries;


import primitives.Point3D;
import primitives.Vector;

/*
 * represents a plane in a space
 */
public class Plane implements Geometry{

	private Point3D pivot;
	private Vector normal;
	
	/**
	 * 
	 * @param pivot a point on the plane
	 * @param normal the normal of the plane
	 */
	public Plane(Point3D pivot, Vector normal) {
		this.pivot = pivot;
		this.normal = normal.normalize();	
	}
	
	public Plane(Point3D p1, Point3D p2, Point3D p3) {
		Vector v1 = p1.subtract(p2);
		Vector v2 = p3.subtract(p2);
		
		try { 
			this.normal = v1.crossProduct(v2).normalized();
		} catch (IllegalArgumentException e) { // if the 3 point on the same line...
			throw new IllegalArgumentException("the 3 points cannot be on the same line");
		}	
		this.pivot = p1;
	}

	@Override
	public Vector getNormal(Point3D point) {
		return normal;
	}
	/**
	 * 
	 * @return the normal of the plane
	 */
	public Vector getNormal() {
		return normal;
	}
	@Override
	public String toString() {
		return "pivot: " + pivot.toString() + ", normal: " + normal;
	}
	/**
	 * 
	 * @return a point on the plane
	 */
	public Point3D getPivot() {
		return pivot;
	}
	 
}
