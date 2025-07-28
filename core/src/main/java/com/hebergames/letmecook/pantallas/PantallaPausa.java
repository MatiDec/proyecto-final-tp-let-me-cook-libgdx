package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.eventos.TextoInteractuable;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

public class PantallaPausa extends Pantalla {

    private final PantallaJuego PANTALLA_JUEGO;
    private final SpriteBatch BATCH;

    private Texto oContinuar, oMenuPrincipal;
    private Entrada entrada;

    private Viewport viewport;
    private OrthographicCamera camara;

    public PantallaPausa(PantallaJuego pantallaJuego) {
        this.PANTALLA_JUEGO = pantallaJuego;
        this.BATCH = Render.batch;
        this.camara = new OrthographicCamera();
        this.viewport = new ScreenViewport(camara);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void show() {

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);
        inicializarOpciones();
        posicionarTextos();
        registrarEntradas();

    }

    private void registrarEntradas() {
        entrada.registrar(new TextoInteractuable(oContinuar, () -> {
            PANTALLA_JUEGO.reanudarJuego();
        }));

        entrada.registrar(new TextoInteractuable(oMenuPrincipal, () -> {
            PANTALLA_JUEGO.detenerHilos();
            cambiarPantalla(new PantallaMenu());
        }));
    }

    private void inicializarOpciones() {
        oContinuar = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        oContinuar.setTexto("Continuar");

        oMenuPrincipal = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        oMenuPrincipal.setTexto("Menú Principal");
    }

    private void posicionarTextos() {
        float anchoViewport = viewport.getWorldWidth();
        float altoViewport = viewport.getWorldHeight();

        float centroX = anchoViewport / 2f;
        float centroY = altoViewport / 2f;

        float espaciado = Math.max(40, altoViewport * 0.1f);
        float alturaTexto = oContinuar.getAlto();

        float posY = centroY + (alturaTexto / 2f) + (espaciado / 2f);

        oContinuar.setPosition(centroX - oContinuar.getAncho() / 2f, posY);
        oMenuPrincipal.setPosition(centroX - oMenuPrincipal.getAncho() / 2f, posY - alturaTexto - espaciado);
    }

    @Override
    public void render(float delta) {
        entrada.actualizarEntradas();
        viewport.apply();
        camara.update();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        BATCH.setProjectionMatrix(camara.combined);
        BATCH.begin();

        BATCH.setColor(0, 0, 0, 0.5f);


        float anchoViewport = viewport.getWorldWidth();
        float altoViewport = viewport.getWorldHeight();
        BATCH.draw(Recursos.PIXEL, 0, 0, anchoViewport, altoViewport);
        BATCH.setColor(1, 1, 1, 1);

        oContinuar.dibujar();
        oMenuPrincipal.dibujar();

        BATCH.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        if (oContinuar != null && oMenuPrincipal != null) {
            posicionarTextos();
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
