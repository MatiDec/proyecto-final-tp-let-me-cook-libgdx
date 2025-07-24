package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.eventos.DatosEntrada;

import java.util.List;

public class JugadorHost extends Jugador {


    public JugadorHost(float x, float y, Animation<TextureRegion> animacion) {
        super(x, y, animacion);

    }

    @Override
    public void manejarEntrada(DatosEntrada datosEntrada) {
        float dx = 0, dy = 0;

        if (datosEntrada.estaPresionada(Input.Keys.W)) dy += DISTANCIA_MOVIMIENTO;
        if (datosEntrada.estaPresionada(Input.Keys.S)) dy -= DISTANCIA_MOVIMIENTO;
        if (datosEntrada.estaPresionada(Input.Keys.A)) dx -= DISTANCIA_MOVIMIENTO;
        if (datosEntrada.estaPresionada(Input.Keys.D)) dx += DISTANCIA_MOVIMIENTO;

        if (dx != 0 || dy != 0) {
            float angulo = (float) Math.toDegrees(Math.atan2(dy, dx)) - 90f; //linea turbianga que no se como cambiar
            setAnguloRotacion(angulo);
        }

        moverSiNoColisiona(dx, dy);
    }

    private void moverSiNoColisiona(float dx, float dy) {
        float deltaTime = Gdx.graphics.getDeltaTime();
    //se puede hacer mas optimo pero tengo miedo de que explote y que no se entienda nada

        float nuevaX = posicion.x + dx * deltaTime;
        Rectangle futuroX = new Rectangle(
            nuevaX,
            posicion.y,
            frameActual.getRegionWidth(),
            frameActual.getRegionHeight()
        );

        boolean colisionX = false;


        if (!colisionX) {
            posicion.x = nuevaX;
        }


        float nuevaY = posicion.y + dy * deltaTime;
        Rectangle futuroY = new Rectangle(
            posicion.x,
            nuevaY,
            frameActual.getRegionWidth(),
            frameActual.getRegionHeight()
        );

        boolean colisionY = false;


        if (!colisionY) {
            posicion.y = nuevaY;
        }
        if (colisionX) {
            velocidad.x = 0;
        } else {
            velocidad.x = dx;
        }
        if (colisionY) {
            velocidad.y = 0;
        } else {
            velocidad.y = dy;
        }
    }


}
