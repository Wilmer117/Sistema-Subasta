package model;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class ArticuloSubasta {

    private final String id;
    private final String nombre;
    private final double precioBase;
    private volatile int tiempoRestante;

    private final PriorityBlockingQueue<Oferta> ofertas;
    private final Set<Postor> postores;

    public ArticuloSubasta(String id, String nombre, double precioBase, int tiempoRestanteSegundos) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del articulo no puede estar vacio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del articulo no puede estar vacio");
        }
        if (precioBase <= 0) {
            throw new IllegalArgumentException("El precio base debe ser positivo");
        }
        if (tiempoRestanteSegundos <= 0) {
            throw new IllegalArgumentException("El tiempo restante debe ser positivo");
        }

        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.tiempoRestante = tiempoRestanteSegundos;
        this.ofertas = new PriorityBlockingQueue<>();
        this.postores = ConcurrentHashMap.newKeySet();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public boolean estaActiva() {
        return tiempoRestante > 0;
    }

    public void reducirTiempo(int segundos) {
        if (segundos < 0) {
            throw new IllegalArgumentException("Los segundos a reducir no pueden ser negativos");
        }
        tiempoRestante = Math.max(0, tiempoRestante - segundos);
    }

    public Oferta getOfertaMasAlta() {
        return ofertas.peek();
    }

    public Set<Postor> getPostores() {
        return new HashSet<>(postores);
    }

    public synchronized ResultadoOferta registrarOferta(Oferta nuevaOferta) {
        if (nuevaOferta == null) {
            throw new IllegalArgumentException("La oferta no puede ser nula");
        }
        if (!estaActiva()) {
            return ResultadoOferta.SUBASTA_CERRADA;
        }

        Oferta ofertaMasAlta = ofertas.peek();
        double montoMinimoRequerido = (ofertaMasAlta != null) ? ofertaMasAlta.getMonto() : precioBase;

        if (nuevaOferta.getMonto() <= montoMinimoRequerido) {
            return ResultadoOferta.MONTO_INSUFICIENTE;
        }
        if (ofertaMasAlta != null && ofertaMasAlta.getPostor().equals(nuevaOferta.getPostor())) {
            return ResultadoOferta.MISMO_POSTOR;
        }

        ofertas.add(nuevaOferta);
        postores.add(nuevaOferta.getPostor());
        notificarPostoresSuperados(nuevaOferta);

        return ResultadoOferta.ACEPTADA;
    }

    private void notificarPostoresSuperados(Oferta nuevaOferta) {
        String mensaje = String.format(
                "Tu oferta en '%s' fue superada. Nueva oferta mas alta: %.2f",
                nombre, nuevaOferta.getMonto()
        );
        for (Postor postor : postores) {
            if (!postor.equals(nuevaOferta.getPostor())) {
                notificar(postor, mensaje);
            }
        }
    }

    private void notificar(Notificable destinatario, String mensaje) {
        destinatario.notificar(mensaje);
    }
}
