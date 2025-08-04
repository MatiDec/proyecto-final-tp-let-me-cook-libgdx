package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.entregables.ingredientes.Ingrediente;

public interface MaquinaProcesadora {
    boolean puedeIniciarProceso();
    boolean iniciarProceso(Ingrediente ingrediente);
    void actualizarProceso(float delta);
    boolean tieneProcesandose();
    Ingrediente obtenerResultado();
    void dibujarIndicador(SpriteBatch batch);
    void detenerProceso();
}
