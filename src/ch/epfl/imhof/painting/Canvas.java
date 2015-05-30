package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.geometry.PolyLine;
/**
 * Represents methods to be used in canvas on which images are painted
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */

public interface Canvas {
	/**
	 * Paints a polyline using a given line style
	 * @param polyLine
	 * 			a polyline to be painted
	 * @param lineStyle
	 * 			a line style to be used for painting the polyline
	 */
	public abstract void drawPolyLine(PolyLine polyLine, LineStyle lineStyle);
	/**
	 * Paints a polygon using a given color
	 * @param polygon
	 * 			a polygon to be painted
	 * @param color
	 * 			a color to be used for painting the polygon
	 */
	public abstract void drawPolygon(Polygon polygon, Color color);
}
