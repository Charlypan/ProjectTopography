package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.painting.SwissPainter;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

public class Main {
	
	private static Projection projection =  new CH1903Projection();
    private static Vector3 sunDirection = new Vector3(-1, 1, 1);

	
	public static void main(String[]  args) throws Exception{
		
		
		String osmFile = args[0]; 
		File hgtFile = new File(args[1]); 
		double latitudeBL = Math.toRadians(Double.parseDouble(args[2])); double longitudeBL = Math.toRadians(Double.parseDouble(args[3]));
		double latitudeTR = Math.toRadians(Double.parseDouble(args[4])); double longitudeTR = Math.toRadians(Double.parseDouble(args[5]));
		int dpi= Integer.parseInt(args[6]); 
		String nameImagePNG = args[7];		
		
		
		
        PointGeo pointBL = new PointGeo(latitudeBL,longitudeBL);
        PointGeo pointTR = new PointGeo(latitudeTR,longitudeTR);
        
        double rayonBlur = 0.0017*(dpi/0.0254);
        
        int height = (int) Math.round(((dpi/0.0254)/25000)*(pointTR.latitude()-pointBL.latitude())*Earth.RADIUS);
        int width =  (int) Math.round(height*(projection.project(pointTR).x()-projection.project(pointBL).x())/(projection.project(pointTR).y()-projection.project(pointBL).y()));
        
        
        
        /*
	     * Doing Relief related actions
	     */
        
	    HGTDigitalElevationModel numericModel = new HGTDigitalElevationModel(hgtFile);
	    ReliefShader reliefShader = new ReliefShader(projection, numericModel, sunDirection);
	    BufferedImage reliefImage = reliefShader.shadedRelief(projection.project(pointBL), projection.project(pointTR), width, height, rayonBlur);
	    numericModel.close();
	    	    
	    BufferedImage finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    
        
		/*
		 * Creation of the Map
		 */
		OSMMap OSMMap = OSMMapReader.readOSMFile(osmFile, true);
        OSMToGeoTransformer Transformer = new OSMToGeoTransformer(new CH1903Projection());
        
        Map map = Transformer.transform(OSMMap);
        
        /*
         * Creation of the Canvas
         */
        
        Java2DCanvas canvas = new Java2DCanvas(projection.project(pointBL), projection.project(pointTR), width, height, dpi, Color.WHITE);
        
        /*
         * Drawing the map with Swiss Painter
         */
        Painter painter = SwissPainter.painter();
	    painter.drawMap(map, canvas);

	    BufferedImage mapImage = canvas.image();

	    
	   /*
	    * Drawing final image
	    */
	    Color colorRelief;
	    Color colorMap;
	    
	    for(int i=0;i<height;i++){
	    	for(int j=0;j<width;j++){
	    		colorRelief = Color.rgb(reliefImage.getRGB(j, i));
	    		colorMap = Color.rgb(mapImage.getRGB(j, i));
	    		finalImage.setRGB(j, i, Color.multiplier(colorRelief, colorMap).toAWTColor().getRGB());
	    	}
	    }
	    
	    
	    /*
	     * Writing created map to IO
	     */
	    
	    ImageIO.write(finalImage, "png", new File(nameImagePNG));
        
	}
}
