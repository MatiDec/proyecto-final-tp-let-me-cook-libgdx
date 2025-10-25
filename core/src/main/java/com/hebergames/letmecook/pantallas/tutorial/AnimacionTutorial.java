package com.hebergames.letmecook.pantallas.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimacionTutorial {
    private Animation<TextureRegion> animacion;
    private float tiempoAcumulado;
    private boolean cargado;
    private int cantidadFrames;

    public AnimacionTutorial(String rutaSpritesheet, int frameWidth, int frameHeight, int cantidadFrames, float fps) {
        this.tiempoAcumulado = 0f;
        this.cantidadFrames = cantidadFrames;
        this.cargado = false;

        cargarAnimacion(rutaSpritesheet, frameWidth, frameHeight, fps);
    }

    private void cargarAnimacion(String rutaSpritesheet, int frameWidth, int frameHeight, float fps) {
        try {
            Texture spritesheet = new Texture(Gdx.files.internal(rutaSpritesheet));
            TextureRegion[][] regiones = TextureRegion.split(spritesheet, frameWidth, frameHeight);

            // Convertir la matriz en un array lineal de frames
            TextureRegion[] frames = new TextureRegion[cantidadFrames];
            int indice = 0;

            outerLoop:
            for (TextureRegion[] regione : regiones) {
                for (int columna = 0; columna < regione.length; columna++) {
                    if (indice >= cantidadFrames) break outerLoop;
                    frames[indice++] = regione[columna];
                }
            }

            float duracionFrame = 1f / fps;
            animacion = new Animation<>(duracionFrame, frames);
            animacion.setPlayMode(Animation.PlayMode.LOOP);
            cargado = true;

            System.out.println("Animación tutorial cargada: " + cantidadFrames + " frames desde " + rutaSpritesheet);

        } catch (Exception e) {
            System.err.println("Error cargando animación desde: " + rutaSpritesheet);
            e.printStackTrace();
            cargado = false;
        }
    }

    public void actualizar(float delta) {
        if (!cargado) return;
        tiempoAcumulado += delta;
    }

    public TextureRegion getFrameActual() {
        if (!cargado || animacion == null) return null;
        return animacion.getKeyFrame(tiempoAcumulado);
    }

    public boolean estaCargado() {
        return cargado;
    }

    public void reiniciar() {
        tiempoAcumulado = 0f;
    }

    // No necesitamos dispose porque la textura se maneja automáticamente
}
