package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * A class that changes from a coordinate system to an other using the Equirectagular projection.
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */


public final class EquirectangularProjection implements Projection {
    /**
     * @param project(PointGeo point)
     *      projects the given point on a plane
     * @param inverse(Point point)
     *      does the inverse of project(PointGeo point), using the given point on a plane
     */
    public Point project(PointGeo point){ return new Point(point.longitude(), point.latitude());  }
    
    public PointGeo inverse(Point point){ return new PointGeo(point.x(), point.y()); }

}
