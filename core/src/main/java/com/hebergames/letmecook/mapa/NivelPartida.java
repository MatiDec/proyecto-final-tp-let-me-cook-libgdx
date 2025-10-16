package com.hebergames.letmecook.mapa;

import com.hebergames.letmecook.sonido.CancionNivel;

public class NivelPartida {
    private Mapa mapa;
    private TurnoTrabajo turno;
    private int puntajeObtenido;
    private boolean completado;
    private CancionNivel cancionNivel;

    public NivelPartida(Mapa mapa, TurnoTrabajo turno, CancionNivel cancion) {
        this.mapa = mapa;
        this.turno = turno;
        this.cancionNivel = cancion;
        this.puntajeObtenido = 0;
        this.completado = false;
    }

    public void marcarCompletado(int puntaje) {
        this.completado = true;
        this.puntajeObtenido = puntaje;
    }

    public Mapa getMapa() { return this.mapa; }

    public TurnoTrabajo getTurno() { return this.turno; }

    public boolean isCompletado() { return this.completado; }

    public int getPuntajeObtenido() { return this.puntajeObtenido; }

    public CancionNivel getCancionNivel() { return this.cancionNivel; }
}
