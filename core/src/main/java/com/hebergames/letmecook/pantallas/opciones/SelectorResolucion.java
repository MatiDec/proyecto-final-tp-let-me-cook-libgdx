package com.hebergames.letmecook.pantallas.opciones;

import com.badlogic.gdx.graphics.Color;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.utiles.Recursos;

public class SelectorResolucion {
    private Texto textoTitulo;
    private Texto textoFlechaIzq;
    private Texto textoFlechaDer;
    private Texto textoResolucionActual;
    private String[] resoluciones;
    private int indiceActual;
    private float x, y;

    private final float ANCHO_BLOQUE_RESOLUCION = 300f; // ancho fijo del bloque
    private final float MARGEN_TITULO_A_FLECHA = 20f;
    private final float MARGEN_FLECHA_A_BLOQUE = 10f;
    private final float MARGEN_BLOQUE_A_FLECHA = 10f;

    public SelectorResolucion(String[] resoluciones, int indiceInicial) {
        this.resoluciones = resoluciones;
        this.indiceActual = indiceInicial;

        textoTitulo = new Texto(Recursos.FUENTE_MENU, 48, Color.WHITE, true);
        textoTitulo.setTexto("Resolución: ");

        textoFlechaIzq = new Texto(Recursos.FUENTE_MENU, 60, Color.WHITE, true);
        textoFlechaIzq.setTexto("<");

        textoFlechaDer = new Texto(Recursos.FUENTE_MENU, 60, Color.WHITE, true);
        textoFlechaDer.setTexto(">");

        textoResolucionActual = new Texto(Recursos.FUENTE_MENU, 48, Color.YELLOW, true);
        actualizarTextoResolucion();
    }

    public void setPosicion(float x, float y) {
        this.x = x;
        this.y = y;
        actualizarPosiciones();
    }

    private void actualizarPosiciones() {
        textoTitulo.setPosition(x, y);

        // Flecha izquierda
        float flechaIzqX = x + textoTitulo.getAncho() + MARGEN_TITULO_A_FLECHA;
        textoFlechaIzq.setPosition(flechaIzqX, y);

        // Centro del bloque fijo
        float bloqueX = flechaIzqX + textoFlechaIzq.getAncho() + MARGEN_FLECHA_A_BLOQUE;
        float bloqueCentro = bloqueX + ANCHO_BLOQUE_RESOLUCION / 2f;

        // Centrar el texto de resolución dentro del bloque
        float textoResX = bloqueCentro - (textoResolucionActual.getAncho() / 2f);
        textoResolucionActual.setPosition(textoResX, y);

        // Flecha derecha fija al final del bloque
        float flechaDerX = bloqueX + ANCHO_BLOQUE_RESOLUCION + MARGEN_BLOQUE_A_FLECHA;
        textoFlechaDer.setPosition(flechaDerX, y);
    }

    private void actualizarTextoResolucion() {
        textoResolucionActual.setTexto(resoluciones[indiceActual]);
        actualizarPosiciones();
    }

    public void siguiente() {
        indiceActual = (indiceActual + 1) % resoluciones.length;
        actualizarTextoResolucion();
        actualizarPosiciones();
    }

    public void anterior() {
        indiceActual = (indiceActual - 1 + resoluciones.length) % resoluciones.length;
        actualizarTextoResolucion();
        actualizarPosiciones();
    }

    public void dibujar() {
        actualizarPosiciones();
        textoTitulo.dibujar();
        textoFlechaIzq.dibujar();
        textoResolucionActual.dibujar();
        textoFlechaDer.dibujar();
    }

    public boolean clickEnFlechaIzquierda(float mouseX, float mouseY) {
        return textoFlechaIzq.fueClickeado(mouseX, mouseY);
    }

    public boolean clickEnFlechaDerecha(float mouseX, float mouseY) {
        return textoFlechaDer.fueClickeado(mouseX, mouseY);
    }

    public String getResolucionActual() {
        return resoluciones[indiceActual];
    }

    public int getIndiceActual() {
        return indiceActual;
    }

    public Texto getTextoFlechaIzq() {
        return textoFlechaIzq;
    }

    public Texto getTextoFlechaDer() {
        return textoFlechaDer;
    }
}
