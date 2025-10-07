package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Cliente;
import com.hebergames.letmecook.entidades.GestorClientes;
import com.hebergames.letmecook.entidades.Jugador;

public class CajaRegistradora extends EstacionTrabajo {

    private Cliente clienteAsignado;
    private boolean pedidoTomado;
    private GestorClientes gestorClientes; // Referencia local

    public CajaRegistradora(Rectangle area) {
        super(area);
        this.clienteAsignado = null;
        this.pedidoTomado = false;
    }

    public void setGestorClientes(GestorClientes gestor) {
        this.gestorClientes = gestor;
    }

    public void asignarCliente(Cliente cliente) {
        this.clienteAsignado = cliente;
        this.pedidoTomado = false;
    }

    public boolean tieneClienteEsperando() {
        return clienteAsignado != null && !pedidoTomado;
    }

    public Cliente getClienteAsignado() {
        return clienteAsignado;
    }

    public void marcarPedidoTomado() {
        this.pedidoTomado = true;
    }

    public void liberarCliente() {
        this.clienteAsignado = null;
        this.pedidoTomado = false;
    }

    @Override
    public void alInteractuar() {
        Jugador jugador = getJugadorOcupante();
        if (jugador == null || gestorClientes == null) return;

        if (!tieneClienteEsperando()) {
            System.out.println("No hay cliente esperando en esta caja");
            return;
        }

        gestorClientes.tomarPedidoEnCaja(this, jugador);
        jugador.salirDeMenu();
    }

    @Override
    protected void iniciarMenu(Jugador jugador) {
        // No necesita menú
    }

    @Override
    public void manejarSeleccionMenu(Jugador jugador, int numeroSeleccion) {
        // No aplica
    }

    @Override
    protected void dibujarMenu(SpriteBatch batch, Jugador jugador) {
        // No necesita menú visual
    }
}
