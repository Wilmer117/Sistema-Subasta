package model;

/**
 * Resultado de intentar registrar una oferta. Reemplaza un boolean "crudo":
 * un boolean solo dice si funciono, no POR QUE fallo.
 */
public enum ResultadoOferta {

    ACEPTADA("Oferta registrada exitosamente"),
    MONTO_INSUFICIENTE("El monto debe superar la oferta mas alta actual"),
    MISMO_POSTOR("No puedes superar tu propia oferta"),
    SUBASTA_CERRADA("La subasta ya finalizo, no se aceptan mas ofertas");

    private final String mensaje;

    ResultadoOferta(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean esExitosa() {
        return this == ACEPTADA;
    }
}
