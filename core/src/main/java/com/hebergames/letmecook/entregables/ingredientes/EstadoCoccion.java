package com.hebergames.letmecook.entregables.ingredientes;

public enum EstadoCoccion {
    CRUDO("Crudo"),
    COCIDO("Cocido"),
    QUEMADO("Quemado");

    private String nombre;

    EstadoCoccion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }
}
