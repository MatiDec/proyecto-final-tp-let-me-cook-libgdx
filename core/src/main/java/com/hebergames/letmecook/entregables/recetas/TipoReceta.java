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

    private final String NOMBRE;
    private final TipoProducto TIPO_PRODUCTO;
    private final String[] INGREDIENTES_REQUERIDOS;

    TipoReceta(final String NOMBRE, final TipoProducto TIPO_PRODUCTO, final String... ingredientes) {
        this.NOMBRE = NOMBRE;
        this.TIPO_PRODUCTO = TIPO_PRODUCTO;
        this.INGREDIENTES_REQUERIDOS = ingredientes;
    }

    public RecetaGenerica crear() {
        ArrayList<String> ingredientes = new ArrayList<>(Arrays.asList(INGREDIENTES_REQUERIDOS));
        return new RecetaGenerica(NOMBRE, ingredientes, TIPO_PRODUCTO);
    }

    public String getNombre() { return this.NOMBRE; }
    public TipoProducto getTipoProducto() { return this.TIPO_PRODUCTO; }
}
