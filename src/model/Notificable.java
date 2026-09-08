package model;

/**
 * Contrato para cualquier entidad que deba recibir notificaciones
 * del sistema de subastas (por ejemplo, cuando su oferta es superada).
 */
public interface Notificable {
    void notificar(String mensaje);
}
