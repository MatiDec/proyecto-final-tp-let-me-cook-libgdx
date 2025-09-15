package com.hebergames.letmecook.utiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;

public class GestorAnimacion {
    private Texture spritesheet;
    private TextureRegion[][] regiones;
    private float duracionFrame;

    public GestorAnimacion(String ruta, int frameWidth, int frameHeight, float duracion) {
        spritesheet = new Texture(Gdx.files.internal(ruta));
        regiones = TextureRegion.split(spritesheet, frameWidth, frameHeight);
        this.duracionFrame = duracion;
    }

    public Animation<TextureRegion> getAnimacion(int fila) {
        return new Animation<>(duracionFrame, regiones[fila]);
    }

    public void dispose() {
        if (spritesheet != null) {
            spritesheet.dispose();
        }
    }
}
