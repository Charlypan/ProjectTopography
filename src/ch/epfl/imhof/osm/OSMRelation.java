package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * Represents a relation in OpenStreetMap
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public final class OSMRelation extends OSMEntity{
	
	private final List<Member> members;
	
	/**
	 * Creates an OSMRelation using an identification number, list of members and attributes
	 * @param id
	 * 			Identification number of an OSMRelation
	 * @param members
	 * 			List of members of an OSMRelation
	 * @param attributes
	 * 			Attributes of an OSMRelation
	 */
	public OSMRelation(long id, List<Member> members, Attributes attributes){
		super(id, attributes);
		this.members = Collections.unmodifiableList(new ArrayList<Member>(members));
	}
	
	/**
	 * Gives a list of members of an OSMRelation
	 * @return
	 * 			A list of members of an OSMRelation
	 */
	public List<Member> members(){ return this.members;	}
	
	/**
	 * Represents a member of a relation (an instance of OSMRelation)
	 *
	 * @author Michal Piotr Pleskowicz (251034)
	 * @author Rafael Ulises Luzius Pizarro Solar (250976)
	 */
	
	
	public final static class Member{
		
		private final Type type;
		private final String role;
		private final OSMEntity member;
		
		/**
		 * Creates a new member of an OSMRelation
		 * @param type
		 * 			A type of a member of an OSMRelation
		 * @param role
		 * 			A role of a member of an OSMRelation
		 * @param member
		 * 			An OpenStreetMap entity belonging to this member
		 */
		public Member(Type type, String role, OSMEntity member){
			this.type = type;
			this.role = role;
			this.member = member;
		}
		
		/**
		 * Gives the type of a member of an OSMRelation
		 * @return
		 * 			A type of a member of an relation
		 */
		public Type type(){ return this.type;}
		/**
		 * Gives the role of a member of an OSMRelation
		 * @return
		 * 			A role of a member of an OSMRelation
		 */
		public String role(){ return this.role;	}
		/**
		 * Gives the OpenStreetMap entity belonging to a member of an OSMRelation
		 * @return
		 * 			An OpenStreetMap entity belonging to a member of an OSMRelation
		 */
		public OSMEntity member(){ return this.member;	}
		/**
		 * Represents the type of a member of an OSMRelation
		 */
		public enum Type{ NODE, WAY, RELATION }
	}
	
	
	public final static class Builder extends OSMEntity.Builder{
		
		private final List<Member> members = new ArrayList<Member>();
		
		/**
		 * Creates a builder of an OSMRelation using an identification number
		 * @param id
		 * 			Identification number of an OSMRelation
		 */
		public Builder(long id){ super(id); }
		
		/**
		 * Adds a member to a list of members
		 * @param type
		 * 			Type of a member that will be added
		 * @param role
		 * 			Role of a member that will be added
		 * @param newMember
		 * 			OpenStreetMap entity of a member that will be added
		 */
		public void addMember(Member.Type type, String role, OSMEntity newMember){ 
			this.members.add(new Member(type, role, newMember));
		}
		
		/**
		 * Builds a new OSMRelation using a list of members
		 * @return
		 * 			A new OSMRelation if the state of OSMEntity is set to complete
		 * @throws IllegalStateException
		 * 			When the state of OSMEntity is set to incomplete
		 */
		public OSMRelation build() throws IllegalStateException{
			if (super.isIncomplete()) throw new IllegalStateException(); 
			return new OSMRelation(super.getId(),members,super.attributes());
		}
	}
}
