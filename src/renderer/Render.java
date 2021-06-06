package renderer;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import elements.Camera;
import primitives.Color;
import primitives.Ray;

/**
 * Rendering the image from the scene
 */
public class Render {
	private ImageWriter imageWriter;
	private Camera camera;
	private RayTracerBase rayTracer;
	private int kA = 1;
	final private BlackBoard sampleBoard = BlackBoard.sempleSquare(1);

	private static final String RESOURCE_ERROR = "Renderer resource not set";
	private static final String RENDER_CLASS = "Render";
	private static final String IMAGE_WRITER_COMPONENT = "Image writer";
	private static final String CAMERA_COMPONENT = "Camera";
	private static final String RAY_TRACER_COMPONENT = "Ray tracer";

	private int threadsCount = 0;
	private static final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores
	private static final double MAX_VARIANCE = 2;
	private boolean print = false; // printing progress percentage

	/**
	 * Set multi-threading <br>
	 * - if the parameter is 0 - number of cores less 2 is taken
	 * 
	 * @param threads number of threads
	 * @return the Render object itself
	 */
	public Render setMultithreading(int threads) {
		if (threads < 0)
			throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");
		if (threads != 0)
			this.threadsCount = threads;
		else {
			int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
			this.threadsCount = cores <= 2 ? 1 : cores;
		}
		return this;
	}

	/**
	 * Set debug printing on
	 * 
	 * @return the Render object itself
	 */
	public Render setDebugPrint() {
		print = true;
		return this;
	}

	/**
	 * Pixel is an internal helper class whose objects are associated with a Render
	 * object that they are generated in scope of. It is used for multithreading in
	 * the Renderer and for follow up its progress.<br/>
	 * There is a main follow up object and several secondary objects - one in each
	 * thread.
	 * 
	 * @author Dan
	 *
	 */
	private class Pixel {
		private long maxRows = 0;
		private long maxCols = 0;
		private long pixels = 0;
		public volatile int row = 0;
		public volatile int col = -1;
		private long counter = 0;
		private int percents = 0;
		private long nextCounter = 0;

		/**
		 * The constructor for initializing the main follow up Pixel object
		 * 
		 * @param maxRows the amount of pixel rows
		 * @param maxCols the amount of pixel columns
		 */
		public Pixel(int maxRows, int maxCols) {
			this.maxRows = maxRows;
			this.maxCols = maxCols;
			this.pixels = (long) maxRows * maxCols;
			this.nextCounter = this.pixels / 100;
			if (Render.this.print)
				System.out.printf("\r %02d%%", this.percents);
		}

		/**
		 * Default constructor for secondary Pixel objects
		 */
		public Pixel() {
		}

		/**
		 * Internal function for thread-safe manipulating of main follow up Pixel object
		 * - this function is critical section for all the threads, and main Pixel
		 * object data is the shared data of this critical section.<br/>
		 * The function provides next pixel number each call.
		 * 
		 * @param target target secondary Pixel object to copy the row/column of the
		 *               next pixel
		 * @return the progress percentage for follow up: if it is 0 - nothing to print,
		 *         if it is -1 - the task is finished, any other value - the progress
		 *         percentage (only when it changes)
		 */
		private synchronized int nextP(Pixel target) {
			++col;
			++this.counter;
			if (col < this.maxCols) {
				target.row = this.row;
				target.col = this.col;
				if (Render.this.print && this.counter == this.nextCounter) {
					++this.percents;
					this.nextCounter = this.pixels * (this.percents + 1) / 100;
					return this.percents;
				}
				return 0;
			}
			++row;
			if (row < this.maxRows) {
				col = 0;
				target.row = this.row;
				target.col = this.col;
				if (Render.this.print && this.counter == this.nextCounter) {
					++this.percents;
					this.nextCounter = this.pixels * (this.percents + 1) / 100;
					return this.percents;
				}
				return 0;
			}
			return -1;
		}

