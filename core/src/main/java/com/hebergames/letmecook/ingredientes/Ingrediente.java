package com.hebergames.letmecook.ingredientes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Ingrediente {
    protected String nombre;
    protected TextureRegion textura;

    public Ingrediente(String nombre, TextureRegion textura) {
        this.nombre = nombre;
        this.textura = textura;
    }

    public String getNombre() { return nombre; }
    public TextureRegion getTextura() { return textura; }
}
