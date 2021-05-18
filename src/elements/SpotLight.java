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
	private double kB = 1;
	/**
	 * PointLight constructor
	 * @param intensity the intensity of the light
	 */
	public SpotLight(Color intensity) {
		super(intensity);
	}

	/**
	 * Extended PointLight constructor
	 * @param intensity the intensity of the light
	 * @param position the position point of the light
	 * @param direction the direction vector of the light
	 */
	public SpotLight(Color intensity, Point3D position, Vector direction) {
		super(intensity,position);
		this.direction = direction.normalize();
	}

	@Override
	public Color getIntensity(Point3D p) {
		double dp = Math.pow(Math.max(0, direction.dotProduct(getL(p))),kB);
		return super.getIntensity().scale(dp);
	}

	/**
	 * set the broadness of the light (kB), kB < 1 -> wider light, kB > 1 -> thiner light  
	 * @param kB the kB to set
	 */
	public SpotLight setkB(double kB) {
		this.kB = kB;
		return this;
	}
	
}
