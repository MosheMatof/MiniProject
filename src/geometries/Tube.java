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
			//if the ray is parallel to the tube
			if (ray.getDir().equals(axis.getDir()) || ray.getDir().equals(axis.getDir().scale(-1))) {
				return null;
			}
			double scalar1 = axis.getDir().dotProduct(ray.getDir());
			Vector v1;
			if (Util.isZero(scalar1)) {
				v1 = ray.getDir();
			}
			else {
				v1 = ray.getDir().subtract(axis.getDir().scale(scalar1));	
			}			
			//if the ray start at the O point
			if (ray.getOrigin().equals(axis.getOrigin())) {
				double A = v1.length();
				double t = radius/A;
				return List.of(ray.getPoint(t));
			}
			
			Vector dP = ray.getOrigin().subtract(axis.getOrigin());
			double scalar2 = dP.dotProduct(axis.getDir());
			Vector v2;
			
			//if the vector from ray's origin to the origin of the tube's ray is vertical to the tube
			if(Util.isZero(scalar2)) {
				v2 = dP;
			}
			else {
				v2 = dP.subtract(axis.getDir().scale(dP.dotProduct(axis.getDir())));
			}
			
			// values of the Quadratic equation of t (At^ + Bt + C) - t is the location of the intersections point on the ray
			double A = v1.lengthSquared();
			double B = v1.dotProduct(v2) * 2;
			double C = v2.lengthSquared() - radius*radius;
			
			//if the discriminant <= 0 - no intersections points
			double dis = B*B - 4*A*C;
			if (dis <= 0) { 
				return null;
			}
			
			double dissqrt = Math.sqrt(dis);
			double t1 = Util.alignZero((-B + dissqrt)/(2*A));
			double t2 = Util.alignZero((-B - dissqrt)/(2*A));
			
			Point3D p1 = ray.getPoint(t1);
			Point3D p2 = ray.getPoint(t2);
			
			//takes only positive solutions of t
			if(t1 > 0) {
				if(t2 > 0) {
					return List.of(p1,p2);
				}
				return List.of(p1);
			}
			if(t2 > 0) {
				return List.of(p2);
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
