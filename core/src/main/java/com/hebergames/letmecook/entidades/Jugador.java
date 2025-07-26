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
    protected Animation<TextureRegion> animacion; // esto con las animaciones, quizá es útil para más adelante, efectivamente fue util
    protected float estadoTiempo;
    protected float anguloRotacion = 0f; // grados


    public final int DISTANCIA_MOVIMIENTO = 200;//Con esto se maneja la "velocidad" de los jugadores

    public Jugador(float x, float y, Animation<TextureRegion> animacion) {
        this.posicion = new Vector2(x, y);
        this.velocidad = new Vector2(0,0);
        this.animacion = animacion;
        this.estadoTiempo = 0;
    }

    public void actualizar(float delta) {
       // solo se cambia si se mueve basicamente
        if (velocidad.x != 0 || velocidad.y != 0) {
            estadoTiempo += delta;
        } else {
            estadoTiempo = 0; //si se queda quieto vuelve al frame inicial pero con la rotacion que tiene de antes
        }


        frameActual = animacion.getKeyFrame(estadoTiempo, true); //aca va en true porque si no no se loopea cuando se mueve

        // actualiza posicion
        posicion.add(velocidad.x * delta, velocidad.y * delta);
    }


    public void dibujar(SpriteBatch batch) {
        TextureRegion frame = frameActual;

        float x = posicion.x;
        float y = posicion.y;
        float width = 32;
        float height = 32;
        float originX = width / 2f;
        float originY = height / 2f;
    //lo de arriba fue tapia
        batch.draw(frame, x, y, originX, originY, width, height, 1f, 1f, anguloRotacion);
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

    public float getAnguloRotacion() {
        return this.anguloRotacion;
    }

    public void setAnguloRotacion(float angulo) {
        this.anguloRotacion = angulo;
    }

}
