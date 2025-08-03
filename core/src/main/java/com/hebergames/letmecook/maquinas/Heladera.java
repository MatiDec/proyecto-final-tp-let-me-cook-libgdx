package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.pantallas.Pantalla;
import com.hebergames.letmecook.pantallas.PantallaHeladera;
import com.hebergames.letmecook.pantallas.PantallaJuego;

public class Heladera extends EstacionTrabajo {

    public Heladera(Rectangle area) {
        super(area);
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
