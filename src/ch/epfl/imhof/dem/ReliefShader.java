package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

/**
 * Represents Earth's topography in two dimensions using data from an HGT file
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public final class ReliefShader {
	private final Projection projection;
	private final HGTDigitalElevationModel numericModel;
	private final Vector3 sunDirection;
	private final double sunVectorNorma;
	
	/**
	 * Constructs topography representation using a map projection, a model of terrain elevation and sun's direction
	 * @param projection
	 * 			a map projection used for for the representation
	 * @param numericModel
	 * 			a model of terrain elevation
	 * @param sunDirection
	 * 			direction of the sun on the topography representations
	 */
	public ReliefShader(Projection projection,HGTDigitalElevationModel numericModel, Vector3 sunDirection) {
		this.projection=projection;
		this.numericModel=numericModel;
		this.sunDirection=sunDirection;
		this.sunVectorNorma=sunDirection.norm();
		
	}
	
	/**
	 * Gives an image of shaded relief
	 * @param pointBL
	 * 			bottom-left point of the image on a map
	 * @param pointTR
	 * 			top-right point of the image on a map
	 * @param width
	 * 			width of the created image
	 * @param height
	 * 			height of the created image
	 * @param Rayon
	 * 			a radius used for gaussian blur of the image
	 * @return
	 * 			an image of shaded relief
	 */
	public BufferedImage shadedRelief(Point pointBL, Point pointTR, int width, int height, double Rayon) {		
		if(Rayon==0.0){
			Function<Point, Point> coordinateChange = Point.alignedCoordinateChange(new Point(0, height-1), pointBL, new Point(width-1,0), pointTR );
			return rawRelief(width, height, coordinateChange);
		} else if(Rayon > 0.0) {
			int rayonCeil = (int) Math.ceil(Rayon);
			Function<Point, Point> coordinateChange = Point.alignedCoordinateChange(new Point( rayonCeil, height-1+rayonCeil), pointBL, new Point(width-1+rayonCeil,rayonCeil), pointTR );
			BufferedImage rawImage = rawRelief(width+2*rayonCeil, height+2*rayonCeil, coordinateChange);
			float[] kernel = kernel(Rayon);
			BufferedImage blurImage = blur(kernel,rawImage);
			return blurImage.getSubimage(rayonCeil, rayonCeil, width, height);
		}
		
		throw new IllegalArgumentException();
	}
	
	/**
	 * Gives an image of shaded relief of the whole map, without a blur
	 * @param width
	 * 			width of an image
	 * @param height
	 * 			height of an image
	 * @param coordinateChange
	 * 			a function used to change coordinates of points from a map to the shaded relief
	 * @return
	 * 			an image of shaded relief of the whole map, without a blur
	 */
	private BufferedImage rawRelief(int width, int height, Function<Point, Point> coordinateChange){
		BufferedImage rawImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Color color;
		double cosAngle;
		Vector3 vector;
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				vector = numericModel.normalAt(projection.inverse((coordinateChange.apply(new Point(j,i)))));
				cosAngle= vector.scalarProduct(sunDirection)/(vector.norm()*sunVectorNorma);
				color = Color.rgb((cosAngle+1)/2,(cosAngle+1)/2,(0.7*cosAngle+1)/2);
				rawImage.setRGB(j, i, color.toAWTColor().getRGB());
			}
		}
		return rawImage;
	}
	
	/**
	 * 
	 * @param Rayon
	 * @return
	 */
	private float[] kernel(double Rayon){
		double delta = Rayon/3;
		int n = (int) (2*Math.ceil(Rayon)+1);
		float[] kernel = new float[n];
		float sum = 0;
		
		for(int i = 0;i<n;i++) {
			double a = -((n-1)/2)+i;
			kernel[i] = (float) (Math.exp(-(a*a)/(2*delta*delta)));
			sum +=kernel[i];
		}
		for(int j = 0;j<n;j++) kernel[j]/=sum;
		return kernel;
	}
	
	/**
	 * 
	 * @param kernel
	 * @param rawImage
	 * @return
	 */
	private BufferedImage blur(float[] kernel, BufferedImage rawImage){
		
		Kernel kernelX = new Kernel(kernel.length, 1, kernel);
		Kernel kernelY = new Kernel(1, kernel.length, kernel);
		
		ConvolveOp convolveX = new ConvolveOp(kernelX, ConvolveOp.EDGE_NO_OP, null);
		ConvolveOp convolveY = new ConvolveOp(kernelY, ConvolveOp.EDGE_NO_OP, null);
		
		return convolveX.filter(convolveY.filter(rawImage, null), null);
		
	}
}
