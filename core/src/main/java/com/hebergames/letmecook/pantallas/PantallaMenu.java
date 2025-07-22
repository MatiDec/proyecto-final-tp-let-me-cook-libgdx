package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g3d.shaders.DepthShader;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.utiles.Configuracion;
import com.hebergames.letmecook.elementos.Imagen;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

public class PantallaMenu implements Screen {

    Imagen fondo;
    SpriteBatch b;

    Texto o1, o2; // Así la cantidad de opciones que tenga el menú

    @Override
    public void show() {
        fondo = new Imagen(Recursos.FONDO);
        fondo.setSize((int)Configuracion.ANCHO, (int)Configuracion.ALTO);
        b = Render.batch;

        o1 = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        o1.setTexto("SURVIVVRS VALLEVV");

        float posInY = (Configuracion.ALTO/2)+(o1.getAlto()/2);
        float avance = o1.getAlto()+40;

        o1.setPosition((Configuracion.ANCHO/2) - (o1.getAncho()/2), posInY);

        o2 = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        o2.setTexto("EL COSO");
        o2.setPosition((Configuracion.ANCHO/2) - (o2.getAncho()/2), posInY-(avance*1));
    }

    @Override
    public void render(float v) {
        b.begin();
        fondo.dibujar();
        o1.dibujar();
        o2.dibujar();
        b.end();
    }

    @Override
    public void resize(int i, int i1) {

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