		/**
		 * Public function for getting next pixel number into secondary Pixel object.
		 * The function prints also progress percentage in the console window.
		 * 
		 * @param target target secondary Pixel object to copy the row/column of the
		 *               next pixel
		 * @return true if the work still in progress, -1 if it's done
		 */
		public boolean nextPixel(Pixel target) {
			int percent = nextP(target);
			if (Render.this.print && percent > 0)
				synchronized (this) {
					notifyAll();
				}
			if (percent >= 0)
				return true;
			if (Render.this.print)
				synchronized (this) {
					notifyAll();
				}
			return false;
		}

		/**
		 * Debug print of progress percentage - must be run from the main thread
		 */
		public void print() {
			if (Render.this.print)
				while (this.percents < 100)
					try {
						synchronized (this) {
							wait();
						}
						System.out.printf("\r %02d%%", this.percents);
						System.out.flush();
					} catch (Exception e) {
					}
		}
	}


	/**
	 * set the imageWriter of the render
	 * @param imageWriter the imageWriter for the render
	 * @return instance of this scene
	 */
	public Render setImageWriter(ImageWriter imageWriter) {
		this.imageWriter = imageWriter;
		return this;
	}

	/**
	 * set the camera of the render
	 * @param camera the camera for the render
	 * @return instance of this scene
	 */
	public Render setCamera(Camera camera) {
		this.camera = camera;
		return this;
	}

	/**
	 * set the rayTracer of the render
	 * @param rayTracer the rayTracer for the render
	 * @return instance of this scene
	 */
	public Render setRayTracer(RayTracerBase rayTracer) {
		this.rayTracer = rayTracer;
		return this;
	}

	/**
	 * setter for the antialising factor.
	 *  the number determine the dimension of the sample rays metrix in each pixel (kA*kA)
	 * @param kA the antialising factor
	 * @return instance of this scene
	 */
	public Render setKA(int kA) {
		this.kA = kA;
		return this;
	}
/**
	 * This function renders image's pixel color map from the scene included with
	 * the Renderer object - with multi-threading
	 */
	private void renderImageThreaded() {
		final int nX = imageWriter.getNx();
		final int nY = imageWriter.getNy();
		final Pixel thePixel = new Pixel(nY, nX);
		final BlackBoard randomBoard = BlackBoard.squareRandom(kA, 1);
		int rbbsSize = 100;
		BlackBoard rbbs[] = new BlackBoard[rbbsSize];
		for(int i = 0; i < rbbsSize; i++)
			rbbs[i] = BlackBoard.squareRandom(kA, 1);
		// Generate threads
		Thread[] threads = new Thread[threadsCount];
		for (int i = threadsCount - 1; i >= 0; --i) {
			threads[i] = new Thread(() -> {
				Pixel pixel = new Pixel();
				while (thePixel.nextPixel(pixel)) {
					int randInt = ThreadLocalRandom.current().nextInt(0, rbbsSize);
					renderImage(nX, nY, pixel.col, pixel.row, rbbs[randInt]);
				}
			});
		}
		// Start threads
		for (Thread thread : threads)
			thread.start();

		// Print percents on the console
		thePixel.print();

		// Ensure all threads have finished
		for (Thread thread : threads)
			try {
				thread.join();
			} catch (Exception e) {
			}

		if (print)
			System.out.print("\r100%");
	}
	/** 
	 * Rendering the image by imageWriter according to the rayTracer and camera 
	 * @param rays the number of rays per pixel
	 */
	private void renderImage(int nX, int nY, int i, int j, BlackBoard randomBoard) {

//		BlackBoard sampleBoard = BlackBoard.sempleSquare(1);
//		BlackBoard randomBoard = BlackBoard.squareRandom(kA, 1);
		if (kA > 4) {
			//get sample of 5 colors
			List<Ray> sampleRays = camera.constructBeamThroughPixel(sampleBoard, nX, nY, i, j);
			List<Color> sampleColors = new LinkedList<>();
			for (Ray ray : sampleRays) {
				sampleColors.add(rayTracer.traceRay(ray));
			}
			Color sampleAvg = new Color(sampleColors);

			if (calcVarianceColors(sampleColors, sampleAvg) < MAX_VARIANCE) {
				imageWriter.writePixel(i, j, sampleAvg);
			} else {
				List<Ray> rays = camera.constructBeamThroughPixel(randomBoard, nX, nY, i, j);
				List<Color> colors = new LinkedList<>();
				for (Ray ray : rays) {
					colors.add(rayTracer.traceRay(ray));
				}
				colors.add(sampleAvg);
				Color AvgColor = new Color(colors);
				imageWriter.writePixel(i, j, AvgColor);
			}
		}
		else{
			List<Ray> rays = camera.constructBeamThroughPixel(randomBoard, nX, nY, i, j);
			List<Color> colors = new LinkedList<>();
			for (Ray ray : rays) {
				colors.add(rayTracer.traceRay(ray));
			}
			Color AvgColor = new Color(colors);
			imageWriter.writePixel(i, j, AvgColor);
		}		
	}

	
	
//	/**
//	 * creates image by only 1 ray from each pixel
//	 */
//	private void renderImageByRandomRays(int i, int j, Color average) {
//		List<Ray> randomRays = camera.constructRaysThroughPixel(imageWriter.getNx(), imageWriter.getNy(), j, i, kA);
//		List<Color> colors = new LinkedList<>();
//		if(average != null)
//			colors.add(average);
//		for (Ray ray : randomRays) {
//			colors.add(rayTracer.traceRay(ray));
//		}
//		Color averageColor = new Color(colors);
//		imageWriter.writePixel(j, i, averageColor);
//	}

