/**
 * 
 */
package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;

/**
 * the interface provide the function findIntersections for any object in space, used to find intersections between objects and rays  
 */
public interface Intersectable {
	/**
	 *represent an intersection point between a ray
	 *and some geometry object
	 */
	public static class GeoPoint{
		
		public Geometry geometry;
	    public Point3D point;
	    
		/**
		 * GeoPoint Constructor
		 * @param geometry the geometry object
		 * @param point the intersection point
		 */
		public GeoPoint(Geometry geometry, Point3D point) {
			super();
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
		
	}
	/**
	 * computes the intersection points between this object and the ray
	 * @param ray the intersect ray
	 * @return list of the intersection points
	 */
	List<Point3D> findIntersections(Ray ray);
	
	List<GeoPoint> findGeoIntersections(Ray ray);
}
