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
    protected TextureRegion frameActual;
    protected Animation<TextureRegion> animacion;
    protected float estadoTiempo;
    protected float anguloRotacion = 0f;

    public final int DISTANCIA_MOVIMIENTO = 400;

    protected GestorAnimacion gestorAnimacion;
    private String objetoEnMano = "vacio";

    public Jugador(float x, float y, GestorAnimacion gestorAnimacion) {
        this.posicion = new Vector2(x, y);
        this.velocidad = new Vector2(0, 0);
        this.gestorAnimacion = gestorAnimacion;
        this.animacion = gestorAnimacion.getAnimacionPorObjeto(objetoEnMano);
        this.estadoTiempo = 0;
    }

    public void actualizar(float delta) {
        if (velocidad.x != 0 || velocidad.y != 0) {
            estadoTiempo += delta;
        } else {
            estadoTiempo = 0; // si no se mueve, vuelve al primer frame
        }

        frameActual = animacion.getKeyFrame(estadoTiempo, true);

        // actualizar posición
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

    public void setObjetoEnMano(String nombreObjeto) {
        if (!nombreObjeto.equalsIgnoreCase(this.objetoEnMano)) {
            this.objetoEnMano = nombreObjeto;
            this.animacion = gestorAnimacion.getAnimacionPorObjeto(nombreObjeto);
            this.estadoTiempo = 0;
        }
    }

    public String getObjetoEnMano() {
        return objetoEnMano;
    }
}
