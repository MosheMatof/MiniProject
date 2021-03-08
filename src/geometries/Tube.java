package geometries;

import primitives.Ray;
import primitives.Vector;

/**
 * represents a tube in the space
 */
public class Tube implements Geometry{

	Ray axis;
	double radius;
	
	public Tube(Ray axis, double radius) {
		this.axis = axis;
		this.radius = radius;
	}

	@Override
	public Vector getNormal() {
		return null;
	}
	
	@Override
	public String toString() {
		return "axis: " + axis.toString() + ", radius: " + radius;
	}
}
