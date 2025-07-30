package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;

public class Freidora extends EstacionTrabajo {
    public Freidora(Rectangle area) {
        super(area);
    }

    @Override
    public void alInteractuar() {
        System.out.println("Usando la freidora");

    }
}
