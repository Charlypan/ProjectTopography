package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import org.xml.sax.SAXException;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * Represents represents a model of terrain elevation in a given area
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public final class HGTDigitalElevationModel implements DigitalElevationModel {
	private final PointGeo pointSW;
	private ShortBuffer buffer;
	private final double delta;
	private final int length;
	private final double s;
	
	/**
	 * Creates a model of terrain elevation using an HGT file
	 * @param file file from which the data will be read
	 * @throws IOException when the input stream can't be obtained
	 */
	public HGTDigitalElevationModel(File file) throws IOException, SAXException {
		
		try (FileInputStream stream =
                new FileInputStream(file)) {
           this.buffer = stream.getChannel()
               .map(MapMode.READ_ONLY, 0, file.length())
               .asShortBuffer();
           }

		char NS = file.getName().charAt(0);
		char EW = file.getName().charAt(3);
		
		if((NS!='N' && NS!='S') || (EW!='E' &&  EW!='W') || Math.sqrt(file.length()/2d)%1!=0) throw new IllegalArgumentException();
		
		this.length = (int) Math.sqrt(file.length()/2d);
		this.delta = Math.toRadians(1d)/(length-1);
		s = Earth.RADIUS * delta;

		double latitudeSW; double longitudeSW;
		if(NS=='N') {
			latitudeSW = Math.toRadians(Double.parseDouble(file.getName().substring(1, 3)));
		} else { 
			latitudeSW = -Math.toRadians(Double.parseDouble(file.getName().substring(1, 3)));
		}
		if(EW=='E') {
			longitudeSW = Math.toRadians( Double.parseDouble(file.getName().substring(4, 7)));
		} else {
			longitudeSW = -Math.toRadians( Double.parseDouble(file.getName().substring(4, 7)));
		}
		
		this.pointSW = new PointGeo(longitudeSW,latitudeSW);
		
	}
	
	/**
	 * Closes the buffer
	 * @throws Exception when buffer doesn't exist.
	 */
	@Override
	public void close() throws Exception {
		buffer=null;
	}
	
	/**
	 * Calculates a normal vector at a given point
	 * @param point a point at which the normal vector will be calculated
	 * @throws IllegalArgumentException when the given area doesn't contain this vector
	 */
	public Vector3 normalAt(PointGeo point) throws IllegalArgumentException {
		int i = (int) Math.floor((point.longitude()-pointSW.longitude())/delta);
		int j = length - 1 - (int) Math.ceil((point.latitude()-pointSW.latitude())/delta);
		
		double v1 = s*(Z(i,j+1)-Z(i+1,j+1)+Z(i,j)-Z(i+1,j))/2;
		double v2 = s*(Z(i,j+1)+Z(i+1,j+1)-Z(i,j)-Z(i+1,j))/2;
		double v3 = s*s;
		
		return new Vector3(v1, v2, v3);
	}
	
	/**
	 * Returns a short value from an HGT file, given it's column and row indices
	 * @param i column index
	 * @param j row index
	 * @return
	 */
	private short Z(int i, int j) {
		return buffer.get((j*length+i));
	}
	

}
