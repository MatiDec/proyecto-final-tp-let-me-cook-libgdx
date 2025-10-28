package com.hebergames.letmecook.pantallas.juego;

import com.hebergames.letmecook.entidades.Jugador;
import java.util.ArrayList;

public class DetectorInactividad {

    private float tiempoInactividad = 0f;
    private final float TIEMPO_LIMITE_INACTIVIDAD;
    private ArrayList<Jugador> jugadores;
    private float[] ultimasPosicionesX;
    private float[] ultimasPosicionesY;

    public DetectorInactividad(ArrayList<Jugador> jugadores, float tiempoLimite) {
        this.jugadores = jugadores;
        this.TIEMPO_LIMITE_INACTIVIDAD = tiempoLimite;
        this.ultimasPosicionesX = new float[jugadores.size()];
        this.ultimasPosicionesY = new float[jugadores.size()];

        for (int i = 0; i < jugadores.size(); i++) {
            ultimasPosicionesX[i] = jugadores.get(i).getPosicion().x;
            ultimasPosicionesY[i] = jugadores.get(i).getPosicion().y;
        }
    }

    public void actualizar(float delta) {
        boolean algunoSeMovio = false;

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            float posX = jugador.getPosicion().x;
            float posY = jugador.getPosicion().y;

            if (Math.abs(posX - ultimasPosicionesX[i]) > 0.1f ||
                Math.abs(posY - ultimasPosicionesY[i]) > 0.1f) {
                algunoSeMovio = true;
                ultimasPosicionesX[i] = posX;
                ultimasPosicionesY[i] = posY;
            }
        }

        if (algunoSeMovio) {
            tiempoInactividad = 0f;
        } else {
            tiempoInactividad += delta;
        }
    }

    public boolean haySuperadoLimite() {
        return tiempoInactividad >= TIEMPO_LIMITE_INACTIVIDAD;
    }

    public float getTiempoInactividad() {
        return tiempoInactividad;
    }

    public void reset() {
        tiempoInactividad = 0f;
    }
}
