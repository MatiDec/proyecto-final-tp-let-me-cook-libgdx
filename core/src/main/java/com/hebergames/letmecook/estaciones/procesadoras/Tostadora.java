package com.hebergames.letmecook.estaciones.procesadoras;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.estaciones.EstacionTrabajo;

public class Tostadora extends EstacionTrabajo {
    public Tostadora(Rectangle area) {
        super(area);
        procesadora = new Procesadora(area, TipoCoccion.TOSTADORA);
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
        if (getJugadorOcupante() != null) {
            manejarProcesamiento(getJugadorOcupante());
        }
    }
}
