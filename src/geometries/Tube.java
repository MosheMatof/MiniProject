package geometries;

import primitives.*;
import static primitives.Util.*;

import java.awt.Point;
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

		Point3D o = p0.add(axis.getDir().scale(t));
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
		try {
			Vector v1 = ray.getDir().subtract(axis.getDir().scale(axis.getDir().dotProduct(ray.getDir())));
			Vector dP = ray.getOrigin().subtract(axis.getOrigin());
			Vector v2 = dP.subtract(v1).subtract(axis.getDir().scale(dP.dotProduct(axis.getDir())));
			
			double A = v1.lengthSquared();
			double B = v1.dotProduct(v2) * 2;
			double C = v2.lengthSquared() - radius*radius;
			
			double dis = B*B - 4*A*C;
			if (dis <= 0) { 
				return null;
			}
			double dissqr = Math.sqrt(dis);
			double t1 = (B*-1 + dissqr)/2*A;
			double t2 = (B*-1 - dissqr)/2*A;
			
			Point3D p1 = ray.getPoint(t1);
			Point3D p2 = ray.getPoint(t2);
			
			//return a list of the intersection points
			return List.of(p1,p2);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
