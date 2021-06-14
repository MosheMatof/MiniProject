package unittests;

import elements.*;
import geometries.*;
import primitives.*;
import renderer.*;
import scene.Scene;

import java.util.List;

import org.junit.Test;

/**
 * generates improving image of mini project 1
 */
public class MP1 {
	private Scene scene = new Scene("mini project 1").setBackground(new Color(50, 120, 255));

	/**
	 * Produce a picture of a spheres affected by
	 */
	@Test
	public void mp1() {
		Camera camera = new Camera(new Point3D(0, 20, -100), new Point3D(0, 2, 0)).setViewPlaneSize(3200, 1800)
				.setDistance(10000); //

		scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

		Geometry sun = new Sphere(new Point3D(0, 40, -131), 30).setEmission(new Color(255, 255, 100));
		// .setMaterial(new
		// Material().setkR(1).setKd(0.2).setKs(0.8).setShininess(100));
		Geometry miror = new Sphere(new Point3D(0, 5.1, 10), 5)
				.setMaterial(new Material().setkR(1).setKd(0.2).setKs(0.8).setShininess(100));
		Geometry floor = new Plane(Point3D.ZERO, new Vector(0, 1, 0)).setEmission(new Color(2, 2, 2))
				.setMaterial(new Material().setKd(0.8).setkR(0.2).setKs(1).setShininess(1));
		// List<Geometry> lamp = List.of(
		// new Cylinder(axis, radius, height)
		// )

		scene.geometries.add( //
				new Sphere(new Point3D(0, 2, 0), 2) //
						.setEmission(new Color(0, 0, 50)) //
						.setMaterial(new Material().setKd(0.2).setKs(0.8).setShininess(100).setkR(0.3)),
				miror, floor, sun, new Sphere(new Point3D(-4, 4.1, -5), 4)
						.setMaterial(new Material().setKd(0.1).setKs(0.9).setShininess(1000).setkR(0.1).setkT(0.9))
		// ,new Cylinder(new Ray(new Point3D(-5, 0,0), Vector.Y) ,1,3).setEmission(new
		// Color(60,10,2))
		// .setMaterial(new
		// Material().setKd(0.5).setKs(0.8).setShininess(100).setkR(0.4))
//                ,new Sphere(new Point3D(-7, 4, 2), 2)
//                .setMaterial(new Material().setkR(1).setKd(0.2).setKs(0.8).setShininess(10))
		);

		Point3D a = new Point3D(-10, 0.001, 150);
		Point3D b = new Point3D(-13, 0.001, 150);
		Point3D c = new Point3D(-30, 0.001, -100);
		Point3D d = new Point3D(-27, 0.001, -100);
		for (int i = 0, n = 1; i < 10; i++, n += 6) {
			scene.geometries.add(new Polygon(a.add(Vector.X.scale(n)), b.add(Vector.X.scale(n)),
					c.add(Vector.X.scale(n)), d.add(Vector.X.scale(n))).setEmission(new Color(1, 1, 1))
							.setMaterial(new Material().setKd(0.8).setkR(0.2).setKs(1).setShininess(100)));
		}
		;

		scene.lights.addAll(List.of(
				new SpotLight(new Color(0, 255, 40), new Point3D(10, 5, -3), new Vector(-22, -3, 5)).setKB(3)
						.setKl(0.0000001).setKq(0.0000005),
				new SpotLight(new Color(40, 80, 150), new Point3D(10, 10, -8), new Vector(-15, -10, 8)).setKl(0.000001)
						.setKq(0.0000005),
				new SpotLight(new Color(30, 200, 70), new Point3D(2, 5, 8), new Vector(-2, -5, -8)).setKB(40)
						.setKl(0.0001).setKq(0.00005),
				new PointLight(new Color(255, 255, 0), new Point3D(0, 200, -300)).setKl(0.0001).setKq(0.00005))
//                ,new SpotLight(new Color(30,3,200), new Point3D(10, -30, 30), new Vector(-1,-3,-3))
//            	.setKB(40).setKl(0.0001).setKq(0.00005))
		);

		ImageWriter imageWriter = new ImageWriter("mini project 1", 1024, 576);
		Render render = new Render() //
				.setImageWriter(imageWriter) //
				.setCamera(camera) //
				.setRayTracer(new BasicRayTracer(scene)).setMultithreading(3).setDebugPrint().setKA(4);

		render.renderImage();
		render.writeToImage();
	}

	
	
	
	@Test
	public void mp2() {
		Camera camera = new Camera(new Point3D(300, 100, -300), new Point3D(0, 2, 0)).setViewPlaneSize(3200, 1800)
				.setDistance(10000); //

		scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

		Geometry sun = new Sphere(new Point3D(0, 30, 0), 4).setEmission(new Color(255, 255, 180))
				.setMaterial(new Material().setkT(1));
		Geometry floor = new Plane(Point3D.ZERO, new Vector(0, 1, 0)).setEmission(new Color(2, 2, 20))
				.setMaterial(new Material().setKd(0.8).setkR(0.2).setKs(1).setShininess(1));			

		scene.geometries.add( floor, sun
				,new Cylinder(new Ray(new Point3D(-20, 0, 0), Vector.Y), 2, 15)
				.setMaterial(new Material().setKd(0.8).setkR(0.2).setKs(1).setShininess(1))
				,new Cylinder(new Ray(new Point3D(20, 0, 0), Vector.Y), 2, 15)
				.setMaterial(new Material().setKd(0.8).setkR(0.2).setKs(1).setShininess(1))
				,new Cylinder(new Ray(new Point3D(0, 0, 20), Vector.Y), 2, 15)
				.setMaterial(new Material().setKd(0.8).setkR(0.2).setKs(1).setShininess(1))
				,new Cylinder(new Ray(new Point3D(0, 0, -20), Vector.Y), 2, 15)
				.setMaterial(new Material().setKd(0.8).setkR(0.2).setKs(1).setShininess(1))
				);


		scene.lights.add(new PointLight(new Color(255, 255, 100), new Point3D(0, 25, 0)).setRadius(4)
		.setKl(0.0001).setKq(0.00005));

		ImageWriter imageWriter = new ImageWriter("mini project 1.2", 800, 450);
		Render render = new Render() //
				.setImageWriter(imageWriter) //
				.setCamera(camera) //
				.setRayTracer(new RayTracerSS(scene).setKSS(50)).setMultithreading(3).setDebugPrint().setKA(4);

		render.renderImage();
		render.writeToImage();
	}
}
