package com.hebergames.letmecook.entregables.ingredientes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.estaciones.procesadoras.MetodoCoccion;

public enum TipoIngrediente {
    PAN("Pan", null, 0f, 0f),
    CARNE("Carne", MetodoCoccion.HORNO, 5f, 15f),
    POLLO("Pollo", MetodoCoccion.HORNO, 6f, 12f),
    MILANESA_CARNE("Milanesa de Carne", MetodoCoccion.HORNO, 7f, 14f),
    MILANESA_POLLO("Milanesa de Pollo", MetodoCoccion.HORNO, 5f, 13f),
    PAPAS("Papas", MetodoCoccion.FREIDORA, 3f, 12f),
    NUGGETS("Nuggets", MetodoCoccion.FREIDORA, 4f, 13f),
    AROS_CEBOLLA("Aros de Cebolla", MetodoCoccion.FREIDORA, 3f, 9f),
    RABAS("Rabas", MetodoCoccion.FREIDORA, 3f, 8f);

    private final String nombre;
    private final MetodoCoccion metodoCoccion;
    private final float TIEMPO_MINIMO_COCCION;
    private final float TIEMPO_MAXIMO_COCCION;

    TipoIngrediente(String nombre, MetodoCoccion metodoCoccion,
                    float tiempoCoccionMinimo, float tiempoCoccionMaximo) {
        this.nombre = nombre;
        this.metodoCoccion = metodoCoccion;
        this.TIEMPO_MINIMO_COCCION = tiempoCoccionMinimo;
        this.TIEMPO_MAXIMO_COCCION = tiempoCoccionMaximo;
    }

    public IngredienteGenerico crear(TextureRegion textura) {
        if (metodoCoccion != null) {
            return new IngredienteGenerico(nombre, textura, metodoCoccion,
                TIEMPO_MINIMO_COCCION, TIEMPO_MAXIMO_COCCION);
        }
        return new IngredienteGenerico(nombre, textura);
    }

    public String getNombre() { return nombre; }
}
