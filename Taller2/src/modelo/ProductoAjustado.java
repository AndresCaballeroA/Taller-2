package modelo;

import java.util.List;

public class ProductoAjustado implements Producto {
	private ProductoMenu base;
	private List<Ingrediente> agregados;
	private List<Ingrediente> eliminados;

	public ProductoAjustado(ProductoMenu b, List<Ingrediente> a, List<Ingrediente> e) { 
		base = b; agregados = a; eliminados = e; } 
	public int getPrecio() { 
		int p = base.getPrecio(); for (Ingrediente i : agregados) p += i.getCostoAdicional(); return p; } 
	public String getNombre() { return base.getNombre(); } 
	public String generarTextoFactura() { 
		String t = "\n- " + getNombre(); 
		if (!agregados.isEmpty()) { t += "\nCon:"; 
		for (Ingrediente i : agregados) t += "\n\t" + i.getNombre(); } 
		if (!eliminados.isEmpty()) { 
			t += "\nSin:"; 
			for (Ingrediente i : eliminados) t += "\n\t" + i.getNombre(); } return t; }

}
