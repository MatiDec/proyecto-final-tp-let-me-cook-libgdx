package com.hebergames.letmecook.eventos;

import com.hebergames.letmecook.elementos.Texto;

public class TextoInteractuable implements Interactuable {

    private final Texto TEXTO;
    private final Runnable ACCION;

    public TextoInteractuable(Texto texto, Runnable accion) {
        this.TEXTO = texto;
        this.ACCION = accion;
    }

    @Override
    public boolean fueClickeado(float x, float y) {
        return TEXTO.fueClickeado(x, y);
    }

    @Override
    public void alClick() {
        ACCION.run();
    }

}
