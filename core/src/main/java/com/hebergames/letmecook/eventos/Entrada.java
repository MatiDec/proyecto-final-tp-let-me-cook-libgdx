package com.hebergames.letmecook.eventos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.maquinas.EstacionTrabajo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entrada implements InputProcessor {

    private final ArrayList<BotonInteractuable> ELEMENTOS_INTERACTUABLES = new ArrayList<>();
    private final Map<Jugador, DatosEntrada> ENTRADAS_POR_JUGADOR = new HashMap<>();
    private final Map<Integer, Jugador> MAPA_TECLAS_JUGADOR = new HashMap<>();
    private final ArrayList<EstacionTrabajo> ESTACIONES = new ArrayList<>();
    private Viewport viewportJuego;
    private Viewport viewportUI; //NO SIRVE DE NADA



    public void registrar(BotonInteractuable i) {
        ELEMENTOS_INTERACTUABLES.add(i);
    }

    public void registrarJugador(Jugador jugador, int[] teclasAsociadas) {
        DatosEntrada datos = new DatosEntrada();
        ENTRADAS_POR_JUGADOR.put(jugador, datos);
        for (Integer key : teclasAsociadas) {
            MAPA_TECLAS_JUGADOR.put(key, jugador);
        }
    }

    public void actualizarEntradas() {
        for (Map.Entry<Jugador, DatosEntrada> entry : ENTRADAS_POR_JUGADOR.entrySet()) {
            Jugador jugador = entry.getKey();
            DatosEntrada datos = entry.getValue();

            datos.arriba = Gdx.input.isKeyPressed(Input.Keys.W);
            datos.abajo = Gdx.input.isKeyPressed(Input.Keys.S);
            datos.izquierda = Gdx.input.isKeyPressed(Input.Keys.A);
            datos.derecha = Gdx.input.isKeyPressed(Input.Keys.D);

            jugador.manejarEntrada(datos);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        Jugador jugador = MAPA_TECLAS_JUGADOR.get(keycode);
        if(jugador != null) {
            ENTRADAS_POR_JUGADOR.get(jugador).presionar(keycode);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        Jugador jugador = MAPA_TECLAS_JUGADOR.get(keycode);
        if(jugador != null) {
            ENTRADAS_POR_JUGADOR.get(jugador).soltar(keycode);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector2 coordenadasUI = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);


            for (BotonInteractuable i : ELEMENTOS_INTERACTUABLES) {
                if(i.fueClickeado(coordenadasUI.x, coordenadasUI.y)) {
                    i.alClick();
                    return true;
                }
            } // si lo de arriba esta cambiado es culpa de mdyt

        if (viewportJuego != null) //aca como el mapa cargaba antes que el viewport nao arrancaba
        {
            Vector2 coordenadasJuego = new Vector2(screenX, screenY);
            viewportJuego.unproject(coordenadasJuego);

            for (EstacionTrabajo e : ESTACIONES) {
                if (e.fueClickeada(coordenadasJuego.x, coordenadasJuego.y)) {
                    e.alInteractuar();
                    return true;
                }
            }
        }

        return false;
    }




    public void registrarEstacionesTrabajo(List<EstacionTrabajo> estaciones) {
        ESTACIONES.clear();
        ESTACIONES.addAll(estaciones);
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public void setViewportJuego(Viewport viewport) {
        this.viewportJuego = viewport;
    }

    public void setViewportUI(Viewport viewport) {
        this.viewportUI = viewport;
    }

}
