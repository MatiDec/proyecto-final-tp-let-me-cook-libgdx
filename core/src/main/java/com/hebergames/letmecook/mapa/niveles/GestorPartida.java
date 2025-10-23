package com.hebergames.letmecook.mapa.niveles;

import com.hebergames.letmecook.mapa.Mapa;
import com.hebergames.letmecook.sonido.CancionNivel;

import java.util.ArrayList;
import java.util.Random;

public class GestorPartida {
    private static GestorPartida instancia;
    private ArrayList<NivelPartida> nivelesPartida;
    private int nivelActual;
    private int puntajeTotalPartida;
    private int cantidadSucursales = 1;

    private GestorPartida() {
        this.nivelesPartida = new ArrayList<>();
        this.nivelActual = 0;
        this.puntajeTotalPartida = 0;
    }

    public static GestorPartida getInstancia() {
        if (instancia == null) {
            instancia = new GestorPartida();
        }
        return instancia;
    }

    public void generarNuevaPartida(ArrayList<String> rutasMapas, int cantidadNiveles) {
        nivelesPartida.clear();
        nivelActual = 0;
        puntajeTotalPartida = 0;

        ArrayList<String> rutasDisponibles = new ArrayList<>(rutasMapas);
        CancionNivel[] cancionesDisponibles = CancionNivel.values();
        Random random = new Random();

        for (int i = 0; i < cantidadNiveles && !rutasDisponibles.isEmpty(); i++) {
            int indexMapa = random.nextInt(rutasDisponibles.size());
            String rutaMapaElegida = rutasDisponibles.remove(indexMapa);

            TurnoTrabajo turnoAleatorio = TurnoTrabajo.values()[random.nextInt(TurnoTrabajo.values().length)];

            CancionNivel cancionNivel = cancionesDisponibles[i % cancionesDisponibles.length];

            Mapa mapaGenerado = new Mapa(rutaMapaElegida, "Sucursal " + (i + 1));
            nivelesPartida.add(new NivelPartida(mapaGenerado, turnoAleatorio, cancionNivel));
        }
    }

    public NivelPartida getNivelActual() {
        if (nivelActual < nivelesPartida.size()) {
            return nivelesPartida.get(nivelActual);
        }
        return null;
    }

    public boolean avanzarNivel(int puntajeNivel) {
        if (nivelActual < nivelesPartida.size()) {
            nivelesPartida.get(nivelActual).marcarCompletado(puntajeNivel);
            puntajeTotalPartida += puntajeNivel;
            nivelActual++;
        }
        return nivelActual < nivelesPartida.size();
    }

    public boolean hayMasNiveles() {
        return nivelActual < nivelesPartida.size();
    }

    public void resetearPartida() {
        for (NivelPartida nivel : nivelesPartida) {
            if (nivel.getMapa() != null) {
                nivel.getMapa().dispose();
            }
        }
        nivelesPartida.clear();
        nivelActual = 0;
        puntajeTotalPartida = 0;
        cantidadSucursales = 1;
    }

    public int getNivelActualIndex() {
        return nivelActual;
    }

    public ArrayList<NivelPartida> getTodosLosNiveles() {
        return nivelesPartida;
    }

    public int getPuntajeTotalPartida() { return this.puntajeTotalPartida; }
}
