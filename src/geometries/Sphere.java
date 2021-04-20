package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

/**
 * represents sphere by point and radius
 */
public class Sphere implements Geometry{

	protected Point3D center;
	protected double radius;
	
	/**
	 * Sphere constructor by point and radius
	 * @param center the center of the sphere
	 * @param radius the radius of the sphere
	 */
	public Sphere(Point3D center, double radius) {
		this.radius = radius;
		this.center = center;	
	}
	@Override
	public Vector getNormal(Point3D point) {
		return point.subtract(center).normalize();
	}
	/**
	 * get the point in the center of the sphere
	 * @return the center point of the sphere
	 */
	public Point3D getCenter() {
		return center;
	}
	/**
	 * get the radius length of the sphere
	 * @return the radius of the sphere
	 */
	public double getRadius() {
		return radius;
	}
	@Override
	public List<Point3D> findIntersections(Ray ray) {
		//if the origin point of the ray is in the center
		if (ray.getOrigin().equals(center)) {
			return List.of(ray.getPoint(radius));
		}
		
		//vector from the origin point of the ray to the center  
		Vector vectorToCenter = center.subtract(ray.getOrigin());
		
		//the length from the origin point of the ray to the middle point between the intersection of the line of the ray 
		double rayToMidSphre = vectorToCenter.dotProduct(ray.getDir());
		
		//the length Squared between the center and the line of the ray
		double centerToRaySqr = vectorToCenter.lengthSquared() - rayToMidSphre*rayToMidSphre;
		
		//if the length between the center and the line of the ray is bigger
		if (centerToRaySqr >= radius*radius) {
			return null;
		}
		
		//the half length between the intersection of the line of the ray
		double halfLenBetweenPoints = Math.sqrt(radius*radius - centerToRaySqr);
		
		// the distance between the origin of the ray and the first intersection point 
		double t1 = Util.alignZero(rayToMidSphre - halfLenBetweenPoints);
		Point3D p1 = null;
		
		//if the distance is negative the intersection doesen't exist 
		if (t1 > 0) {
			p1 = ray.getPoint(t1);
		}
		// the distance between the origin of the ray and the second intersection point
		double t2 = Util.alignZero(rayToMidSphre + halfLenBetweenPoints);
		Point3D p2 = null;
		
		//if the distance is negative the intersection doesen't exist
		if (t2 > 0) {
			p2 = ray.getPoint(t2);
		}
		
		//return a list of the intersection points
		if (p1 != null ) {
			if (p2 != null) {
				return List.of(p1,p2);
			}
			else {			
				return List.of(p1);
			}
		}
		else if (p2 != null) {
			return List.of(p2);
		}	
		return null;
	}
	
}
