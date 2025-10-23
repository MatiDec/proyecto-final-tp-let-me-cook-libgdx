package com.hebergames.letmecook.entregables.productos.bebidas;

public enum TamanoBebida {
    PEQUENO("Pequeño", 1.0f),
    MEDIANO("Mediano", 1.5f),
    GRANDE("Grande", 2.0f);

    private String nombre;
    private float multiplicadorTiempo;

    TamanoBebida(String nombre, float multiplicadorTiempo) {
        this.nombre = nombre;
        this.multiplicadorTiempo = multiplicadorTiempo;
    }

    public String getNombre() {
        return nombre;
    }

    public float getMultiplicadorTiempo() {
        return multiplicadorTiempo;
    }
}
