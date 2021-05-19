/**
 * 
 */
package elements;

import primitives.*;

/**
 * represent light source from infinity
 */
public class DirectionalLight extends Light implements LightSource{

	private Vector direction;

	/**
	 * Extended DirectionalLight constructor
	 * @param intensity the intensity of the light
	 * @param direction the direction vector of the light
	 */
	public DirectionalLight(Color intensity, Vector direction) {
		super(intensity);
		this.direction = direction.normalize();
	}

	@Override
	public Color getIntensity(Point3D p) {
		return intensity;
	}

	@Override
	public Vector getL(Point3D p) {
		return direction;
	}

}
