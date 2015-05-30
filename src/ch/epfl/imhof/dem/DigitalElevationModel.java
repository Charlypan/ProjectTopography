package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
/**
 * Represents methods used by terrain elevation model
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public interface DigitalElevationModel extends AutoCloseable {
	public abstract Vector3 normalAt(PointGeo point) throws IllegalArgumentException;
}
