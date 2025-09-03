package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.eventos.TextoInteractuable;
import com.hebergames.letmecook.utiles.Configuracion;
import com.hebergames.letmecook.elementos.Imagen;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

public class PantallaMenu extends Pantalla {

    private Imagen fondo;
    private SpriteBatch BATCH;

    private Texto o1, o2, o3, o4; // Así la cantidad de opciones que tenga el menú

    private static final float DISENO_ANCHO = 1920f;
    private static final float DISENO_ALTO = 1080f;

    // Viewport y cámara
    private Viewport viewport;
    private OrthographicCamera camara;

    @Override
    public void show() {
        camara = new OrthographicCamera();
        viewport = new ScreenViewport(camara);
        camara.setToOrtho(false,
            Gdx.graphics.getWidth() * 1f,
            Gdx.graphics.getHeight() * 1f);
        camara.update();

        inicializarTextos();
        configurarEntrada();

        fondo = new Imagen(Recursos.FONDO);
        BATCH = Render.batch;

    }

    private void configurarEntrada() {
        Entrada entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);

        //La flechita es porque TextoInteractuable recibe una función, es por el Runnable. Lo mejor sería reemplazarlo pq es una expresión lambda y es complejo, se puede simplificar.
        TextoInteractuable multijugadorLocal = new TextoInteractuable(o1, () ->
            cambiarPantalla(new PantallaJuego()));

        TextoInteractuable multijugadorOnline = new TextoInteractuable(o2, () ->
            System.out.println("Acá debería entrar al modo multijugador online"));

        TextoInteractuable opciones = new TextoInteractuable(o3, () ->
            cambiarPantalla(new PantallaOpciones()));

        TextoInteractuable salir = new TextoInteractuable(o4, () -> {
            dispose();
            Gdx.app.exit();
        });

        entrada.registrar(multijugadorLocal);
        entrada.registrar(multijugadorOnline);
        entrada.registrar(opciones);
        entrada.registrar(salir);
    }

    private void inicializarTextos() {
        o1 = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        o1.setTexto("Multijugador Local");

        o2 = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        o2.setTexto("Multijugador Online");

        o3 = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        o3.setTexto("Opciones");

        o4 = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        o4.setTexto("Salir");

        posicionarTextos();
    }

    @Override
    public void render(float v) {
        viewport.apply();
        camara.update();
        actualizarFondo();
        BATCH.setProjectionMatrix(camara.combined);
        BATCH.begin();
        fondo.dibujar();
        o1.dibujar();
        o2.dibujar();
        o3.dibujar();
        o4.dibujar();
        BATCH.end();
    }

    private void actualizarFondo() {
        fondo.setSize((int)Configuracion.ANCHO, (int)Configuracion.ALTO);
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
        float alturaOpcion = o1.getAlto();

        float alturaTotal = (alturaOpcion * 4) + (espaciado * 3);
        float posicionInicial = centroY + (alturaTotal / 2f) - (alturaOpcion / 2f);

        o1.setPosition(centroX - (o1.getAncho() / 2f), posicionInicial);
        o2.setPosition(centroX - (o2.getAncho() / 2f), posicionInicial - (alturaOpcion + espaciado));
        o3.setPosition(centroX - (o3.getAncho() / 2f), posicionInicial - (alturaOpcion + espaciado) * 2);
        o4.setPosition(centroX - (o4.getAncho() / 2f), posicionInicial - (alturaOpcion + espaciado) * 3);
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
