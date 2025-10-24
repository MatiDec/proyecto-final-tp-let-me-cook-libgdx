package com.hebergames.letmecook.entregables.recetas;

import com.hebergames.letmecook.entregables.productos.TipoProducto;

import java.util.ArrayList;
import java.util.Arrays;

public enum TipoReceta {
    HAMBURGUESA_CARNE("Hamburguesa de Carne", TipoProducto.HAMBURGUESA_CARNE, "Carne (Bien hecho)", "Pan"),
    HAMBURGUESA_POLLO("Hamburguesa de Pollo", TipoProducto.HAMBURGUESA_POLLO, "Pollo (Bien hecho)", "Pan"),
    PAPAS_FRITAS("Papas Fritas", TipoProducto.PAPAS_FRITAS, "Envase de Papas", "Papas (Bien hecho)"),
    NUGGETS_POLLO("Nuggets de pollo", TipoProducto.NUGGETS_POLLO, "Envase de Nuggets", "Nuggets (Bien hecho)"),
    AROS_CEBOLLA("Aros de Cebolla", TipoProducto.AROS_CEBOLLA, "Envase de Aros de Cebolla", "Aros de Cebolla (Bien hecho)"),
    RABAS("Rabas", TipoProducto.RABAS, "Envase de Rabas", "Rabas (Bien hecho)"),
    MILANESA_CARNE("Milanesa de Carne", TipoProducto.MILANESA_CARNE, "Bandeja para Milanesa de Carne", "Milanesa de Carne (Bien hecho)"),
    MILANESA_POLLO("Milanesa de Pollo", TipoProducto.MILANESA_POLLO, "Bandeja para Milanesa de Pollo", "Milanesa de Pollo (Bien hecho)");

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
