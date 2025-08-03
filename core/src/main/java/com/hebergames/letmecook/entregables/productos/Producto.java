package com.hebergames.letmecook.entregables.productos;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.entregables.ObjetoAlmacenable;

public abstract class Producto implements ObjetoAlmacenable {
    protected String nombre;
    protected TextureRegion textura;

    public Producto(String nombre, TextureRegion textura) {
        this.nombre = nombre;
        this.textura = textura;
    }

    public String getNombre() { return nombre; }
    public TextureRegion getTextura() { return textura; }
}
