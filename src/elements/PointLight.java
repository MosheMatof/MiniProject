package elements;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import primitives.*;
import static primitives.Util.*;


/**
 * represent light source with a narrow range of light
 */
public class PointLight extends Light implements LightSource {

	private Point3D position;
	private double radius = 1;
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

	/**
	 * Extended PointLight constructor with radius
	 * @param intensity the intensity of the light
	 * @param position the position point of the light
	 * @param radius the radius of the light
	 */
	public PointLight(Color intensity, Point3D position, double radius) {
		super(intensity);
		this.position = position;
		this.radius = radius;
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
	 * get sample vectors from the center and the edge of the light source to a given point
	 * @param p the requested point
	 * @param normal the normal in the requested point
	 * @return the vector from the edge of the light source to a given point
	 */
	public List<Vector> getSampleVector(Point3D p, Vector normal) {
		if (radius > 0) {
			Vector l = p.subtract(position).normalize();
			Vector v = l.crossProduct(normal).normalize();//orthogonal to the normal
			return List.of(p.subtract(position.add(normal.scale(radius))).normalize(), 
			p.subtract(position.add(normal.scale(-radius))).normalize(),
			p.subtract(position.add(v.scale(radius))).normalize(),
			p.subtract(position.add(v.scale(-radius))).normalize(), l);
		}
		return List.of(getL(p));
	}
	/**
	 * get random vectors from all the radius of the light source, to the given point
	 * @param p the requested point
	 * @param normal the normal in the requested point
	 * @param n the amount of vectors
	 * @return the vector from the edge of the light source to a given point
	 */
	public List<Vector> getRandomVectors(Point3D p, Vector normal, int n) {
		if (radius > 0) {
			Vector l = p.subtract(position).normalize();
			Vector v = l.crossProduct(normal).normalize();//orthogonal to the normal

			List<Vector> vectors = new LinkedList<>();
			for (int i = 0; i < n; i++) {
				Point3D pos = position;
				double sn = ThreadLocalRandom.current().nextDouble(2 * radius) - radius;
				if(!isZero(sn))
					pos = pos.add(normal.scale(sn));
				double sv = ThreadLocalRandom.current().nextDouble(2 * radius) - radius;
				if(!isZero(sv))
					pos = pos.add(v.scale(sv));
				vectors.add(p.subtract(pos).normalize());
			}
			return vectors;
		}
		return List.of(getL(p));
	}
	/**
	 * set the kC
	 * @param kC the kC to set
	 * @return instance of this PointLight
	 */
	public PointLight setKc(double kC) {
		this.kC = kC;
		return this;
	}

	/**
	 * set the kL
	 * @param kL the kL to set
	 * @return instance of this PointLight
	 */
	public PointLight setKl(double kL) {
		this.kL = kL;
		return this;
	}
	/**
	 * set the kQ
	 * @param kQ the kQ to set
	 * @return instance of this pointLight
	 */
	public PointLight setKq(double kQ) {
		this.kQ = kQ;
		return this;
	}
	/**
	 * getter for the radius
	 * @return the radius of the light
	 */
	public double getRadius(){
		return radius;
	}

	@Override
	public double getDistance(Point3D point) {
		return position.distance(point);
	}

}
