package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.hebergames.letmecook.eventos.DatosEntrada;

import java.util.Set;

public abstract class Jugador {
    protected Vector2 posicion;
    protected Vector2 velocidad;
    protected TextureRegion frameActual; // Esto tiene que ver con las texturas, se puede usar más adelante o no, lo dejo por si sirve
    protected Animation<TextureRegion> animacion; // esto con las animaciones, quizá es útil para más adelante
    protected float estadoTiempo;

    public final int DISTANCIA_MOVIMIENTO = 200;//Con esto se maneja la "velocidad" de los jugadores

    public Jugador(float x, float y, Animation<TextureRegion> animacion) {
        this.posicion = new Vector2(x, y);
        this.velocidad = new Vector2(0,0);
        this.animacion = animacion;
        this.estadoTiempo = 0;
    }

    public void actualizar(float delta) {
        posicion.add(velocidad.x * delta, velocidad.y * delta);
        estadoTiempo += delta;
        frameActual = animacion.getKeyFrame(estadoTiempo, false);
    }

    public void dibujar(SpriteBatch batch) {
        batch.draw(frameActual, posicion.x, posicion.y);
    }

    public void mover(float dx, float dy) {
        this.velocidad.set(dx, dy);
    }

    public Vector2 getPosicion() {
        return this.posicion;
    }

    public int getDistanciaMovimiento() {
        return this.DISTANCIA_MOVIMIENTO;
    }

    public abstract void manejarEntrada(DatosEntrada datos);
}
