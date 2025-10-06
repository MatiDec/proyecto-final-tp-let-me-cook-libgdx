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
    private final Map<Jugador, ConfiguracionTeclas> CONFIG_TECLAS_JUGADOR = new HashMap<>();
    private final ArrayList<EstacionTrabajo> ESTACIONES = new ArrayList<>();
    private Viewport viewportJuego;
    private Viewport viewportUI;
    private float ultimoClickX = 0f;

    private final Map<Jugador, int[]> TECLAS_MENU_POR_JUGADOR = new HashMap<>();
    private final Map<Jugador, Integer> NUMERO_SELECCIONADO = new HashMap<>();

    public void registrar(BotonInteractuable i) {
        ELEMENTOS_INTERACTUABLES.add(i);
    }

    public void registrarJugador(Jugador jugador, ConfiguracionTeclas config) {
        DatosEntrada datos = new DatosEntrada();
        ENTRADAS_POR_JUGADOR.put(jugador, datos);
        CONFIG_TECLAS_JUGADOR.put(jugador, config);

        // Mapear todas las teclas del jugador
        MAPA_TECLAS_JUGADOR.put(config.getArriba(), jugador);
        MAPA_TECLAS_JUGADOR.put(config.getAbajo(), jugador);
        MAPA_TECLAS_JUGADOR.put(config.getIzquierda(), jugador);
        MAPA_TECLAS_JUGADOR.put(config.getDerecha(), jugador);
        MAPA_TECLAS_JUGADOR.put(config.getInteractuar(), jugador);
    }

    public void actualizarEntradas() {
        for (Map.Entry<Jugador, DatosEntrada> entry : ENTRADAS_POR_JUGADOR.entrySet()) {
            Jugador jugador = entry.getKey();
            DatosEntrada datos = entry.getValue();
            ConfiguracionTeclas config = CONFIG_TECLAS_JUGADOR.get(jugador);

            if (config != null) {
                datos.arriba = Gdx.input.isKeyPressed(config.getArriba());
                datos.abajo = Gdx.input.isKeyPressed(config.getAbajo());
                datos.izquierda = Gdx.input.isKeyPressed(config.getIzquierda());
                datos.derecha = Gdx.input.isKeyPressed(config.getDerecha());
            }

            jugador.manejarEntrada(datos);
        }

        for (Jugador jugador : NUMERO_SELECCIONADO.keySet()) {
            int numeroSeleccionado = NUMERO_SELECCIONADO.get(jugador);

            if (jugador.estaEnMenu()) {
                EstacionTrabajo estacion = jugador.getEstacionActual();
                if (estacion != null) {
                    estacion.manejarSeleccionMenu(jugador, numeroSeleccionado);
                }
            }
        }

        NUMERO_SELECCIONADO.clear();

    }

    public void registrarTeclasMenu(Jugador jugador, int[] teclas) {
        TECLAS_MENU_POR_JUGADOR.put(jugador, teclas);
    }

    @Override
    public boolean keyDown(int keycode) {
        Jugador jugador = MAPA_TECLAS_JUGADOR.get(keycode);
        if(jugador != null) {
            ConfiguracionTeclas config = CONFIG_TECLAS_JUGADOR.get(jugador);

            System.out.println("DEBUG Entrada: Tecla presionada: " + keycode + " por jugador en posición: " + jugador.getPosicion());

            // Si es la tecla de interacción, interactuar con estaciones
            if (config != null && keycode == config.getInteractuar()) {
                System.out.println("DEBUG Entrada: Tecla de interacción detectada");
                interactuarConEstacionCercana(jugador);
            }

            ENTRADAS_POR_JUGADOR.get(jugador).presionar(keycode);
            return true;
        } else {
            System.out.println("DEBUG Entrada: Tecla " + keycode + " no mapeada a ningún jugador");
        }

        for (Map.Entry<Jugador, int[]> entry : TECLAS_MENU_POR_JUGADOR.entrySet()) {
            jugador = entry.getKey();
            int[] teclas = entry.getValue();

            for (int i = 0; i < teclas.length; i++) {
                if (keycode == teclas[i]) {
                    // Guarda la selección como 1, 2, 3, 4, 5 (índice + 1)
                    NUMERO_SELECCIONADO.put(jugador, i + 1);
                    return true;
                }
            }
        }

        return false;
    }

    private void interactuarConEstacionCercana(Jugador jugador) {
        Vector2 posJugador = jugador.getPosicion();
        float radioInteraccion = 100f; // Ajusta según necesites

        System.out.println("DEBUG: Buscando estación cerca de: " + posJugador);

        for (EstacionTrabajo estacion : ESTACIONES) {
            // Calcular distancia aproximada (puedes mejorar esto según tu implementación)
            if (estacion.estaCerca(posJugador.x, posJugador.y, radioInteraccion)) {
                System.out.println("DEBUG: Estación encontrada, interactuando...");
                estacion.interactuarConJugador(jugador);
                break;
            }
        }
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
        ultimoClickX = screenX * (Gdx.graphics.getWidth() / (float) Gdx.graphics.getWidth());

        // Solo permitir clicks en elementos de UI (botones de menú, etc.)
        for (BotonInteractuable i : ELEMENTOS_INTERACTUABLES) {
            if(i.fueClickeado(coordenadasUI.x, coordenadasUI.y)) {
                i.alClick();
                return true;
            }
        }

        // Ya no se permiten clicks en estaciones de trabajo durante el juego
        return false;
    }

    public void registrarEstacionesTrabajo(List<EstacionTrabajo> estaciones) {
        ESTACIONES.clear();
        ESTACIONES.addAll(estaciones);
    }

    public DatosEntrada getDatosEntrada(Jugador jugador) {
        return ENTRADAS_POR_JUGADOR.get(jugador);
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

    public float getUltimoClickX() {
        return this.ultimoClickX;
    }
}
