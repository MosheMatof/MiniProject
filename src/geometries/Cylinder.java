package geometries;

import java.util.LinkedList;
import java.util.List;

import primitives.*;

/**
 * 
 * represents a Cylinder in space
 *
 */
public class Cylinder extends Tube {

	private double height;

	/**
	 * Cylinder constructor
	 * 
	 * @param axis   a ray that represents the axis of the Cylinder
	 * @param radius the radius length of the Cylinder
	 * @param height the height of the Cylinder
	 */
	public Cylinder(Ray axis, double radius, double height) {
		super(axis, radius);
		this.height = height;
	}

	@Override
	public Vector getNormal(Point3D point) {
		// if the point is the same point of the cylinder axis
		if (point.equals(this.getAxis().getOrigin())) {
			return axis.getDir().scale(-1);
		}

		Vector vector = point.subtract(axis.getOrigin());
		double t = axis.getDir().dotProduct(vector);
		// if t == 0 then the point is on the lower surface of the cylinder
		if (Util.isZero(t)) {
			return axis.getDir().scale(-1);
		}
		// if t == height then the point is on the lower surface of the cylinder
		if (Util.isZero(t - height)) {
			return axis.getDir();
		}
		// the point is on the sides of the cylinder
		Point3D o = axis.getOrigin().add(vector.scale(t));
		return point.subtract(o).normalize();
	}

//	@Override
//	public String toString() {
//		return super.toString() + ", height: " + height;
//	}
//
//	@Override
//	public List<Point3D> findIntersections(Ray ray) {
//		Point3D topPoint = this.axis.getPoint(height);
//		Point3D bottomPoint = this.axis.getOrigin();
//		Vector axisVec = this.axis.getDir();
//		List<Point3D> validPoints = new LinkedList<Point3D>();
//
//		// check the intersections with the tube
//		List<Point3D> tubePoints = super.findIntersections(ray);
//		if (tubePoints != null) {
//			for (Point3D p : tubePoints) {
//				double s1 = Util.alignZero(axisVec.dotProduct(p.subtract(topPoint)));
//				double s2 = Util.alignZero(axisVec.dotProduct(p.subtract(bottomPoint)));
//				if (s1 < 0 && s2 > 0)
//					validPoints.add(p);
//			}
//			if (validPoints.size() == 2) {
//				return validPoints;
//			}
//		}
//
//		// check the intersections with the caps
//		double sqrRadius = this.radius * this.radius;
//		
//		// the upper cap
//		Plane topPlane = new Plane(topPoint, axisVec);
//		List<Point3D> planePoint = topPlane.findIntersections(ray);
//		if (planePoint != null) {
//			Point3D p = planePoint.get(0);
//			double s = Util.alignZero(p.subtract(topPoint).lengthSquared());
//			if (s < sqrRadius) {
//				validPoints.add(p);
//				if (validPoints.size() == 2)
//					return validPoints;
//			}
//		}
//		// the lower cap
//		Plane bottomPlane = new Plane(bottomPoint, axisVec);
//		planePoint = bottomPlane.findIntersections(ray);
//		if (planePoint != null) {
//			Point3D p = planePoint.get(0);
//			double s = Util.alignZero(p.subtract(bottomPoint).lengthSquared());
//			if (s < sqrRadius) {
//				validPoints.add(p);
//				if (validPoints.size() == 2)
//					return validPoints;
//			}
//		}
//		if (validPoints.size() > 0) {
//			return validPoints;
//		}
//		return null;
//	}
//
	@Override
	public List<GeoPoint> findGeoIntersections(Ray ray) {
		Point3D topPoint = this.axis.getPoint(height);
		Point3D bottomPoint = this.axis.getOrigin();
		Vector axisVec = this.axis.getDir();
		List<GeoPoint> validPoints = new LinkedList<GeoPoint>();

		// check the intersections with the tube
		List<GeoPoint> tubePoints = super.findGeoIntersections(ray);
		if (tubePoints != null) {
			for (GeoPoint gp : tubePoints) {
				double s1 = Util.alignZero(axisVec.dotProduct(gp.point.subtract(topPoint)));
				double s2 = Util.alignZero(axisVec.dotProduct(gp.point.subtract(bottomPoint)));
				if (s1 < 0 && s2 > 0)
					gp.geometry = this;
					validPoints.add(gp);
			}
			if (validPoints.size() == 2) {
				return validPoints;
			}
		}

		// check the intersections with the caps
		double sqrRadius = this.radius * this.radius;
		
		// the upper cap
		Plane topPlane = new Plane(topPoint, axisVec);
		List<GeoPoint> planePoint = topPlane.findGeoIntersections(ray);
		if (planePoint != null) {
			GeoPoint p = planePoint.get(0);
			double s = Util.alignZero(p.point.subtract(topPoint).lengthSquared());
			if (s < sqrRadius) {
				p.geometry = this;
				validPoints.add(p);
				if (validPoints.size() == 2)
					return validPoints;
			}
		}
		// the lower cap
		Plane bottomPlane = new Plane(bottomPoint, axisVec);
		planePoint = bottomPlane.findGeoIntersections(ray);
		if (planePoint != null) {
			GeoPoint p = planePoint.get(0);
			double s = Util.alignZero(p.point.subtract(bottomPoint).lengthSquared());
			if (s < sqrRadius) {
				p.geometry = this;
				validPoints.add(p);
				if (validPoints.size() == 2)
					return validPoints;
			}
		}
		if (validPoints.size() > 0) {
			return validPoints;
		}
		return null;
	}
	
	/**
	 * get the height of the Cylinder
	 * 
	 * @return the height of the Cylinder
	 */
	public double getHeight() {
		return height;
	}

}
