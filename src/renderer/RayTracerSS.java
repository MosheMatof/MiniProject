package renderer;

import java.util.List;

import elements.LightSource;
import geometries.Intersectable;
import geometries.Intersectable.GeoPoint;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

public class RayTracerSS extends BasicRayTracer {
	private int kSS = 0;

	public RayTracerSS(Scene scene) {
		super(scene);
	}

	/**
	 * setter for the soft shadow factor. the number of the rays to send to the
	 * light
	 * 
	 * @param kSS the soft shadow factor
	 * @return instance of this scene
	 */
	public RayTracerSS setKSS(int kSS) {
		this.kSS = kSS;
		return this;
	}

	@Override
	protected double transparency(LightSource ls, GeoPoint intersection, Vector l, Vector n, double nv) {
		// check if the light is either pointLight or spotLight
		if (kSS <= 1 || ls.getRadius() == 0)
			return super.transparency(ls, intersection, l, n, nv);

		List<Ray> rays = ls.getSampleBeam(intersection.point);

		double ktFinal = 1, prev = 1, kt = 1;
		boolean equal = true; 
		for (Ray ray : rays) {
			kt = super.transparency(ls, intersection, ray.getDir(), n, nv);
			ktFinal += kt;
			if (kt != prev)
				equal = false;
			prev = kt;
		}
		if (equal)
			return kt;
		ktFinal /= rays.size();
		if (ktFinal <= MIN_CALC_COLOR_K)
			return 0.0;
		if (ktFinal == 1)
			return 1;

		rays = ls.getRandomBeam(intersection.point, kSS);
		ktFinal = 1;
		for (Ray ray : rays) {
			ktFinal += super.transparency(ls, intersection, ray.getDir(), n, nv);
		}
		return ktFinal / rays.size();
	}
	@Override
	public RayTracerBase setBVH() {
		Intersectable.BVH = true;
		return this;
	}
}
