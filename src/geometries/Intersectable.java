/**
 * 
 */
package geometries;

import java.util.List;
import java.util.stream.Collectors;
import primitives.Point3D;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

/**
 * the interface provide the function findIntersections for any object in space, used to find intersections between objects and rays  
 */
public interface Intersectable {
	/**
	 *represent an intersection point between a ray
	 *and some geometry object
	 */
	public static class GeoPoint{
		
		/**
		 * the geometry of the GeoPoint
		 */
		public Geometry geometry;
		/**
		 * the point of the GeoPoint
		 */
	    public Point3D point;
	    
		/**
		 * GeoPoint Constructor
		 * @param geometry the geometry object
		 * @param point the intersection point
		 */
		public GeoPoint(Geometry geometry, Point3D point) {
			this.geometry = geometry;
			this.point = point;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof GeoPoint))
				return false;
			GeoPoint other = (GeoPoint)obj;
			return this.point.equals(other.point) && this.geometry.equals(other.geometry);
		}
		
		
		/**
		 * get the normal of the geometry at this point
		 * @return the normal of the geometry at this point
		 */
		public Vector getNormal() {
			return geometry.getNormal(point);
		}
		
		@Override
		public String toString() {
			return point.toString() + geometry.toString();
		}
		
	}
	/**
	 * a region in space
	 */
	public static class Boundary {

		public final double maxX, minX;
		public final double maxY, minY;
		public final double maxZ, minZ;
		public final Point3D center;
		public final double volume;
		
		/**
		 * full constructor for {@link Boundary}
		 */
		public Boundary(double maxX, double minX, double maxY, double minY, double maxZ, double minZ) {
			this.maxX = maxX;
			this.minX = minX;
			this.maxY = maxY;
			this.minY = minY;
			this.maxZ = maxZ;
			this.minZ = minZ;
			center = new Point3D((maxX + minX) * 0.5, (maxY + minY) * 0.5, (maxZ + minZ) * 0.5);
			volume = (maxX - minX)*(maxY - minY)*(maxZ - minZ);
		}
		/**
		 * check if the ray is intersects the boundary of intersectable
		 * @param r the ray 
		 * @param maxDist the max distance for checking the intersection
		 * @return true if there is an intersection otherwise fales
		 */
		public boolean isIntersect(Ray r, double maxDist) {
			double dx = r.getDir().getX();
			double dy = r.getDir().getY();
			double dz = r.getDir().getZ();
			double x = r.getOrigin().getX();
			double y = r.getOrigin().getY();
			double z = r.getOrigin().getZ();
			
			//find the interval that the ray is between minX and maxX
			double t1x = dx != 0? (minX - x) / dx: Double.POSITIVE_INFINITY * (minX - x);
			double t2x = dx != 0? (maxX - x) / dx: Double.POSITIVE_INFINITY * (maxX - x);
			//if t1x > t2x swap t1x and t2x
			if (t1x > t2x) {double temp = t1x; t1x = t2x; t2x = temp;}
			
			//find the interval that the ray is between minY and maxY
			double t1y = dy != 0? (minY - y) / dy: Double.POSITIVE_INFINITY * (minY - y);
			double t2y = dy != 0? (maxY - y) / dy: Double.POSITIVE_INFINITY * (maxY - y);
			//if t1y > t2y swap t1y and t2y
			if (t1y > t2y) {double temp = t1y; t1y = t2y; t2y = temp;}
			
			//find the interval that the ray is between minZ and maxZ
			double t1z = dz != 0? (minZ - z) / dz: Double.POSITIVE_INFINITY * (minZ - z);
			double t2z = dz != 0? (maxZ - z) / dz: Double.POSITIVE_INFINITY * (maxZ - z);
			//if t1z > t2z swap t1z and t2z
			if (t1z > t2z) {double temp = t1z; t1z = t2z; t2z = temp;}
			
			//find the intersection between all the intervals
			//max(t1x, t1y, t1z);
			double tS = t1x > t1y? t1y > t1z? t1y : t1z : t1x > t1z? t1x: t1z; 
			//min(t2x, t2y, t2z)
			double tE = t2x < t2y? t2x < t2z? t2x : t2z : t2y < t2z? t2y: t2z; 
			
			//tE < 0 means that the ray start after the boundary
			if(Util.alignZero(tE) <= 0 || tS > maxDist) 
				return false;
			return  Util.alignZero(tE - tS) > 0;
		}

		/**
		 * calculates the length of the boundary along the x axis
		 * @return the length of the boundary along the x axis
		 */
		public double lenX() {
			return maxX - minX;
		}
		/**
		 * calculates the length of the boundary along the y axis
		 * @return the length of the boundary along the y axis
		 */
		public double lenY() {
			return maxY - minY;
		}
		/**
		 * calculates the length of the boundary along the z axis
		 * @return the length of the boundary along the z axis
		 */
		public double lenZ() {
			return maxZ - minZ;
		}
	}
	
	/**
	 * computes the intersection points between this object and the ray
	 * @param ray the intersect ray
	 * @return list of the intersection points
	 */
	default List<Point3D> findIntersections(Ray ray){
		var geoList = findGeoIntersections(ray, Double.POSITIVE_INFINITY);
	    return geoList == null ? null
	                           : geoList.stream().map(gp -> gp.point).collect(Collectors.toList());
	}
	
	/**
	 * create a list of {@link GeoPoint} of the intersection points up to the distance 'maxDist'
	 * @param ray the ray to find it's intersection points
	 * @param maxDist the max distance to look for intersections
	 * @return a list of {@link GeoPoint} of the intersection points
	 */
	List<GeoPoint> findGeoIntersections(Ray ray, double maxDist);
	
	/**
	 * getter for the boundary of geometry
	 * @return the boundary
	 */
	public Boundary getBoundary();
	
	/**
	 * check if the boundary of the Intersectable is infinite
	 * @return if the boundary of the Intersectable is infinite true otherwise false
	 */
	public boolean isInfinite();

}
