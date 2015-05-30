package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
/**
 * Represents a generator of painters for roads
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public final class RoadPainterGenerator {
	/**
	 * Represents properties of a generator of road painters
	 *
	 * @author Michal Piotr Pleskowicz (251034)
	 * @author Rafael Ulises Luzius Pizarro Solar (250976)
	 */

	public static final class RoadSpec{
		private final Predicate<Attributed<?>> filter;
		private final float wi;
		private final Color ci;
		private final float wc;
		private final Color cc;
		/**
		 * Creates a specification of a generator of road painters
		 * @param filter
		 * 			a filter used by a painter
		 * @param wi
		 * 			interior width of a painter
		 * @param ci
		 * 			interior color of a painter
		 * @param wc
		 * 			border width of a painter
		 * @param cc
		 * 			border color of a painter
		 */
        public RoadSpec(Predicate<Attributed<?>> filter, float wi, Color ci, float wc, Color cc){
        	this.filter = filter;
        	this.wi = wi;
        	this.ci = ci;
        	this.wc = wc;
        	this.cc = cc;
        }
        /**
         * Gives the filter used in a generator of road painters
         * @return
         * 			a filter used in a generator of road painters
         */
        public Predicate<Attributed<?>> getFilter(){ return this.filter; }
	}
	/**
	 * Gives a painter with properties passed as an argument
	 * @param roadSpecs
	 * 			specification of a generator of road painters
	 * @return
	 * 			a painter with properties passed as an argument
	 */
	public static Painter painterForRoads(RoadSpec ...roadSpecs) {
		/*
		 * Initializing painters and filters
		 */
		Painter interiorBridge = (m,c) -> {};
		Painter outlineBridge = (m,c) -> {};
		Painter interiorNormal = (m,c) -> {};
		Painter outlineNormal = (m,c) -> {};
		Painter tunnel = (m,c) -> {};
		
		Predicate<Attributed<?>> filterBridge = Filters.tagged("bridge");
		Predicate<Attributed<?>> filterTunnel = Filters.tagged("tunnel");
		
		/*
		 * Stacking painters from roadSpecs
		 */
		for(RoadSpec roadSpec : roadSpecs){
			Predicate<Attributed<?>> filter = roadSpec.getFilter();
			
			LineStyle styleInter = new LineStyle(roadSpec.wi, roadSpec.ci, LineStyle.LineCap.round, LineStyle.LineJoin.round, new float[] {});
			LineStyle styleOuter = new LineStyle(roadSpec.wi+2*roadSpec.wc, roadSpec.cc, LineStyle.LineCap.butt ,LineStyle.LineJoin.round , new float[] {});
			
			interiorBridge = interiorBridge.above(Painter.line(styleInter).when(filterBridge.and(filter))); 				
			outlineBridge = outlineBridge.above(Painter.line(styleOuter).when(filterBridge.and(filter)));
			interiorNormal = interiorNormal.above(Painter.line(styleInter).when(filter.and(filterTunnel.negate()).and(filterBridge.negate())));
			outlineNormal = outlineNormal.above(Painter.line(styleOuter.withLineCap(LineStyle.LineCap.round)).when(filter.and(filterTunnel.negate()).and(filterBridge.negate())));
			
			
			LineStyle styleTunnel = new LineStyle(roadSpec.wi/2, roadSpec.cc, LineStyle.LineCap.butt ,LineStyle.LineJoin.round , new float[] {2*roadSpec.wi,2*roadSpec.wi});
			
			tunnel = tunnel.above(Painter.line(styleTunnel).when(filterTunnel.and(filter)));
		}
		
		return interiorBridge.above(outlineBridge).above(interiorNormal).above(outlineNormal).above(tunnel);
	}



	
}
