package geometries;

import primitives.Point3D;
import primitives.Vector;

interface Geometry {

	/**
	 * 
	 * @param point3d
	 * @return the normal in a specific point
	 */
	public Vector getNormal(Point3D point3d);

}
