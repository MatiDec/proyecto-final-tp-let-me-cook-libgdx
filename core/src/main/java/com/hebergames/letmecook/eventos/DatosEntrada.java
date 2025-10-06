package com.hebergames.letmecook.eventos;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DatosEntrada {

    private final Set<Integer> TECLAS_PRESIONADAS = ConcurrentHashMap.newKeySet();
    public boolean arriba, abajo, izquierda, derecha;

    public void presionar(int keycode) {
        this.TECLAS_PRESIONADAS.add(keycode);
    }

    public void soltar(int keycode) {
        this.TECLAS_PRESIONADAS.remove(keycode);
    }

    public boolean estaPresionada(int keycode) {
        return this.TECLAS_PRESIONADAS.contains(keycode);
    }

    public Set<Integer> getTeclasPresionadas() {
        return this.TECLAS_PRESIONADAS;
    }
}
