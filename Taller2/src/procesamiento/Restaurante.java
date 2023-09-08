package procesamiento;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import modelo.Combo;
import modelo.Ingrediente;
import modelo.Pedido;
import modelo.ProductoMenu;

public class Restaurante {

	private Map<Integer, Ingrediente> ingredientes;
	private Map<Integer, ProductoMenu> menuBase;
	private Map<Integer, ProductoMenu> bebidas;
	private Map<Integer, Combo> combos;
	private Map<Integer, Pedido> pedidos;
	private Pedido pedidoEnCurso;

	public Restaurante(Map<Integer, Ingrediente> ingredients, Map<Integer, ProductoMenu> baseMenu,
            Map<Integer, ProductoMenu> drinks, Map<Integer, Combo> combos) {
		this.ingredientes  = ingredients;
	    this.menuBase  = baseMenu;
	    this.bebidas  = drinks;
	    this.combos = combos;
	    this.pedidos  = new HashMap<Integer, Pedido>();
	}

	public void iniciarPedido(String customerName, String customerAddress) {
		pedidoEnCurso  = new Pedido(customerName, customerAddress);
	}

	public void cerrarYGuardarPedido() throws IOException {
			int id = this.pedidoEnCurso.getIdPedido();
			File file = new File("./facturas/factura" + id + ".txt");
			file.createNewFile();
			pedidoEnCurso.guardarFactura(file);

			boolean found = false;
			for (int i = 0; i < pedidos.keySet().size() && found == false; i++) {
				Pedido pedido = pedidos.get(i);
				if (pedidoEnCurso.equals(pedido)) {
					System.out.println("\nExiste un pedido identico al tuyo.");
					found = true;
				}
			}

			Pedido.incrementarNumeroPedidos(); this.pedidos.put(id, this.pedidoEnCurso); this.pedidoEnCurso = null;
	}


	public Pedido  getPedidoEnCurso() { return this.pedidoEnCurso; }
	public Map<Integer, ProductoMenu> getMenuBase() { return this.menuBase; }
	public Map<Integer, ProductoMenu> getBebidas() { return this.bebidas; }
	public Map<Integer, Combo> getCombos() { return this.combos; }
	public Map<Integer, Ingrediente> getIngredientes() { return this.ingredientes; }
	public Pedido buscarPedido(int ID) { return pedidos.get(ID); }
	public Ingrediente buscarIngrediente(int id) { return ingredientes.get(id); }


}
