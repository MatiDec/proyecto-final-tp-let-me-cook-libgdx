package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class ClientePresencial extends Cliente {

    private TextureRegion texturaCliente;
    private boolean visible;
    private boolean recienAparecido;

    public ClientePresencial(float tiempoToleancia, Rectangle ubicacion, TextureRegion textura) {
        super(tiempoToleancia, ubicacion);
        this.texturaCliente = textura;
        this.visible = false;
    }

    @Override
    public void actualizar(float delta) {
        if (!activo) return;

        this.tiempoActual += delta;

        // Si se agotó el tiempo de tolerancia, desaparece
        if (this.tiempoActual >= this.tiempoToleancia) {
            desaparecer();
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (this.visible && this.activo && this.texturaCliente != null) {
            batch.draw(texturaCliente, ubicacion.x, ubicacion.y, ubicacion.width, ubicacion.height);
        }
    }

    @Override
    public void aparecer() {
        this.visible = true;
        this.activo = true;
        this.tiempoActual = 0f;
        this.recienAparecido = true;
        this.setPedidoAsignado(false);
    }

    @Override
    public void desaparecer() {
        this.visible = false;
        this.activo = false;
    }

    @Override
    public boolean acabaDeAparecer() {
        return this.recienAparecido && !isPedidoAsignado();
    }

    public void setRecienAparecido(boolean recienAparecido) {
        this.recienAparecido = recienAparecido;
    }

    public boolean isVisible() {
        return this.visible;
    }
}
