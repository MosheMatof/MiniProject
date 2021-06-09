package elements;

import java.util.List;

import primitives.*;
import renderer.BlackBoard;

/**
 * represent light source with a narrow range of light
 */
public class PointLight extends Light implements LightSource {

	protected Point3D position;
	protected double radius = 0;
	private double kC = 1;
	private double kL = 0;
	private double kQ = 0;

	protected BlackBoard randomBB = null;
	protected BlackBoard sampleBB = null;

	/**
	 * Extended PointLight constructor
	 * 
	 * @param intensity the intensity of the light
	 * @param position  the position point of the light
	 */
	public PointLight(Color intensity, Point3D position) {
		super(intensity);
		this.position = position;
	}

	@Override
	public Color getIntensity(Point3D p) {
		double dSqr = p.distanceSquared(position);
		double denom = kC + kL * Math.sqrt(dSqr) + kQ * dSqr;
		return intensity.reduce(denom);
	}

	@Override
	public Vector getL(Point3D p) {
		return p.subtract(position).normalize();
	}

	@Override
	public List<Ray> getSampleBeam(Point3D p) {
		Vector l = getL(p);
		Ray ray = new Ray(position, l);
		if (sampleBB == null)
			sampleBB = BlackBoard.sampleCircle(radius);

		Vector vRight = l.getOrthogonal().normalize();
		Vector vUp = l.crossProduct(vRight).normalize();
		return ray.createBeamThrough(sampleBB, vUp, vRight, p);
	}

	@Override
	public List<Ray> getRandomBeam(Point3D p, int kSS) {
		Vector l = getL(p);
		Ray ray = new Ray(position, l);
		randomBB = BlackBoard.circleRandom(kSS, radius);
		Vector vRight = l.getOrthogonal().normalize();
		Vector vUp = l.crossProduct(vRight).normalize();
		return ray.createBeamThrough(randomBB, vUp, vRight, p);
	}

	/**
	 * set the kC
	 * 
	 * @param kC the kC to set
	 * @return instance of this PointLight
	 */
	public PointLight setKc(double kC) {
		this.kC = kC;
		return this;
	}

	/**
	 * set the kL
	 * 
	 * @param kL the kL to set
	 * @return instance of this PointLight
	 */
	public PointLight setKl(double kL) {
		this.kL = kL;
		return this;
	}

	/**
	 * set the kQ
	 * 
	 * @param kQ the kQ to set
	 * @return instance of this pointLight
	 */
	public PointLight setKq(double kQ) {
		this.kQ = kQ;
		return this;
	}

	@Override
	public double getRadius() {
		return radius;
	}
	
	/**
	 * Set radius for soft shadow
	 * @param radius value
	 * @return Point light itself
	 */
	public PointLight setRadius(double radius) {
		this.radius = radius;
		return this;
	}

	@Override
	public double getDistance(Point3D point) {
		return position.distance(point);
	}

}
