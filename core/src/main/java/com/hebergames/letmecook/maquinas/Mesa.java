package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;


public class Mesa extends EstacionTrabajo {
    public Mesa(Rectangle area) {
        super(area);
    }

    @Override
    public void alInteractuar() {
        System.out.println("Plato arma");

    }
}
