package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a polygon with or without holes using a ClosedPolyLine
 * and a list of ClosedPolyLine holes
 * 
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */


public final class Polygon {
	
    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;
    
    /**
     * Creates a polygon using a given closed polyLine as its outer line and a set of closed polyLines as its holes
     * @param shell
     * 			A closed polyLine that will be used as an outer line of a polygon
     * @param holes
     * 			A list of closed polyLines that will be used as holes of a polygon
     */			 
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes){
        this.shell = shell;
        this.holes = Collections.unmodifiableList(new ArrayList<ClosedPolyLine>(holes));
    }    
    
    /**
     * Creates a polygon using a given closed polyLine
     * @param shell
     * 			A closed polyLine used to create a polygon
     */
    public Polygon(ClosedPolyLine shell){
        this.shell = shell;
        this.holes = Collections.unmodifiableList(Collections.emptyList());
    }
    
    /**
     * Gives a closed polyLine serving as an outer line of a polygon
     * @return
     * 			A closed polyLine serving as an outer line of a polygon
     */
    public ClosedPolyLine shell(){ return shell; }
    /**
     * Gives a list of closed polyLines used as holes in a polygon
     * @return
     * 			A list of closed polyLines used as holes in a polygon
     */
    public List<ClosedPolyLine> holes(){ return holes; }
}
