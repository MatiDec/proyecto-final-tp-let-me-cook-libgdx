package com.hebergames.letmecook.mapa.niveles;

import com.hebergames.letmecook.entregables.productos.CategoriaProducto;

public enum TurnoTrabajo {
    MANANA("Mañana", CategoriaProducto.DESAYUNO, CategoriaProducto.BEBIDAS),
    TARDE("Tarde", CategoriaProducto.ALMUERZO, CategoriaProducto.BEBIDAS),
    NOCHE("Noche", CategoriaProducto.CENA, CategoriaProducto.BEBIDAS, CategoriaProducto.POSTRE);

    private String nombre;
    private CategoriaProducto[] categoriasProductos;

    TurnoTrabajo(String nombre, CategoriaProducto... categorias) {
        this.nombre = nombre;
        this.categoriasProductos = categorias;
    }

    public CategoriaProducto[] getCategoriasProductos() {
        return this.categoriasProductos;
    }

    // Mantener compatibilidad con código anterior
    @Deprecated
    public CategoriaProducto getCategoriaProductos() {
        return categoriasProductos.length > 0 ? categoriasProductos[0] : null;
    }

    public boolean aceptaCategoria(CategoriaProducto categoria) {
        for (CategoriaProducto cat : categoriasProductos) {
            if (cat == categoria) {
                return true;
            }
        }
        return false;
    }

    public String getNombre() {
        return this.nombre;
    }
}
