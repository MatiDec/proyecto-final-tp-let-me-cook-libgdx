package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.entidades.JugadorHost;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.maquinas.EstacionTrabajo;

import java.util.ArrayList;

public class GestorEntradaJuego {

    private Entrada entrada;
    private final JugadorHost jugador;
    private final ArrayList<EstacionTrabajo> estaciones;

    private static final int[] TECLAS_MOVIMIENTO = {
        Input.Keys.W,
        Input.Keys.A,
        Input.Keys.S,
        Input.Keys.D
    };

    public GestorEntradaJuego(JugadorHost jugador, ArrayList<EstacionTrabajo> estaciones) {
        this.jugador = jugador;
        this.estaciones = estaciones;
        this.entrada = new Entrada();
    }

    public void configurarEntrada(Viewport viewportJuego, Viewport viewportUI) {
        entrada = new Entrada();
        entrada.setViewportJuego(viewportJuego);
        entrada.setViewportUI(viewportUI);
        Gdx.input.setInputProcessor(entrada);
        entrada.registrarJugador(jugador, TECLAS_MOVIMIENTO);
        entrada.registrarEstacionesTrabajo(estaciones);
    }

    public void actualizarEntradas() {
        entrada.actualizarEntradas();
    }

    public Entrada getEntrada() {
        return entrada;
    }
}
