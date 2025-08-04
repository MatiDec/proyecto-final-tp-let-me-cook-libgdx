package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.pantallas.Pantalla;
import com.hebergames.letmecook.pantallas.PantallaJuego;
import com.hebergames.letmecook.pantallas.pantallasmaquinas.PantallaMaquina;
import com.hebergames.letmecook.pantallas.pantallasmaquinas.PantallaHeladera;

public class Heladera extends EstacionTrabajo {

    public Heladera(Rectangle area) {
        super(area);
    }

    @Override
    protected PantallaMaquina crearPantallaMaquina() {
        return new PantallaHeladera();
    }

    @Override
    public void alInteractuar() {
        System.out.println("Abrio heladera");
        Pantalla pantallaActual = Pantalla.getPantallaActual();
        if(pantallaActual instanceof PantallaJuego) {
            ((PantallaJuego) pantallaActual).abrirHeladera();
        }
    }
}
