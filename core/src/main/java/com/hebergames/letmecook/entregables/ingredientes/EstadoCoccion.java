package com.hebergames.letmecook.entregables.ingredientes;

public enum EstadoCoccion {
    CRUDO("Crudo"),
    COCIDO("Cocido"),
    QUEMADO("Quemado");

    private final String ESTADO;

    EstadoCoccion(final String ESTADO) {
        this.ESTADO = ESTADO;
    }

    public String getESTADO() {
        return this.ESTADO;
    }
}
