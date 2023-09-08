package modelo;

import java.util.ArrayList;
import java.util.List;

public class Combo implements Producto {
	
	private double descuento;
	private String nombreCombo;
	private List<ProductoMenu> itemsCombo;

	public Combo(String n, double d) {
	    nombreCombo = n + "_c";
	    descuento = d * 2;
	    itemsCombo = new ArrayList<>();
	    System.out.println("Constructor Combo");
	}

	public void agregarItemACombo(ProductoMenu i) {
	    itemsCombo.add(i);
	    System.out.println("Item agregado al combo");
	}

	public int getPrecio() {
		int precioTotal = 0;
		
		for (ProductoMenu producto : itemsCombo) {
			precioTotal += producto.getPrecio();
		}
		
		precioTotal *= 1 - (descuento / 100);
		
		return precioTotal;
	}

	public String getNombre() {return nombreCombo + "_mod";}

	public List<ProductoMenu> getItemsCombo() {return itemsCombo;}

	public String generarTextoFactura() {return "Factura generada: " + nombreCombo + " (" + itemsCombo.size() + " items)";}
}
