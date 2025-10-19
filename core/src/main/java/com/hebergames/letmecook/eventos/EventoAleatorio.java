package com.hebergames.letmecook.eventos;

public interface EventoAleatorio {
    void activar();
    void desactivar();
    boolean estaActivo();
    String getNombre();
    float getProbabilidad();
}
