package com.hebergames.letmecook.entregables.ingredientes;

public enum AnimacionIngrediente {
    VACIO("vacio", 0),
    CARNE("carne", 1),
    PAN("pan", 2);
    // aca abajo agregar el resto

    private final String nombre;
    private final int filaAnimacion;

    AnimacionIngrediente(String nombre, int filaAnimacion) {
        this.nombre = nombre;
        this.filaAnimacion = filaAnimacion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getFilaAnimacion() {
        return filaAnimacion;
    }

}
