package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.elementos.MapaColisiones;
import com.hebergames.letmecook.entidades.JugadorHost;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.utiles.Render;

public class PantallaJuego extends Pantalla {

    private SpriteBatch batch;
    private Entrada entrada;
    private JugadorHost jugadorHost;
    private Texture jugadorSheet;
    private Animation<TextureRegion> animacionJugador;
    private MapaColisiones mapaColisiones;

   //debug aca tambien
    //private ShapeRenderer shapeRenderer;

    @Override
    public void show() {
        batch = Render.batch;

        jugadorSheet = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/imagenes/imagendepruebanomoral.png"));
        TextureRegion[][] tmp = TextureRegion.split(jugadorSheet, 32, 32);
        animacionJugador = new Animation<>(0.5f, tmp[0]);

        mapaColisiones = new MapaColisiones("core/src/main/java/com/hebergames/letmecook/imagenes/colisionesnomoral.png");

        jugadorHost = new JugadorHost(100, 100, animacionJugador, mapaColisiones.getZonasColision());

        entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);
        entrada.registrarJugador(jugadorHost, new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D});

       //debug
        //shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        jugadorHost.actualizar(delta);
        entrada.actualizarEntradas();


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
        jugadorHost.dibujar(batch);
        batch.end();


        //esto de aca es debug no se olviden del dispose de abajo
        /*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        for (Rectangle r : mapaColisiones.getZonasColision()) {
            shapeRenderer.rect(r.x, r.y, r.width, r.height);
        }
        shapeRenderer.end();
         */
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        jugadorSheet.dispose();
        //shapeRenderer.dispose();
    }
}
