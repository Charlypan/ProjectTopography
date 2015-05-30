package ch.epfl.imhof;

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


public final class Attributed<T> {
	
	private final T value;
	private final Attributes attributes;
	
	/**
	 * 
	 * @param value
	 * 			Given object that we will assign attributes to
	 * @param attributes
	 * 			An instance of class Attributes that will be assigned to the value
	 */			
	public Attributed(T value, Attributes attributes){
		this.value = value;
		this.attributes = attributes;
	}
	
	/**
	 * @return 
	 * 			A value of an attributed object
	 */
	public T value(){ return value; }
	
	/**
	 * @return 
	 * 			Assigned attributes
	 */
	public Attributes attributes(){	return attributes; }
	
	/**
	 * Checks whether attributes contain a given attribute
	 * @param attributeName
	 * 			Name of an attribute to be checked
	 * @return 
	 * 			True if attributes contain a given attribute, false otherwise
	 */
	public boolean hasAttribute(String attributeName){ return attributes.contains(attributeName); }
	
	/**
	 * Gives the value of an attribute with a given name
	 * @param attributeName
	 * 			Name of the attribute that we want a value of
	 * @return 
	 * 			Value of an attribute with a given name
	 */
	public String attributeValue(String attributeName){	return attributes.get(attributeName); }
	
	/**
	 * Gives the value of an attribute with a given name
	 * @param attributeName
	 * 			Name of the attribute that we want a value of
	 * @param defaultValue
	 * 			Name of the default value 
	 * @return 
	 * 			Value of an attribute with a given name or in case it doesn't exist, the value of the attribute with the given defaultValue 
	 */
	public String attributeValue(String attributeName, String defaultValue){ return attributes.get(attributeName, defaultValue); }
	
	/**
	 * Gives a value of an attribute with a given name or a default value passed as a parameter 
	 * @param attributeName
	 * 			Name of an attribute whose value we want to check
	 * @param defaultValue
	 * 			Default value to return in case the attribute with a given name doesn't exist
	 * @return 
	 * 			Value of an attribute with a given name or a default value
	 */
	public int attributeValue(String attributeName, int defaultValue){ return attributes.get(attributeName, defaultValue); }
}
