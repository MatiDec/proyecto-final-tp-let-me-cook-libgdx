package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.pantallas.Pantalla;
import com.hebergames.letmecook.pantallas.PantallaJuego;


public class Mesa extends EstacionTrabajo {
    public Mesa(Rectangle area) {
        super(area);
    }

    @Override
    public void alInteractuar() {
        System.out.println("Abre mesa");
        Pantalla pantallaActual = Pantalla.getPantallaActual();
        if(pantallaActual instanceof PantallaJuego) {
            ((PantallaJuego) pantallaActual).abrirMesa();
        }
    }
}
