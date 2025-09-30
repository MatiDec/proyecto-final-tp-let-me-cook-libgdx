package com.hebergames.letmecook.elementos;

import com.badlogic.gdx.graphics.Color;

public class EfectoHover {

    private final Texto texto;
    private final Color colorOriginal;
    private final Color colorHover;
    private boolean estaEnHover = false;

    public EfectoHover(Texto texto, Color colorHover) {
        this.texto = texto;
        this.colorOriginal = new Color(texto.fuente.getColor());
        this.colorHover = colorHover;
    }

    public void actualizar(float mouseX, float mouseY) {
        boolean mouseEncima = texto.fueClickeado(mouseX, mouseY);

        if (mouseEncima && !estaEnHover) {
            // El mouse acaba de entrar al área del botón
            aplicarHover();
            estaEnHover = true;
        } else if (!mouseEncima && estaEnHover) {
            // El mouse acaba de salir del área del botón
            quitarHover();
            estaEnHover = false;
        }
    }

    private void aplicarHover() {
        texto.fuente.setColor(colorHover);
    }

    private void quitarHover() {
        texto.fuente.setColor(colorOriginal);
    }

    public boolean isEnHover() {
        return estaEnHover;
    }
}
