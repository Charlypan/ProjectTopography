package ch.epfl.imhof.osm;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;

/**
 * Represents a Reader of an OpenStreetMap
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public final class OSMMapReader {
	
	/**
	 * Creates a reader of an OpenStreetMap
	 */
	private OSMMapReader() {}
	
	/**
	 * Reads data from a file and creates a new map using it
	 * @param fileName
	 * 			Name of the file from which the data will be read
	 * @param unGZip
	 * 			Boolean stating if the given file is a GZIP file
	 * @return
	 * 			A new Map with the data from a given file
	 * @throws IOException
	 * 			when there are any errors with the input
	 * @throws SAXException
	 * 			when there are any errors with parsing the XML file
	 */
	public static OSMMap readOSMFile(String fileName, boolean unGZip) throws IOException, SAXException {
		OSMMap.Builder mapBuilder = new OSMMap.Builder();
		try( BufferedInputStream i = unGZip ? new BufferedInputStream(new GZIPInputStream(new FileInputStream(fileName))) : new BufferedInputStream(new FileInputStream(fileName)) ){
			XMLReader r = XMLReaderFactory.createXMLReader();
		   	r.setContentHandler(new DefaultHandler() {
		   		
				private OSMWay.Builder wayBuilder;
			   	private OSMNode.Builder nodeBuilder;
			   	private OSMRelation.Builder relationBuilder;
				private PointGeo position;
				private long id;
			   	private String key;
			   	private String value;
			   	private String ref;
			   	private Type type;
			   	private String role;
    			
			   	@Override
		    	public void startElement(String uri,
		                                 String lName,
		                                 String qName,
		                                 Attributes atts)
		                              		   throws SAXException {
		    		switch(qName){
		    			case "node": 
			    			id = Long.parseLong(atts.getValue("id"));
			    			position = new PointGeo(Math.toRadians(Double.parseDouble(atts.getValue("lon"))), Math.toRadians(Double.parseDouble(atts.getValue("lat"))));
					       	nodeBuilder = new OSMNode.Builder(id, position);
					       	break;
			        	
			    		case "way":
			    			id = Long.parseLong(atts.getValue("id"));
			    			wayBuilder = new OSMWay.Builder(id);
			    			break;
			    			
			    		case "relation":
			    			id = Long.parseLong(atts.getValue("id"));
			    			relationBuilder = new OSMRelation.Builder(id);
			    			break;
			    				
			    		case "nd":
			    			id = Long.parseLong(atts.getValue("ref"));
			        		break;
			        			
			    		case "tag":
			    			key = atts.getValue("k");
			    			value = atts.getValue("v");
			    			
			    			break;
			    		
			    		case "member":
			    			type = Type.valueOf(atts.getValue("type").toUpperCase());
			        		ref = atts.getValue("ref");
			    			role = atts.getValue("role");
			    			break;
			    			
			    			
		    		}

			   	}

		        @Override
		        public void endElement(String uri,
		                               String lName,
		                               String qName) {
		        	switch(qName){
		        		case "node": 
		    				mapBuilder.addNode(nodeBuilder.build());
		    				nodeBuilder = null;
				        	break;
				        	
		    			case "way":
		    				if(!wayBuilder.isIncomplete()){
		    					mapBuilder.addWay(wayBuilder.build());
		    				}
		    				wayBuilder = null; 	
		    				break;		
		    				
		    			case "relation":
		    				if(!relationBuilder.isIncomplete()){
		    					mapBuilder.addRelation(relationBuilder.build());
		    				}
		    				relationBuilder = null;
		    				break;
		    				
		    			case "nd":
		    				if(mapBuilder.nodeForId(id)!=null){
		    					wayBuilder.addNode(mapBuilder.nodeForId(id));
		    				} else { wayBuilder.setIncomplete(); }
		        			break;
		        			
		    			case "tag":
		    				if(nodeBuilder!=null) nodeBuilder.setAttribute(key,value);
		    				else if(wayBuilder!=null) wayBuilder.setAttribute(key, value);
		    				else if(relationBuilder!=null) relationBuilder.setAttribute(key, value);
		    				break;
		    				
		    			case "member":
		    				if(mapBuilder.wayForId(Long.parseLong(ref))!=null){	
		    					relationBuilder.addMember(type, role, mapBuilder.wayForId(Long.parseLong(ref)));
		    				} else {
		    					relationBuilder.setIncomplete();
		    				}
		    				break;
	    				} 

		        }
			  });
		      r.parse(new InputSource(i));
			} 
		return mapBuilder.build();
	}
}