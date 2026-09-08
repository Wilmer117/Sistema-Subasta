package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Oferta implements Comparable<Oferta> {

    private final double monto;
    private final Postor postor;
    private final LocalDateTime fechaHora;

    public Oferta(double monto, Postor postor) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto de la oferta debe ser positivo");
        }
        this.postor = Objects.requireNonNull(postor, "El postor no puede ser nulo");
        this.monto = monto;
        this.fechaHora = LocalDateTime.now();
    }

    public double getMonto() {
        return monto;
    }

    public Postor getPostor() {
        return postor;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    @Override
    public int compareTo(Oferta otra) {
        return Double.compare(otra.monto, this.monto);
    }

    @Override
    public String toString() {
        return String.format("Oferta{monto=%.2f, postor=%s}", monto, postor);
    }
}
