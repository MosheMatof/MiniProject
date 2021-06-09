/**
 * 
 */
package elements;

import java.util.List;

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

	@Override
	public double getDistance(Point3D point) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public double getRadius() {
		return 0;
	}

	@Override
	public List<Ray> getSampleBeam(Point3D p) {
		return List.of(new Ray(p, direction));
	}

	@Override
	public List<Ray> getRandomBeam(Point3D p, int kSS) {
		return List.of(new Ray(p, direction));
	}
	
}
