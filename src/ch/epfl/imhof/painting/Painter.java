package ch.epfl.imhof.painting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;
/**
 * Represents methods of a painter that's able to draw on canvas
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public interface Painter {
	/**
	 * Draws a map on given canvas
	 * @param map 
	 * 			map to be drawn
	 * @param canvas 
	 * 			canvas to draw the map on
	 */
	public void drawMap(Map map, Java2DCanvas canvas);
	/**
	 * Draws a polygon with a given color
	 * @param color 
	 * 			color of the polygon to be drawn
	 * @return
	 * 			a polygon drawn with a given color
	 */
	public static Painter polygon(Color color){
		return (m,c) -> {
			for(Attributed<Polygon> polygon : m.polygons()){
				c.drawPolygon(polygon.value(), color);
			}
		};
	}
	/**
	 * Constructs a painter for the map with a given style
	 * @param style
	 * 			a style that the painter will follow
	 * @return
	 * 			a painter with a given style
	 */
	public static Painter line(LineStyle style){
		return (m,c) -> {
			for(Attributed<PolyLine> polyline : m.polyLines()){
				c.drawPolyLine(polyline.value(), style);
			}
		};
	}
	/**
	 * Creates a painter for the map with a given style, specified by the arguments
	 * @param width
	 * 			width of the painter
	 * @param color
	 * 			color of the painter
	 * @param lineCap
	 * 			line cap of the painter
	 * @param lineJoin
	 * 			line join of the painter
	 * @param dashingPattern
	 * 			dashing pattern of the painter 
	 * @return
	 * 			a painter with a style specified by the arguments
	 */
	public static Painter line(float width, Color color, LineCap lineCap, LineJoin lineJoin, float ...dashingPattern){ 
		return line(new LineStyle(width, color, lineCap, lineJoin, dashingPattern));
	}
	/**
	 * Creates a painter for the map with a given width and color
	 * @param width
	 * 			width of the painter
	 * @param color
	 * 			color of the painter
	 * @return
	 * 			a painter for the map with a given width and color
	 */
	public static Painter line(float width, Color color){
		return line(new LineStyle(width, color));
	}
	/**
	 * Creates an outline painter for the map with a given style
	 * @param style
	 * 			style of the painter
	 * @return 
	 * 			a painter for the map with a given style
	 */
	public static Painter outline(LineStyle style){
		return (m,c) -> {
			for(Attributed<Polygon> polygon : m.polygons()) {
				c.drawPolyLine(polygon.value().shell(), style);
				for(ClosedPolyLine hole : polygon.value().holes()){
					c.drawPolyLine(hole, style);
				}
			}
		};
	}
	/**
	 * Creates an outline painter for the map with a given style, specified by the arguments 
	 * @param width
	 * 			width of a painter
	 * @param color
	 * 			color of a painter
	 * @param lineCap
	 * 			line cap of a painter
	 * @param lineJoin
	 * 			line join of a painter 
	 * @param dashingPattern
	 * 			dashing pattern of a painter
	 * @return
	 * 			an outline painter with a given style, specified by the arguments
	 */
	public static Painter outline(float width, Color color, LineCap lineCap, LineJoin lineJoin, float[] dashingPattern){ 
		return outline(new LineStyle(width, color, lineCap, lineJoin, dashingPattern));
	}
	/**
	 * Creates an outline painter for the map with a given width and color
	 * @param width
	 * 			width of the painter
	 * @param color
	 * 			color of the painter
	 * @return
	 * 			an outline painter for the map with a given width and color
	 */
	public static Painter outline(float width, Color color){
		return outline(new LineStyle(width, color));
	}
	/**
	 * Creates a painter for the map with a filter 
	 * @param filter
	 * 			filter used by the painter
	 * @return
	 * 			a painter for the map, painting only objects specified by the filter
	 */
	public default Painter when(Predicate<Attributed<?>> filter){
		return (m,c) -> {
			List<Attributed<Polygon>> polygons = new ArrayList<Attributed<Polygon>>(m.polygons());
			List<Attributed<PolyLine>> polylines = new ArrayList<Attributed<PolyLine>>(m.polyLines());
			polygons.removeIf(filter.negate());
			polylines.removeIf(filter.negate());
			drawMap(new Map(polylines, polygons), c);
		};
	}
	/**
	 * Puts current painter above the one passed as an argument 
	 * @param painter
	 * 			painter to be put underneath the current one
	 * @return
	 * 			a painter made of two painters, one working under another
	 */
	public default Painter above(Painter painter){
		return (m,c) -> {
			painter.drawMap(m, c);
			drawMap(m,c);
		};
	}
	/**
	 * Stacks painters, layer on layer
	 * @return
	 * 			a painter made from all the layers of painters combined
	 */
	public default Painter layered(){
		Painter finalPainter = (m,c) -> {};
		for(int i=-5; i<=5 ; i++) {
			Predicate<Attributed<?>> filter = Filters.onLayer(i);
			finalPainter = when(filter).above(finalPainter);
		}
		return finalPainter;
	}
}

