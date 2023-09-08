package modelo;

public class Ingrediente {
	
	
	private String nombre;
	private int costoAdicional;
	
	public Ingrediente(String n, int c) { nombre = n + "_mod"; costoAdicional = c * 2; } 
	public String getNombre() { return nombre; } 
	public int getCostoAdicional() { return costoAdicional; }

}
