package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.hebergames.letmecook.eventos.DatosEntrada;
import com.hebergames.letmecook.utiles.GestorAnimacion;

public abstract class Jugador {
    protected Vector2 posicion;
    protected Vector2 velocidad;
    protected TextureRegion frameActual; // Esto tiene que ver con las texturas, se puede usar más adelante o no, lo dejo por si sirve
    protected Animation<TextureRegion> animacion; // esto con las animaciones, quizá es útil para más adelante, efectivamente fue util
    protected float estadoTiempo;
    protected float anguloRotacion = 0f; // grados de rotación


    public final int DISTANCIA_MOVIMIENTO = 400;//Con esto se maneja la "velocidad" de los jugadores

    protected GestorAnimacion gestorAnimacion;

    public Jugador(float x, float y, GestorAnimacion gestorAnimacion) {
        this.posicion = new Vector2(x, y);
        this.velocidad = new Vector2(0,0);
        this.gestorAnimacion = gestorAnimacion;
        this.animacion = gestorAnimacion.getAnimacion(0);
        this.estadoTiempo = 0;
    }

    public void actualizar(float delta) {
       // solo se cambia si se mueve basicamente
        if (velocidad.x != 0 || velocidad.y != 0) {
            estadoTiempo += delta;
        } else {
            estadoTiempo = 0; //si se queda quieto vuelve al frame inicial, pero con la rotacion que tiene de antes
        }


        frameActual = animacion.getKeyFrame(estadoTiempo, true);

        // actualiza posicion
        posicion.add(velocidad.x * delta, velocidad.y * delta);
    }


    public void dibujar(SpriteBatch batch) {
        TextureRegion frame = frameActual;

        float x = posicion.x;
        float y = posicion.y;
        float width = 128;
        float height = 128;
        float originX = width / 2f;
        float originY = height / 2f;

        batch.draw(frame, x, y, originX, originY, width, height, 1f, 1f, anguloRotacion);
    }

    public Vector2 getPosicion() {
        return this.posicion;
    }

    public abstract void manejarEntrada(DatosEntrada datos);

    public void setAnguloRotacion(float angulo) {
        this.anguloRotacion = angulo;
    }

    public void setAnimacion(int fila) {
        Animation<TextureRegion> nuevaAnimacion = gestorAnimacion.getAnimacion(fila);
        if (nuevaAnimacion != null) {
            this.animacion = nuevaAnimacion;
            this.estadoTiempo = 0;
        }
    }
}
