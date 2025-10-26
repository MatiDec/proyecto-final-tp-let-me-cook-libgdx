package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Screen;

public abstract class Pantalla implements Screen {

    private static Pantalla pantallaActual;

    public static Pantalla getPantallaActual() {
        return pantallaActual;
    }

    public static void cambiarPantalla(Pantalla nuevaPantalla) {
        if (pantallaActual != null) {
            pantallaActual.hide();
        }
        pantallaActual = nuevaPantalla;
    }
}
