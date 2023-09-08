package modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

	private static int numeroPedidos = 0;
	private int idPedido;
	private String nombreCliente;
	private String direccionCliente;
	private List<Producto> itemsPedido;

	public Pedido(String n, String d) { nombreCliente = n; direccionCliente = d; idPedido = numeroPedidos; itemsPedido = new ArrayList<>(); } 
	public int getIdPedido() { return idPedido; } 
	public void agregarProducto(Producto nuevoItem) { itemsPedido.add(nuevoItem); } 
	public List<Producto> getItemsPedido() { return itemsPedido; } 
	public int getPrecioNetoPedido() { 
		int p = 0; for (Producto x : itemsPedido) p += x.getPrecio(); return p; } 
	public int getPrecioTotalPedido() { return this.getPrecioNetoPedido() + this.getPrecioIVAPedido(); } 
	public int getPrecioIVAPedido() { return (int) (this.getPrecioNetoPedido() * 0.19); } 
	private String generarTextoFactura() { 
		String t = ""; 
		t += "Factura\n\nPedido " + this.idPedido + "\nNombre: " + this.nombreCliente + "\nDirección: " + this.direccionCliente + "\n"; 
		for (Producto x : itemsPedido) t += x.generarTextoFactura(); 
		t += "\n\nPrecio Neto: $" + this.getPrecioNetoPedido(); 
		t += "\nIVA: $" + this.getPrecioIVAPedido(); 
		t += "\nPrecio Total: $" + this.getPrecioTotalPedido(); return t; } 
	public void guardarFactura(File archivo) throws IOException { 
		PrintWriter b = new PrintWriter(new FileWriter(archivo)); 
		String t = generarTextoFactura(); b.println(t); b.close(); } 
	public boolean equals(Pedido p) { return this.itemsPedido.equals(p.itemsPedido); } 
	public static void incrementarNumeroPedidos() { numeroPedidos++; }

}
