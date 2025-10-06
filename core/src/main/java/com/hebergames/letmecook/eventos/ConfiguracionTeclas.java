package com.hebergames.letmecook.eventos;

public class ConfiguracionTeclas {
    private final int arriba;
    private final int abajo;
    private final int izquierda;
    private final int derecha;
    private final int interactuar;

    public ConfiguracionTeclas(int arriba, int abajo, int izquierda, int derecha, int interactuar) {
        this.arriba = arriba;
        this.abajo = abajo;
        this.izquierda = izquierda;
        this.derecha = derecha;
        this.interactuar = interactuar;
    }

    public int getArriba() {
        return arriba;
    }

    public int getAbajo() {
        return abajo;
    }

    public int getIzquierda() {
        return izquierda;
    }

    public int getDerecha() {
        return derecha;
    }

    public int getInteractuar() {
        return interactuar;
    }
}
