package com.hebergames.letmecook.entregables.ingredientes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.estaciones.procesadoras.MetodoCoccion;

public enum TipoIngrediente {
    PAN("Pan", null, 0f, 0f),
    CARNE("Carne", MetodoCoccion.HORNO, 5f, 15f);

    private final String nombre;
    private final MetodoCoccion metodoCoccion;
    private final float tiempoCoccionMinimo;
    private final float tiempoCoccionMaximo;

    TipoIngrediente(String nombre, MetodoCoccion metodoCoccion,
                    float tiempoCoccionMinimo, float tiempoCoccionMaximo) {
        this.nombre = nombre;
        this.metodoCoccion = metodoCoccion;
        this.tiempoCoccionMinimo = tiempoCoccionMinimo;
        this.tiempoCoccionMaximo = tiempoCoccionMaximo;
    }

    public IngredienteGenerico crear(TextureRegion textura) {
        if (metodoCoccion != null) {
            return new IngredienteGenerico(nombre, textura, metodoCoccion,
                tiempoCoccionMinimo, tiempoCoccionMaximo);
        }
        return new IngredienteGenerico(nombre, textura);
    }

    public String getNombre() { return nombre; }
    public MetodoCoccion getMetodoCoccion() { return metodoCoccion; }
    public boolean esCocinableInterna() { return metodoCoccion != null; }
}
