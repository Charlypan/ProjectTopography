package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Represents a closed PolyLine.
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */

public final class ClosedPolyLine extends PolyLine {
	
    /**
     * Creates a closed polyLine using a list of points
     * @param points
     * 			List of points used to create a closed polyLine
     */
    public ClosedPolyLine(List<Point> points) {
        super(points);
    }
    
    /**
     * Checks whether a polyLine is closed
     * @return
     * 			True, because an instance of ClosedPolyLine is a closed polyLine
     */
    public boolean isClosed(){
        return true;
    }
    
    /**
     * Gives an area created by a closed polyLine
     * @return
     * 			Area created by a close polyLine
     */
    public double area(){
        double area=0;
        int j;
        for (int i = 0; i < points().size(); i++) {
            j=(i+1)%points().size();
            area+=points().get(i).x()*points().get(j).y()-points().get(j).x()*points().get(i).y();
        }
        area=Math.abs(area/2);
        return area;
    }
    
    /**
     * Checks whether a given point is contained in a polygon created by a closed polyLine
     * @param p
     * 			Point to be checked whether it's contained inside a polygon created by a closed polyLine 
     * @return 
     * 			True if the given Point is in the polygon created by a closed polyLine, false otherwise.
     */
    public boolean containsPoint(Point p){
        
        double indice=0;
        int j;
        for (int i = 0; i < points().size(); i++) 
        {
            j=(i+1)%points().size();
            if (points().get(i).y() <= p.y()) 
            {
                if (points().get(j).y()> p.y() && 
(points().get(i).x() - p.x())*(points().get(j).y() - p.y()) 
> (points().get(j).x() -p.x())*(points().get(i).y() -p.y())) 
                {
                    indice+=1;
                }
                
            } 
            else if (points().get(j).y() <= p.y() &&
(points().get(i).x() - p.x())*(points().get(j).y() - p.y()) 
<= (points().get(j).x() -p.x())*(points().get(i).y() -p.y())) 
            {
                indice-=1;                            
            }      
        }
        return (indice != 0);
    }
}    