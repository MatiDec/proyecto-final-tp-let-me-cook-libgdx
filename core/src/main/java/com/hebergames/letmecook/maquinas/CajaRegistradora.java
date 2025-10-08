package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Cliente;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.pedidos.EstadoPedido;
import com.hebergames.letmecook.pedidos.GestorPedidos;

public class CajaRegistradora extends EstacionTrabajo {
    private Cliente clienteAsignado;
    private GestorPedidos gestorPedidos;

    public CajaRegistradora(Rectangle area) {
        super(area);
    }

    public void setGestorPedidos(GestorPedidos gestor) {
        this.gestorPedidos = gestor;
    }

    public void asignarCliente(Cliente cliente) {
        this.clienteAsignado = cliente;
    }

    public boolean tieneCliente() {
        return clienteAsignado != null;
    }

    public Cliente getCliente() {
        return clienteAsignado;
    }

    public void liberarCliente() {
        this.clienteAsignado = null;
    }

    public boolean tomarPedido() {
        if (gestorPedidos != null && tieneCliente() &&
                clienteAsignado.getPedido().getEstadoPedido() == EstadoPedido.EN_ESPERA) {
            return gestorPedidos.tomarPedido(this);
        }
        return false;
    }

    @Override
    public void alInteractuar() {
        // Se maneja en manejarSeleccionMenu
    }

    @Override
    protected void iniciarMenu(Jugador jugador) {
        // No se usa menú numérico aquí
    }

    @Override
    public void manejarSeleccionMenu(Jugador jugador, int numeroSeleccion) {
        // No se usa menú numérico aquí
    }

    @Override
    protected void dibujarMenu(SpriteBatch batch, Jugador jugador) {
        // Aquí se dibuja info del pedido del cliente si existe
    }

    @Override
    protected void alLiberar() {
        // Mantener el cliente asignado hasta que se tome el pedido
    }
}
