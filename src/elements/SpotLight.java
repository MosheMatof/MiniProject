/**
 * 
 */
package elements;

import primitives.*;

/**
 * represent light source with a wide range of light
 */
public class SpotLight extends PointLight implements LightSource {

	private Vector direction;
	private double kB = 1;

	/**
	 * Extended PointLight constructor
	 * 
	 * @param intensity the intensity of the light
	 * @param position  the position point of the light
	 * @param direction the direction vector of the light
	 */
	public SpotLight(Color intensity, Point3D position, Vector direction) {
		super(intensity, position);
		this.direction = direction.normalize();
	}

	@Override
	public Color getIntensity(Point3D p) {
		double dp = Util.alignZero(direction.dotProduct(getL(p)));
		if (dp<=0)return Color.BLACK;
		if (kB != 1)
			dp = Math.pow(dp,kB);
		return super.getIntensity(p).scale(dp);
	}

	/**
	 * set the broadness of the light (kB), kB < 1 -> wider light, kB > 1 -> thiner
	 * light
	 * 
	 * @param kB the kB to set
	 */
	public SpotLight setKB(double kB) {
		this.kB = kB;
		return this;
	}

}
