package geometries;

import primitives.*;

/**
 * the abstract class provide the getNormal function for any geometries, used for reflection proposes.
 */
public abstract class Geometry implements Intersectable {

	private Color emission = Color.BLACK;
	private Material material = new Material();
	
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
	public Geometry setEmission(Color emission) {
		this.emission = emission;
		return this;
	}
	
	/**
	 * get the material
	 * @return the material
	 */
	public Material getMaterial() {
		return material;
	}
	
	/**
	 * sets the material of the geometry
	 * @param material the material to set
	 * @return it self
	 */
	public Geometry setMaterial(Material material) {
		this.material = material;
		return this;
	}
}
