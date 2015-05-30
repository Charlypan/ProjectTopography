package ch.epfl.imhof;

import java.lang.Math;

/**
 * A point represented in spherical geographical coordinates
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */

public final class PointGeo {
	
    private double longitude;
    private double latitude;
    
    /**
     * Constructs a point using given longitude and latitude
     *
     * @param longitude
     *         The longitude of a point, in radians
     * @param latitude
     *         The latitude of a point, in radians
     * @throws IllegalArgumentException
     *         When longitude is not in interval [-π; π]
     * @throws IllegalArgumentException
     *         When latitude is not in interval [-π/2; π/2]
     */
    public PointGeo(double longitude, double latitude){
        this.longitude = longitude;        
        this.latitude = latitude;
        if( Math.abs(longitude) > Math.PI  || Math.abs(latitude) > (Math.PI)/2 ){
            throw new IllegalArgumentException();
        }
    }
    
   /**
    * @return 
    * 			The longitude of a point 
    */
    public double longitude(){return longitude;}
    
   /**
    * @return 
    * 			The latitude of a point
    */
    public double latitude(){return latitude;}
         
}