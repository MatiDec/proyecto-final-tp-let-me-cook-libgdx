package com.hebergames.letmecook.pantallas;

public enum OpcionMenuPrincipal {

    MULTI_LOCAL("Multijugador Local"), MULTI_ONLINE("Multijugador Online"), TUTORIAL("Tutorial de juego"), OPCIONES("Opciones"), SALIR("Salir");

    private String nombre;

    OpcionMenuPrincipal(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }
}
