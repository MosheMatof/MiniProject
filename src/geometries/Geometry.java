package geometries;

import primitives.*;

/**
 * the abstract class provide the getNormal function for any geometries, used for reflection proposes.
 */
abstract class Geometry implements Intersectable {

	protected Color emission = Color.BLACK;
	
	/**
	 * get the orthogonal vector in length 1 from a point on a geometry object
	 * @param point point to produce the normal at
	 * @return the normal in a specific point
	 */
	abstract public Vector getNormal(Point3D point);
	/**
	 * get the emission
	 * @return the emission
	 */
	public Color getEmission() {
		return emission;
	}

	/**
	 * set the emission
	 * @param emission the new emission
	 * @return it self
	 */
	Geometry setEmission(Color emission) {
		this.emission = emission;
		return this;
	}
}
