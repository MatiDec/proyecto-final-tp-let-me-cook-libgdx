package com.hebergames.letmecook.estaciones.bebidas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.estaciones.EstacionTrabajo;


public class Cafetera extends EstacionTrabajo {
    public Cafetera(Rectangle area) {
        super(area);
    }

    @Override
    protected void alLiberar() {

    }

    @Override
    protected void iniciarMenu(Jugador jugador) {

    }

    @Override
    public void manejarSeleccionMenu(Jugador jugador, int direccion) {

    }

    @Override
    protected void dibujarMenu(SpriteBatch batch, Jugador jugador) {

    }

    @Override
    public void alInteractuar() {
        System.out.println("Preparando café");

    }
}
