package com.hebergames.letmecook.eventos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.hebergames.letmecook.entidades.Jugador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Entrada implements InputProcessor {

    private final ArrayList<BotonInteractuable> ELEMENTOS_INTERACTUABLES = new ArrayList<>();
    private final Map<Jugador, DatosEntrada> ENTRADAS_POR_JUGADOR = new HashMap<>();
    private final Map<Integer, Jugador> MAPA_TECLAS_JUGADOR = new HashMap<>();

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
        float yCorrecto = Gdx.graphics.getHeight() - screenY;

        for (BotonInteractuable i : ELEMENTOS_INTERACTUABLES) {
            if(i.fueClickeado(screenX, yCorrecto)) {
                i.alClick();
                return true;
            }
        }
        return false;
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
}
