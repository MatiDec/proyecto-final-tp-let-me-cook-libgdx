package com.hebergames.letmecook.mapa;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.maquinas.CajaRegistradora;
import com.hebergames.letmecook.maquinas.EstacionTrabajo;
import com.hebergames.letmecook.maquinas.MesaRetiro;
import java.util.ArrayList;

public class GestorMapa {

    private Mapa mapaActual;
    private ArrayList<EstacionTrabajo> estaciones;

    public GestorMapa(String rutaInicial) {
        cargarMapa(rutaInicial);
    }

    public void cargarMapa(String ruta) {
        if (mapaActual != null) {
            mapaActual.dispose();
        }
        mapaActual = new Mapa(ruta);
        estaciones = mapaActual.getEstacionesTrabajo();
    }

    public void renderizar(OrthographicCamera camara) {
        if (mapaActual != null) {
            mapaActual.render(camara);
        }
    }

    public void actualizarEstaciones(float delta) {
        for (EstacionTrabajo estacion : estaciones) {
            estacion.actualizar(delta);
            estacion.verificarDistanciaYLiberar();
        }
    }

    public void dibujarIndicadores(SpriteBatch batch) {
        for (EstacionTrabajo estacion : estaciones) {
            estacion.dibujarIndicador(batch);
        }
    }

    public void asignarColisionesYInteracciones(Jugador jugador) {
        if (mapaActual != null) {
            jugador.setColisionables(mapaActual.getRectangulosColision());
            jugador.setInteractuables(mapaActual.getRectangulosInteractuables());
        }
    }

    public ArrayList<EstacionTrabajo> getEstaciones() {
        return estaciones;
    }

    public Mapa getMapaActual() {
        return mapaActual;
    }

    public ArrayList<CajaRegistradora> getCajas() {
        ArrayList<CajaRegistradora> cajas = new ArrayList<>();
        for (EstacionTrabajo e : estaciones) {
            if (e instanceof CajaRegistradora) {
                cajas.add((CajaRegistradora) e);
            }
        }
        return cajas;
    }

    public ArrayList<MesaRetiro> getMesas() {
        ArrayList<MesaRetiro> mesas = new ArrayList<>();
        for (EstacionTrabajo e : estaciones) {
            if (e instanceof MesaRetiro) {
                mesas.add((MesaRetiro) e);
            }
        }
        return mesas;
    }

    public void dispose() {
        if (mapaActual != null) {
            mapaActual.dispose();
        }
    }
}
