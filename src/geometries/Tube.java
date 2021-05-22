package geometries;

import primitives.*;
import static primitives.Util.*;

import java.util.List;

import geometries.Intersectable.GeoPoint;
/**
 * represents an infinite tube in a 3d space
 */
public class Tube extends Geometry{

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
	public List<GeoPoint> findGeoIntersections(Ray ray, double maxDist) {
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
			Vector dP;
			//if the ray start at the O point
			if (ray.getOrigin().equals(axis.getOrigin())) {
				double A = v1.length();
				double t = radius/A;
				return t <= maxDist ? List.of(new GeoPoint(this, ray.getPoint(t))) : null;
			}
			//if the ray start at the axis of the tube
			else {
				dP = ray.getOrigin().subtract(axis.getOrigin());
				Vector nDp = dP.normalized();
				if (nDp.equals(axis.getDir()) || nDp.equals(axis.getDir().scale(-1))) {
					double A = v1.length();
					double t = radius/A;
					return t <= maxDist ? List.of(new GeoPoint(this, ray.getPoint(t))) : null;
				}		
			}
			
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
			double a = v1.lengthSquared();
			double b = v1.dotProduct(v2) * 2;
			double c = v2.lengthSquared() - radius*radius;
			
			//if the discriminant <= 0 - no intersections points (one solution considered as no intersections - the ray is tangents)
			double dis = b*b - 4*a*c;
			if (dis <= 0) { 
				return null;
			}
			
			double dissqrt = Math.sqrt(dis);
			double t1 = Util.alignZero((-b + dissqrt)/(2*a)); 
			double t2 = Util.alignZero((-b - dissqrt)/(2*a));

			if (t2 <= 0 || t1 > maxDist) {
				return null;
			}
			
			Point3D p2 = ray.getPoint(t2);
			
			if(t1 <= 0) {
				return t2 > 0 ? List.of(new GeoPoint(this, p2)) : null;
			}
			
			if(t2 >= maxDist) {
				return t1 < maxDist ? List.of(new GeoPoint(this, ray.getPoint(t1))) : null;
			}
			return List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, p2));
		}
		catch (Exception e) {
			return null;
		}
	}
}
