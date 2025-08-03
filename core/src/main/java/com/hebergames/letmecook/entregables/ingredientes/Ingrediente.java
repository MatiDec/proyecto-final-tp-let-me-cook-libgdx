package com.hebergames.letmecook.entregables.ingredientes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.entregables.ObjetoAlmacenable;

public abstract class Ingrediente implements ObjetoAlmacenable {
    protected String nombre;
    protected TextureRegion textura;

    public Ingrediente(String nombre, TextureRegion textura) {
        this.nombre = nombre;
        this.textura = textura;
    }

    public String getNombre() { return nombre; }
    public TextureRegion getTextura() { return textura; }
}
