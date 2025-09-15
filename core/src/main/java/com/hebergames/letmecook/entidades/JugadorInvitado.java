package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.eventos.DatosEntrada;
import com.hebergames.letmecook.utiles.GestorAnimacion;

import java.util.Set;

public class JugadorInvitado extends Jugador{

    public JugadorInvitado(float x, float y, GestorAnimacion gestorAnimacion) {
        super(x, y,  gestorAnimacion);
    }

    @Override
    public void manejarEntrada(DatosEntrada datos) {

    }
}
