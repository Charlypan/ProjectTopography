package ch.epfl.imhof.painting;

/**
 * Represents a style of painted line
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */

public final class LineStyle {

	private final float width;
	private final Color color;
	private final LineCap lineCap;
	private final LineJoin lineJoin;
	private final float[] dashingPattern;
	
	/**
	 * Constructs a style using parameters passed as arguments - width, color, line cap,
	 *  line join and a dashing pattern
	 * @param width
	 * 			width of a line
	 * @param color
	 * 			color of a line
	 * @param lineCap
	 * 			type of line cap
	 * @param lineJoin
	 * 			type of line join
	 * @param dashingPattern
	 * 			dashhing pattern of a line
	 * 
	 */
	public LineStyle(float width, Color color, LineCap lineCap, LineJoin lineJoin, float ...dashingPattern){
		this.width = width;
		this.color = color;
		this.lineCap = lineCap;
		this.lineJoin = lineJoin;
		this.dashingPattern = dashingPattern;
	}
	/**
	 * Constructs a line style using width and color passed as arguments and preset settings 
	 * for the rest of attributes
	 * @param width
	 * 			width of a line
	 * @param color
	 * 			color of a line
	 */
	public LineStyle(float width, Color color){
		this(width,color,LineStyle.LineCap.butt ,LineStyle.LineJoin.bevel , new float[] {});
		}
	public enum LineCap{
		butt, round, square
	}
	public enum LineJoin{
		bevel, miter, round
	}
	/**
	 * Returns width of a line of this style
	 * @return 
	 * 			width of a line of this style
	 */
	public float width(){
		return this.width;
	}
	/**
	 * Returns color of a line of this style
	 * @return 
	 * 			color of a line of this style
	 */
	public Color color(){
		return this.color;
	}
	/**
	 * Returns a line cap of this style
	 * @return 
	 * 			a line cap of this style
	 */
	public LineCap lineCap(){
		return this.lineCap;
	}/**
	 * Returns a line join of this style
	 * @return 
	 * 			a line join of this style
	 */
	public LineJoin lineJoin(){
		return this.lineJoin;
	}/**
	 * Returns dashing pattern of a line of this style
	 * @return 
	 * 			dashing of a line of this style
	 */
	public float[] dashingPattern(){
		return this.dashingPattern;
	}
	/**
	 * Creates a new line style from the current one, changing only its width
	 * @param width
	 * 			new width of a line of this style
	 * @return
	 * 			a new line style created from the current one, with a new value for width
	 */
	public LineStyle withWidth(float width){
		return new LineStyle(width, this.color, LineStyle.LineCap.butt ,LineStyle.LineJoin.bevel , new float[] {});
	}
	/**
	 * Creates a new line style from the current one, changing only its color
	 * @param color
	 * 			new color of a line of this style
	 * @return
	 * 			a new line style created from the current one, with a new value for color
	 */
	public LineStyle withColor(Color color){
		return new LineStyle(this.width, color, LineStyle.LineCap.butt ,LineStyle.LineJoin.bevel , new float[] {});
	}
	/**
	 * Creates a new line style from the current one, changing only its line cap
	 * @param lineCap
	 * 			new line cap of this style
	 * @return
	 * 			a new line style created from the current one, with a new value for line cap
	 */
	public LineStyle withLineCap(LineCap lineCap){
		return new LineStyle(this.width, this.color, lineCap, LineStyle.LineJoin.bevel , new float[] {});
	}
	/**
	 * Creates a new line style from the current one, changing only its line join
	 * @param lineJoin
	 * 			new line join of this style
	 * @return
	 * 			a new line style created from the current one, with a new value for line join
	 */
	public LineStyle withLineJoin(LineJoin lineJoin){
		return new LineStyle(this.width, this.color, LineStyle.LineCap.butt, lineJoin, new float[] {});
	}
	/**
	 * Creates a new line style from the current one, changing only its dashing pattern
	 * @param dashingPattern
	 * 			new dashing pattern of a line of this style
	 * @return
	 * 			a new line style created from the current one, with a new value for dashing pattern
	 */
	public LineStyle withDashingPattern(float[] dashingPattern){
		return new LineStyle(this.width, this.color, LineStyle.LineCap.butt, LineStyle.LineJoin.bevel, dashingPattern);
	}
}
