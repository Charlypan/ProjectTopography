package ch.epfl.imhof.geometry;


import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * A group of points that are linked in certain order, can be seen as polygon lines.
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */

public abstract class PolyLine {
    private final List<Point> pointList;
    
    /**
     * Creates a polyLine using a given list of points
     * @param points
     * 			List of points used to create a polyLine
     */
    public PolyLine(List<Point> points){
        if(points.size()==0){ throw new IllegalArgumentException(); } 
        else { this.pointList = Collections.unmodifiableList(new ArrayList<Point>(points)); }
    }
    
    /**
     * Checks whether a polyLine is closed
     * @return
     * 			True when the first and last point in the list of points is the same, false otherwise
     */
    public abstract boolean isClosed();
    
    /**
     * Gives a list of points of a polyLine
     * @return
     * 			List of points of a polyLine
     */
    public List<Point> points() { return pointList;}
    
    /**
     * Gives the first point in a list of points of a polyLine
     * @return
     * 			The first point in a list of points of a polyLine
     */
    public Point firstPoint()	{ return pointList.get(0); }
 
    public final static class Builder{
        private List<Point> pointNewList = new ArrayList<Point>();
        
        /**
         * Adds a point to a list of points of a polyLine
         * @param newPoint
         * 			A point to be added to a list of points of a polyLine
         */
        public void addPoint(Point newPoint) { this.pointNewList.add(newPoint); }
        
        /**
         * Builds a new open polyLine
         * @return
         * 			A new open polyLine
         */
        public OpenPolyLine buildOpen()	{ return new OpenPolyLine(pointNewList); }
        
        /**
         * Builds a new closed polyLine
         * @return
         * 			A new closed polyLine
         */
        public ClosedPolyLine buildClosed()	{ return new ClosedPolyLine(pointNewList); }
    }
}

