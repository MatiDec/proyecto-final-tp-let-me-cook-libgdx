package com.hebergames.letmecook.eventos.eventosaleatorios;

import java.util.ArrayList;
import java.util.Random;

public class GestorEventosAleatorios {
    private ArrayList<EventoAleatorio> eventosActivos;
    private ArrayList<EventoAleatorio> eventosPosibles;
    private Random random;
    private static GestorEventosAleatorios instancia;

    private GestorEventosAleatorios() {
        this.eventosActivos = new ArrayList<>();
        this.eventosPosibles = new ArrayList<>();
        this.random = new Random();
    }

    public static GestorEventosAleatorios getInstancia() {
        if (instancia == null) {
            instancia = new GestorEventosAleatorios();
        }
        return instancia;
    }

    public void registrarEventoPosible(EventoAleatorio evento) {
        eventosPosibles.add(evento);
    }

    public void iniciarRonda() {
        desactivarTodosLosEventos();

        for (EventoAleatorio evento : eventosPosibles) {
            if (random.nextFloat() < evento.getProbabilidad()) {
                evento.activar();
                eventosActivos.add(evento);
            }
        }
    }

    public void finalizarRonda() {
        desactivarTodosLosEventos();
    }

    private void desactivarTodosLosEventos() {
        for (EventoAleatorio evento : eventosActivos) {
            evento.desactivar();
        }
        eventosActivos.clear();
    }

    public void limpiarEventos() {
        desactivarTodosLosEventos();
        eventosPosibles.clear();
    }

    public ArrayList<EventoAleatorio> getEventosActivos() {
        return eventosActivos;
    }

    public EventoPisoMojado getEventoPisoMojado() {
        for (EventoAleatorio evento : eventosActivos) {
            if (evento instanceof EventoPisoMojado) {
                return (EventoPisoMojado) evento;
            }
        }
        return null;
    }

    public void reset() {
        limpiarEventos();
    }
}