	// /**
	//  * calculate the average color from all the colors in the list 'colors'
	//  * @param colors the list of colors to find the average from
	//  * @return the average color from all the colors in the list 'colors'
	//  */
	// private Color averageColor(List<Color> colors) {
	// 	Color sumColors = Color.BLACK;
	// 	for (Color color : colors) {
	// 		sumColors = sumColors.add(new Color(color.getColor()));
	// 	}
	// 	return sumColors.reduce(colors.size());
	// }
	/**
	 * calculates the variance of list of color
	 * @param colors list of colors
	 * @param average the averade color
	 * @return the variance 
	 */
	private double calcVarianceColors(List<Color> colors, Color average) {
		int rAvg = average.getColor().getRed();
		int gAvg = average.getColor().getGreen();
		int bAvg = average.getColor().getBlue();
		double r = 0,g = 0,b = 0;
		for (Color color : colors) {
			r += Math.abs(color.getColor().getRed() - rAvg);
			g += Math.abs(color.getColor().getGreen() - gAvg);
			b += Math.abs(color.getColor().getBlue() - bAvg);
		}
		return (r+g+b)/colors.size();
	}

	/**
	 * prints a grid on the image. the spaces between the lines is the size of the "interval"
	 * @param interval the space size between the lines
	 * @param color the color of the grid
	 */
	public void printGrid(int interval, Color color) {
		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();
		// paint the vertical lines
		for (int i = 0; i < nY; i += interval) {
			for (int j = 0; j < nX; j++) {
				imageWriter.writePixel(i, j, color);
			}
		}
		// paint the horizontal lines
		for (int j = 0; j < nY; j += interval) {
			for (int i = 0; i < nX; i++) {
				imageWriter.writePixel(i, j, color);
			}
		}
	}

	public void renderImage() {
		var render = "Render";
		if (imageWriter == null)
			throw new MissingResourceException("imageWriter is null ", render, "imageWriter");
		if (rayTracer == null)
			throw new MissingResourceException("rayTracer is null ", render, "rayTracer");
		if (camera == null)
			throw new MissingResourceException("camera is null ", render, "camera");

		final int nX = imageWriter.getNx();
		final int nY = imageWriter.getNy();
		BlackBoard randomBoard = BlackBoard.squareRandom(kA, 1);
		if (threadsCount == 0)
			for (int i = 0; i < nY; ++i)
				for (int j = 0; j < nX; ++j)
					renderImage(nX, nY, j, i, randomBoard);
		else
			renderImageThreaded();
		}
	/**
	 * Generates the image
	 */
	public void writeToImage() {
		if (imageWriter == null)
			throw new MissingResourceException("imageWriter is null ", "Render", "imageWriter");
		imageWriter.writeToImage();
	}

}
