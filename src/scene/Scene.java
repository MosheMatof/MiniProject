/**
 * 
 */
package scene;

import java.util.LinkedList;
import java.util.List;

import elements.AmbientLight;
import elements.LightSource;
import geometries.Geometries;
import primitives.Color;

/**
 * represent scene of geometries
 */
public class Scene {

	/**
	 * name of the scene
	 */
	public String name;
	/**
	 * the geometries at the scene
	 */
	public Geometries geometries = new Geometries();
	/**
	 * ambient light of the scene
	 */
	public AmbientLight ambientLight = new AmbientLight();
	/**
	 * background color of the scene
	 */
	public Color background = Color.BLACK;
	/**
	 * list of the light source at the scene
	 */
	public List<LightSource> lights = new LinkedList<LightSource>();
	
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
	 * @param background the color of the background
	 * @return this scene
	 */
	public Scene setBackground(Color background) {
		this.background = background;
		return this;
	}

	/**
	 * sets light source of the scene
	 * @param lights the lights source to set to this scene
	 * @return this scene
	 */
	public Scene setLights(List<LightSource> lights) {
		this.lights = lights;
		return this;
	}
}
