/**
 * 
 */
package elements;

import primitives.Point3D;
import primitives.Vector;

/**
 * the point of view the scene
 */
public class Camera {

	private Point3D p0;

	private Vector vUp;
	private Vector vTo;
	private Vector vRight;

	public Camera(Point3D p0, Vector vUp, Vector vTo, double width, double height, double dis) {
		super();
		this.p0 = p0;
		this.vUp = vUp;
		this.vTo = vTo;
		this.width = width;
		this.height = height;
		this.dis = dis;
	}
	public Point3D getP0() {
		return p0;
	}
	public Vector getvUp() {
		return vUp;
	}
	public Vector getvTo() {
		return vTo;
	}
	public Vector getvRight() {
		return vRight;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	public double getDis() {
		return dis;
	}
	private double width;
	private double height;
	private double dis;
}
