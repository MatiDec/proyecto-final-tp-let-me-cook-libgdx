package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.eventos.DatosEntrada;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.eventos.ConfiguracionTeclas;
import com.hebergames.letmecook.maquinas.EstacionTrabajo;

import java.util.ArrayList;
import java.util.List;

public class GestorEntradaJuego {

    private Entrada entrada;
    private final List<Jugador> jugadores;
    private final ArrayList<EstacionTrabajo> estaciones;
    private static GestorEntradaJuego instancia;

    // Configuración de teclas para Jugador 1 (WASD + E)
    private static final ConfiguracionTeclas CONFIG_JUGADOR_1 = new ConfiguracionTeclas(
        Input.Keys.W,    // arriba
        Input.Keys.S,    // abajo
        Input.Keys.A,    // izquierda
        Input.Keys.D,    // derecha
        Input.Keys.E     // interactuar
    );

    // Configuración de teclas para Jugador 2 (Flechas + Enter/Espacio)
    private static final ConfiguracionTeclas CONFIG_JUGADOR_2 = new ConfiguracionTeclas(
        Input.Keys.UP,      // arriba
        Input.Keys.DOWN,    // abajo
        Input.Keys.LEFT,    // izquierda
        Input.Keys.RIGHT,   // derecha
        Input.Keys.ENTER    // interactuar (puedes usar Input.Keys.SPACE si prefieres)
    );

    public static final int[] TECLAS_MENU_JUGADOR_1 = {
        Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3, Input.Keys.NUM_4, Input.Keys.NUM_5
    };

    public static final int[] TECLAS_MENU_JUGADOR_2 = {
        Input.Keys.NUMPAD_1, Input.Keys.NUMPAD_2, Input.Keys.NUMPAD_3, Input.Keys.NUMPAD_4, Input.Keys.NUMPAD_5
    };

    public GestorEntradaJuego(List<Jugador> jugadores, ArrayList<EstacionTrabajo> estaciones) {
        this.jugadores = jugadores;
        this.estaciones = estaciones;
        this.entrada = new Entrada();
        instancia = this;
    }

    public static GestorEntradaJuego getInstancia() {
        return instancia;
    }

    public void configurarEntrada(Viewport viewportJuego, Viewport viewportUI) {
        entrada = new Entrada();
        entrada.setViewportJuego(viewportJuego);
        entrada.setViewportUI(viewportUI);
        Gdx.input.setInputProcessor(entrada);

        // Registrar todos los jugadores con sus configuraciones
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            ConfiguracionTeclas config = obtenerConfiguracionTeclas(i);
            entrada.registrarJugador(jugador, config);
            entrada.registrarTeclasMenu(jugadores.get(0), TECLAS_MENU_JUGADOR_1);
            if (jugadores.size() > 1) {
                entrada.registrarTeclasMenu(jugadores.get(1), TECLAS_MENU_JUGADOR_2);
            }
        }

        entrada.registrarEstacionesTrabajo(estaciones);
    }

    private ConfiguracionTeclas obtenerConfiguracionTeclas(int indiceJugador) {
        if (indiceJugador == 1) {
            return CONFIG_JUGADOR_2;
        }
        return CONFIG_JUGADOR_1;
    }

    public void actualizarEntradas() {
        entrada.actualizarEntradas();
    }

    public DatosEntrada getDatosEntradaParaJugador(Jugador jugador) {
        return entrada.getDatosEntrada(jugador);
    }

    public Entrada getEntrada() {
        return entrada;
    }

    /**
     * Obtiene la configuración de teclas predeterminada para un jugador específico
     */
    public static ConfiguracionTeclas getConfiguracionPredeterminada(int numeroJugador) {
        return numeroJugador == 1 ? CONFIG_JUGADOR_1 : CONFIG_JUGADOR_2;
    }
}
