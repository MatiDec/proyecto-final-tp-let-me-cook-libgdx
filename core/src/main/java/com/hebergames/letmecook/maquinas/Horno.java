package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;

public class Horno extends EstacionTrabajo {
    public Horno(Rectangle area) {
        super(area);
    }

    @Override
    public void alInteractuar() {
        System.out.println("Usando el horno");
        // aca hace que funque tapia
    }
}

