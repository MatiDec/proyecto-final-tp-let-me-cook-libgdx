package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.eventos.DatosEntrada;

import java.util.Set;

public class JugadorInvitado extends Jugador{

    public JugadorInvitado(float x, float y, Animation<TextureRegion> animacion) {
        super(x, y, animacion);
    }

    @Override
    public void manejarEntrada(DatosEntrada datos) {

    }
}
