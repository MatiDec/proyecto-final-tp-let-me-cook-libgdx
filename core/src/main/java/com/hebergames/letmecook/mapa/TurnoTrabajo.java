package com.hebergames.letmecook.mapa;

import com.hebergames.letmecook.entregables.productos.CategoriaProducto;

public enum TurnoTrabajo {
    MANANA("Mañana", CategoriaProducto.DESAYUNO),
    TARDE("Tarde", CategoriaProducto.ALMUERZO),
    NOCHE("Noche", CategoriaProducto.CENA);

    private String nombre;
    private CategoriaProducto categoriaProductos;

    TurnoTrabajo(String nombre, CategoriaProducto categoria) {
        this.nombre = nombre;
        this.categoriaProductos = categoria;
    }

    public CategoriaProducto getCategoriaProductos() { return this.categoriaProductos; }

    public String getNombre() { return this.nombre; }
}
