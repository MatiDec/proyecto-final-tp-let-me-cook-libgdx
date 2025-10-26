package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.mapa.niveles.GestorPartida;
import com.hebergames.letmecook.mapa.niveles.NivelPartida;
import com.hebergames.letmecook.pantallas.superposiciones.InfoDiaNivel;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

import java.util.ArrayList;

public class PantallaFinal extends Pantalla {

    private final String tiempo;
    private final int puntaje;

    private Texto titulo;
    private Texto resumenTiempo;
    private Texto resumenPuntaje;
    private Texto opcionMenu;
    private ArrayList<InfoDiaNivel> diasNiveles;

    private SpriteBatch batch;

    public PantallaFinal(String tiempo, int puntaje) {
        this.tiempo = tiempo;
        this.puntaje = puntaje;
    }

    @Override
    public void show() {
        batch = Render.batch;
        GestorPartida gestorPartida = GestorPartida.getInstancia();

        titulo = new Texto(Recursos.FUENTE_MENU, 64, Color.WHITE, true);
        titulo.setTexto("¡Partida Finalizada!");
        titulo.setPosition(Gdx.graphics.getWidth()/2f - titulo.getAncho()/2f,
            Gdx.graphics.getHeight() - 100);

        resumenPuntaje = new Texto(Recursos.FUENTE_MENU, 40, Color.YELLOW, true);
        resumenPuntaje.setTexto("Puntaje Total: " + puntaje);
        resumenPuntaje.setPosition(Gdx.graphics.getWidth()/2f - resumenPuntaje.getAncho()/2f,
            Gdx.graphics.getHeight() - 180);

        opcionMenu = new Texto(Recursos.FUENTE_MENU, 28, Color.YELLOW, true);
        opcionMenu.setTexto("Presiona ENTER para volver al menú");
        opcionMenu.setPosition(Gdx.graphics.getWidth()/2f - opcionMenu.getAncho()/2f, 80);

        resumenTiempo = new Texto(Recursos.FUENTE_MENU, 40, Color.CYAN, true);
        resumenTiempo.setTexto("Tiempo total: " + tiempo);
        resumenTiempo.setPosition(Gdx.graphics.getWidth()/2f - resumenTiempo.getAncho()/2f,
            Gdx.graphics.getHeight() - 230);

        diasNiveles = new ArrayList<>();
        ArrayList<NivelPartida> niveles = gestorPartida.getTodosLosNiveles();
        System.out.println("Cantidad de niveles cargados: " + niveles.size());


        for (int i = 0; i < niveles.size(); i++) {
            NivelPartida nivel = niveles.get(i);
            InfoDiaNivel info = new InfoDiaNivel(i + 1, nivel);
            diasNiveles.add(info);
        }

        posicionarTarjetas();
    }

    private void posicionarTarjetas() {
        float anchoVentana = Gdx.graphics.getWidth();
        float altoVentana = Gdx.graphics.getHeight();

        int cantidadDias = diasNiveles.size();
        float anchoTotal = (cantidadDias * Recursos.ANCHO_DIA) + ((cantidadDias - 1) * Recursos.ESPACIADO);
        float inicioX = (anchoVentana - anchoTotal) / 2f;
        float posY = altoVentana / 2f + 50f;

        for (int i = 0; i < diasNiveles.size(); i++) {
            InfoDiaNivel info = diasNiveles.get(i);
            float posX = inicioX + (i * (Recursos.ANCHO_DIA + Recursos.ESPACIADO));
            info.setPosicion(posX, posY);
        }
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

        for (InfoDiaNivel info : diasNiveles) {
            info.dibujar(batch, false);
        }
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Pantalla.cambiarPantalla(new PantallaMenu());
        }
    }

    @Override
    public void resize(int width, int height) {
        if (titulo != null) {
            titulo.setPosition(width/2f - titulo.getAncho()/2f, height - 100);
            resumenPuntaje.setPosition(width/2f - resumenPuntaje.getAncho()/2f, height - 180);
            opcionMenu.setPosition(width/2f - opcionMenu.getAncho()/2f, 80);
            posicionarTarjetas();
        }
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
