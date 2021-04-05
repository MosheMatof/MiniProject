package geometries;

import primitives.*;
import static primitives.Util.*;

import java.util.List;
/**
 * represents an infinite tube in a 3d space
 */
public class Tube implements Geometry{

	protected Ray axis;
	protected double radius;
	
	/**
	 * Tube constructor by ray and radius
	 * @param axis a ray represents the direction of the tube
	 * @param radius the radius length of the Tube
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

		Point3D o = p0.add(u.scale(t));
		return point.subtract(o).normalize();	
	}
	
	@Override
	public String toString() {
		return "axis: " + axis.toString() + ", radius: " + radius;
	}

	/**
	 * get the ray represents the direction of the tube
	 * @return the axis of the Tube
	 */
	public Ray getAxis() {
		return axis;
	}

	/**
	 * get the radius length of the Tube
	 * @return the Radius of the Tube
	 */
	public double getRadius() {
		return radius;
	}

	@Override
	public List<Point3D> findIntersections(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}
}
