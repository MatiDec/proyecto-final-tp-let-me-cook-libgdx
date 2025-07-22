package com.hebergames.letmecook;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.pantallas.Pantalla;
import com.hebergames.letmecook.pantallas.PantallaMenu;
import com.hebergames.letmecook.utiles.Render;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class LetMeCookPrincipal extends Game {

    public static PantallaMenu menuPrincipal = new PantallaMenu();
    public static Pantalla pantallaActual = menuPrincipal;

    @Override
    public void create() {
        Render.batch = new SpriteBatch();
        pantallaActual = obtenerPantallaActual();
        this.setScreen(pantallaActual);
    }

    private Pantalla obtenerPantallaActual() {
        return menuPrincipal.getPantallaElegida();
    }

    @Override
    public void render() {
        super.render();

        if(pantallaActual instanceof PantallaMenu) {
            PantallaMenu menu = (PantallaMenu) pantallaActual;
            Pantalla nueva = menu.getPantallaElegida();
            if(nueva != pantallaActual) {
                pantallaActual = nueva;
                setScreen(nueva);
            }
        }
    }

    @Override
    public void dispose() {
        Render.batch.dispose();
    }
}
