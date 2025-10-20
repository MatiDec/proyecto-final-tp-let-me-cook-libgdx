package com.hebergames.letmecook.eventos.entrada;

import com.badlogic.gdx.graphics.Color;
import com.hebergames.letmecook.elementos.Texto;

public class TextoInteractuable implements BotonInteractuable {

    private final Texto TEXTO;
    private final Runnable ACCION;

    private final Color colorOriginal;
    private final Color colorHover;
    private boolean estaEnHover = false;

    public TextoInteractuable(Texto texto, Runnable accion) {
        this(texto, accion, new Color(1f, 0.8f, 0.2f, 1f));//un color default de hover
    }

    public TextoInteractuable(Texto texto, Runnable accion, Color colorHover) {
        this.TEXTO = texto;
        this.ACCION = accion;
        this.colorOriginal = new Color(texto.getFuente().getColor());
        this.colorHover = colorHover;
    }

    @Override
    public boolean fueClickeado(float x, float y) {
        return TEXTO.fueClickeado(x, y);
    }

    @Override
    public void alClick() {
        ACCION.run();
    }

    public void actualizarHover(float mouseX, float mouseY) {
        boolean mouseEncima = TEXTO.fueClickeado(mouseX, mouseY);

        if (mouseEncima && !estaEnHover) {
            // El mouse acaba de entrar al área del botón
            TEXTO.getFuente().setColor(colorHover);
            estaEnHover = true;
        } else if (!mouseEncima && estaEnHover) {
            // El mouse acaba de salir del área del botón
            TEXTO.getFuente().setColor(colorOriginal);
            estaEnHover = false;
        }
    }

    public boolean isEnHover() {
        return estaEnHover;
    }
}
