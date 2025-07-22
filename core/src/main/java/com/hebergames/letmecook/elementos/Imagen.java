package com.hebergames.letmecook.elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.hebergames.letmecook.utiles.Render;

public class Imagen {

    private Texture t;
    private Sprite s;

    public Imagen(String ruta) {
        t = new Texture(ruta);
        s = new Sprite(t);
    }

    public void dibujar() {
        s.draw(Render.batch);
    }

    public void setSize(int ancho, int alto) {
        s.setSize(ancho, alto);
    }

}
