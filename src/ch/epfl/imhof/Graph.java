package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * A graph representing nodes and connections between them
 * It's a generic class, that means that instead of nodes you can use any object N
 * 
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public final class Graph<N> {
	private final Map<N, Set<N>> neighbors;
	
	/**
	 * Creates a new graph using a map of nodes and sets of neighbors assigned to them
	 * @param neighbors
	 * 			A map of nodes and sets of neighbors assigned to them
	 */
	public Graph(Map<N, Set<N>> neighbors){
		/*
		 * Loop to make sure each set of the element is immutable
		 */
		Map<N, Set<N>> constructingNeighbors = new HashMap<N, Set<N>>();
		for(N element : neighbors.keySet()){
			constructingNeighbors.put(element, Collections.unmodifiableSet(new HashSet<N>(neighbors.get(element))));
		}
		this.neighbors = Collections.unmodifiableMap(new HashMap<N, Set<N>>(constructingNeighbors));
	}
	
	/**
	 * Gives a set of nodes contained in this graph
	 * @return 
	 * 			A set of nodes contained in this graph
	 */
	public Set<N> nodes(){	return neighbors.keySet();	}
	
	/**
	 * Gives a set of nodes which are neighbors of a given node
	 * @param node
	 * 			A given node whose set of neighbors will be returned
	 * @return 
	 * 			A set of nodes which are neighbors of a given node
	 * @throws IllegalArgumentException
	 * 			When the graph doesn't contain a given node
	 */
	public Set<N> neighborsOf(N node) throws IllegalArgumentException{
		if(neighbors.containsKey(node)){
			return neighbors.get(node);
		}
		else {
			throw new IllegalArgumentException();
		}
	}
	
	public final static class Builder<N>{
		
		private final Map<N, Set<N>> neighbors = new HashMap<N, Set<N>>();
		
		/**
		 * Adds a new node to the graph
		 * @param n
		 * 			A node that will be added to the graph
		 */
		public void addNode(N n){
			if(!neighbors.containsKey(n)){
				neighbors.put(n, new HashSet<N>());
			}
		}
		
		/**
		 * Adds an edge between two given nodes in a graph 
		 * @param n1
		 * 			First node used to create a connection
		 * @param n2
		 * 			Second node used to create a connection
		 * @throws IllegalArgumentException
		 * 			When at least one node is not contained in a graph
		 */
		public void addEdge(N n1,N n2) throws IllegalArgumentException{
			if(neighbors.containsKey(n1) && neighbors.containsKey(n2)){ 
				neighbors.get(n1).add(n2);
				neighbors.get(n2).add(n1);
				}
			else{
				throw new IllegalArgumentException();
			}	
		}
		
		/**
		 * Builds a graph using a map of nodes and sets of neighbors assigned to them
		 * @return 
		 * 			Graph constructed using a map of nodes and sets of neighbors assigned to them 
		 */
		public Graph<N> build(){ return new Graph<N>(neighbors); }
	}
}
