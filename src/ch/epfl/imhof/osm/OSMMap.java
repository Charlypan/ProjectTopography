package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A map of attributes that links a value (PolyLine, Node, Way) to it's attributes
 *
 * Example, if a Polygon represents a wood zone, that Polygon will have
 * a map of attributes linked to it as ( "natural" : Wood | "ele" : wood number element (OSM) )
 *
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public final class  OSMMap {
	
	private final List<OSMWay> ways;
	private final List<OSMRelation> relations;
	
	/**
	 * Creates a new unmodifiable OpenStreetMap
	 * @param ways
	 * 			A list of ways in an OSMMap
	 * @param relations
	 * 			A list of relations in an OSMMap
	 */
	public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations){
		this.ways = Collections.unmodifiableList(new ArrayList<OSMWay>(ways)); 
		this.relations = Collections.unmodifiableList(new ArrayList<OSMRelation>(relations));
	}
	
	/**
	 * Gives a list of ways in an OSMMap
	 * @return
	 * 			A list of ways in an OSMMap
	 */
	public List<OSMWay> ways(){ return ways; }
	
	/**
	 * Gives a list of relations in an OSMMap
	 * @return
	 * 			A list of relations in an OSMMap
	 * 
	 */
	public List<OSMRelation> relations(){ return relations; }
	
	public static class Builder{
		
		private final Map<Long,OSMNode> nodes = new HashMap<Long,OSMNode>();
		private final Map<Long,OSMWay> ways = new HashMap<Long,OSMWay>();
		private final Map<Long,OSMRelation> relations = new HashMap<Long,OSMRelation>();
		
		/**
		 * Adds a node to a list of nodes in an OSMMap
		 * @param node
		 * 			A node that will be added to a list of nodes in an OSMMap
		 */
		public void addNode(OSMNode node){ nodes.put(node.id(),node);}
		
		/**
		 * Searches for a node with a given identification number in a list of nodes in an OSMMap
		 * @param id
		 * 			Identification number of a node that will be searched
		 * @return
		 * 			OSMNode with a given id or null, if it doesn't exist
		 */
		public OSMNode nodeForId(long id){ return nodes.get(id); }
		
		/**
		 * Adds a way to a list of ways in an OSMMap
		 * @param newWay
		 * 			A way that will be added to a list of ways in an OSMMap
		 */
		public void addWay(OSMWay newWay){ ways.put(newWay.id(),newWay);}
		 
		/**
		 * Searches for a way with a given identification number in a list of ways in an OSMMap
		 * @param id
		 * 			Identification number of a way that will be searched
		 * @return
		 * 			OSMWay with a given id or null, if it doesn't exist
		 */			
		public OSMWay wayForId(long id){ return ways.get(id); }
		
		/**
		 * Adds a relation to a list of relations in an OSMMap
		 * @param realtion
		 * 			A relation that will be added to a list of relations in an OSMMap
		 */
		public void addRelation(OSMRelation relation){ relations.put(relation.id(),relation); }
		
		/**
		 * Searches for a relation with a given identification number in a list of relations in an OSMMap
		 * @param id
		 * 			Identification number of a relation that will be searched
		 * @return
		 * 			OSMRelation with a given id or null, if it doesn't exist
		 */		
		public OSMRelation relationForId(long id){ return relations.get(id); }
		
		/**
		 * Builds an OSMMap using a list of ways and a list of relations
		 * @return
		 * 			A new map built using a list of ways and a list of relations
		 */
		public OSMMap build(){ return new OSMMap(ways.values(), relations.values()); }
	}
		
}
