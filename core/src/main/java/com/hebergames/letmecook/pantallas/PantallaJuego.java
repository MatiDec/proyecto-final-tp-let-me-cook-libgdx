package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.elementos.MapaColisiones;
import com.hebergames.letmecook.entidades.JugadorHost;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;
import com.hebergames.letmecook.utiles.GestorAudio;

public class PantallaJuego extends Pantalla {

    private SpriteBatch batch;
    private Entrada entrada;
    private JugadorHost jugadorHost;
    private Texture jugadorSheet;
    private Animation<TextureRegion> animacionJugador;
    private MapaColisiones mapaColisiones;

    private PantallaPausa pantallaPausa;
    private boolean juegoEnPausa = false;

    private GestorAudio gestorAudio;

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

        pantallaPausa = new PantallaPausa(this);

        //Música de fondo en el nivel.
        gestorAudio = GestorAudio.getInstance();
        gestorAudio.cargarMusica("musica_fondo", Recursos.CANCION_FONDO);
        gestorAudio.reproducirCancion("musica_fondo", true);
        gestorAudio.pausarMusica();

        //debug
        //shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePausa();
        }

        if (!juegoEnPausa) {
            jugadorHost.actualizar(delta);
            entrada.actualizarEntradas();
            gestorAudio.reanudarMusica();
        }

        // Renderizar el juego siempre (para que se vea de fondo)
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

        if (juegoEnPausa) {
            pantallaPausa.render(delta);
        }
    }

    public void togglePausa() {
        juegoEnPausa = !juegoEnPausa;

        if (juegoEnPausa) {
            pantallaPausa.show();
            gestorAudio.pausarMusica();
        } else {
            entrada = new Entrada();
            Gdx.input.setInputProcessor(entrada);
            entrada.registrarJugador(jugadorHost, new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D});
            gestorAudio.reanudarMusica();
        }
    }

    public void reanudarJuego() {
        juegoEnPausa = false;
        entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);
        entrada.registrarJugador(jugadorHost, new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D});

        gestorAudio.reanudarMusica();
    }

    public boolean isJuegoEnPausa() {
        return this.juegoEnPausa;
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
        // Pausar música cuando la aplicación se minimiza
        if (gestorAudio != null) {
            gestorAudio.pausarMusica();
        }
    }

    @Override
    public void resume() {
        // Reanudar música cuando la aplicación vuelve al foco
        if (gestorAudio != null && !juegoEnPausa) {
            gestorAudio.reanudarMusica();
        }
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        jugadorSheet.dispose();
        if (pantallaPausa != null) {
            pantallaPausa.dispose();
        }
        // Liberar recursos de audio
        if (gestorAudio != null) {
            gestorAudio.dispose();
        }
        //shapeRenderer.dispose();
    }
}
