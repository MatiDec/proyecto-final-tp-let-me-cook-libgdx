package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.utiles.Recursos;

import java.util.ArrayList;

public class GestorUIJuego {

    private final ArrayList<ObjetoVisualizable> objetosUI;
    private final Texto textoContador;
    private final Texto textoInventario1;
    private final Texto textoInventario2;

    private static final float MARGEN = 50f;

    public GestorUIJuego() {
        objetosUI = new ArrayList<>();

        textoContador = new Texto(Recursos.FUENTE_MENU, 32, Color.WHITE, true);
        textoContador.setTexto("00:00");

        textoInventario1 = new Texto(Recursos.FUENTE_MENU, 32, Color.GREEN, true); // J1 en verde
        textoInventario1.setTexto("J1 Inventario: Vacío");

        textoInventario2 = new Texto(Recursos.FUENTE_MENU, 32, Color.BLUE, true); // J2 en azul
        textoInventario2.setTexto("J2 Inventario: Vacío");

        objetosUI.add(textoContador);
        objetosUI.add(textoInventario1);
        objetosUI.add(textoInventario2);

        actualizarPosiciones(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void actualizarTiempo(int segundos) {
        int minutos = segundos / 60;
        int segundosRestantes = segundos % 60;
        String tiempoFormateado = String.format("%02d:%02d", minutos, segundosRestantes);
        textoContador.setTexto(tiempoFormateado);
    }

    public void actualizarInventario(String nombreItem1, String nombreItem2) {
        if (nombreItem1 != null) {
            textoInventario1.setTexto("J1 Inventario: " + nombreItem1);
        }
        if (nombreItem2 != null) {
            textoInventario2.setTexto("J2 Inventario: " + nombreItem2);
        }

    }

    public void dibujar(SpriteBatch batch) {
        for (ObjetoVisualizable obj : objetosUI) {
            obj.dibujarEnUi(batch);
        }
    }

    public void actualizarPosiciones(float anchoUI, float altoUI) {
        // Contador de tiempo (Arriba, centro)
        textoContador.setPosition(anchoUI / 2f - textoContador.getAncho() / 2f, altoUI - MARGEN);
        // Inventario Jugador 1 (Arriba, izquierda)
        textoInventario1.setPosition(MARGEN, altoUI - MARGEN);
        // Inventario Jugador 2 (Arriba, derecha)
        textoInventario2.setPosition(anchoUI - textoInventario2.getAncho() - MARGEN, altoUI - MARGEN);
    }
}
