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

	/**
	 * Extended PointLight constructor
	 * @param intensity the intensity of the light      
	 * @param position the position point of the light  
	 * @param kC
	 * @param kL
	 * @param kQ
	 * @param direction
	 */
	public SpotLight(Color intensity, Point3D position, double kC , double kL, double kQ, Vector direction) {
		super(intensity, position, kC , kL, kQ);
		this.direction = direction;
	}

	@Override
	public Color getIntensity(Point3D p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getL(Point3D p) {
		// TODO Auto-generated method stub
		return null;
	}

}
