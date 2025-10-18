package com.hebergames.letmecook.utiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import java.util.HashMap;
import java.util.Map;

public class GestorAnimacion {
    private Texture spritesheet;
    private TextureRegion[][] regiones;
    private float duracionFrame;
    private Map<String, Integer> animaciones; // Mapa nombre -> fila

    public GestorAnimacion(String ruta, int frameWidth, int frameHeight, float duracion) {
        spritesheet = new Texture(Gdx.files.internal(ruta));
        regiones = TextureRegion.split(spritesheet, frameWidth, frameHeight);
        this.duracionFrame = duracion;
        animaciones = new HashMap<>();

        registrarAnimacion("vacio", 0);
        registrarAnimacion("carne", 1);
        registrarAnimacion("pan", 2);
    }

    public void registrarAnimacion(String nombre, int filaAnimacion) {
        animaciones.put(nombre.toLowerCase(), filaAnimacion);
    }

    public Animation<TextureRegion> getAnimacionPorObjeto(String nombreObjeto) {
        int fila = animaciones.getOrDefault(nombreObjeto.toLowerCase(), animaciones.get("vacio"));
        return getAnimacion(fila);
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
