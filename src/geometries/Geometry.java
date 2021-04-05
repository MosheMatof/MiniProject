package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * the interface provide the getNormal function for any geometries, used for reflection proposes.
 */
interface Geometry extends Intersectable {

	/**
	 * get the orthogonal vector in length 1 from a point on a geometry object
	 * @param point3d
	 * @return the normal in a specific point
	 */
	public Vector getNormal(Point3D point3d);

}
