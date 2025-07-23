package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.eventos.TextoInteractuable;
import com.hebergames.letmecook.utiles.Configuracion;
import com.hebergames.letmecook.elementos.Imagen;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

public class PantallaMenu extends Pantalla {

    Imagen fondo;
    SpriteBatch b;

    Entrada entrada;

    Texto o1, o2, o3, o4; // Así la cantidad de opciones que tenga el menú

    @Override
    public void show() {
        fondo = new Imagen(Recursos.FONDO);
        fondo.setSize((int)Configuracion.ANCHO, (int)Configuracion.ALTO);
        b = Render.batch;

        o1 = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        o1.setTexto("Multijugador Local");

        float posInY = (Configuracion.ALTO/2)+(o1.getAlto()/2);
        float avance = o1.getAlto()+40;

        o1.setPosition((Configuracion.ANCHO/2) - (o1.getAncho()/2), posInY);

        o2 = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        o2.setTexto("Multijugador Online");
        o2.setPosition((Configuracion.ANCHO/2) - (o2.getAncho()/2), posInY-(avance*1));

        o3 = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        o3.setTexto("Opciones");
        o3.setPosition((Configuracion.ANCHO/2) - (o3.getAncho()/2), posInY-(avance*2));

        o4 = new Texto(Recursos.FUENTE_MENU, 72, Color.WHITE, true);
        o4.setTexto("Salir");
        o4.setPosition((Configuracion.ANCHO/2) - (o4.getAncho()/2), posInY-(avance*3));

        entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);

        //La flechita es porque TextoInteractuable recibe una función, es por el Runnable. Lo mejor sería reemplazarlo pq es una expresión lambda y es complejo, se puede simplificar.
        TextoInteractuable multijugadorLocal = new TextoInteractuable(o1, () ->
            cambiarPantalla(new PantallaJuego()));

        TextoInteractuable multijugadorOnline = new TextoInteractuable(o2, () ->
            System.out.println("Acá debería entrar al modo multijugador online"));

        TextoInteractuable opciones = new TextoInteractuable(o3, () ->
            System.out.println("Acá debería entrar al menu de opciones"));

        TextoInteractuable salir = new TextoInteractuable(o4, () ->
            Gdx.app.exit());

        entrada.registrar(multijugadorLocal);
        entrada.registrar(multijugadorOnline);
        entrada.registrar(opciones);
        entrada.registrar(salir);

    }

    @Override
    public void render(float v) {
        b.begin();
        fondo.dibujar();
        o1.dibujar();
        o2.dibujar();
        o3.dibujar();
        o4.dibujar();
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
