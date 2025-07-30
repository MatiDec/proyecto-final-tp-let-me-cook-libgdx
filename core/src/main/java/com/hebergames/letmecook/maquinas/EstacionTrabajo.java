package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;

public abstract class EstacionTrabajo {
    protected Rectangle area;

    public EstacionTrabajo(Rectangle area) {
        this.area = area;
    }

    public Rectangle getArea() {
        return area;
    }

    public boolean fueClickeada(float x, float y) {
        return area.contains(x, y);
    }

    public abstract void alInteractuar();
}
