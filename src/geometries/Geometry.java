package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * the interface provide the getNormal function for any geometries, used for reflection proposes.
 */
interface Geometry extends Intersectable {

	/**
	 * get the orthogonal vector in length 1 from a point on a geometry object
	 * @param point point to produce the normal at
	 * @return the normal in a specific point
	 */
	public Vector getNormal(Point3D point);

}
