package elements;

import primitives.Color;

/**
 * represent ambient light of a scene
 */
public class AmbientLight extends Light {

	/**
	 * AmbientLight constructor 
	 * @param color the color of the light
	 * @param i the value to scale by
	 */
	public AmbientLight(Color color, double i) {
		super(color.scale(i));
	}
}
