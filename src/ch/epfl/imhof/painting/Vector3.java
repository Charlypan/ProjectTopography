package ch.epfl.imhof.painting;
/**
 * Represents a vector in R3
 *
 * @author Michal Piotr Pleskowicz (251034)
 * @author Rafael Ulises Luzius Pizarro Solar (250976)
 */
public final class Vector3 {
	private final double a, b, c;
	/**
	 * Constructs a vector in R3
	 * @param a value of the first dimension
	 * @param b value of the second dimension
	 * @param c value of the third dimension
	 */
	public Vector3(double a, double b, double c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
	/**
	 * Calculates the norm of a vector
	 * @return norm of the vector
	 */
	public double norm(){
		double norm = Math.abs(Math.sqrt(a*a+b*b+c*c));
		return norm;
	}
	/**
	 * Gives back a normalized version of the vector
	 * @return normalized vector
	 */
	public Vector3 normalized(){
		return new Vector3(a/this.norm(), b/this.norm(), c/this.norm());
	}
	/**
	 * Calculates the scalar product of two vectors
	 * @param vector second vector to be used in calculations
	 * @return scalar product of two vectors
	 */
	public double scalarProduct(Vector3 vector){
		return this.a*vector.a+this.b*vector.b+this.c*vector.c;
	}
}
