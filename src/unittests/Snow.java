package unittests;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
	private Scene scene = new Scene("Snow").setBackground(new Color(70, 120, 200));

	@Test
	public void test() {
		Camera camera = new Camera(new Point3D(100, 30, 100), new Point3D(0, 7, 0)).setViewPlaneSize(3200, 1800)
				.setDistance(10000);
		Color snowColor = new Color(100, 100, 100);

		Geometry planet1 = new Sphere(new Point3D(100, -990, 100), 1000).setEmission(snowColor)
				.setMaterial(new Material().setKs(0.9).setKd(0.5).setShininess(1000).setSnow());
		Geometry planet2 = new Sphere(new Point3D(-1000, -990, -100), 1000).setEmission(snowColor)
				.setMaterial(new Material().setKs(0.9).setKd(0.5).setShininess(1000));
		Geometry planet3 = new Sphere(new Point3D(-100, -990, -1000), 1000).setEmission(snowColor)
				.setMaterial(new Material().setKs(0.9).setKd(0.5).setShininess(1000));
		Geometry planet4 = new Sphere(new Point3D(-1000, -1200, -1000), 1000).setEmission(snowColor)
				.setMaterial(new Material().setKs(0.9).setKd(0.5).setShininess(1000));

		Geometries snowMan = new Geometries(List.of(
				new Sphere(new Point3D(0, 0, 0), 10).setEmission(snowColor)
						.setMaterial(new Material().setKd(0.5).setSnow()),
				new Sphere(new Point3D(0, 10, 0), 6).setEmission(snowColor)
						.setMaterial(new Material().setKd(0.5).setSnow()),
				new Sphere(new Point3D(0, 18, 0), 4).setEmission(snowColor)
						.setMaterial(new Material().setKd(0.5).setSnow()),
				new Sphere(new Point3D(2, 18, 3.5), 0.5).setEmission(Color.BLACK),
				new Sphere(new Point3D(4, 18, 0), 0.5).setEmission(Color.BLACK),
				new Cylinder(new Ray(new Point3D(3.2, 18, 2.4), new Vector(2, 0, 1)), 0.5, 3)
						.setEmission(new Color(200, 50, 0))));
		
		List<Intersectable> snow = produceSonw(4000, new Boundary(250, -20, 110, -10, 250, -20));
		Geometries tree1 = produceTree(400, new Point3D(40, 0, -10), 10, 40);
		Geometries tree2 = produceTree(400, new Point3D(-50, -35, -40), 10, 40);
		Geometries tree3 = produceTree(400, new Point3D(-120, -30, -20), 10, 40);
		List<Intersectable> trees1 = produceRandomTreesOnSphere(200,100, (Sphere) planet3, 5, 20, 0.7f,2);
		List<Intersectable> trees2 = produceRandomTreesOnSphere(200,100, (Sphere) planet2, 5, 20, 0.7f,3);
		List<Intersectable> smallTrees = produceRandomSmallTreesOnSphere(150, (Sphere) planet4, 5, 20, 0.2f);
		int k = 0;
		double t = 0, s = 1;
		for (double i = 0, j = 0; t < 1500; k++) {
			snow.forEach(g -> g = new Sphere(((Sphere) g).getCenter().add(new Vector(0, -0.4, 0)), 0.2));
			if(i < 30) {
				 i+= 0.1; j-= 0.1;
			}
			else {
				s += 0.30;
				t = s*s;
			}
			scene.geometries.add(snow);
			scene.geometries.add(planet1, planet2, planet3, planet4);
			scene.geometries.add(snowMan);
			scene.geometries.add(tree1);
			scene.geometries.add(tree2);
			scene.geometries.add(tree3);
			scene.geometries.add(trees1);
			scene.geometries.add(trees2);
			scene.geometries.add(smallTrees);
			scene.lights = List.of(new DirectionalLight(new Color(400, 400, 400), new Vector(1, -1, -3)),
					new DirectionalLight(new Color(200, 200, 200), new Vector(0, -1, 0)));
			scene.setAmbientLight(new AmbientLight(new Color(100, 100, 255), 0.1));

			ImageWriter imageWriter = new ImageWriter("Snow" + k, 1024, 576);
			//camera.setPositionAndTarget(new Point3D(350, 700, 350), new Point3D(0, 0, 0));
			camera.setPositionAndTarget(new Point3D(380 + j + t, 20 + 2*i + t*0.5, 320 + i + t), new Point3D(0, 0, 0));
	
			Render render = new Render() //
					.setImageWriter(imageWriter) //
					.setCamera(camera) //
					.setRayTracer(new BasicRayTracer(scene)).setMultithreading(3).setDebugPrint();

			render.renderImage();
			render.writeToImage();
			scene.geometries = new Geometries();
		}
	}

	private List<Intersectable> produceSonw(int num, Boundary bound) {
		List<Intersectable> snow = new LinkedList<Intersectable>();
		for (int i = 0; i < num; i++) {
			double x = ThreadLocalRandom.current().nextDouble(bound.minX, bound.maxX);
			double y = ThreadLocalRandom.current().nextDouble(bound.minY, bound.maxY);
			double z = ThreadLocalRandom.current().nextDouble(bound.minZ, bound.maxZ);
			snow.add(new Sphere(new Point3D(x, y, z), 0.2).setEmission(new Color(255, 255, 255)));
			
		}
		return snow;
	}

	private Geometries produceTree(int num, Point3D p, double radius, double height) {
		double delta = radius * 0.1;
		double pX = p.getX(), pY = p.getY() + radius, pZ = p.getZ();
		double maxX = pX + radius, minX = pX - radius;
		double maxZ = pZ + radius, minZ = pZ - radius;
		Geometries tree = new Geometries();
		for (int i = 0; i < num; i++) {
			double xxzz, rNew, x, y, z;
			do {
				z = ThreadLocalRandom.current().nextDouble(minZ, maxZ);
				x = ThreadLocalRandom.current().nextDouble(minX, maxX);
				y = ThreadLocalRandom.current().nextDouble(pY, pY + height);
				rNew = radius * (height / (height + (y - pY) * 2));
				rNew = rNew * rNew;
				xxzz = (x - pX) * (x - pX) + (z - pZ) * (z - pZ);
			} while (!(xxzz > rNew - delta && xxzz < rNew + delta));
			double t = ThreadLocalRandom.current().nextDouble(2, 5);
			tree.add(
					new Triangle(new Point3D(x, y, z), new Point3D(x, y + t, z),
							new Point3D(x , y, z> pZ ? z + t : z - t)).setEmission(new Color(5, 60, 10))
									.setMaterial(new Material().setKs(0.5).setKd(0.7).setShininess(1000)),
					new Triangle(new Point3D(x, y, z), new Point3D(x, y + t, z),
							new Point3D(x > pX ? x + t : x - t, y, z)).setEmission(new Color(5, 60, 10))
									.setMaterial(new Material().setKs(0.5).setKd(0.7).setShininess(1000)));
		}
		tree.add(new Cylinder(new Ray(p, Vector.Y), delta, height).setEmission(new Color(30, 13, 9)));
		tree.initConstructHeirarchy();
		return tree;
	}

	private List<Intersectable> produceRandomSmallTreesOnSphere(int num, Sphere sphere, double radius, double height, float range) {
		List<Intersectable> trees = new LinkedList<>();
		double sRadius = sphere.getRadius(), delta = sRadius * range;
		double sqrRadius = sRadius * sRadius;
		double sX = sphere.getCenter().getX(), sY = sphere.getCenter().getY(), sZ = sphere.getCenter().getZ();
		double maxX = sX + delta, minX = sX - delta;
		double maxZ = sZ + delta, minZ = sZ - delta;
		for (int i = 0; i < num; i++) {
			double x = ThreadLocalRandom.current().nextDouble(minX, maxX);
			double z = ThreadLocalRandom.current().nextDouble(minZ, maxZ);
			double y = Math.sqrt(-(x * x + sX * sX + z * z + sZ * sZ) + 2 * (sX * x + sZ * z) + sqrRadius) + sY;

			Point3D mid = new Point3D(x, y, z);
			Point3D top = mid.add(Vector.Y.scale(height));
			trees.add(
					new Geometries( List.of(
					new Triangle(top, mid.add(Vector.X.scale(radius)), mid.add(Vector.X.scale(-radius)))
							.setEmission(new Color(5, 60, 10)).setMaterial(new Material().setKd(0.7).setKs(0.5)),
					new Triangle(top, mid.add(Vector.Z.scale(radius)), mid.add(Vector.Z.scale(-radius)))
							.setEmission(new Color(5, 60, 10)).setMaterial(new Material().setKd(0.7).setKs(0.5)))));
		}
		return trees;
	}
