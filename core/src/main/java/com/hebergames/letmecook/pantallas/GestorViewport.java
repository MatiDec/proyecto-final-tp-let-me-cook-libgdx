package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GestorViewport {

    private static final float MUNDO_ANCHO = 1920f;
    private static final float MUNDO_ALTO = 1080f;
    private static final float ZOOM_JUEGO = 1.7f;

    private final Viewport viewportJuego;
    private final Viewport viewportUI;
    private final OrthographicCamera camaraJuego;
    private final OrthographicCamera camaraUI;

    public GestorViewport() {
        camaraJuego = new OrthographicCamera();
        camaraJuego.setToOrtho(false, 1920, 1080);
        camaraJuego.zoom = ZOOM_JUEGO;

        camaraUI = new OrthographicCamera();
        camaraUI.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewportJuego = new FitViewport(MUNDO_ANCHO, MUNDO_ALTO, camaraJuego);
        viewportUI = new ScreenViewport(camaraUI);
    }

    public void actualizarCamaraJuego(Vector2 posicionJugador) {
        camaraJuego.position.set(posicionJugador.x, posicionJugador.y, 0);
        camaraJuego.update();
    }

    public void actualizarCamaraUI() {
        camaraUI.update();
    }

    public void resize(int width, int height) {
        viewportJuego.update(width, height);
        viewportUI.update(width, height, true);
    }

    public Vector2 convertirCoordenadasJuego(int screenX, int screenY) {
        Vector2 coordenadas = new Vector2(screenX, screenY);
        viewportJuego.unproject(coordenadas);
        return coordenadas;
    }

    public Vector2 convertirCoordenadasUI(int screenX, int screenY) {
        Vector2 coordenadas = new Vector2(screenX, screenY);
        viewportUI.unproject(coordenadas);
        return coordenadas;
    }

    public Viewport getViewportJuego() {
        return viewportJuego;
    }

    public Viewport getViewportUI() {
        return viewportUI;
    }

    public OrthographicCamera getCamaraJuego() {
        return camaraJuego;
    }

    public OrthographicCamera getCamaraUI() {
        return camaraUI;
    }
}
