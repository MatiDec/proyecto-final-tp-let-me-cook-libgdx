package com.hebergames.letmecook.entregables.recetas;

import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.entregables.productos.TipoProducto;

import java.util.ArrayList;

// Nuevo archivo: RecetaGenerica.java
public class RecetaGenerica extends Receta {
    private TipoProducto tipoProducto;

    public RecetaGenerica(String nombre, ArrayList<String> ingredientesRequeridos,
                          TipoProducto tipoProducto) {
        super(nombre, ingredientesRequeridos, tipoProducto.getCategoria());
        this.tipoProducto = tipoProducto;
    }

    @Override
    public Producto preparar() {
        return tipoProducto.crear();
    }
}
