package com.hebergames.letmecook.estaciones;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.clientes.Cliente;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.eventos.puntaje.CallbackPuntaje;
import com.hebergames.letmecook.pedidos.GestorPedidos;
import com.hebergames.letmecook.pedidos.ResultadoEntrega;

public class MesaRetiro extends EstacionTrabajo {
    private Cliente clienteAsignado;
    private GestorPedidos gestorPedidos;
    private CallbackPuntaje callbackPuntaje;

    public MesaRetiro(Rectangle area) {
        super(area);
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

    public void setGestorPedidos(GestorPedidos gestor) {
        this.gestorPedidos = gestor;
    }

    public void setCallbackPuntaje(CallbackPuntaje callback) {
        this.callbackPuntaje = callback;
    }

    public ResultadoEntrega entregarProducto(Producto producto) {
        if (gestorPedidos != null && tieneCliente() && producto != null) {
            ResultadoEntrega resultado = gestorPedidos.entregarPedido(this, producto);

            // Notificar puntos mediante callback
            if (callbackPuntaje != null) {
                callbackPuntaje.onPuntosObtenidos(resultado.getPuntos());
            }

            return resultado;
        }
        return new ResultadoEntrega(false, 0, "No se puede entregar el pedido");
    }

    public void liberarCliente() {
        this.clienteAsignado = null;
    }

    @Override
    public void alInteractuar() {
        // Se maneja en EstacionTrabajo
    }

    @Override
    protected void iniciarMenu(Jugador jugador) {
        // No necesita menú
    }

    @Override
    public void manejarSeleccionMenu(Jugador jugador, int numeroSeleccion) {
        // No necesita menú
    }

    @Override
    protected void dibujarMenu(SpriteBatch batch, Jugador jugador) {
        // Aquí dibujar info del cliente esperando
    }

    @Override
    protected void alLiberar() {
        // El cliente se libera cuando se entrega el pedido
    }
}
