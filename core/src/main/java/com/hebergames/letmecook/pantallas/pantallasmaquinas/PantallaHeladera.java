package com.hebergames.letmecook.pantallas.pantallasmaquinas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entidades.JugadorHost;
import com.hebergames.letmecook.entregables.ingredientes.Carne;
import com.hebergames.letmecook.entregables.ingredientes.Pan;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.eventos.TextoInteractuable;
import com.hebergames.letmecook.pantallas.Pantalla;
import com.hebergames.letmecook.pantallas.PantallaJuego;
import com.hebergames.letmecook.utiles.Configuracion;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

public class PantallaHeladera extends Pantalla {

    private final JugadorHost JUGADOR;
    private final SpriteBatch BATCH;

    private Texto tCarne, tPan, tCerrar, tInventario;
    private Entrada entrada;
    private Viewport viewport;
    private OrthographicCamera camara;

    // Texturas de ingredientes (cargarlas desde recursos)
    private TextureRegion texturaCarne;
    private TextureRegion texturaPan;

    public PantallaHeladera() {
        this.JUGADOR = Configuracion.getInstancia().getJugadorPrincipal();
        this.BATCH = Render.batch;
        this.camara = new OrthographicCamera();
        this.viewport = new ScreenViewport(camara);

        cargarTexturas();
    }

    private void cargarTexturas() {
        Texture ingredientesTextura = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/ingredientes.png"));
        TextureRegion[][] tmp = TextureRegion.split(ingredientesTextura, 32, 32);
        texturaCarne = tmp[0][0];
        texturaPan = tmp[0][1];
    }

    @Override
    public void show() {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);
        inicializarTextos();
        posicionarTextos();
        registrarEntradas();
    }

    private void inicializarTextos() {
        tCarne = new Texto(Recursos.FUENTE_MENU, 48, Color.WHITE, true);
        tCarne.setTexto("Carne");

        tPan = new Texto(Recursos.FUENTE_MENU, 48, Color.WHITE, true);
        tPan.setTexto("Pan");

        tCerrar = new Texto(Recursos.FUENTE_MENU, 48, Color.RED, true);
        tCerrar.setTexto("Cerrar");

        tInventario = new Texto(Recursos.FUENTE_MENU, 32, Color.YELLOW, true);
        actualizarTextoInventario();
    }

    private void actualizarTextoInventario() {
        if (JUGADOR.tieneInventarioLleno()) {
            tInventario.setTexto("Inventario: " + JUGADOR.getInventario().getNombre());
        } else {
            tInventario.setTexto("Inventario: Vacío");
        }
    }

    private void posicionarTextos() {
        float anchoViewport = viewport.getWorldWidth();
        float altoViewport = viewport.getWorldHeight();
        float centroX = anchoViewport / 2f;
        float centroY = altoViewport / 2f;
        float espaciado = 80f;

        tCarne.setPosition(centroX - tCarne.getAncho() / 2f, centroY + espaciado);
        tPan.setPosition(centroX - tCarne.getAncho() / 2f, centroY + espaciado*2);
        tCerrar.setPosition(centroX - tCerrar.getAncho() / 2f, centroY - espaciado * 3);
        tInventario.setPosition(50, altoViewport - 50);
    }

    private void registrarEntradas() {
        entrada.registrar(new TextoInteractuable(tCarne, () -> {
            if (!JUGADOR.tieneInventarioLleno()) {
                JUGADOR.guardarEnInventario(new Carne(texturaCarne));
                PantallaJuego juego = (PantallaJuego) Pantalla.getPantallaActual();
                JUGADOR.setAnimacion(juego.getAnimacionConItem("carne"));
                actualizarTextoInventario();
            }
        }));

        entrada.registrar(new TextoInteractuable(tPan, () -> {
            if (!JUGADOR.tieneInventarioLleno()) {
                JUGADOR.guardarEnInventario(new Pan(texturaPan));
                PantallaJuego juego = (PantallaJuego) Pantalla.getPantallaActual();
                JUGADOR.setAnimacion(juego.getAnimacionConItem("pan"));
                actualizarTextoInventario();
            }
        }));

        entrada.registrar(new TextoInteractuable(tCerrar, () -> {
            Pantalla pantallaActual = Pantalla.getPantallaActual();
            if (pantallaActual instanceof PantallaJuego) {
                ((PantallaJuego) pantallaActual).cerrarHeladera();
            }
        }));
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

        tCarne.dibujar();
        tPan.dibujar();
        tCerrar.dibujar();
        tInventario.dibujar();

        BATCH.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        posicionarTextos();
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
