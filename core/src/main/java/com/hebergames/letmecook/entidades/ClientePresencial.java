package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class ClientePresencial extends Cliente {

    private TextureRegion texturaCliente;
    private boolean visible;

    public ClientePresencial(float tiempoToleancia, Rectangle ubicacion, TextureRegion textura) {
        super(tiempoToleancia, ubicacion);
        this.texturaCliente = textura;
        this.visible = false;
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
        if (visible && activo && texturaCliente != null) {
            batch.draw(texturaCliente, ubicacion.x, ubicacion.y, ubicacion.width, ubicacion.height);
        }
    }

    @Override
    public void aparecer() {
        visible = true;
        activo = true;
        tiempoActual = 0f;
    }

    @Override
    public void desaparecer() {
        visible = false;
        activo = false;
    }

    public boolean isVisible() {
        return visible;
    }
}
