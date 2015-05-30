package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
/**
 * Represents a filter of objects
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public abstract class Filters implements Predicate<Attributed<?>>{
	
	private Filters() {}
	/**
	 * Creates a filter keeping all the objects whose name is the same as the passed argument
	 * @param key
	 * 			a name of an object to be kept 
	 * @return
	 * 			a new filter keeping all the objects whose name is the same as the passed argument
	 */
	public static Predicate<Attributed<?>> tagged(String key){
		return a -> a.attributes().contains(key);
	}
	
	/**
	 * Creates a filter keeping all the objects with a given name and having one of the given values 
	 * @param key
	 * 			a name of an object to be kept
	 * @param values
	 * 			a list of values to be kept
	 * @return
	 * 			a new filter keeping all the objects with a given name and having one of the given values 
	 */
	public static Predicate<Attributed<?>> tagged(String key, String ...values){
		return a -> {if(a.attributes().contains(key)) {
						for(String value : values){
							if(value.equals(a.attributeValue(key))) return true;
						}
					}
					return false;
			};
		}
	/**
	 * Creates a filter keeping all the objects that are on a given layer
	 * @param layer
	 * 			a layer from which all the objects will be kept
	 * @return
	 * 			a new filter keeping all the objects that are on a given layer
	 */
	public static Predicate<Attributed<?>> onLayer(int layer){
		return a -> a.attributeValue("layer",0)==layer;
	}
	
}
