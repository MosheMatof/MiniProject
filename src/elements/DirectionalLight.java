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
	 * DirectionalLight constructor
	 * @param intensity the intensity of the light
	 */
	protected DirectionalLight(Color intensity) {
		super(intensity);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Extended DirectionalLight constructor
	 * @param intensity the intensity of the light
	 * @param direction the direction vector of the light
	 */
	public DirectionalLight(Color intensity, Vector direction) {
		super(intensity);
		this.direction = direction;
	}

	@Override
	public Color getIntensity(Point3D p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getL(Point3D p) {
		return direction;
	}

}
