package consola;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import modelo.Combo;
import modelo.Ingrediente;
import modelo.Pedido;
import modelo.Producto;
import modelo.ProductoAjustado;
import modelo.ProductoMenu;
import procesamiento.LoaderRestaurante;
import procesamiento.Restaurante;

public class Aplicacion {

	private Restaurante restaurante;
	
	/**
	 Este procedimiento se emplea con el fin de desplegar un mensaje en la terminal, 
	 con el propósito de solicitar datos al usuario y posteriormente capturar la entrada del mismo.

	@param mensaje: El mensaje que será exhibido ante el usuario.
	
	@return: La secuencia de caracteres que el usuario ingrese en respuesta.
	 */
	public String input(String mensaje) {
		try {
		    System.out.print(mensaje + ": ");
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    return br.readLine();
		} catch (IOException ex) {
		    System.out.println("Se ha producido una excepción al leer desde la consola");
		    ex.printStackTrace();
		}
		return null;
	}

	public void mostrarMenu() {
		System.out.println("\n1. Ver Menú\n2. Iniciar Pedido\n3. Agregar Productos al Pedido\n4. Cerrar Pedido y Factura\n5. Buscar Pedido por ID\n6. Salir\n");
	}

	private void ejecutarMostrarMenu() {	
		Map<Integer, ProductoMenu> menuBase = restaurante.getMenuBase();
	
		System.out.println("\n=== MENÚ ===\n");
	
		System.out.println("Productos:\n");
		menuBase.forEach((key, productoMenu) -> {
		    System.out.println("(" + key + ") " + productoMenu.getNombre() + ": $" + productoMenu.getPrecio());
		});
	
		Map<Integer, Ingrediente> ingredientes = restaurante.getIngredientes();
	
		System.out.println("\nIngredientes:\n");
		ingredientes.forEach((key, ingrediente) -> {
		    System.out.println("(" + key + ") " + ingrediente.getNombre() + ": $" + ingrediente.getCostoAdicional());
		});
	
		Map<Integer, ProductoMenu> bebidas = restaurante.getBebidas();
	
		System.out.println("\nBebidas:\n");
		bebidas.forEach((key, bebida) -> {
		    System.out.println("(" + key + ") " + bebida.getNombre() + ": $" + bebida.getPrecio());
		});
	
		Map<Integer, Combo> combos = restaurante.getCombos();
	
		System.out.println("\nCombos:\n");
		combos.forEach((key, combo) -> {
		    System.out.println("(" + key + ") " + combo.getNombre());
		    System.out.println("Productos del combo: ");
		    combo.getItemsCombo().forEach(producto -> {
		        System.out.println("- " + producto.getNombre());
		    });
		    System.out.println("Precio del combo: $" + combo.getPrecio() + "\n");
		});
	}