//	private Geometries produceRandomSmallTreesOnSphere(int num, Sphere sphere, double radius, double height, float range) {
//		Geometries trees = new Geometries();
//		double sRadius = sphere.getRadius(), delta = sRadius * range;
//		double sqrRadius = sRadius * sRadius;
//		double sX = sphere.getCenter().getX(), sY = sphere.getCenter().getY(), sZ = sphere.getCenter().getZ();
//		double maxX = sX + delta, minX = sX - delta;
//		double maxZ = sZ + delta, minZ = sZ - delta;
//		for (int i = 0; i < num; i++) {
//			double x = ThreadLocalRandom.current().nextDouble(minX, maxX);
//			double z = ThreadLocalRandom.current().nextDouble(minZ, maxZ);
//			double y = Math.sqrt(-(x * x + sX * sX + z * z + sZ * sZ) + 2 * (sX * x + sZ * z) + sqrRadius) + sY;
//			
//			Point3D mid = new Point3D(x, y, z);
//			Point3D top = mid.add(Vector.Y.scale(height));
//			trees.add(
//					new Geometries( List.of(
//							new Triangle(top, mid.add(Vector.X.scale(radius)), mid.add(Vector.X.scale(-radius)))
//							.setEmission(new Color(5, 60, 10)).setMaterial(new Material().setKd(0.7).setKs(0.5)),
//							new Triangle(top, mid.add(Vector.Z.scale(radius)), mid.add(Vector.Z.scale(-radius)))
//							.setEmission(new Color(5, 60, 10)).setMaterial(new Material().setKd(0.7).setKs(0.5)))));
//		}
//		trees.initConstructHeirarchy();
//		return trees;
//	}
	/**
	 * 
	 * @param numOfTrees
	 * @param numOfLeaves
	 * @param sphere
	 * @param radius
	 * @param height
	 * @param SpeardRange
	 * @param quarter 1: +x,+z. 2: -x,+z. 3: +x,-z. 4: -x,-z. else: all
	 * @return
	 */
	private List<Intersectable> produceRandomTreesOnSphere(int numOfTrees,int numOfLeaves, Sphere sphere, double radius, double height, float SpeardRange,int quarter) {
		List<Intersectable> trees = new LinkedList<>();
		double sRadius = sphere.getRadius(), delta = sRadius * SpeardRange;
		double sqrRadius = sRadius * sRadius;
		double sX = sphere.getCenter().getX(), sY = sphere.getCenter().getY(), sZ = sphere.getCenter().getZ();
		double maxX = sX, minX = sX, maxZ = sZ, minZ = sZ;
		switch (quarter) {
		case 1: {
			maxX += delta;
			maxZ += delta;
			break;
		}
		case 2: {
			minX -= delta;
			maxZ += delta;
			break;
		}
		case 3: {
			maxX += delta;
			minZ -= delta;
			break;
		}
		case 4: {
			minX -= delta;
			minZ -= delta;
			break;
		}
		default:
			maxX += delta; minX -= delta;
			maxZ += delta; minZ -= delta;
		}

		for (int i = 0; i < numOfTrees; i++) {
			double x = ThreadLocalRandom.current().nextDouble(minX, maxX);
			double z = ThreadLocalRandom.current().nextDouble(minZ, maxZ);
			double y = Math.sqrt(-(x * x + sX * sX + z * z + sZ * sZ) + 2 * (sX * x + sZ * z) + sqrRadius) + sY;
			
			Point3D base = new Point3D(x, y, z);
			trees.add(produceTree(numOfLeaves, base, radius, height));
		}
		return trees;
	}

