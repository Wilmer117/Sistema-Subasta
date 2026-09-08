package model;

import java.util.Objects;

public class Postor implements Notificable {

    private final String id;
    private final String nombre;

    public Postor(String id, String nombre) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del postor no puede estar vacio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del postor no puede estar vacio");
        }
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public void notificar(String mensaje) {
        System.out.println("[Notificacion -> " + nombre + "] " + mensaje);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Postor)) return false;
        Postor postor = (Postor) o;
        return id.equals(postor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nombre + "(" + id + ")";
    }
}
