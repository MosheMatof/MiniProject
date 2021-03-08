package geometries;

import primitives.Ray;
import primitives.Vector;

/**
 * 
 * represents a Cylinder in space
 *
 */
public class Cylinder extends Tube{

	double height;
	
	public Cylinder(Ray axis, double radius, double height) {
		super(axis, radius);
	}
	
	@Override
	public Vector getNormal() {
		return null;
	}
	
	@Override
	public String toString() {
		return super.toString() + ", height: " + height;
	}
}
