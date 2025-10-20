package com.hebergames.letmecook.entregables.ingredientes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.estaciones.procesadoras.MetodoCoccion;

public class Carne extends Ingrediente {

    public Carne(TextureRegion textura) {
        super("Carne", textura, MetodoCoccion.HORNO, 5f, 15f);
    }
}
