package com.hebergames.letmecook.mapa;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.estaciones.CajaVirtual;
import com.hebergames.letmecook.eventos.eventosaleatorios.EventoPisoMojado;
import com.hebergames.letmecook.eventos.eventosaleatorios.GestorEventosAleatorios;
import com.hebergames.letmecook.estaciones.CajaRegistradora;
import com.hebergames.letmecook.estaciones.EstacionTrabajo;
import com.hebergames.letmecook.estaciones.MesaRetiro;
import java.util.ArrayList;

public class GestorMapa {

    private Mapa mapaActual;
    private ArrayList<EstacionTrabajo> estaciones;

    public GestorMapa() {
        this.estaciones = new ArrayList<>();
    }

    public void setMapaActual(Mapa mapa) {
        if (this.mapaActual != null && this.mapaActual != mapa) {
            this.mapaActual.dispose();
        }
        this.mapaActual = mapa;
        this.estaciones = mapa.getEstacionesTrabajo();
    }

    public void cargarMapa(String ruta, String nombreSucursal) {
        if (mapaActual != null) {
            mapaActual.dispose();
        }
        mapaActual = new Mapa(ruta, nombreSucursal);
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
        GestorEventosAleatorios gestorEventos = GestorEventosAleatorios.getInstancia();
        EventoPisoMojado eventoPiso = gestorEventos.getEventoPisoMojado();
        if (eventoPiso != null) {
            eventoPiso.dibujar(batch);
        }

        // Dibujar indicadores y estados de estaciones
        for (EstacionTrabajo estacion : estaciones) {
            estacion.dibujarIndicador(batch);
            estacion.dibujarIndicadorError(batch);
            estacion.dibujarEstado(batch);
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

    public ArrayList<Rectangle> getTilesCaminables() {
        if (mapaActual != null) {
            return mapaActual.getTilesCaminables();
        }
        return new ArrayList<>();
    }

    public void dispose() {
        if (mapaActual != null) {
            mapaActual.dispose();
        }
    }
}
