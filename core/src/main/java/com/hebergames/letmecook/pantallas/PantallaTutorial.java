package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.elementos.Imagen;
import com.hebergames.letmecook.utiles.Render;

public class PantallaTutorial extends Pantalla {

    private Imagen imagenTutorial;
    private Imagen botonCerrar;
    private SpriteBatch BATCH;

    private Viewport viewport;
    private OrthographicCamera camara;

    private Rectangle areaBotonCerrar;
    private Vector3 coordenadasTouch;

    private static final String RUTA_TUTORIAL = "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/pantallaTutorial.png";
    private static final String RUTA_BOTON_CERRAR = "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/botonCerrar.png";
    private static final float TAMANO_BOTON = 60f;
    private static final float MARGEN_BOTON = 20f;

    @Override
    public void show() {
        System.out.println("Se creó una pantalla tutorial.");
        camara = new OrthographicCamera();
        viewport = new ScreenViewport(camara);
        camara.setToOrtho(false,
            Gdx.graphics.getWidth() * 1f,
            Gdx.graphics.getHeight() * 1f);
        camara.update();

        BATCH = Render.batch;
        coordenadasTouch = new Vector3();
        areaBotonCerrar = new Rectangle();

        // Cargar la imagen del tutorial
        imagenTutorial = new Imagen(RUTA_TUTORIAL);

        // Crear botón de cerrar (X)
        botonCerrar = new Imagen(RUTA_BOTON_CERRAR);

        posicionarElementos();
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        camara.update();

        // Manejar input
        manejarInput();

        BATCH.setProjectionMatrix(camara.combined);
        BATCH.begin();

        // Dibujar imagen del tutorial
        imagenTutorial.dibujar();

        // Dibujar botón de cerrar
        dibujarBotonCerrar();

        BATCH.end();
    }

    private void manejarInput() {
        // Detectar tecla ESC
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            volverAlMenu();
            return;
        }

        // Detectar clic en botón de cerrar
        if (Gdx.input.justTouched()) {
            coordenadasTouch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camara.unproject(coordenadasTouch);

            if (areaBotonCerrar.contains(coordenadasTouch.x, coordenadasTouch.y)) {
                volverAlMenu();
            }
        }
    }

    private void dibujarBotonCerrar() {
        // Dibujar el botón de cerrar
        if (botonCerrar != null) {
            botonCerrar.dibujar();
        }
    }

    private void posicionarElementos() {
        float anchoVirtual = viewport.getWorldWidth();
        float altoVirtual = viewport.getWorldHeight();

        // Posicionar imagen del tutorial (pantalla completa)
        imagenTutorial.setSize((int)anchoVirtual, (int)altoVirtual);
        imagenTutorial.setPosicion(0, 0);

        // Posicionar botón de cerrar (esquina superior derecha)
        float xBoton = anchoVirtual - TAMANO_BOTON - MARGEN_BOTON;
        float yBoton = altoVirtual - TAMANO_BOTON - MARGEN_BOTON;

        areaBotonCerrar.set(xBoton, yBoton, TAMANO_BOTON, TAMANO_BOTON);

        botonCerrar.setSize((int)TAMANO_BOTON, (int)TAMANO_BOTON);
        botonCerrar.setPosicion((int)xBoton, (int)yBoton);
    }

    private void volverAlMenu() {
        cambiarPantalla(new PantallaMenu());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        posicionarElementos();
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
