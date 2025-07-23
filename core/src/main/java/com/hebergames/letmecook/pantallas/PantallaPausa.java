package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.eventos.TextoInteractuable;
import com.hebergames.letmecook.utiles.Configuracion;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

public class PantallaPausa extends Pantalla {

    private final PantallaJuego PANTALLA_JUEGO;
    private final SpriteBatch BATCH;

    private Texto oContinuar, oMenuPrincipal, oSalir;
    private Entrada entrada;

    public PantallaPausa(PantallaJuego pantallaJuego) {
        this.PANTALLA_JUEGO = pantallaJuego;
        this.BATCH = Render.batch;
    }

    @Override
    public void show() {
        entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);

        oContinuar = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        oContinuar.setTexto("Continuar");

        oMenuPrincipal = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        oMenuPrincipal.setTexto("Menú Principal");

        oSalir = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        oSalir.setTexto("Salir");

        float centroX = Configuracion.ANCHO / 2f;
        float posY = Configuracion.ALTO / 2f + oContinuar.getAlto();
        float espaciado = oContinuar.getAlto() + 40;

        oContinuar.setPosition(centroX - oContinuar.getAncho() / 2, posY);
        oMenuPrincipal.setPosition(centroX - oMenuPrincipal.getAncho() / 2, posY - espaciado);
        oSalir.setPosition(centroX - oSalir.getAncho() / 2, posY - espaciado * 2);

        entrada.registrar(new TextoInteractuable(oContinuar, () -> {
            PANTALLA_JUEGO.reanudarJuego();
        }));

        entrada.registrar(new TextoInteractuable(oMenuPrincipal, () -> {
            cambiarPantalla(new PantallaMenu());
        }));

        entrada.registrar(new TextoInteractuable(oSalir, () -> {
            Gdx.app.exit();
        }));
    }

    @Override
    public void render(float delta) {
        entrada.actualizarEntradas();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        BATCH.begin();

        BATCH.setColor(0, 0, 0, 0.5f);
        BATCH.draw(Recursos.PIXEL, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        BATCH.setColor(1, 1, 1, 1);

        oContinuar.dibujar();
        oMenuPrincipal.dibujar();
        oSalir.dibujar();

        BATCH.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
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
    public void dispose() {}
}
