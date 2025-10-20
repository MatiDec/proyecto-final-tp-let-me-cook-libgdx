package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.mapa.niveles.GestorPartida;
import com.hebergames.letmecook.mapa.niveles.NivelPartida;
import com.hebergames.letmecook.pantallas.juego.InfoDiaNivel;
import com.hebergames.letmecook.pantallas.juego.PantallaJuego;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

import java.util.ArrayList;

public class PantallaCalendario extends Pantalla {
    private final PantallaJuego PANTALLA_JUEGO;
    private final SpriteBatch BATCH;

    private GestorPartida gestorPartida;
    private Viewport viewport;
    private OrthographicCamera camara;

    private Texto tituloCalendario;
    private ArrayList<InfoDiaNivel> diasNiveles;

    public PantallaCalendario(PantallaJuego pantallaJuego) {
        this.PANTALLA_JUEGO = pantallaJuego;
        this.BATCH = Render.batch;
        this.camara = new OrthographicCamera();
        this.viewport = new ScreenViewport(camara);
        this.gestorPartida = GestorPartida.getInstancia();
        this.diasNiveles = new ArrayList<>();
    }

    @Override
    public void show() {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        inicializarCalendario();
    }

    private void inicializarCalendario() {
        tituloCalendario = new Texto(Recursos.FUENTE_MENU, 48, Color.WHITE, true);
        tituloCalendario.setTexto("Calendario de actividades");

        diasNiveles.clear();
        ArrayList<NivelPartida> niveles = gestorPartida.getTodosLosNiveles();

        for (int i = 0; i < niveles.size(); i++) {
            NivelPartida nivel = niveles.get(i);
            InfoDiaNivel info = new InfoDiaNivel(i + 1, nivel);
            diasNiveles.add(info);
        }

        posicionarElementos();
    }

    private void posicionarElementos() {
        float anchoViewport = viewport.getWorldWidth();
        float altoViewport = viewport.getWorldHeight();
        float centroX = anchoViewport / 2f;

        tituloCalendario.setPosition(
            centroX - tituloCalendario.getAncho() / 2f,
            altoViewport - 80f
        );

        int cantidadDias = diasNiveles.size();
        float anchoTotal = (cantidadDias * Recursos.ANCHO_DIA) + ((cantidadDias - 1) * Recursos.ESPACIADO);
        float inicioX = centroX - (anchoTotal / 2f);
        float posY = altoViewport / 2f;

        for (int i = 0; i < diasNiveles.size(); i++) {
            InfoDiaNivel info = diasNiveles.get(i);
            float posX = inicioX + (i * (Recursos.ANCHO_DIA + Recursos.ESPACIADO));
            info.setPosicion(posX, posY);
        }
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        camara.update();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        BATCH.setProjectionMatrix(camara.combined);
        BATCH.begin();

        BATCH.setColor(0, 0, 0, 0.7f);
        float anchoViewport = viewport.getWorldWidth();
        float altoViewport = viewport.getWorldHeight();
        BATCH.draw(Recursos.PIXEL, 0, 0, anchoViewport, altoViewport);
        BATCH.setColor(1, 1, 1, 1);

        tituloCalendario.dibujar();

        int nivelActualIndex = gestorPartida.getNivelActualIndex();
        for (int i = 0; i < diasNiveles.size(); i++) {
            InfoDiaNivel info = diasNiveles.get(i);
            boolean esNivelActual = (i == nivelActualIndex);
            info.dibujar(BATCH, esNivelActual);
        }

        BATCH.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if (tituloCalendario != null) {
            posicionarElementos();
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
