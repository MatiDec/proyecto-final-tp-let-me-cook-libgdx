package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Screen;

public abstract class Pantalla implements Screen {

    private static Pantalla pantallaActual;
    private static Pantalla pantallaAnterior;

    public static Pantalla getPantallaActual() {
        return pantallaActual;
    }
    public static Pantalla getPantallaAnterior() {
        return pantallaAnterior;
    }

    public static void cambiarPantalla(Pantalla nuevaPantalla) {
        if (pantallaActual != null) {
            pantallaActual.hide();
        }
        pantallaAnterior = pantallaActual;
        pantallaActual = nuevaPantalla;
        pantallaActual.show();
    }
}
