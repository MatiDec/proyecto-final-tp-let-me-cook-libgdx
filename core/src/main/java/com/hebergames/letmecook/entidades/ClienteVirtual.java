package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class ClienteVirtual extends Cliente {

    private TextureRegion texturaInactiva;
    private TextureRegion texturaActiva;
    private TextureRegion texturaActual;

    public ClienteVirtual(float tiempoToleancia, Rectangle ubicacion,
                          TextureRegion texturaInactiva, TextureRegion texturaActiva) {
        super(tiempoToleancia, ubicacion);
        this.texturaInactiva = texturaInactiva;
        this.texturaActiva = texturaActiva;
        this.texturaActual = texturaInactiva;
    }

    @Override
    public void actualizar(float delta) {
        if (!activo) return;

        tiempoActual += delta;

        // Si se agotó el tiempo de tolerancia, desaparece
        if (tiempoActual >= tiempoToleancia) {
            desaparecer();
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (texturaActual != null) {
            batch.draw(texturaActual, ubicacion.x, ubicacion.y, ubicacion.width, ubicacion.height);
        }
    }

    @Override
    public void aparecer() {
        texturaActual = texturaActiva;
        activo = true;
        tiempoActual = 0f;
    }

    @Override
    public void desaparecer() {
        texturaActual = texturaInactiva;
        activo = false;
    }
}
