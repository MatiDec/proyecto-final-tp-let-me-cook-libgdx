package com.hebergames.letmecook.pantallas.pantallasmaquinas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.entidades.JugadorHost;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.pantallas.Pantalla;
import com.hebergames.letmecook.utiles.Configuracion;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

public abstract class PantallaMaquina extends Pantalla {

    protected final JugadorHost JUGADOR;
    protected final SpriteBatch BATCH;
    protected Entrada entrada;
    protected Viewport viewport;
    protected OrthographicCamera camara;

    private boolean tieneOverlay;

    public PantallaMaquina(boolean tieneOverlay) {
        this.JUGADOR = Configuracion.getInstancia().getJugadorPrincipal();
        this.BATCH = Render.batch;
        this.camara = new OrthographicCamera();
        this.viewport = new ScreenViewport(camara);
        this.tieneOverlay = tieneOverlay;
    }

    @Override
    public void show() {
        if (tieneOverlay) {
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            entrada = new Entrada();
            Gdx.input.setInputProcessor(entrada);
            inicializarInterfaz();
            posicionarElementos();
            registrarInteracciones();
        }
        ejecutarLogicaMaquina();
    }

    @Override
    public void render(float delta) {
        if (tieneOverlay) {
            entrada.actualizarEntradas();
            viewport.apply();
            camara.update();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            BATCH.setProjectionMatrix(camara.combined);
            BATCH.begin();

            // Fondo semitransparente
            BATCH.setColor(0, 0, 0, 0.5f);
            float anchoViewport = viewport.getWorldWidth();
            float altoViewport = viewport.getWorldHeight();
            BATCH.draw(Recursos.PIXEL, 0, 0, anchoViewport, altoViewport);
            BATCH.setColor(1, 1, 1, 1);

            renderizarInterfaz();

            BATCH.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        actualizarLogicaMaquina(delta);
    }

    @Override
    public void resize(int width, int height) {
        if (tieneOverlay) {
            viewport.update(width, height, true);
            posicionarElementos();
        }
    }

    // Métodos abstractos que cada máquina debe implementar
    protected abstract void ejecutarLogicaMaquina();
    protected abstract void actualizarLogicaMaquina(float delta);

    // Métodos por defecto para máquinas con overlay (pueden ser sobrescritos)
    protected void inicializarInterfaz() {}
    protected void posicionarElementos() {}
    protected void registrarInteracciones() {}
    protected void renderizarInterfaz() {}

    // Método helper para cerrar la máquina
    protected abstract void cerrarMaquina();

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
