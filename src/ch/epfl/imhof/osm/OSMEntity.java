package ch.epfl.imhof.osm;
import ch.epfl.imhof.Attributes;
/**
 * Abstract class representing an OSMEntity such as a way, relation or node
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public abstract class OSMEntity {
	
	private final long id;
	private final Attributes attributes;
	
	/**
	 * Creates an OSMEntity using an id and attributes 
	 * @param id
	 * 			Identification number of an OSMEntity
	 * @param attributes
	 * 			Attributes of an OSMEntity
	 */
	public OSMEntity(long id, Attributes attributes){
		this.id = id;
		this.attributes = attributes;
	}
	
	/**
	 * Gives the identification number of an OSMEntity
	 * @return
	 * 			Identification number of an OSMEntity
	 */
	public long id() { return id; }
	
	/**
	 * Gives the attributes of an OSMEntity
	 * @return
	 * 			Attributes of an OSMEntity
	 */
	public Attributes attributes() { return attributes; }
	
	/**
	 * Checks whether this OSMEntity has an attribute with a given key
	 * @param key
	 * 			A key to be checked whether it's in the attributes of this OSMEntity
	 * @return
	 * 			True when attributes of this OSMEntity contain a given key, false otherwise
	 */
	public boolean hasAttribute(String key)	{ return attributes.contains(key);	}
	
	/**
	 * Gives the value of an attribute with a given key
	 * @param key
	 * 			A key which value is to be returned
	 * @return
	 * 			A value of an attribute with a given key
	 */
	public String attributeValue(String key){ return attributes.get(key); }
	
	
	public abstract static class Builder{
		
		private final long id;
		private boolean incomplete=false;
		private final Attributes.Builder attributesBuilder = new Attributes.Builder();
		
		/**
		 * Constructs a builder using an id passed as an argument
		 * @param id
		 * 			Identification number of an OSMEntity that will be built
		 */
		public Builder(long id) { this.id=id;}
		
		/**
		 * Gives the identification number of an OSMEntity that will be built
		 * @return
		 * 			The identification number of an OSMEntity that will be built
		 */
		protected long getId() { return id; }
		
		/**
		 * Adds an attribute to the attributes of an OSMEntity
		 * @param key
		 * 			A key of an attribute that will be added to the attributes of an OSMEntity
		 * @param value
		 * 			A value that will be assigned to a given key in attributes of an OSMEntity
		 */
		public void setAttribute(String key, String value)	{ attributesBuilder.put(key, value); }
		
		/**
		 * Sets the status of OSMEntity that's being built to incomplete
		 */
		public void setIncomplete() 						{ this.incomplete = true; }
		
		/**
		 * Sets the status of OSMEntity that's being built to complete
		 */
		public boolean isIncomplete()						{ return this.incomplete; }
		
		/**
		 * Gives attributes of an OSMEntity that's being built
		 * @return
		 * 			The attributes of an OSMEntity that's being built
		 */
		protected Attributes attributes(){ return attributesBuilder.build(); }
	}
}
