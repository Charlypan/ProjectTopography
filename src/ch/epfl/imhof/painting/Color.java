package ch.epfl.imhof.painting;

/**
 * Represents a color.
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */

public final class Color {
	
	private final double r, g, b;

    /**
     * Color red (pure).
     */
    public final static Color RED = new Color(1, 0, 0);

    /**
     * Color green (pure).
     */
    public final static Color GREEN = new Color(0, 1, 0);

    /**
     * Color blue (pure).
     */
    public final static Color BLUE = new Color(0, 0, 1);

    /**
     * Color black.
     */
    public final static Color BLACK = new Color(0, 0, 0);

    /**
     * Color white.
     */
    public final static Color WHITE = new Color(1, 1, 1);

    /**
     * Constructs a color with red, green and blue components, whose values must be in interval [0;1].
     *
     * @param r
     *            red component
     * @param g
     *            green component
     * @param b
     *            blue component
     * @throws IllegalArgumentException
     *             if one of the components' value is outside of interval [0;1].
     */
    private Color(double r, double g, double b) {
        if (! (0.0 <= r && r <= 1.0))
            throw new IllegalArgumentException("invalid red component: " + r);
        if (! (0.0 <= g && g <= 1.0))
            throw new IllegalArgumentException("invalid green component: " + g);
        if (! (0.0 <= b && b <= 1.0))
            throw new IllegalArgumentException("invalid blue component: " + b);

        this.r = r;
        this.g = g;
        this.b = b;
    }
    /**
     * Constructs a grey color with one component whose value must be in the interval [0;1].
     * @param a
     * 			intensity of grey color
     * @return
     * 			a new grey color corresponding to the given value
     */
    public static Color gray(double a) {
    	return new Color(a,a,a);
    }
    /**
     * Constructs a color with red, green and blue components, whose values must be in interval [0;1].
     * @param r
     * 			red component
     * @param g
     * 			green component
     * @param b
     * 			blue component
     * @return
     * 			a new color corresponding to given RGB values
     */			
    public static Color rgb(double r, double g, double b) {
    	return new Color(r,g,b);
    }
    
    /**   
     * Constructs a color by unpacking three RGB components, each of them being
     * stocked on 8 bits. R component occupies bits 23 to 16, G component - 15 to 8,
     * and B component - 7 to 0. The components are also supposed to be gamma-encoded
     * according to the standard sRGB.
     * 
     * @param packedRGB
     *          a color encoded in RGB format.
     * @return
     * 			a new color corresponding to the unpacked RGB components
     */
    
    public static Color rgb(int packedRGB) {
        
         return new Color(((packedRGB >> 16) & 0xFF) / 255d,
        		 ((packedRGB >>  8) & 0xFF) / 255d,
        		 ((packedRGB >>  0) & 0xFF) / 255d);
    }
    
    /**
     * Blends two colors together by multiplying them.
     * @param color1
     * 			first color to be blended
     * @param color2
     * 			second color to be blended
     * @return
     * 			a new color created by multiplying two given colors
     */
    
    public static Color multiplier(Color color1, Color color2) {
    	return new Color(color1.r()*color2.r(), color1.g()*color2.g(), color1.b()*color2.b());
    }
    
    /**
     * Converts the current color to an AWT color.
     * @return 
     * 		an AWT color corresponding to the current color
     */
    public java.awt.Color toAWTColor() {
        return new java.awt.Color((float) r,(float) g,(float) b);
    }

    /**
     * Returns the red component of the current color, value in interval [0;1]
     * @return
     *		the red component of the current color
     */
    public double r() { return r; }

    /**
     * Returns the green component of the current color, value in interval [0;1]
     * @return
     *		the green component of the current color
     */
    public double g() { return g; }

    /**
     * Returns the blue component of the current color, value in interval [0;1]
     * @return
     *		the blue component of the current color
     */
    public double b() { return b; }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("(%f, %f, %f)", r, g, b);
    }
}
