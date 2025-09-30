package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.utiles.Recursos;

import java.util.ArrayList;

public class GestorUIJuego {

    private final ArrayList<ObjetoVisualizable> objetosUI;
    private final Texto textoContador;
    private final Texto textoInventario;

    private static final float MARGEN = 50f;

    public GestorUIJuego() {
        objetosUI = new ArrayList<>();

        textoContador = new Texto(Recursos.FUENTE_MENU, 32, Color.WHITE, true);
        textoContador.setTexto("00:00");

        textoInventario = new Texto(Recursos.FUENTE_MENU, 32, Color.WHITE, true);
        textoInventario.setTexto("Inventario: Nada");

        objetosUI.add(textoContador);
        objetosUI.add(textoInventario);

        actualizarPosiciones(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void actualizarTiempo(int segundos) {
        int minutos = segundos / 60;
        int segundosRestantes = segundos % 60;
        String tiempoFormateado = String.format("%02d:%02d", minutos, segundosRestantes);
        textoContador.setTexto(tiempoFormateado);
    }

    public void actualizarInventario(String nombreItem) {
        textoInventario.setTexto("Inventario: " + nombreItem);
    }

    public void dibujar(SpriteBatch batch) {
        for (ObjetoVisualizable obj : objetosUI) {
            obj.dibujarEnUi(batch);
        }
    }

    public void actualizarPosiciones(float anchoUI, float altoUI) {
        textoContador.setPosition(MARGEN, MARGEN);
        textoInventario.setPosition(MARGEN, altoUI - MARGEN);
    }
}
