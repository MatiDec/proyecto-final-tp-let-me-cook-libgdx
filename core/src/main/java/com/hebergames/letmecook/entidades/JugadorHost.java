package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.eventos.DatosEntrada;

import java.util.Set;

public class JugadorHost extends Jugador {

    public JugadorHost(float x, float y, Animation<TextureRegion> animacion) {
        super(x, y, animacion);
        final int DISTANCIA_MOVIMIENTO = super.getDistanciaMovimiento();
    }

    @Override
    public void manejarEntrada(DatosEntrada datosEntrada) {
        float dx = 0, dy = 0;

        if (datosEntrada.estaPresionada(Input.Keys.W)) dy += DISTANCIA_MOVIMIENTO;
        if (datosEntrada.estaPresionada(Input.Keys.S)) dy -= DISTANCIA_MOVIMIENTO;
        if (datosEntrada.estaPresionada(Input.Keys.A)) dx -= DISTANCIA_MOVIMIENTO;
        if (datosEntrada.estaPresionada(Input.Keys.D)) dx += DISTANCIA_MOVIMIENTO;

        mover(dx, dy);
    }

}
