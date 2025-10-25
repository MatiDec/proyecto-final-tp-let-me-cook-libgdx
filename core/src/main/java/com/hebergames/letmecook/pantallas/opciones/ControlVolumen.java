package com.hebergames.letmecook.pantallas.opciones;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.utiles.Recursos;

public class ControlVolumen {
    private Texto textoTitulo;
    private Texto textoFlechaIzq;
    private Texto textoFlechaDer;
    private int volumenActual;
    private float x, y;

    private final int INCREMENTO = 10;
    private final int CUADRADOS_TOTAL = 10;
    private final float ANCHO_CUADRADO = 20f;
    private final float ALTO_CUADRADO = 20f;
    private final float ESPACIADO_CUADRADO = 5f;

    private final float MARGEN_TITULO_A_FLECHA = 20f;
    private final float MARGEN_FLECHA_A_BARRA = 10f;
    private final float MARGEN_BARRA_A_FLECHA_DER = 10f;

    public ControlVolumen(String titulo, int volumenInicial) {
        this.volumenActual = volumenInicial;
        textoTitulo = new Texto(Recursos.FUENTE_MENU, 48, Color.WHITE, true);
        textoTitulo.setTexto(titulo);

        textoFlechaIzq = new Texto(Recursos.FUENTE_MENU, 60, Color.WHITE, true);
        textoFlechaIzq.setTexto("<");

        textoFlechaDer = new Texto(Recursos.FUENTE_MENU, 60, Color.WHITE, true);
        textoFlechaDer.setTexto(">");
    }

    public void setPosicion(float x, float y) {
        this.x = x;
        this.y = y;
        actualizarPosiciones();
    }

    private void actualizarPosiciones() {
        textoTitulo.setPosition(x, y);

        float anchoBarra = (CUADRADOS_TOTAL * ANCHO_CUADRADO) + ((CUADRADOS_TOTAL - 1) * ESPACIADO_CUADRADO);
        // Flecha izquierda fija después del título
        float flechaIzqX = x + textoTitulo.getAncho() + MARGEN_TITULO_A_FLECHA;
        textoFlechaIzq.setPosition(flechaIzqX, y);

        // Barra comienza después de la flecha izquierda
        float inicioBarraX = flechaIzqX + textoFlechaIzq.getAncho() + MARGEN_FLECHA_A_BARRA;

        // Flecha derecha fija después de la barra completa
        float flechaDerX = inicioBarraX + anchoBarra + MARGEN_BARRA_A_FLECHA_DER;
        textoFlechaDer.setPosition(flechaDerX, y);
    }

    public void aumentarVolumen() {
        volumenActual = Math.min(100, volumenActual + INCREMENTO);
    }

    public void disminuirVolumen() {
        volumenActual = Math.max(0, volumenActual - INCREMENTO);
    }

    public void dibujar(SpriteBatch batch) {
        textoTitulo.dibujar();
        textoFlechaIzq.dibujar();
        textoFlechaDer.dibujar();

        actualizarPosiciones();

        float anchoBarra = (CUADRADOS_TOTAL * ANCHO_CUADRADO) + ((CUADRADOS_TOTAL - 1) * ESPACIADO_CUADRADO);
        float inicioBarraX = x + textoTitulo.getAncho() + 30f;
        float barraY = y - 15f;

        int cuadradosLlenos = volumenActual / INCREMENTO;

        for (int i = 0; i < CUADRADOS_TOTAL; i++) {
            float cuadradoX = inicioBarraX + (i * (ANCHO_CUADRADO + ESPACIADO_CUADRADO));

            if (i < cuadradosLlenos) {
                batch.setColor(Color.WHITE);
            } else {
                batch.setColor(Color.DARK_GRAY);
            }

            batch.draw(Recursos.PIXEL, cuadradoX, barraY, ANCHO_CUADRADO, ALTO_CUADRADO);
        }

        batch.setColor(Color.WHITE);
    }

    public boolean clickEnFlechaIzquierda(float mouseX, float mouseY) {
        return textoFlechaIzq.fueClickeado(mouseX, mouseY);
    }

    public boolean clickEnFlechaDerecha(float mouseX, float mouseY) {
        return textoFlechaDer.fueClickeado(mouseX, mouseY);
    }

    public int getVolumen() {
        return volumenActual;
    }

    public Texto getTextoFlechaIzq() {
        return textoFlechaIzq;
    }

    public Texto getTextoFlechaDer() {
        return textoFlechaDer;
    }
}
