package ch.epfl.imhof;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * A map represented by a list of polyLines and polygons with attributes
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public final class Map {
	
	private final List<Attributed<PolyLine>> polyLines;
	private final List<Attributed<Polygon>> polygons;
	
	/**
	 * Constructs a map using attributed lists of polyLines and polygons
	 * @param polyLines
	 * 			A list of polyLines with attributes
	 * @param polygons
	 * 			A list of polygons with attributes
	 */
	public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons){
		
		this.polyLines = Collections.unmodifiableList(new ArrayList<Attributed<PolyLine>>(polyLines));
		this.polygons = Collections.unmodifiableList(new ArrayList<Attributed<Polygon>>(polygons));
		
	}
	
	/**
	 * @return 
	 * 			A list of attributed polyLines
	 */
	public List<Attributed<PolyLine>> polyLines(){ return polyLines;}
	
	/**
	 * @return 
	 * 			A list of attributed polygons
	 */
	public List<Attributed<Polygon>> polygons(){ return polygons; }
	
	public static class Builder{
		
		private List<Attributed<PolyLine>> polyLines = new ArrayList<Attributed<PolyLine>>();
		private List<Attributed<Polygon>> polygons = new ArrayList<Attributed<Polygon>>();
		
		/**
		 * Adds an attributed polyLine to a list of attributed polyLines
		 * @param newPolyLine
		 * 			An attributed polyLine that is to be added
		 */
		public void addPolyLine(Attributed<PolyLine> newPolyLine){ polyLines.add(newPolyLine);}
		
		/**
		 * Adds an attributed polygon to a list of attributed polygons
		 * @param newPolygon
		 * 			An attributed polyLine that is to be added
		 */
		public void addPolygon(Attributed<Polygon> newPolygon){ polygons.add(newPolygon);}
		
		/**
		 * Constructs a new map using lists of attributed polyLines and polygons
		 * @return 
		 * 			A map built with lists of attributed polyLines and polygons
		 */
		public Map build(){ return new Map(polyLines, polygons);}
		
	}

}
