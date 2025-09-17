package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.pedidos.Pedido;
import com.hebergames.letmecook.utiles.Render;

public abstract class Cliente {
    private static int siguienteId = 1;
    private final int ID_CLIENTE;
    protected float tiempoToleancia;
    protected float tiempoActual;
    protected boolean activo;
    protected Rectangle ubicacion;
    protected SpriteBatch BATCH;
    protected boolean pedidoAsignado;

    public Cliente(float tiempoToleancia, Rectangle ubicacion) {
        this.ID_CLIENTE = siguienteId++;
        this.tiempoToleancia = tiempoToleancia;
        this.tiempoActual = 0f;
        this.activo = true;
        this.ubicacion = ubicacion;
        this.BATCH = Render.batch;
        this.pedidoAsignado = false;
    }

    public abstract void actualizar(float delta);
    public abstract void dibujar(SpriteBatch batch);
    public abstract void aparecer();
    public abstract void desaparecer();

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Rectangle getUbicacion() {
        return ubicacion;
    }

    public float getTiempoRestante() {
        return Math.max(0, tiempoToleancia - tiempoActual);
    }

    public int getIdCliente() { return this.ID_CLIENTE; }

    public abstract boolean acabaDeAparecer();

    public boolean isPedidoAsignado() { return this.pedidoAsignado; }

    protected void setPedidoAsignado(boolean pedidoAsignado) { this.pedidoAsignado = pedidoAsignado;}
}
