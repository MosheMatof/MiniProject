package geometries;

import java.lang.reflect.Constructor;

import primitives.Point3D;
import primitives.Vector;

/*
 * represents a plane in a space
 */
public class Plane implements Geometry{

	final Point3D pivot;
	final Vector normal;
	
	public Plane(Point3D pivot, Vector normal) {
		this.pivot = pivot;
		normal.normalize();
		this.normal = normal;	
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
	public Vector getNormal() {
		return normal;
	}
	 
}
