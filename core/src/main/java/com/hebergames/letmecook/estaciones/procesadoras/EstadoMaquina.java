package com.hebergames.letmecook.estaciones.procesadoras;

public enum EstadoMaquina {
    INACTIVA(0),
    ACTIVA(1),
    LISTA(2);

    private final int indice;

    EstadoMaquina(int indice) {
        this.indice = indice;
    }

    public int getIndice() {
        return indice;
    }
}
