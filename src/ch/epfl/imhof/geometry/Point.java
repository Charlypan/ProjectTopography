package ch.epfl.imhof.geometry;

import java.util.function.Function;

/**
 * A point represented in Cartesian geographical coordinates
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */


public final class Point {
	
    private final double x;
    private final double y;
    
    /**
     * Constructs a point using given longitude and latitude
     *
     * @param x
     *      the coordinates of a point, in meters
     * @param y
     *      the coordinates of a point, in meters
    */
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gives a value of x coordinate
     * @return
     * 			Value of x coordinate
     */
    public double x(){return x;}
    
    /**
     * Gives a value of y coordinate
     * @return
     * 			Value of y coordinate
     */
    public double y(){return y;}
    /**
     * A function changing coordinates from one coordinate system to another
     * @param point1a
     * 			coordinates of a first point in first coordinate system
     * @param point2a
     * 			coordinates of a first point in second coordinate system
     * @param point1b
     * 			coordinates of a second point in first coordinate system
     * @param point2b
     * 			coordinates of a second point in second coordinate system
     * @return a function changing coordinates from one coordinate system to another
     */
    
    public static Function<Point, Point> alignedCoordinateChange(Point point1a, Point point2a, Point point1b, Point point2b){
    	double alpha1 = (point2b.x()-point2a.x())/(point1b.x()-point1a.x());
    	double alpha2 = (point2b.y()-point2a.y())/(point1b.y()-point1a.y());
    	double beta1 = point2a.x() - point1a.x()*alpha1;
    	double beta2 = point2a.y() - point1a.y()*alpha2;
    	return point -> new Point(alpha1 * point.x() + beta1, alpha2 * point.y() + beta2);
    }
}


