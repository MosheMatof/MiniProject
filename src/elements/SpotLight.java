/**
 * 
 */
package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * represent light source with a wide range of light
 */
public class SpotLight extends PointLight implements LightSource {

	private Vector direction;
	/**
	 * PointLight constructor
	 * @param intensity the intensity of the light
	 */
	public SpotLight(Color intensity) {
		super(intensity);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Extended PointLight constructor
	 * @param intensity the intensity of the light
	 * @param position the position point of the light
	 * @param direction the direction vector of the light
	 */
	public SpotLight(Color intensity, Point3D position, Vector direction) {
		super(intensity,position);
		this.direction = direction;
	}

	@Override
	public Color getIntensity(Point3D p) {
		double dp = Math.max(0, direction.dotProduct(getL(p)));
		return super.getIntensity().scale(dp);
	}
}
