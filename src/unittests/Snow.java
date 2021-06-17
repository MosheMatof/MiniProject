package unittests;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import elements.AmbientLight;
import elements.Camera;
import elements.DirectionalLight;
import geometries.Cylinder;
import geometries.Geometries;
import geometries.Geometry;
import geometries.Intersectable;
import geometries.Intersectable.Boundary;
import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
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
	private Scene scene = new Scene("Snow").setBackground(new Color(4, 80, 250));

	@Test
	public void test() {
		Camera camera = new Camera(new Point3D(350, 50, 350), new Point3D(0, 0, 0)).setViewPlaneSize(3200, 1800)
				.setDistance(10000);
		Color snowColor = new Color(100,100,100);
		
		Geometry planet1 = new Sphere(new Point3D(100, -990, 100),1000).setEmission(snowColor)
				.setMaterial(new Material().setKs(0.9).setKd(0.5).setShininess(1000).setSnow());
		Geometry planet2 = new Sphere(new Point3D(-1000, -990, -100),1000).setEmission(snowColor)
				.setMaterial(new Material().setKs(0.9).setKd(0.5).setShininess(1000));
		Geometry planet3 = new Sphere(new Point3D(-100, -990, -1000),1000).setEmission(snowColor)
				.setMaterial(new Material().setKs(0.9).setKd(0.5).setShininess(1000));
		Geometry planet4 = new Sphere(new Point3D(-1000, -1200, -1000),1000).setEmission(snowColor)
				.setMaterial(new Material().setKs(0.9).setKd(0.5).setShininess(1000));
//		Geometry moon = new Sphere(new Point3D(-2500, 10, -3000),100).setEmission(new Color(100,100,100))
//				.setMaterial(new Material().setkT(1).setKs(0.5).setShininess(300).setKd(0.5));
		Geometries snowMan = new Geometries(List.of(
				new Sphere(new Point3D(0, 0, 0),10).setEmission(snowColor)
				.setMaterial(new Material().setKd(0.5).setSnow())
				,new Sphere(new Point3D(0, 10, 0),6).setEmission(snowColor)
				.setMaterial(new Material().setKd(0.5).setSnow())
				,new Sphere(new Point3D(0, 18, 0),4).setEmission(snowColor)
				.setMaterial(new Material().setKd(0.5).setSnow())
				,new Sphere(new Point3D(2, 18, 3.5),0.5).setEmission(Color.BLACK)
				,new Sphere(new Point3D(4, 18, 0),0.5).setEmission(Color.BLACK)
				,new Cylinder(new Ray(new Point3D(3.2, 18, 2.4),new Vector(2,0,1)),0.5,3)
				.setEmission(new Color(80,20,0))
				));
//		Geometry check = new Polygon(new Point3D(0, 10, 0),new Point3D(100, 10, 0)
//				,new Point3D(100, 10, 100),new Point3D(0, 10, 100)).setEmission(Color.BLACK);
		LinkedList<Intersectable> snow = produceSonw(2000, new Boundary(200,-50,60,-10,200,-50));
		Geometries Tree1 = produceTree(200,new Point3D(40, 10, -10),10,40);
		Geometries Tree2= produceTree(200,new Point3D(-50, -25, -40),10,40);
		Geometries Tree3= produceTree(200,new Point3D(-120, -20, -20),10,40);
		for (int i = 0,j=-30; i < 1; i++,j--) {
			snow.forEach(g -> g = new Sphere(((Sphere)g).getCenter().add(new Vector(0,-0.2,0)),0.2));
			
			scene.geometries.add(planet1,planet2,planet3,planet4);
			scene.geometries.add(snowMan);
			scene.geometries.add(snow);
			scene.geometries.add(Tree1);
			scene.geometries.add(Tree2);
			scene.geometries.add(Tree3);
			scene.lights = List.of(new DirectionalLight(new Color(400,400,400), new Vector(1,-1,-3))
					,new DirectionalLight(new Color(100,100,100), new Vector(0,-1,0)));
			scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

			ImageWriter imageWriter = new ImageWriter("Snow" , 1024, 576);
			camera.setPositionAndTarget(new Point3D(350 +j, 50 + 2*i, 350+i), new Point3D(0, 0, 0));
			Render render = new Render() //
					.setImageWriter(imageWriter) //
					.setCamera(camera) //
					.setRayTracer(new BasicRayTracer(scene)).setMultithreading(3).setDebugPrint();

			render.renderImage();
			render.writeToImage();
			scene.geometries = new Geometries();
		}	
	}
	
	
	
	private LinkedList<Intersectable> produceSonw(int num,Boundary bound) {
		LinkedList<Intersectable> snow = new LinkedList<Intersectable>();
		for (int i = 0; i < num; i++) {
				double x = ThreadLocalRandom.current().nextDouble(bound.minX,bound.maxX);
				double y = ThreadLocalRandom.current().nextDouble(bound.minY,bound.maxY);
				double z = ThreadLocalRandom.current().nextDouble(bound.minZ,bound.maxZ);
				snow.add(
				new Sphere(new Point3D(x, y, z), 0.2).setEmission(new Color(200,200,200))
				.setMaterial(new Material()));
				
		}
		return snow;
	}
	private Geometries produceTree(int num,Point3D p,double radius,double height) {
		double delta = radius *0.1;
		double pX = p.getX(),pY = p.getY(),pZ = p.getZ();
		double maxX = pX + radius,minX = pX - radius;
		double maxZ = pZ + radius,minZ = pZ - radius;
		Geometries snow = new Geometries();
		for (int i = 0; i < num; i++) {
			double xxzz,rNew,x,y,z;
			do {
				z = ThreadLocalRandom.current().nextDouble(minZ,maxZ);
				x = ThreadLocalRandom.current().nextDouble(minX,maxX);
				y = ThreadLocalRandom.current().nextDouble(pY,pY + height);
				rNew = radius*(height/(height+(y-pY)*2));
				rNew = rNew *rNew;
				xxzz = (x-pX)*(x-pX) + (z-pZ)*(z-pZ);
			} while (!(xxzz >rNew-delta && xxzz < rNew + delta));
			double t = ThreadLocalRandom.current().nextDouble(3,5);
			snow.add(
					new Triangle(new Point3D(x, y, z),new Point3D(x, y+t, z),new Point3D(x, y,z>pZ? z + t:z-t ))
					.setEmission(new Color(5,60,10)).setMaterial(new Material().setKd(0.7).setKs(0.5))
					,new Triangle(new Point3D(x, y, z),new Point3D(x, y+t, z),new Point3D(x>pX ? x+t: x - t, y,z ))
					.setEmission(new Color(5,60,10)).setMaterial(new Material().setKd(0.7).setKs(0.5)));
//			new Triangle(new Point3D(x<midX ? x+t: x - t, y, z),new Point3D(x, y+t, z),new Point3D(x>midX ? x+t: x - t, y,z>midZ? z + t:z-t ))
//			.setEmission(new Color(10,255,30))
//			,new Triangle(new Point3D(x, y, z<midZ? z + t:z-t),new Point3D(x, y+t, z),new Point3D(x>midX ? x+t: x - t, y,z>midZ? z + t:z-t ))
//			.setEmission(new Color(10,255,30)));
			
		}
		snow.add(new Cylinder(new Ray(p.add(Vector.Y.scale(-radius)),Vector.Y),delta,height)
				.setEmission(new Color(30, 13, 9)));
		return snow;
	}

	private Geometries produceRandomTrees(int num,Boundary bound) {
		Geometries snow = new Geometries();
		for (int i = 0; i < num; i++) {
				double x = ThreadLocalRandom.current().nextDouble(bound.minX,bound.maxX);
				double y = ThreadLocalRandom.current().nextDouble(bound.minY,bound.maxY);
				double z = ThreadLocalRandom.current().nextDouble(bound.minZ,bound.maxZ);
				snow.add(
				new Sphere(new Point3D(x, y, z), 0.2).setEmission(new Color(200,200,200))
				.setMaterial(new Material()));
				
		}
		return snow;
	}
}