	private void ejecutarAgregarProductos() {
		char opc;
		boolean running = true;
		Pedido currentOrder = restaurante.getPedidoEnCurso();
		Map<Integer, ProductoMenu> baseMenu = restaurante.getMenuBase();
		Map<Integer, ProductoMenu> drinks = restaurante.getBebidas();
		Map<Integer, Combo> combos = restaurante.getCombos();

		ejecutarMostrarMenu();
		do {
		    opc = input("Seleccione una opción: ").toUpperCase().charAt(0);
		    if (opc == 'B') {
		        int drinkOption = Integer.parseInt(input("Ingrese el ID de la bebida"));
		        ProductoMenu drink = drinks.get(drinkOption);
		        if (!(drink == null)) {
		            currentOrder.agregarProducto(drink);
		            System.out.println("\nSe agregó la bebida " + drink.getNombre() + " (" + drinkOption + ") " + "correctamente.\n");
		        } else {
		            System.out.println("\nNo existe una bebida con este ID.");
		        }
		    } else if (opc == 'C') {
		        int comboOption = Integer.parseInt(input("Ingrese el ID del combo"));
		        Combo combo = combos.get(comboOption);
		        if (!(combo == null)) {
		            currentOrder.agregarProducto(combo);
		            System.out.println("\nSe agregó el combo " + combo.getNombre() + " (" + comboOption + ") " + "correctamente.\n");
		        } else {
		            System.out.println("\nNo existe un combo con este ID.");
		        }
		    } else if (opc == 'P') {
		        int productOption = Integer.parseInt(input("Ingrese el ID del producto"));
		        ProductoMenu baseProduct = baseMenu.get(productOption);

		        List<Ingrediente> addedIngredients = new ArrayList<>();
		        List<Ingrediente> removedIngredients = new ArrayList<>();

		        char addChoice = input("¿Desea agregar ingredientes?  Si(S) / No(N)").toUpperCase().charAt(0);

		        if (addChoice == 'S') {
		            do {
		                int ingredientID = Integer.parseInt(input("Ingrese el ID del ingrediente a agregar"));
		                Ingrediente ingredient = restaurante.buscarIngrediente(ingredientID);
		                if (!(ingredient == null)) {
		                    addedIngredients.add(ingredient);
		                    System.out.println("\nSe agregó el ingrediente " + ingredient.getNombre() + " (" + ingredientID + ") " + "correctamente.\n");
		                } else {
		                    System.out.println("\nNo existe un ingrediente con este ID.");
		                }
		                addChoice = input("¿Desea agregar otro ingrediente?  Si(S) / No(N)").toUpperCase().charAt(0);
		            } while (addChoice == 'S');
		        }

		        char removeChoice = input("¿Desea eliminar ingredientes?  Si(S) / No(N)").toUpperCase().charAt(0);

		        if (removeChoice == 'S') {
		            do {
		                int ingredientID = Integer.parseInt(input("Ingrese el ID del ingrediente a eliminar"));
		                Ingrediente ingredient = restaurante.buscarIngrediente(ingredientID);
		                if (!(ingredient == null)) {
		                    removedIngredients.add(ingredient);
		                    System.out.println("\nSe eliminó el ingrediente " + ingredient.getNombre() + " (" + ingredientID + ") " + "correctamente.\n");
		                } else {
		                    System.out.println("\nNo existe un ingrediente con este ID.");
		                }
		                removeChoice = input("¿Desea eliminar otro ingrediente?  Si(S) / No(N)").toUpperCase().charAt(0);
		            } while (removeChoice == 'S');
		        }

		        if (!(baseProduct == null)) {
		            Producto product;
		            if (!addedIngredients.isEmpty() || !removedIngredients.isEmpty()) {
		                product = new ProductoAjustado(baseProduct, addedIngredients, removedIngredients);
		            } else {
		                product = baseProduct;
		            }
		            currentOrder.agregarProducto(product);
		            System.out.println("\nSe agregó el producto " + product.getNombre() + " (" + productOption + ") " + "correctamente.\n");
		        } else {
		            System.out.println("\nNo existe un producto con este ID.");
		        }
		    } else if (opc == 'S') {
		        running = false;
		    } else {
		        System.out.println("\nPor favor seleccione una opción válida.");
		    }
		} while (running);

		mostrarPedido(currentOrder);
	}

