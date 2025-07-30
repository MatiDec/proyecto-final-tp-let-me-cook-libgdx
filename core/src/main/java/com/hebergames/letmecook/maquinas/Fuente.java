package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;

public class Fuente extends EstacionTrabajo {
    public Fuente(Rectangle area) {
        super(area);
    }

    @Override
    public void alInteractuar() {
        System.out.println("bebida");
        // ozores wasnt jiar
    }
}
