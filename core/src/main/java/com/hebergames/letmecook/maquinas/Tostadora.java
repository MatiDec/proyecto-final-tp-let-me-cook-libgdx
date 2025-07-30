package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;

public class Tostadora extends EstacionTrabajo {
    public Tostadora(Rectangle area) {
        super(area);
    }

    @Override
    public void alInteractuar() {
        System.out.println("Tostando pan");
        // tapia was here
    }
}
