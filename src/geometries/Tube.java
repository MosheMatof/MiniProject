package geometries;

import primitives.*;
import static primitives.Util.*;
/**
 * represents a tube in the space
 */
public class Tube implements Geometry{

	protected Ray axis;
	protected double radius;
	
	/**
	 * 
	 * @param axis the axis of the Tube
	 * @param radius the radius of the Tube
	 */
	public Tube(Ray axis, double radius) {
		this.axis = axis;
		this.radius = radius;
	}

	@Override
	public Vector getNormal(Point3D point) {
		Point3D p0 = axis.getOrigin();
		Vector u = point.subtract(p0);
		double t = axis.getDir().dotProduct(u);
		if (isZero(t))
			return point.subtract(p0).normalize();

		Point3D o =p0.add(u.scale(t));
		return point.subtract(o).normalize();	
	}
	
	@Override
	public String toString() {
		return "axis: " + axis.toString() + ", radius: " + radius;
	}

	/**
	 * 
	 * @return the axis of the Tube
	 */
	public Ray getAxis() {
		return axis;
	}

	/**
	 * 
	 * @return the Radius of the Tube
	 */
	public double getRadius() {
		return radius;
	}
}
