package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class ClienteVirtual extends Cliente {

    private TextureRegion texturaInactiva;
    private TextureRegion texturaActiva;
    private TextureRegion texturaActual;
    private boolean nuevo;

    public ClienteVirtual(float tiempoToleancia, Rectangle ubicacion,
                          TextureRegion texturaInactiva, TextureRegion texturaActiva) {
        super(tiempoToleancia, ubicacion);
        this.texturaInactiva = texturaInactiva;
        this.texturaActiva = texturaActiva;
        this.texturaActual = texturaInactiva;
    }

    @Override
    public void actualizar(float delta) {
        if (!this.activo) return;

        this.tiempoActual += delta;

        // Si se agotó el tiempo de tolerancia, desaparece
        if (this.tiempoActual >= this.tiempoToleancia) {
            desaparecer();
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (this.texturaActual != null) {
            batch.draw(texturaActual, ubicacion.x, ubicacion.y, ubicacion.width, ubicacion.height);
        }
    }

    @Override
    public void aparecer() {
        this.nuevo = true;
        this.texturaActual = texturaActiva;
        this.activo = true;
        this.tiempoActual = 0f;
    }

    @Override
    public void desaparecer() {
        this.texturaActual = texturaInactiva;
        this.activo = false;
    }

    public boolean acabaDeAparecer() {
        if(this.nuevo) {
            this.nuevo = false;
            return true;
        }
        return false;
    }
}
