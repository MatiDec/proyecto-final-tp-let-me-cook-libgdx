package com.hebergames.letmecook.entregables.ingredientes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.utiles.GestorTexturas;

public enum TipoEnvase {
    ENVASE_PAPAS("Envase de Papas", "Papas (Bien hecho)"),
    ENVASE_NUGGETS("Envase de Nuggets", "Nuggets (Bien hecho)"),
    ENVASE_AROS_CEBOLLA("Envase de Aros de Cebolla", "Aros de Cebolla (Bien hecho)"),
    ENVASE_RABAS("Envase de Rabas", "Rabas (Bien hecho)"),
    ENVASE_MILANESA_CARNE("Bandeja para Milanesa de Carne", "Milanesa de Carne (Bien hecho)"),
    ENVASE_MILANESA_POLLO("Bandeja para Milanesa de Pollo", "Milanesa de Pollo (Bien hecho)");

    private final String nombre;
    private final String ingredienteRequerido;

    TipoEnvase(String nombre, String ingredienteRequerido) {
        this.nombre = nombre;
        this.ingredienteRequerido = ingredienteRequerido;
    }

    public static TipoEnvase obtenerPorIngrediente(String nombreIngrediente) {
        for (TipoEnvase tipo : values()) {
            if (tipo.ingredienteRequerido.equals(nombreIngrediente)) {
                return tipo;
            }
        }
        return null;
    }

    public IngredienteGenerico crearEnvase() {
        TextureRegion textura = GestorTexturas.getInstance().getTexturaIngrediente();
        return new IngredienteGenerico(nombre, textura);
    }

    public String getNombre() {
        return nombre;
    }

    public String getIngredienteRequerido() {
        return ingredienteRequerido;
    }
}