//	private Geometries Stone(Point3D base, int num,double radiusX,double radiusZ,double height) {
//		double deltaX = radiusX * 0.1,deltaZ = radiusZ * 0.1,deltaY = height * 0.1;
//		double pX = base.getX(), pY = base.getY(), pZ = base.getZ();
//		double maxX = pX + radiusX, minX = pX - radiusX;
//		double maxZ = pZ + radiusZ, minZ = pZ - radiusZ;
//		double maxY = pY + height;
//		Geometries stone = new Geometries();
//		List<PointS> points = new LinkedList<>();
//		for (int i = 0; i < num; i++) {
//			double x, y, z;
//			do {
//				z = ThreadLocalRandom.current().nextDouble(minZ, maxZ);
//				x = ThreadLocalRandom.current().nextDouble(minX, maxX);
//				y = ThreadLocalRandom.current().nextDouble(pY, maxY);
//			} while ((x < maxX - deltaX && x > minX + deltaX) || (z < maxZ - deltaZ && z > minZ + deltaZ) || (y < maxY - deltaY));
//			points.add(new PointS(x,y,z));
//		}
//		
//		int id = 0;
//		for (PointS firstP : points) {
//			if (firstP.uses < 1) {
//				points.sort(Comparator.comparingDouble(p -> p.p.distance(firstP.p)));
//				List<PointS> firstPoints = points.subList(0, 3);
//				firstPoints.sort(Comparator.comparingDouble(p -> p.p.distance(firstP.p)));
//				List<PointS> tempPoints = new LinkedList<>(firstPoints);
//				PointS temp = firstPoints.get(2);
//				for (int i = 0; i < 8; i++) {
//					final PointS fTemp = temp;
//					points.sort(Comparator.comparingDouble(p -> p.p.distance(fTemp.p)));
//					int to = points.size() > 4 ? 4 : points.size();
//					List<PointS> closepoints = points.subList(1, to);
//					closepoints.sort(Comparator.comparingDouble(p -> p.p.distance(firstP.p)));
//					PointS temp2 = closepoints.get(0);
//					if ((temp2 == temp || temp == firstP) && i != 0)
//						temp2 = closepoints.get(1);
//					if(temp2 == firstP) {
//						try {
//							final int fId = id++;
//							tempPoints.forEach(p -> p.addId(fId));
//							Point3D[] vertics = tempPoints.stream().map(p -> p.p).toArray(Point3D[]::new);
//							stone.add(new Polygon(vertics).setEmission(new Color(100, 0, 0)));
//							continue;
//						} catch (Exception e) {
//							continue;
//						}		
//					}
//					tempPoints.add(temp2);
//					temp = temp2;
//				}
//			}
//		}
//
////		for (PointS firstP : points.stream().filter(p -> p.uses < 2).collect(Collectors.toList())) {
////			if (firstP.uses < 2) {
////				PointS temp = firstP;
////				List<PointS> tempPoints = new LinkedList<>();
////				tempPoints.add(firstP);
////				for (int i = 0; i < 8; i++) {
////					final PointS fTemp = temp;
////					points.sort(Comparator.comparingDouble(p -> p.p.distance(fTemp.p)));
////					int to = points.size() > 4 ? 4 : points.size();
////					List<PointS> closepoints = points.stream().filter(p -> !p.isId(fTemp)).collect(Collectors.toList()).subList(1, to);
////					closepoints.sort(Comparator.comparingDouble(p -> p.p.distance(firstP.p)));
////					temp = closepoints.get(0);
////					PointS temp2 = closepoints.get(0);
////					if ((temp2 == temp || temp == firstP) && i != 0)
////						temp2 = closepoints.get(1);
////					if(temp2 == firstP) {
////						try {
////							final int fId = id++;
////							tempPoints.forEach(p -> p.addId(fId));
////							Point3D[] vertics = tempPoints.stream().map(p -> p.p).toArray(Point3D[]::new);
////							stone.add(new Polygon(vertics).setEmission(new Color(100, 100, 100)));
////							continue;
////						} catch (Exception e) {
////							continue;
////						}			
////					}
////					tempPoints.add(temp);
////					temp = temp2;
////				}
////			}
////		}
//		stone.initConstructHeirarchy();
//		return stone;
//	}
//	
//	class PointS{
//		public Point3D p;
//		public List<Integer> ids = new LinkedList<>();
//		private int uses = 0;
//		public PointS(Point3D p) {
//			this.p = p;
//		}
//		public PointS(double x, double y, double z) {
//			this.p = new Point3D(x, y, z);
//		}
//		
//		public int getUses() {
//			return uses;
//		}
//		public void addId(int id) {
//			ids.add(id);
//			uses++;
//		}
//		public boolean isId(PointS other) {
//			for (Integer id : ids) {
//				for (Integer otherId : other.ids) {
//					if(id == otherId) return true;
//				}
//			}
//			return false;
//		}
//	}
}
