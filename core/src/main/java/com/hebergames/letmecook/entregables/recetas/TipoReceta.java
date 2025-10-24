package com.hebergames.letmecook.entregables.recetas;

import com.hebergames.letmecook.entregables.productos.TipoProducto;

import java.util.ArrayList;
import java.util.Arrays;

public enum TipoReceta {
    HAMBURGUESA("Hamburguesa", TipoProducto.HAMBURGUESA, "Carne (Cocido)", "Pan");

    private final String nombre;
    private final TipoProducto tipoProducto;
    private final String[] ingredientesRequeridos;

    TipoReceta(String nombre, TipoProducto tipoProducto, String... ingredientes) {
        this.nombre = nombre;
        this.tipoProducto = tipoProducto;
        this.ingredientesRequeridos = ingredientes;
    }

    public RecetaGenerica crear() {
        ArrayList<String> ingredientes = new ArrayList<>(Arrays.asList(ingredientesRequeridos));
        return new RecetaGenerica(nombre, ingredientes, tipoProducto);
    }

    public String getNombre() { return nombre; }
    public TipoProducto getTipoProducto() { return tipoProducto; }
}
