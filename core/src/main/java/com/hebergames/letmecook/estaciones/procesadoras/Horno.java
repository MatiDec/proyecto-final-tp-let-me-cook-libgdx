package com.hebergames.letmecook.estaciones.procesadoras;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.estaciones.EstacionTrabajo;

public class Horno extends EstacionTrabajo {

    public Horno(Rectangle area) {
        super(area);
        procesadora = new Procesadora(area, TipoCoccion.HORNO);
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
        System.out.println("Interacción con horno realizada");
        if (getJugadorOcupante() != null) {
            manejarProcesamiento(getJugadorOcupante());
        }
    }

}
