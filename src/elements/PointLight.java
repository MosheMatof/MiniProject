/**
 * 
 */
package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * represent light source with a narrow range of light
 */
public class PointLight extends Light implements LightSource {

	private Point3D position;
	private double kC = 1;
	private double kL = 0;
	private double kQ = 0;

	/**
	 * Extended PointLight constructor
	 * @param intensity the intensity of the light
	 * @param position the position point of the light
	 */
	public PointLight(Color intensity, Point3D position) {
		super(intensity);
		this.position = position;
	}

	@Override
	public Color getIntensity(Point3D p) {
		double dSqr = p.distanceSquared(position);
		double denom = kC + kL*Math.sqrt(dSqr) + kQ*dSqr;
		return intensity.reduce(denom);
	}

	@Override
	public Vector getL(Point3D p) {
		return p.subtract(position).normalize();
	}

	/**
	 * set the kC
	 * @param kC the kC to set
	 * @return instance of this PointLight
	 */
	public PointLight setkC(double kC) {
		this.kC = kC;
		return this;
	}

	/**
	 * set the kL
	 * @param kL the kL to set
	 * @return instance of this PointLight
	 */
	public PointLight setkL(double kL) {
		this.kL = kL;
		return this;
	}

	/**
	 * set the kQ
	 * @param kQ the kQ to set
	 * @return instance of this pointLight
	 */
	public PointLight setkQ(double kQ) {
		this.kQ = kQ;
		return this;
	}

}
