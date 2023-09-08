package modelo;

public class ProductoMenu implements Producto {

	private String nombre;
	private int precioBase;

	public ProductoMenu(String n, int p) { nombre = n; precioBase = p; } 
	public String getNombre() { return nombre; } 
	public int getPrecio() { return precioBase; } 
	public String generarTextoFactura() { return "\n- " + getNombre() + ": $" + getPrecio(); }

}
