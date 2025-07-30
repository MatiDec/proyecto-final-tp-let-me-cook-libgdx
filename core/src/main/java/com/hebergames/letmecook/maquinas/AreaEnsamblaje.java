package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;


public class AreaEnsamblaje extends EstacionTrabajo {
    public AreaEnsamblaje(Rectangle area) {
        super(area);
    }

    @Override
    public void alInteractuar() {
        System.out.println("Plato arma");

    }
}
