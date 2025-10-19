package com.hebergames.letmecook.eventos;

import com.hebergames.letmecook.maquinas.EstacionTrabajo;

public class EventoMaquinaRota implements EventoAleatorio {
    private EstacionTrabajo estacionAfectada;
    private boolean activo;
    private static final String NOMBRE = "Máquina Fuera de Servicio";
    private static final float PROBABILIDAD = 0.15f; // 15% por ronda

    public EventoMaquinaRota(EstacionTrabajo estacion) {
        this.estacionAfectada = estacion;
        this.activo = false;
    }

    @Override
    public void activar() {
        if (estacionAfectada != null && !activo) {
            estacionAfectada.setFueraDeServicio(true);
            activo = true;
            System.out.println("EVENTO: " + estacionAfectada.getClass().getSimpleName() + " fuera de servicio!");
        }
    }

    @Override
    public void desactivar() {
        if (estacionAfectada != null && activo) {
            estacionAfectada.setFueraDeServicio(false);
            activo = false;
        }
    }

    @Override
    public boolean estaActivo() {
        return activo;
    }

    @Override
    public String getNombre() {
        return NOMBRE;
    }

    @Override
    public float getProbabilidad() {
        return PROBABILIDAD;
    }

    public EstacionTrabajo getEstacionAfectada() {
        return estacionAfectada;
    }
}
