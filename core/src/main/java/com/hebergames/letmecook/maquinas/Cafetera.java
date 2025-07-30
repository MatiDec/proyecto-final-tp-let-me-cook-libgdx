package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;


public class Cafetera extends EstacionTrabajo {
    public Cafetera(Rectangle area) {
        super(area);
    }

    @Override
    public void alInteractuar() {
        System.out.println("Preparando café");

    }
}
