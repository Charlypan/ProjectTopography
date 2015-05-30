package ch.epfl.imhof.painting;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.function.Function;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
/**
 * Represents canvas on which images are painted 
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */

public final class Java2DCanvas implements Canvas {
	
	private final BufferedImage bufferedImage;
	private final Graphics2D graphics;
	private final Function<Point, Point> transformer;
	/**
	 * Constructs canvas using passed arguments
	 * @param pointBL
	 * 			bottom-left corner of canvas
	 * @param pointTR
	 * 			top-right corner of canvas
	 * @param width
	 * 			width of canvas in pixels
	 * @param height
	 * 			height of canvas in pixels
	 * @param dpi
	 * 			dots per inch of canvas
	 * @param background
	 * 			color of the background
	 */
	public Java2DCanvas(Point pointBL,Point pointTR, int width, int height, double dpi, Color background){
		double resolution = dpi/72;
		this.transformer=Point.alignedCoordinateChange(pointBL, new Point(0, height/resolution), pointTR, new Point(width/resolution,0));
		this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		graphics = bufferedImage.createGraphics();
		graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		graphics.scale(resolution, resolution);
		graphics.setColor(background.toAWTColor());
		graphics.fillRect(0, 0, width, height);
	}

	@Override
	public void drawPolyLine(PolyLine polyLine, LineStyle lineStyle) {
		/*
		 * Builds the line shape.
		 */
		
		Path2D line = PathDessinateur(polyLine);
		if(polyLine.isClosed()) line.closePath();
		/*
		 * Builds the BasicStoke from lineStyle.
		 */
		int cap = BasicStroke.CAP_BUTT;
		int join = BasicStroke.JOIN_BEVEL;
		switch(lineStyle.lineCap()){
			case butt : cap = BasicStroke.CAP_BUTT;break;
			case round : cap = BasicStroke.CAP_ROUND;break;
			case square : cap = BasicStroke.CAP_SQUARE;break;
		}
		switch(lineStyle.lineJoin()){
			case bevel : join = BasicStroke.JOIN_BEVEL;break;
			case miter : join = BasicStroke.JOIN_MITER;break;
			case round : join = BasicStroke.JOIN_ROUND;break;
		}
		
		float[] dashingPattern=lineStyle.dashingPattern();
		if(lineStyle.dashingPattern().length==0) dashingPattern=null;
		BasicStroke s = new BasicStroke(lineStyle.width(), cap, join, 10.0f, dashingPattern, 0.0f);

		/*
		 * Draws the line in graphics
		 */
		graphics.setStroke(s);
		graphics.setColor(lineStyle.color().toAWTColor());
		graphics.draw(line);
		
	}
	
	@Override
	public void drawPolygon(Polygon polygon, Color color) {

		/*
		 * Builds the shell area of the Polygon.
		 */
		Path2D shellBorder = PathDessinateur(polygon.shell());
		shellBorder.closePath();
		Area polygonArea = new Area(shellBorder);

		/*
		 * Subtracts to the shell area each hole.
		 */
		for(ClosedPolyLine hole : polygon.holes()){
			Path2D holePath = PathDessinateur(hole);
			holePath.closePath();
			polygonArea.subtract(new Area(holePath));
			
		}
		
		/* Draws the Area in graphics */
		graphics.setColor(color.toAWTColor());
		graphics.fill(polygonArea);
		
	}
	/**
	 * Gives the image of canvas
	 * @return the image of canvas
	 */
	public BufferedImage image(){
		return bufferedImage;
	}
	
	
	/**
	 * Creates a Path2D from a polyline		
	 * @param polyline
	 * 		polyline to be used to design a path
	 * @return 
	 * 		The completed Path2D
	 */
	private Path2D PathDessinateur(PolyLine polyline) {
		Iterator<Point> itPoint = polyline.points().iterator();
		Point p = transformer.apply(itPoint.next());
		Path2D line = new Path2D.Double();
		line.moveTo(p.x(),p.y());
		while(itPoint.hasNext()) {
			p = transformer.apply(itPoint.next());
			line.lineTo(p.x(), p.y());
			}
		return line;
		
		
	}
	
	
	
	
}
