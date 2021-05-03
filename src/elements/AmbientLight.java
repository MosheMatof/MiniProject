package elements;

import primitives.Color;

/**
 * represent ambient light of a scene
 */
public class AmbientLight {

	Color intensity;
	/**
	 * AmbientLight constructor 
	 * @param color the color of the light
	 * @param i the value to scale by
	 */
	public AmbientLight(Color color, double i) {
		intensity = color.scale(i);
	}
	/**
	 * get the intensity of the ambient light
	 * @return the intensity of the ambient light
	 */
	public Color getIntensity(){
		return intensity;
	}
}
