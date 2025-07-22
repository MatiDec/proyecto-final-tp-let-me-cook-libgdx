package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.entidades.JugadorHost;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.utiles.Render;

public class PantallaJuego extends Pantalla {

    private SpriteBatch batch;
    private Entrada entrada;
    private JugadorHost jugadorHost;
    private Texture jugadorSheet;
    private Animation<TextureRegion> animacionJugador;

    @Override
    public void show() {
        batch = Render.batch;

        jugadorSheet = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/imagenes/pruebadeimagen.png"));//AGREGAR TEXTURA DE PRUEBA
        TextureRegion[][] tmp = TextureRegion.split(jugadorSheet, 32, 32);
        animacionJugador = new Animation<>(0.5f, tmp[0]);// esto esta como inicio, modificar mas tarde

        jugadorHost = new JugadorHost(100, 100, animacionJugador);

        entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);

        entrada.registrarJugador(jugadorHost, new int[] {Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D});
    }

    @Override
    public void render(float delta) {
        entrada.actualizarEntradas();
        jugadorHost.actualizar(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        jugadorHost.dibujar(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        jugadorSheet.dispose();
    }
}
