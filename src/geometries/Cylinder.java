package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * 
 * represents a Cylinder in space
 *
 */
public class Cylinder extends Tube{

	private double height;
	
	/**
	 * 
	 * @param axis the axis of the Cylinder
	 * @param radius the radius of the Cylinder
	 * @param height the height of the Cylinder
	 */
	public Cylinder(Ray axis, double radius, double height) {
		super(axis, radius);
	}
	
	@Override
	public Vector getNormal(Point3D point) {
		return null;
	}
	
	@Override
	public String toString() {
		return super.toString() + ", height: " + height;
	}

	/**
	 * 
	 * @return the height of the Cylinder
	 */
	public double getHeight() {
		return height;
	}
}
