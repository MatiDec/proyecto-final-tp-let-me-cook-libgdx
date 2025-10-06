package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Jugador;

public class Heladera extends EstacionTrabajo {

    public Heladera(Rectangle area) {
        super(area);
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
        System.out.println("Abrio heladera");
    }
}
