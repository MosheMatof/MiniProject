/**
 * 
 */
package geometries;

import java.util.List;
import java.util.stream.Collectors;

import primitives.Point3D;
import primitives.Ray;
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
		
		@Override
		public String toString() {
			return point.toString() + geometry.toString();
		}
		/**
		 * get the normal of the geometry at this point
		 * @return the normal of the geometry at this point
		 */
		public Vector getNormal() {
			return geometry.getNormal(point);
		}
	}
	
	/**
	 * computes the intersection points between this object and the ray
	 * @param ray the intersect ray
	 * @return list of the intersection points
	 */
	default List<Point3D> findIntersections(Ray ray){
		var geoList = findGeoIntersections(ray);
	    return geoList == null ? null
	                           : geoList.stream().map(gp -> gp.point).collect(Collectors.toList());

	}
	
	/**
	 * create a list of {@link GeoPoint} of the intersection points
	 * @param ray the ray to find it's intersection points
	 * @return a list of {@link GeoPoint} of the intersection points
	 */
	List<GeoPoint> findGeoIntersections(Ray ray);
	
}