	private void ejecutarCerraryGuardarPedido() {
		Pedido currentOrder = restaurante.getPedidoEnCurso();
		mostrarPedido(currentOrder);

		List<Producto> orderItems = currentOrder.getItemsPedido();
		if (orderItems.isEmpty()) {
		    System.out.println("\nTu pedido no puede ser procesado en este momento.");
		    return;
		}

		char response = input("¿Desea finalizar su pedido?  Si(S) / No(N)").toUpperCase().charAt(0);

		if (response == 'S') {
		    try {
		        restaurante.cerrarYGuardarPedido();
		        System.out.println("\nTu pedido ha sido registrado exitosamente. Puedes revisar tu factura.");
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
	
	private void ejecutarIniciarPedido() {
		if (!(restaurante.getPedidoEnCurso() == null)) {
			System.out.println("¡Oops! Algo salió mal. Ya hay un pedido");
		} else {
			System.out.println("Bienvenido al sistema de pedidos.");
			System.out.print("Ingrese su nombre ");
			String cliente = input("");

			System.out.print("Ingrese su dirección ");
			String direccion = input("");

			restaurante.iniciarPedido(cliente, direccion);

			System.out.println("\nPedido registrado exitosamente. ¡Gracias!");
		}
	}

	private void mostrarPedido(Pedido pedido) {
		List<Producto> orderItems = pedido.getItemsPedido();
		if (orderItems.isEmpty()) {
		    System.out.println("\nTu pedido está actualmente sin productos.");
		} else {
		    System.out.println("\nDetalle del Pedido:\n");
		    for (Producto item : orderItems) {
		        System.out.println("- " + item.getNombre() + ": $" + item.getPrecio());
		    }
		    System.out.println("\nPrecio Neto: " + pedido.getPrecioNetoPedido() +
		                       "\nIVA: " + pedido.getPrecioIVAPedido() +
		                       "\nPrecio Total: " + pedido.getPrecioTotalPedido());
		}
	}

	private void ejecutarCargarRestaurante() {
		String menuFile = "./data/menu.txt";
		String drinksFile = "./data/bebidas.txt";
		String ingredientsFile = "./data/ingredientes.txt";
		String combosFile = "./data/combos.txt";

		try {
		    restaurante = LoaderRestaurante.cargarArchivos(menuFile, drinksFile, ingredientsFile, combosFile);
		} catch (FileNotFoundException e) {
		    System.out.println("ERROR: Archivo no encontrado.");
		} catch (IOException e) {
		    System.out.println("ERROR: Hubo un problema al leer el archivo.");
		    System.out.println(e.getMessage());
		}
	}
	
	public void ejecutarAplicacion() {
		System.out.println("¡Bienvenido al Restaurante!");

		ejecutarCargarRestaurante();
		Scanner scanner = new Scanner(System.in);
        boolean running = true;

		while (running) {
            try {
                System.out.println("\nOpciones:");
                System.out.println("1. Mostrar Menú");
                System.out.println("2. Iniciar Pedido");
                System.out.println("3. Agregar Productos al Pedido");
                System.out.println("4. Cerrar y Guardar Pedido");
                System.out.println("5. Buscar Pedido");
                System.out.println("6. Salir\n");

                int opcionSeleccionada = Integer.parseInt(scanner.nextLine());

                switch (opcionSeleccionada) {
                    case 1:
                        System.out.println("Mostrando el menú del restaurante...");
                        ejecutarMostrarMenu();
                        break;
                    case 2:
                        System.out.println("Iniciando un nuevo pedido...");
                        ejecutarIniciarPedido();
                        break;
                    case 3:
                        System.out.println("Agregando productos al pedido...");
                        ejecutarAgregarProductos();
                        break;
                    case 4:
                        System.out.println("Cerrando y guardando el pedido...");
                        ejecutarCerraryGuardarPedido();
                        break;
                    case 5:
                        System.out.println("Buscando un pedido...");
                        ejecutarBuscarPedido();
                        break;
                    case 6:
                        System.out.println("\nSaliendo de la aplicación...");
                        running = false;
                        break;
                    default:
                        System.out.println("\nPor favor, seleccione una opción válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nDebe seleccionar uno de los números de las opciones.\n");
            }
        }
		scanner.close();
    }

	private void ejecutarBuscarPedido() {
		int orderID;
		try {
		    orderID = Integer.parseInt(input("\nIngresa el ID del pedido que deseas buscar"));
		} catch (NumberFormatException e) {
		    System.out.println("\nPor favor, introduce un número válido.");
		    return;
		}
		Pedido foundOrder = restaurante.buscarPedido(orderID);
		if (foundOrder == null) {
		    System.out.println("\nNo se encontró ningún pedido con este ID.");
		    return;
		}
		mostrarPedido(foundOrder);
	}
	
	public static void main(String[] argumentos) {
	    Aplicacion aplic = new Aplicacion();
	    aplic.ejecutarAplicacion();
	}

}
