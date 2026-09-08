package model;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SistemaSubastas {

    private final ConcurrentMap<String, ArticuloSubasta> articulos;

    public SistemaSubastas() {
        this.articulos = new ConcurrentHashMap<>();
    }

    public void agregarArticulo(ArticuloSubasta articulo) {
        Objects.requireNonNull(articulo, "El articulo no puede ser nulo");
        if (articulos.containsKey(articulo.getId())) {
            throw new IllegalArgumentException("Ya existe un articulo con id: " + articulo.getId());
        }
        articulos.put(articulo.getId(), articulo);
    }

    public ResultadoOferta registrarOferta(String articuloId, Oferta oferta) {
        ArticuloSubasta articulo = articulos.get(articuloId);
        if (articulo == null) {
            throw new NoSuchElementException("Articulo no encontrado: " + articuloId);
        }
        return articulo.registrarOferta(oferta);
    }

    public ArticuloSubasta getArticulo(String articuloId) {
        return articulos.get(articuloId);
    }

    public Collection<ArticuloSubasta> listarArticulos() {
        return articulos.values();
    }
}
