package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

import java.lang.Math;


/**
 * A class that changes from a coordinate system to an other using the swisstopo system CH1903. (Values are not exact)
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */


public final class CH1903Projection implements Projection {
    /**
     * @param project(PointGeo point)
     *      projects the given point on a plane
     * @param inverse(Point point)
     *      does the inverse of project(PointGeo point), using the given point on a plane
     */
    public Point project(PointGeo point){
    	
        double lamda1 = (1.0/10000)*(Math.toDegrees(point.longitude())*3600-26782.5);
        double phi1 = (1.0/10000)*(Math.toDegrees(point.latitude())*3600-169028.66);
        double x =  600072.37
                  + 211455.93 * lamda1
                  - 10938.51 * lamda1*phi1
                  - 0.36 * lamda1*Math.pow(phi1, 2)
                  - 44.54 * Math.pow(lamda1, 3);
        double y =  200147.07
                  + 308807.95 * phi1
                  + 3745.25 * Math.pow(lamda1, 2)
                  + 76.63 * Math.pow(phi1,2)
                  - 194.56 * Math.pow(lamda1, 2) * phi1
                  + 119.79 * Math.pow(phi1, 3);
        return new Point(x,y);
        
    }
    
    public PointGeo inverse(Point point){
        double x1 = (point.x() - 600000)/1000000.0;
        double y1 = (point.y() - 200000)/1000000.0;
        double lamda0 =   2.6779094 
                        + 4.728982 * x1
                        + 0.791484 * x1 * y1
                        + 0.1306 * x1 * Math.pow(y1, 2)
                        - 0.0436 * Math.pow(x1, 3);
        double phi0 = 16.9023892
                    + 3.238272 * y1
                    - 0.270978 * Math.pow(x1, 2)
                    - 0.002528 * Math.pow(y1, 2)
                    - 0.0447 * Math.pow(x1, 2) * y1
                    - 0.0140 * Math.pow(y1, 3);
        double lamda = lamda0 * (100.0/36);
        double phi = phi0 * (100.0/36);
        
        return new PointGeo(Math.toRadians(lamda), Math.toRadians(phi));
    }
    

}
