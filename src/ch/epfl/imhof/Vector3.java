package ch.epfl.imhof;

public class Vector3 {
	private double a, b, c;
	public Vector3(double a, double b, double c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
	public double norm(){
		double norm = Math.sqrt(a*a+b*b+c*c);
		return norm;
	}
	public Vector3 normalized(){
		return new Vector3(a/this.norm(), b/this.norm(), c/this.norm());
	}
	public double scalarProduct(Vector3 vector){
		return this.a*vector.a+this.b*vector.b+this.c*vector.c;
	}
}
