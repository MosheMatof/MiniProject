package unittests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import elements.AmbientLight;
import elements.Camera;
import elements.DirectionalLight;
import geometries.Cylinder;
import geometries.Geometries;
import geometries.Geometry;
import geometries.Intersectable.Boundary;
import geometries.Sphere;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import renderer.BasicRayTracer;
import renderer.ImageWriter;
import renderer.Render;
import scene.Scene;

public class Snow {
	private Scene scene = new Scene("Snow").setBackground(new Color(2, 10, 100));

	@Test
	public void test() {
		Camera camera = new Camera(new Point3D(200, 50, 200), new Point3D(0, 0, 0)).setViewPlaneSize(3200, 1800)
				.setDistance(10000);
		
		Geometry planet = new Sphere(new Point3D(100, -990, 100),1000).setEmission(new Color(200,200,200))
				.setMaterial(new Material().setKs(0.5).setShininess(300).setKd(0.5));
		Geometry moon = new Sphere(new Point3D(-2500, 10, -3000),100).setEmission(new Color(100,100,100))
				.setMaterial(new Material().setkT(1).setKs(0.5).setShininess(300).setKd(0.5));
		Geometries snowMan = new Geometries(List.of(
				new Sphere(new Point3D(0, 0, 0),10).setEmission(new Color(200,200,200))
				.setMaterial(new Material().setKs(0.5).setShininess(300).setKd(0.5))
				,new Sphere(new Point3D(0, 10, 0),6).setEmission(new Color(200,200,200))
				.setMaterial(new Material().setKs(0.5).setShininess(300).setKd(0.5))
				,new Sphere(new Point3D(0, 18, 0),4).setEmission(new Color(200,200,200))
				.setMaterial(new Material().setKs(0.5).setShininess(300).setKd(0.5))
				,new Sphere(new Point3D(2, 18, 3.5),0.5).setEmission(Color.BLACK)
				,new Sphere(new Point3D(4, 18, 0),0.5).setEmission(Color.BLACK)
				,new Cylinder(new Ray(new Point3D(3.2, 18, 2.4),new Vector(2,0,1)),0.5,3)
				.setEmission(new Color(255,69,0))
				));
		
		
		scene.geometries.add(planet,moon);
		scene.geometries.add(snowMan);
		scene.geometries.add(produceSonw(1000, new Boundary(50,-50,30,0,50,-50)));
		//scene.geometries.add(produceTree(100, new Boundary(30,20,20,4,30,20)));
		scene.lights.add(new DirectionalLight(new Color(200,200,100), new Vector(1,-1,3)));
		scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

		
		ImageWriter imageWriter = new ImageWriter("Snow", 1024, 576);
		Render render = new Render() //
				.setImageWriter(imageWriter) //
				.setCamera(camera) //
				.setRayTracer(new BasicRayTracer(scene)).setMultithreading(3).setDebugPrint();

		render.renderImage();
		render.writeToImage();
	}
	
	private Geometries produceSonw(int num,Boundary bound) {
		Geometries snow = new Geometries();
		for (int i = 0; i < num; i++) {
				double x = ThreadLocalRandom.current().nextDouble(bound.minX,bound.maxX);
				double y = ThreadLocalRandom.current().nextDouble(bound.minY,bound.maxY);
				double z = ThreadLocalRandom.current().nextDouble(bound.minZ,bound.maxZ);
				snow.add(
				new Sphere(new Point3D(x, y, z), 0.2).setEmission(new Color(255,255,255))
				.setMaterial(new Material()));
				
		}
		return snow;
	}
	private Geometries produceTree(int num,Boundary bound) {
		double radius = (bound.maxX - bound.minX)*0.5;
		double delta = radius *0.2;
		double height = bound.maxZ - bound.minZ;
		double midX =(bound.maxX + bound.minX)*0.5;
		double midY =(bound.maxY + bound.minY)*0.5;
		Geometries snow = new Geometries();
		for (int i = 0; i < num; i++) {
			double xxyy,rNew,x,y,z;
			do {
				z = ThreadLocalRandom.current().nextDouble(bound.minZ,bound.maxZ);
				x = ThreadLocalRandom.current().nextDouble(bound.minX,bound.maxX);
				y = ThreadLocalRandom.current().nextDouble(bound.minY,bound.maxY);
				rNew = radius*(height/(height-(z-bound.minZ)));
				rNew = rNew *rNew;
				xxyy = (x-midX)*(x-midX) + (y-midY)*(y-midY);
			} while (!(xxyy >rNew-1 && xxyy < rNew + 1));
			snow.add(
					new Sphere(new Point3D(x, y, z), 0.2).setEmission(new Color(10,255,30))
					.setMaterial(new Material()));
		}
		return snow;
	}

}
