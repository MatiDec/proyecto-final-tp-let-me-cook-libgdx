package com.hebergames.letmecook.estaciones;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Jugador;

public class Basurero extends EstacionTrabajo{


    public Basurero(Rectangle area) {
        super(area);
    }

    @Override
    protected void alLiberar() {

    }

    @Override
    protected void iniciarMenu(Jugador jugador) {

    }

    @Override
    public void manejarSeleccionMenu(Jugador jugador, int numeroSeleccion) {

    }

    @Override
    protected void dibujarMenu(SpriteBatch batch, Jugador jugador) {

    }

    @Override
    public void alInteractuar() {
        Jugador jugador = getJugadorOcupante();
        if (jugador == null) {
            System.out.println("ERROR: Jugador ocupante es null en Heladera");
            return;
        }
        else{
            if(jugador.tieneInventarioLleno())
            {
                jugador.descartarInventario();
                System.out.println("Se te borro todo del inventario");
            }
            System.out.println("Hola pocovi");

        }
    }
}
