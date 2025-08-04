package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.pantallas.Pantalla;
import com.hebergames.letmecook.pantallas.PantallaJuego;
import com.hebergames.letmecook.pantallas.pantallasmaquinas.PantallaMaquina;
import com.hebergames.letmecook.pantallas.pantallasmaquinas.PantallaMesa;

public class Mesa extends EstacionTrabajo {

    public Mesa(Rectangle area) {
        super(area);
    }

    @Override
    protected PantallaMaquina crearPantallaMaquina() {
        return PantallaMesa.getInstancia();
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
