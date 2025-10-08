package com.hebergames.letmecook.pedidos;

public class ResultadoEntrega {
    private boolean exitoso;
    private int puntos;
    private String mensaje;

    public ResultadoEntrega(boolean exitoso, int puntos, String mensaje) {
        this.exitoso = exitoso;
        this.puntos = puntos;
        this.mensaje = mensaje;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getMensaje() {
        return mensaje;
    }
}
