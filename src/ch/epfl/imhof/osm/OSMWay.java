package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * Represents a way in OpenStreetMap
 * 
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public final class OSMWay extends OSMEntity {
	
	private final List<OSMNode> nodes;
	
	/**
	 * Creates an OSMWay using an identification number, list of nodes and attributes
	 * @param id
	 * 			Identification number of an OSMWay
	 * @param nodes
	 * 			List of nodes of an OSMWay
	 * @param attributes
	 * 			Attributes of an OSMWay
	 * @throws IllegalArgumentException
	 * 			when there's less than two nodes in a list of nodes of an OSMWay
	 */
	public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) throws IllegalArgumentException{
		super(id, attributes);
		if(nodes.size()<2) throw new IllegalArgumentException();
		this.nodes = Collections.unmodifiableList(new ArrayList<OSMNode>(nodes));
		
	}
	
	/**
	 * Counts number of nodes in a list of nodes of an OSMWay
	 * @return
	 * 			The number of nodes in a list of nodes of an OSMWay
	 */
	public int nodesCount(){ return nodes.size();}
	
	/**
	 * Gives a list of nodes of an OSMWay
	 * @return
	 * 			A list of nodes of an OSMWay
	 */
	public List<OSMNode> nodes(){ return this.nodes;}
	
	/**
	 * Gives a list of nodes of an OSMWay without repetitions
	 * @return
	 * 			A list of nodes of an OSMWay without repetitions
	 */
	public List<OSMNode> nonRepeatingNodes(){
		if(!this.isClosed()){
			return this.nodes;
		} else {
			return this.nodes.subList(0, this.nodes().size()-1);	
			}
		}
	
	/**
	 * Gives the first node of a list of nodes of an OSMWay
	 * @return
	 * 			The first node of a list of nodes of an OSMWay
	 */
	public OSMNode firstNode(){ return nodes.get(0);}
	
	/**
	 * Gives the last node of a list of nodes of an OSMWay
	 * @return
	 * 			The last node of a list of nodes of an OSMWay
	 */
	public OSMNode lastNode(){ return nodes.get(nodes.size()-1);}
	
	/**
	 * Checks whether the way is closed
	 * @return
	 * 			True when the way is closed, false otherwise
	 */
	public boolean isClosed(){
		return firstNode().equals(lastNode()); 
		}
	
	
	public static class Builder extends OSMEntity.Builder {
		
		private final List<OSMNode> nodes=new ArrayList<OSMNode>();
		
		/**
		 * Creates a new builder of an OSMWay using an identification number
		 * @param id
		 * 			Identification number of an OSMWay
		 */
		public Builder(long id){
			super(id);
		}
		
		/**
		 * Adds a node to a list of nodes of an OSMWay
		 * @param newNode
		 * 			A node that will be added to a list of nodes of an OSMWay
		 */
		public void addNode(OSMNode newNode){
			nodes.add(newNode);
		}
		
		/**
		 * Builds a new OSMWay
		 * @return
		 * 			A new OSMWay if the state of OSMEntity is set to complete
		 * @throws IllegalStateException
		 * 			When the state of OSMEntity is set to incomplete
		 */
		public OSMWay build() throws IllegalStateException{
			if (isIncomplete()) throw new IllegalStateException(); 
			return new OSMWay(super.getId(),nodes,super.attributes());
			
		}
		
		@Override
		public boolean isIncomplete() {
			if(nodes.size()<2) return true;
			return super.isIncomplete();
			}
		}
}

