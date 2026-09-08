import model.ArticuloSubasta;
import model.Oferta;
import model.Postor;
import model.ResultadoOferta;

/**
 * Suite de pruebas manual (sin JUnit). Cada metodo prueba una regla
 * de negocio o una validacion de forma aislada.
 */
public class PruebasAutomaticas {

    private static int total;
    private static int exitosas;

    public static void ejecutar() {
        total = 0;
        exitosas = 0;

        System.out.println("\n===== EJECUTANDO PRUEBAS AUTOMATICAS =====\n");

        probarOfertaValida();
        probarMontoInsuficiente();
        probarMismoPostor();
        probarNotificacionAPostorSuperado();
        probarSubastaCerrada();
        probarValidacionDeEntradas();

        System.out.println("\n===== RESULTADO: " + exitosas + "/" + total + " pruebas pasaron =====\n");
    }

    private static void verificar(boolean condicion, String descripcion) {
        total++;
        if (condicion) {
            exitosas++;
            System.out.println("[OK]    " + descripcion);
        } else {
            System.out.println("[FALLO] " + descripcion);
        }
    }

    private static void probarOfertaValida() {
        ArticuloSubasta articulo = new ArticuloSubasta("T1", "Articulo de prueba", 100.0, 60);
        Postor ana = new Postor("PA", "Ana");

        ResultadoOferta resultado = articulo.registrarOferta(new Oferta(150.0, ana));

        verificar(resultado == ResultadoOferta.ACEPTADA, "Una oferta mayor al precio base se acepta");
        verificar(articulo.getOfertaMasAlta().getMonto() == 150.0, "La oferta mas alta refleja el monto registrado");
    }

    private static void probarMontoInsuficiente() {
        ArticuloSubasta articulo = new ArticuloSubasta("T2", "Articulo de prueba", 100.0, 60);
        Postor ana = new Postor("PA", "Ana");
        Postor luis = new Postor("PL", "Luis");

        articulo.registrarOferta(new Oferta(150.0, ana));
        ResultadoOferta resultado = articulo.registrarOferta(new Oferta(120.0, luis));

        verificar(resultado == ResultadoOferta.MONTO_INSUFICIENTE, "Una oferta menor a la oferta mas alta se rechaza");
    }

    private static void probarMismoPostor() {
        ArticuloSubasta articulo = new ArticuloSubasta("T3", "Articulo de prueba", 100.0, 60);
        Postor ana = new Postor("PA", "Ana");

        articulo.registrarOferta(new Oferta(150.0, ana));
        ResultadoOferta resultado = articulo.registrarOferta(new Oferta(200.0, ana));

        verificar(resultado == ResultadoOferta.MISMO_POSTOR, "El mismo postor no puede superar su propia oferta");
    }

    private static void probarNotificacionAPostorSuperado() {
        ArticuloSubasta articulo = new ArticuloSubasta("T4", "Articulo de prueba", 100.0, 60);
        Postor ana = new Postor("PA", "Ana");
        Postor luis = new Postor("PL", "Luis");

        articulo.registrarOferta(new Oferta(150.0, ana));
        articulo.registrarOferta(new Oferta(200.0, luis));

        verificar(articulo.getPostores().contains(ana) && articulo.getPostores().contains(luis),
                "Ambos postores quedan registrados tras varias ofertas validas");
    }

    private static void probarSubastaCerrada() {
        ArticuloSubasta articulo = new ArticuloSubasta("T5", "Articulo de prueba", 100.0, 10);
        articulo.reducirTiempo(10);

        Postor ana = new Postor("PA", "Ana");
        ResultadoOferta resultado = articulo.registrarOferta(new Oferta(150.0, ana));

        verificar(!articulo.estaActiva(), "estaActiva() es false cuando el tiempo restante llega a 0");
        verificar(resultado == ResultadoOferta.SUBASTA_CERRADA, "No se aceptan ofertas cuando la subasta cerro");
    }

    private static void probarValidacionDeEntradas() {
        boolean lanzoExcepcionPostor = false;
        try {
            new Postor("", "Nombre valido");
        } catch (IllegalArgumentException e) {
            lanzoExcepcionPostor = true;
        }
        verificar(lanzoExcepcionPostor, "Crear un Postor con id vacio lanza IllegalArgumentException");

        boolean lanzoExcepcionOferta = false;
        try {
            new Oferta(-10.0, new Postor("PX", "Test"));
        } catch (IllegalArgumentException e) {
            lanzoExcepcionOferta = true;
        }
        verificar(lanzoExcepcionOferta, "Crear una Oferta con monto negativo lanza IllegalArgumentException");

        boolean lanzoExcepcionArticulo = false;
        try {
            new ArticuloSubasta("T6", "Articulo", -50.0, 60);
        } catch (IllegalArgumentException e) {
            lanzoExcepcionArticulo = true;
        }
        verificar(lanzoExcepcionArticulo, "Crear un ArticuloSubasta con precioBase negativo lanza IllegalArgumentException");
    }
}
