package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A map of attributes that links the key (ex "name") to the value (ex "Geneva")
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */

public final class Attributes {
	
	private final Map<String, String> attributes;
	
	/**
	 * Creates an instance of Attributes using a map of attributes passed as a parameter
	 * @param attributes
	 * 			A map of attributes that links a name(String) to a value(String)
	 */
	public Attributes(Map<String, String> attributes){
		this.attributes = Collections.unmodifiableMap(new HashMap<String,String>(attributes));
	}
	
	/**
	 * Checks whether this object's map of attributes is empty
	 * @return 
	 * 			True if this object's map of attributes is empty, false otherwise
	 */
	public boolean isEmpty() { return attributes.isEmpty();}
	
	/**
	 * Checks whether the map of attributes contains a given key
	 * @param key
	 * 			A key to be checked if it's in the map of attributes 
	 * @return 
	 * 			True if the map of attributes contains the given key, false otherwise
	 */
	public boolean contains(String key) { return attributes.containsKey(key);}
	
	/**
	 * Gives a value assigned to a given key
	 * @param key
	 * 			Key which value will be returned
	 * @return 
	 * 			The value assigned to a given key
	 */
	public String get(String key){ return attributes.get(key);}
	
	/**
	 * Gives a value assigned to a given key or a default value in case the key doesn't exist
	 * @param key 
	 * 			A key which value will be checked and maybe returned
	 * @param defaultValue
	 * 			A value which will be returned if the key doesn't exist
	 * @return 
	 * 			A value assigned to a given key or a default value in case the key doesn't exist
	 */
	public String get(String key, String defaultValue){	return attributes.getOrDefault(key, defaultValue);}
	
	/**
	 * Gives an integer value assigned to a given key or a default value in case the key doesn't exist
	 * @param key 
	 * 			A key whose value will be checked and maybe returned
	 * @param defaultValue
	 * 			A value which will be returned if the key doesn't exist
	 * @return 
	 * 			An integer value assigned to a given key or a default value in case the key doesn't exist
	 */
	public int get(String key, int defaultValue){
		try{
			Integer.parseInt(attributes.get(key));
		} catch(NumberFormatException e) {
			return defaultValue;
		}
		return Integer.parseInt(attributes.get(key));
    }
	
	/**
	 * Creates an instance of Attributes from a current instance leaving only attributes whose keys are in a given set of keys to keep
	 * @param keysToKeep
	 * 			A set of keys that will be kept from current map of attributes
	 * @return 
	 * 			A new instance of Attributes that only contains attributes whose keys are in a given list of keys to keep
	 */
	public Attributes keepOnlyKeys(Set<String> keysToKeep){
		Attributes.Builder building = new Attributes.Builder();
		for(String element : keysToKeep){ 
			if(this.contains(element)){
				building.put(element, attributes.get(element));
			}
		}
		return building.build();
	}
	
	
	public final static class Builder{
		
		private Map<String, String> mapBuilder = new HashMap<String,String>();
		
		/**
		 * Puts a given pair of key and value in a map of String and String. It will reassign the value if the given
		 * @param key
		 * 			A key that will be put in a map
		 * @param value
		 * 			A value that will be assigned to a key
		 */
		public void put(String key, String value){
			mapBuilder.put(key, value);
		}
		
		/**
		 * Builds a new instance of Attributes
		 * @return 
		 * 			A new instance of Attributes built from a map of String and String
		 */
		public Attributes build(){ return new Attributes(mapBuilder); }
	}
}
