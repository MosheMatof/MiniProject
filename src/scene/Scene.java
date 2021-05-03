/**
 * 
 */
package scene;

import elements.AmbientLight;
import geometries.Geometries;
import primitives.Color;

/**
 * represent scene of geometries
 */
public class Scene {

	public String name;
	public Geometries geometries;
	public AmbientLight ambientLight;
	public Color background;
	/**
	 * scene constructor
	 * @param name the name of the scene
	 */
	public Scene(String name) {
		this.name = name;
	}
	/**
	 * set the ambient light of the scene
	 * @param ambientLight the ambient light of the scene 
	 * @return this scene
	 */
	public Scene setAmbientLight(AmbientLight ambientLight) {
		this.ambientLight = ambientLight;
		return this;
	}
	/**
	 * set the background of the scene
	 * @param color the color of the background
	 * @return this scene
	 */
	public Scene setBackground(Color background) {
		this.background = background;
		return this;
	}

}
