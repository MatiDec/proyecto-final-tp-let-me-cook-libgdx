package com.hebergames.letmecook.utiles;

import com.hebergames.letmecook.entidades.JugadorHost;

public class Configuracion {

    public static final float ANCHO = 1920;
    public static final float ALTO = 1080;
    private static Configuracion instancia;
    private JugadorHost jugadorPrincipal;

    private Configuracion() {}

    public static Configuracion getInstancia() {
        if(instancia == null) {
            instancia = new Configuracion();
        }
        return instancia;
    }

    public JugadorHost getJugadorPrincipal() {
        return jugadorPrincipal;
    }

    public void setJugadorPrincipal(JugadorHost jugadorHost) {
        this.jugadorPrincipal = jugadorHost;
    }
}
