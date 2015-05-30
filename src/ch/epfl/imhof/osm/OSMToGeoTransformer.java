package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.projection.Projection;

public final class OSMToGeoTransformer {
		
	private static final Set<String> areaAttributes = new HashSet<>(Arrays.asList("aeroway", "amenity", "building", "harbour", "historic",
		"landuse", "leisure", "man_made", "military", "natural",
		"office", "place", "power", "public_transport", "shop",
		"sport", "tourism", "water", "waterway", "wetland"));
	private static final Set<String> polylinesAttributes = new HashSet<>(Arrays.asList("bridge", "highway", "layer", "man_made", "railway",
	    "tunnel", "waterway"));
	private static final Set<String> polygonAttributes = new HashSet<>(Arrays.asList("building", "landuse", "layer", "leisure", "natural",
	    "waterway"));
	private Projection projection;

	/**
	 * Gives a type of projection used in a transformer
	 * @param projection
	 * 			A type of projection used in a transformer
	 */
	public OSMToGeoTransformer(Projection projection){
		this.projection = projection;
	}
	
	/**
	 * Transforms an OSMMap passed as an argument to a map of Polygons and PolyLines
	 * @param map
	 * 			An OSMMap that will be transformed
	 * @return
	 * 			A Map transformed with the projection of the constructor
	 */
	public Map transform(OSMMap map){		
		Map.Builder mapBuilder = new Map.Builder();
		
		/*	
		 * Goes through all the ways of the map, and constructs for each Way a PolyLines or a Polygon.
		 * Filter the needed attributes, then builds an Attributed<N> with the corresponding PolyLines or Polygon.
		 */
		Attributes attributesKeepKeysOnly;
		for(OSMWay way : map.ways()){
			if(way.attributes()!=null){
				if(way.isClosed()) {
					if(("1").equals(way.attributes().get("area")) 
							|| ("yes").equals(way.attributes().get("area")) 
							|| ("true").equals(way.attributes().get("area"))
							|| hasArea(way.attributes())) {	// Implies is an area builds an Attributed<Polygon>
						attributesKeepKeysOnly = way.attributes().keepOnlyKeys(polygonAttributes);
						if(attributesKeepKeysOnly!=null && !attributesKeepKeysOnly.isEmpty()){
							mapBuilder.addPolygon(new Attributed<Polygon> (new Polygon( (ClosedPolyLine) polyLineBuilderFromWay(true, way)), attributesKeepKeysOnly) ) ;
						}
					} else { // Implies it's a closed PolyLine so builds an Attributed<ClosedPolyLine>
						attributesKeepKeysOnly = way.attributes().keepOnlyKeys(polylinesAttributes);
						if(attributesKeepKeysOnly!=null && !attributesKeepKeysOnly.isEmpty()){
							mapBuilder.addPolyLine(new Attributed<PolyLine>(polyLineBuilderFromWay(true, way), attributesKeepKeysOnly)) ;	
						}
					}
				} else { // Implies it's an open PolyLine so builds an Attributed<OpenPolyLine>
					attributesKeepKeysOnly = way.attributes().keepOnlyKeys(polylinesAttributes);
					if(attributesKeepKeysOnly!=null && !attributesKeepKeysOnly.isEmpty()){
						mapBuilder.addPolyLine(new Attributed<PolyLine>(polyLineBuilderFromWay(false, way), attributesKeepKeysOnly)) ;	

					}
				}
			}
		}

		/*
		 * Goes through the relations and builds a list of Polygons with holes for each relation.
		 */
		List<Attributed<Polygon>> assembledPolygons;
		for(OSMRelation relation : map.relations()){
			if(relation.attributes().contains("type") && relation.attributes().get("type").equals("multipolygon")){
				attributesKeepKeysOnly = relation.attributes().keepOnlyKeys(polygonAttributes);
				if(!attributesKeepKeysOnly.isEmpty() && attributesKeepKeysOnly!=null){ 
					assembledPolygons = assemblePolygon(relation);
					if(assembledPolygons!=null){
						for(Attributed<Polygon> attributedPolygon : assembledPolygons){
							mapBuilder.addPolygon(attributedPolygon);
						}
					}
				}
			}
		}
		return mapBuilder.build();
	}
	
