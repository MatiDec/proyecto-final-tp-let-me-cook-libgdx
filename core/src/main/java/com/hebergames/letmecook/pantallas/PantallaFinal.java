package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

public class PantallaFinal extends Pantalla {

    private final String tiempo;
    private final int puntaje;

    private Texto titulo;
    private Texto resumenTiempo;
    private Texto resumenPuntaje;
    private Texto opcionMenu;

    private SpriteBatch batch;

    public PantallaFinal(String tiempo, int puntaje) {
        this.tiempo = tiempo;
        this.puntaje = puntaje;
    }

    @Override
    public void show() {
        batch = Render.batch;

        titulo = new Texto(Recursos.FUENTE_MENU, 64, Color.WHITE, true);
        titulo.setTexto("¡Nivel Finalizado!");
        titulo.setPosition(Gdx.graphics.getWidth()/2f - titulo.getAncho()/2f,
            Gdx.graphics.getHeight() - 200);

        resumenTiempo = new Texto(Recursos.FUENTE_MENU, 32, Color.WHITE, true);
        resumenTiempo.setTexto("Tiempo: " + tiempo);
        resumenTiempo.setPosition(Gdx.graphics.getWidth()/2f - resumenTiempo.getAncho()/2f,
            Gdx.graphics.getHeight() - 300);

        resumenPuntaje = new Texto(Recursos.FUENTE_MENU, 32, Color.WHITE, true);
        resumenPuntaje.setTexto("Puntaje: " + puntaje);
        resumenPuntaje.setPosition(Gdx.graphics.getWidth()/2f - resumenPuntaje.getAncho()/2f,
            Gdx.graphics.getHeight() - 350);

        opcionMenu = new Texto(Recursos.FUENTE_MENU, 28, Color.YELLOW, true);
        opcionMenu.setTexto("Presiona ENTER para volver al menú");
        opcionMenu.setPosition(Gdx.graphics.getWidth()/2f - opcionMenu.getAncho()/2f, 150);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        titulo.dibujarEnUi(batch);
        resumenTiempo.dibujarEnUi(batch);
        resumenPuntaje.dibujarEnUi(batch);
        opcionMenu.dibujarEnUi(batch);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // Cambiar a pantalla menú principal (debes reemplazar PantallaMenu por tu clase real)
            Pantalla.cambiarPantalla(new PantallaMenu());
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
