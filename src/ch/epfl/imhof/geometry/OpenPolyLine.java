package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Represents an open PolyLine.
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */

public final class OpenPolyLine extends PolyLine {
	
	/**
	 * Creates an open polyLine using a given list of points
	 * @param points
	 * 			List of points that will be used to create an open polyLine
	 */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }
    
    /**
     * Checks whether a polyLine is closed
     * @return
     * 			False, because an instance of OpenPolyLine is an open polyLine (not closed)
     */
    public boolean isClosed(){
            return false;
    }
}