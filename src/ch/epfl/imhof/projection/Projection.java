package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * An interface with 2 methods to change from a coordinate system to another.
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */


public interface Projection {
    /**
     * @param project(PointGeo point)
     *      projects the given point on a plane
     * @param inverse(Point point)
     *      does the inverse of project(PointGeo point), using the given point on a plane
     */
    public Point project(PointGeo point);
    public PointGeo inverse(Point point);
}
