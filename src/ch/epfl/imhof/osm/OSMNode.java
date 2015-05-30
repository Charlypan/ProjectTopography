package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;
/**
 * Represents a node in OpenStreetMap
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public final class OSMNode extends OSMEntity{
	
	private final PointGeo position;
	
	/**
	 * Creates an OSMNode using an identification number, position and attributes
	 * @param id
	 * 			Identification number of an OSMNode
	 * @param position
	 * 			Position of an OSMNode
	 * @param attributes
	 * 			Attributes of an OSMNode
	 */
	public OSMNode(long id, PointGeo position, Attributes attributes){
		super(id, attributes);
		this.position = position;
	}
	
	/**
	 * Gives the position of a point
	 * @return
	 * 			Position of a point
	 */
	public PointGeo position(){ return this.position; }
	
	
	public static class Builder extends OSMEntity.Builder{
	
		private final PointGeo position;
		
		/**
		 * Creates a builder of an OSMNode
		 * @param id
		 * 			Identification number of an OSMNode
		 * @param position
		 * 			Position of an OSMNode
		 */
		public Builder(long id, PointGeo position){
			super(id);
			this.position = position;
		}
		
		/**
		 * Builds a new OSMNode
		 * @return
		 * 			A new OSMNode if the state of OSMEntity is set to complete
		 * @throws IllegalStateException
		 * 			When the state of OSMEntity is set to incomplete
		 */
		public OSMNode build() throws IllegalStateException{
			if (super.isIncomplete()) throw new IllegalStateException(); 
			return new OSMNode(this.getId(), position, super.attributes());			
			}
		
	}
}