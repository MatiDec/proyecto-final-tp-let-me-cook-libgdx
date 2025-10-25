package com.hebergames.letmecook.pantallas.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.eventos.entrada.Entrada;
import com.hebergames.letmecook.pantallas.Pantalla;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

public class PantallaDetalleTutorial extends Pantalla {
    private SpriteBatch batch;
    private OrthographicCamera camara;
    private Viewport viewport;
    private AnimacionTutorial animacion;
    private Texture botonCerrar;
    private Rectangle areaCerrar;
    private PantallaTutorial pantallaTutorial;
    private Texto textoInfo;

    public PantallaDetalleTutorial(ElementoTutorial elemento, PantallaTutorial pantallaTutorial) {
        this.pantallaTutorial = pantallaTutorial;
        batch = Render.batch;
        camara = new OrthographicCamera();
        viewport = new ScreenViewport(camara);

        // Crear animación usando los datos del elemento
        animacion = new AnimacionTutorial(
            elemento.getRutaSpritesheet(),
            elemento.getFrameWidth(),
            elemento.getFrameHeight(),
            elemento.getCantidadFrames(),
            elemento.getFps()
        );

        try {
            botonCerrar = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/botonCerrar.png"));
        } catch (Exception e) {
            System.err.println("Error cargando botón cerrar");
        }

        textoInfo = new Texto(Recursos.FUENTE_MENU, 24, Color.YELLOW, true);
        if (!animacion.estaCargado()) {
            textoInfo.setTexto("Tutorial no disponible");
        } else {
            textoInfo.setTexto("Presiona ESC o haz clic en X para volver");
        }
    }

    @Override
    public void show() {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        Entrada entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);

        float tamanoBtn = 50f;

        areaCerrar = new Rectangle(
            viewport.getWorldWidth() - tamanoBtn - 20f,
            viewport.getWorldHeight() - tamanoBtn - 20f,  // CAMBIAR tamanoBtn por tamanoBtn
            tamanoBtn,  // CAMBIAR tamanoBtn por tamanoBtn
            tamanoBtn
        );

        entrada.setCallbackClick((worldX, worldY) -> {
            System.out.println("Click en detalle tutorial: " + worldX + ", " + worldY);
            System.out.println("Área cerrar: " + areaCerrar);

            if (areaCerrar.contains(worldX, worldY)) {
                System.out.println("Cerrando detalle tutorial");
                cerrar();
            }
        });
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            cerrar();
            return;
        }

        // Actualizar animación
        if (animacion != null) {
            animacion.actualizar(delta);
        }

        viewport.apply();
        camara.update();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.setProjectionMatrix(camara.combined);
        batch.begin();

        // Fondo semi-transparente
        batch.setColor(0, 0, 0, 0.9f);
        batch.draw(Recursos.PIXEL, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.setColor(1, 1, 1, 1);

        // Dibujar animación centrada
        if (animacion != null && animacion.estaCargado()) {
            TextureRegion frameActual = animacion.getFrameActual();
            if (frameActual != null) {
                float escala = 4f; // Escalar el frame para que se vea más grande
                float anchoFrame = frameActual.getRegionWidth() * escala;
                float altoFrame = frameActual.getRegionHeight() * escala;

                // Limitar el tamaño máximo
                float maxAncho = viewport.getWorldWidth() * 0.8f;
                float maxAlto = viewport.getWorldHeight() * 0.8f;

                if (anchoFrame > maxAncho) {
                    float factor = maxAncho / anchoFrame;
                    anchoFrame *= factor;
                    altoFrame *= factor;
                }

                if (altoFrame > maxAlto) {
                    float factor = maxAlto / altoFrame;
                    anchoFrame *= factor;
                    altoFrame *= factor;
                }

                float xFrame = (viewport.getWorldWidth() - anchoFrame) / 2f;
                float yFrame = (viewport.getWorldHeight() - altoFrame) / 2f;

                // Marco blanco alrededor
                batch.setColor(Color.WHITE);
                float grosorMarco = 5f;
                batch.draw(Recursos.PIXEL, xFrame - grosorMarco, yFrame - grosorMarco,
                    anchoFrame + 2 * grosorMarco, altoFrame + 2 * grosorMarco);

                // Fondo negro
                batch.setColor(0.1f, 0.1f, 0.1f, 1f);
                batch.draw(Recursos.PIXEL, xFrame, yFrame, anchoFrame, altoFrame);

                // Frame de la animación
                batch.setColor(1, 1, 1, 1);
                batch.draw(frameActual, xFrame, yFrame, anchoFrame, altoFrame);
            }
        }

        // Texto informativo
        textoInfo.setPosition(
            viewport.getWorldWidth() / 2f - textoInfo.getAncho() / 2f,
            50f
        );
        textoInfo.dibujar();

        // Botón cerrar
        if (botonCerrar != null) {
            batch.draw(botonCerrar, areaCerrar.x, areaCerrar.y, areaCerrar.width, areaCerrar.height);
        }

        batch.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void cerrar() {
        cambiarPantalla(pantallaTutorial);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        float tamanoBton = 50f;
        areaCerrar.set(
            viewport.getWorldWidth() - tamanoBton - 20f,
            viewport.getWorldHeight() - tamanoBton - 20f,
            tamanoBton,
            tamanoBton
        );
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (botonCerrar != null) {
            botonCerrar.dispose();
        }
        // No necesitamos dispose de animación porque usa TextureRegion
    }
}
