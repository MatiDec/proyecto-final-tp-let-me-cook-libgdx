package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.eventos.TextoInteractuable;
import com.hebergames.letmecook.utiles.GestorJugadores;
import com.hebergames.letmecook.elementos.Imagen;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

public class PantallaMenu extends Pantalla {

    private Imagen fondo;
    private SpriteBatch BATCH;

    private Texto[] opcionesTexto;
    private TextoInteractuable[] opcionesInteractuables;

    private static final int TOTAL_OPCIONES = OpcionMenuPrincipal.values().length;

    private static final float DISENO_ANCHO = 1920f;
    private static final float DISENO_ALTO = 1080f;

    // Viewport y cámara
    private Viewport viewport;
    private OrthographicCamera camara;
    private Vector3 coordenadasMouse;

    @Override
    public void show() {
        camara = new OrthographicCamera();
        viewport = new ScreenViewport(camara);
        camara.setToOrtho(false,
            Gdx.graphics.getWidth() * 1f,
            Gdx.graphics.getHeight() * 1f);
        camara.update();

        coordenadasMouse = new Vector3();

        inicializarTextos();
        configurarEntrada();

        fondo = new Imagen(Recursos.FONDO);
        BATCH = Render.batch;
    }

    private void configurarEntrada() {
        Entrada entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);

        // Inicializar array de interactuables
        opcionesInteractuables = new TextoInteractuable[TOTAL_OPCIONES];

        // Crear cada opción interactuable con su acción correspondiente
        opcionesInteractuables[0] = new TextoInteractuable(opcionesTexto[0], () ->
            cambiarPantalla(new PantallaJuego()));

        opcionesInteractuables[1] = new TextoInteractuable(opcionesTexto[1], () ->
            System.out.println("Acá debería entrar al modo multijugador online"));

        opcionesInteractuables[2] = new TextoInteractuable(opcionesTexto[2], () ->
            cambiarPantalla(new PantallaTutorial()));

        opcionesInteractuables[3] = new TextoInteractuable(opcionesTexto[3], () ->
            cambiarPantalla(new PantallaOpciones()));

        opcionesInteractuables[4] = new TextoInteractuable(opcionesTexto[4], () -> {
            dispose();
            Gdx.app.exit();
        });

        // Registrar todas las opciones
        for (TextoInteractuable opcion : opcionesInteractuables) {
            entrada.registrar(opcion);
        }
    }

    private void inicializarTextos() {
        opcionesTexto = new Texto[TOTAL_OPCIONES];

        // Crear todos los textos
        for (int i = 0; i < TOTAL_OPCIONES; i++) {
            opcionesTexto[i] = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
            opcionesTexto[i].setTexto(OpcionMenuPrincipal.values()[i].getNombre());
        }

        posicionarTextos();
    }

    @Override
    public void render(float v) {
        viewport.apply();
        camara.update();
        actualizarFondo();

        // Actualizar efectos hover
        actualizarHover();

        BATCH.setProjectionMatrix(camara.combined);
        BATCH.begin();

        fondo.dibujar();

        // Dibujar todas las opciones
        for (Texto opcion : opcionesTexto) {
            opcion.dibujar();
        }

        BATCH.end();
    }

    private void actualizarHover() {
        coordenadasMouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camara.unproject(coordenadasMouse);

        for (TextoInteractuable opcion : opcionesInteractuables) {
            opcion.actualizarHover(coordenadasMouse.x, coordenadasMouse.y);
        }
    }

    private void actualizarFondo() {
        fondo.setSize((int) GestorJugadores.ANCHO, (int) GestorJugadores.ALTO);
        fondo.setPosicion(0, 0);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        posicionarTextos();
        actualizarFondo();
    }

    private void posicionarTextos() {
        float anchoVirtual = viewport.getWorldWidth();
        float altoVirtual = viewport.getWorldHeight();

        float centroX = anchoVirtual / 2f;
        float centroY = altoVirtual / 2f;

        float espaciado = Math.max(40, altoVirtual * 0.08f);
        float alturaOpcion = opcionesTexto[0].getAlto();

        // Calcular altura total del menú
        float alturaTotal = (alturaOpcion * TOTAL_OPCIONES) + (espaciado * (TOTAL_OPCIONES - 1));
        float posicionInicial = centroY + (alturaTotal / 2f) - (alturaOpcion / 2f);

        // Posicionar todas las opciones
        for (int i = 0; i < TOTAL_OPCIONES; i++) {
            float yPos = posicionInicial - (alturaOpcion + espaciado) * i;
            opcionesTexto[i].setPosition(
                centroX - (opcionesTexto[i].getAncho() / 2f),
                yPos
            );
        }
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

    }
}
