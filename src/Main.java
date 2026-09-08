import java.util.NoSuchElementException;
import java.util.Scanner;
import model.ArticuloSubasta;
import model.Oferta;
import model.Postor;
import model.ResultadoOferta;
import model.SistemaSubastas;

public class Main {

    private static final SistemaSubastas sistema = new SistemaSubastas();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Sistema de Subastas en Linea ===");

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero("Elige una opcion: ");
            switch (opcion) {
                case 1 -> crearArticulo();
                case 2 -> registrarOferta();
                case 3 -> verOfertaMasAlta();
                case 4 -> avanzarTiempo();
                case 5 -> verPostores();
               
                case 7 -> continuar = false;
                default -> System.out.println("Opcion invalida.\n");
            }
        }
        System.out.println("Hasta luego.");
    }

    private static void mostrarMenu() {
        System.out.println("""
                --------------------------------
                1. Crear articulo
                2. Registrar oferta
                3. Ver oferta mas alta
                4. Avanzar tiempo (cerrar subasta)
                5. Ver postores registrados
                
                7. Salir
                --------------------------------""");
    }

    private static void crearArticulo() {
        try {
            String id = leerTexto("Id del articulo: ");
            String nombre = leerTexto("Nombre del articulo: ");
            double precioBase = leerDouble("Precio base: ");
            int tiempoRestante = leerEntero("Tiempo restante (segundos): ");

            sistema.agregarArticulo(new ArticuloSubasta(id, nombre, precioBase, tiempoRestante));
            System.out.println("Articulo creado correctamente.\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Error al crear el articulo: " + e.getMessage() + "\n");
        }
    }

    private static void registrarOferta() {
        try {
            String articuloId = leerTexto("Id del articulo: ");
            String postorId = leerTexto("Id del postor: ");
            String nombrePostor = leerTexto("Nombre del postor: ");
            double monto = leerDouble("Monto de la oferta: ");

            Postor postor = new Postor(postorId, nombrePostor);
            ResultadoOferta resultado = sistema.registrarOferta(articuloId, new Oferta(monto, postor));

            if (resultado.esExitosa()) {
                System.out.println("Oferta aceptada: " + resultado.getMensaje() + "\n");
            } else {
                System.out.println("Oferta rechazada: " + resultado.getMensaje() + "\n");
            }
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void verOfertaMasAlta() {
        ArticuloSubasta articulo = buscarArticuloOMostrarError();
        if (articulo == null) return;

        Oferta ofertaMasAlta = articulo.getOfertaMasAlta();
        System.out.println(ofertaMasAlta == null
                ? "Aun no hay ofertas registradas.\n"
                : "Oferta mas alta: " + ofertaMasAlta + "\n");
    }

    private static void avanzarTiempo() {
        ArticuloSubasta articulo = buscarArticuloOMostrarError();
        if (articulo == null) return;

        int segundos = leerEntero("Segundos a avanzar: ");
        try {
            articulo.reducirTiempo(segundos);
            System.out.println("Tiempo restante: " + articulo.getTiempoRestante()
                    + " s | Activa: " + articulo.estaActiva() + "\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void verPostores() {
        ArticuloSubasta articulo = buscarArticuloOMostrarError();
        if (articulo == null) return;

        System.out.println("Postores registrados: " + articulo.getPostores() + "\n");
    }

    private static ArticuloSubasta buscarArticuloOMostrarError() {
        String articuloId = leerTexto("Id del articulo: ");
        ArticuloSubasta articulo = sistema.getArticulo(articuloId);
        if (articulo == null) {
            System.out.println("Articulo no encontrado.\n");
        }
        return articulo;
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un numero entero valido.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un numero valido.");
            }
        }
    }
}
