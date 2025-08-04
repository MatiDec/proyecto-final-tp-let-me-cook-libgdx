package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.pantallas.pantallasmaquinas.PantallaMaquina;

public class Freidora extends EstacionTrabajo {
    public Freidora(Rectangle area) {
        super(area);
    }

    @Override
    protected PantallaMaquina crearPantallaMaquina() {
        return null;
    }

    @Override
    public void alInteractuar() {
        System.out.println("Usando la freidora");

    }
}