	/**
	 * Creates a list of closed polyLines with a given role from a given relation
	 * @param relation
	 * 			A relation from which the polyLines will be extracted
	 * @param role
	 * 			A role of closed polyLines
	 * @return
	 * 			A List of ClosedPolyLines from the relation which have the required role
	 */
	private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role){
		
		/*
		 * Goes through the ways of the relation that satisfy the current role.
		 * Adds each node of the way to a graph and links it to his neighbors.
		 * Builds the graph
		 */
		Graph.Builder<OSMNode> graphBuilder = new Graph.Builder<OSMNode>();
		OSMWay way;
		for(Member member : relation.members()){
			if(member.type().toString()=="WAY" && member.role().equals(role)){
				way = (OSMWay) member.member();
                for (int i = 0; i < way.nodes().size() - 1; i++){
                	graphBuilder.addNode(way.nodes().get(i));
                	graphBuilder.addNode(way.nodes().get(i+1));
                	graphBuilder.addEdge(way.nodes().get(i), way.nodes().get(i+1));
                }
			}
		}
		Graph<OSMNode> graph = graphBuilder.build();
		for(OSMNode node : graph.nodes()){
			if(graph.neighborsOf(node).size()!=2){return new ArrayList<ClosedPolyLine>();}
		}

		/*
		 * Goes through all the nodes of a graph to construct a list of ClosedPolyLines (rings) 
		 */
		List<ClosedPolyLine> finalPolyLineList = new ArrayList<ClosedPolyLine>();
		boolean creatingPolyLine;	
		ArrayList<OSMNode> unusedNodes = new ArrayList<OSMNode>(graph.nodes());
		OSMNode currentNode;
		PolyLine.Builder lineBuilder;
		
		while(unusedNodes.size()>0){
			lineBuilder = new PolyLine.Builder();
			currentNode = unusedNodes.get(0);
			creatingPolyLine=true;
			do {
				lineBuilder.addPoint(projection.project(currentNode.position()));
				unusedNodes.remove(currentNode);
				currentNode = unusedNeighbor(unusedNodes, graph.neighborsOf(currentNode));
				if(currentNode==null){
					finalPolyLineList.add(lineBuilder.buildClosed());
					creatingPolyLine=false;
				}
			} while (creatingPolyLine);
		}
		return finalPolyLineList;
	}
	
	/**
	 * Creates a list of Attributed<Polygons> with holes from a relation
	 * @param relation
	 * 			A relation from which the polygons with holes will be extracted
	 * @return
	 * 			A list of Attributed<Polygon>
	 */
	private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation){
		
		/*
		 * Goes through all the relations and adds each inner way to innerWays and each outer way to outerWays.
		 */
		List<Attributed<Polygon>> polygonList = new ArrayList<Attributed<Polygon>>();
		List<ClosedPolyLine> outerLines = ringsForRole(relation, "outer");
		if(outerLines.isEmpty()) return null;
		Collections.sort(outerLines, new ComparatorOfAreas());
		List<ClosedPolyLine> innerLines = ringsForRole(relation, "inner");		
		HashMap<ClosedPolyLine,List<ClosedPolyLine>> polygonMap = new HashMap<ClosedPolyLine,List<ClosedPolyLine>>(); 
		
		
		/* 
		 * Puts an OuterLine key (with an empty arrayList of closedPolyLines as a value)to a map that will represent Polygons and its corresponding holes
		 */
		for(ClosedPolyLine initalator : outerLines){ polygonMap.put(initalator, new ArrayList<ClosedPolyLine>());}

		/*
		 * Adds pairs of shells of a polygon and a list of its corresponding holes to a Map
		 */
		for(ClosedPolyLine outerLine : outerLines){
			Iterator<ClosedPolyLine> iterator = innerLines.iterator();
			while(iterator.hasNext()){
				ClosedPolyLine currentLine = iterator.next();
				if(outerLine.containsPoint(currentLine.firstPoint()) && (outerLine.area() > currentLine.area()))
				{
					polygonMap.get(outerLine).add(currentLine);
					iterator.remove();
				}
			}
		}
		
		/*
		 * Constructs a new Attributed<Polygon> for each Polygon with its corresponding holes.
		 * Each Polygon has the same attributes as the relation.
		 * Adds each Attributed<Polygon> to the polygonList
		 */
		Attributes attributesKeepKeysOnly = relation.attributes().keepOnlyKeys(polygonAttributes);;
		for(ClosedPolyLine shell : polygonMap.keySet()){
			polygonList.add( new Attributed<Polygon> ( new Polygon(shell, polygonMap.get(shell)), attributesKeepKeysOnly) );
		}
		return polygonList;
	}
	
	
	/**
	 * Creates a PolyLine using an OSMWay
	 * @param closed
	 * 			Boolean stating whether an OSMWay is closed
	 * @param way
	 * 			An OSMWay which will be used to create a PolyLine
	 * @return
	 * 			A PolyLine built from an OSMWay
	 */
	private PolyLine polyLineBuilderFromWay(boolean closed, OSMWay way) {
		
		PolyLine.Builder polylineBuilder = new PolyLine.Builder();
		for(OSMNode node : way.nonRepeatingNodes()){
			polylineBuilder.addPoint(projection.project(node.position()));
		}
		return closed ? polylineBuilder.buildClosed() : polylineBuilder.buildOpen();
	}
	
	/**
	 * Gives a node from a set that hasn't been "used" yet, which means a node from a set which is contained in a list of unused nodes
	 * @param unusedNodes
	 * 			An ArrayList of nodes that haven't been used yet
	 * @param set
	 * 			A set of of nodes from which an unused one will be chosen
	 * @return
	 * 			Null if an unused node doesn't exist, an unused node otherwise
	 */
	private OSMNode unusedNeighbor(ArrayList<OSMNode> unusedNodes, Set<OSMNode> set){
		for(OSMNode node : set){
			if(unusedNodes.contains(node)) return node;
		}
		return null;
	}
	
	/**
	 * Checks whether an OSMEntity's attributes have an attribute corresponding to an area 
	 * @param attributes
	 * 			Attributes which will be checked
	 * @return
	 * 			True when attributes contain an attribute corresponding to area, false otherwise
	 */
	private boolean hasArea(Attributes attributes){
		for(String gowno : areaAttributes){
			if(attributes.contains(gowno)) return true;
		}
		return false;
	}
	
	/**
	 * 
	 * Changes the comparator to a comparator of areas of ClosedPolyLines
	 *
	 * @author Rafael Ulises Luzius Pizarro Solar (250976)
	 */
	private static final class ComparatorOfAreas implements Comparator<ClosedPolyLine>  {   
		@Override
		public int compare(ClosedPolyLine c1, ClosedPolyLine c2) {
			if (c1.area() > c2.area())
				return 1;
			else if (c1.area() < c2.area())
				return -1;
			return 0;
        }
    }
}
