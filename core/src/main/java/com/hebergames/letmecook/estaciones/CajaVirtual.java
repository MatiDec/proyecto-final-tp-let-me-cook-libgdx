package com.hebergames.letmecook.estaciones;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.entidades.clientes.Cliente;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.eventos.puntaje.CallbackPuntaje;
import com.hebergames.letmecook.pedidos.EstadoPedido;
import com.hebergames.letmecook.pedidos.GestorPedidos;
import com.hebergames.letmecook.pedidos.ResultadoEntrega;
import com.hebergames.letmecook.utiles.GestorTexturas;

public class CajaVirtual extends EstacionTrabajo implements EstacionEntrega {
    private Cliente clienteVirtual;
    private GestorPedidos gestorPedidos;
    private CallbackPuntaje callbackPuntaje;
    private TextureRegion texturaCajaApagada;
    private TextureRegion texturaCajaEncendida;

    public CajaVirtual(Rectangle area) {
        super(area);
        cargarTexturas();
    }

    private void cargarTexturas() {
        this.texturaCajaApagada = GestorTexturas.getInstance().getTexturaVirtualInactiva();
        this.texturaCajaEncendida = GestorTexturas.getInstance().getTexturaVirtualActiva();
    }

    public void setGestorPedidos(GestorPedidos gestor) {
        this.gestorPedidos = gestor;
    }

    public void setCallbackPuntaje(CallbackPuntaje callback) {
        this.callbackPuntaje = callback;
    }

    public void asignarCliente(Cliente cliente) {
        this.clienteVirtual = cliente;
    }

    public boolean tieneCliente() {
        return clienteVirtual != null;
    }

    public Cliente getCliente() {
        return clienteVirtual;
    }

    public void liberarCliente() {
        this.clienteVirtual = null;
    }

    @Override
    public void alInteractuar() {
        Jugador jugador = getJugadorOcupante();
        if (jugador == null) return;

        // Si tiene cliente virtual en espera, tomar pedido (como CajaRegistradora)
        if (tieneCliente() && clienteVirtual.getPedido().getEstadoPedido() == EstadoPedido.EN_ESPERA) {
            if (gestorPedidos != null) {
                // El cliente virtual usa ESTA MISMA caja como "mesa de retiro"
                clienteVirtual.getPedido().setEstadoPedido(EstadoPedido.EN_PREPARACION);
                clienteVirtual.resetearTiempo();
                System.out.println("Pedido virtual tomado");
            }
            jugador.salirDeMenu();
            alLiberar();
        }
        // Si tiene cliente en preparación y jugador tiene producto, entregar (como MesaRetiro)
        else if (tieneCliente() &&
            clienteVirtual.getPedido().getEstadoPedido() == EstadoPedido.EN_PREPARACION &&
            jugador.getInventario() instanceof Producto) {

            Producto productoJugador = (Producto) jugador.getInventario();

            ResultadoEntrega resultado = gestorPedidos.entregarPedido(this, productoJugador);

            // Notificar puntos mediante callback
            if (callbackPuntaje != null) {
                callbackPuntaje.onPuntosObtenidos(resultado.getPuntos());
            }

            jugador.sacarDeInventario();
            System.out.println(resultado.getMensaje());

            jugador.salirDeMenu();
            alLiberar();
        }
    }

    public void dibujarEstadoCaja(SpriteBatch batch) {
        // Siempre dibujar la textura base de la caja
        if (tieneCliente()) {
            batch.draw(texturaCajaEncendida, area.x, area.y, area.width, area.height);

            // Superponer la textura del cliente virtual
            TextureRegion texturaClienteVirtual = GestorTexturas.getInstance().getTexturaCliente();
            if (texturaClienteVirtual != null) {
                // Centrar el cliente en la caja
                float clienteX = area.x + (area.width / 2f) - 32f; // 32 = mitad del tamaño del cliente (64/2)
                float clienteY = area.y + (area.height / 2f) - 32f;
                batch.draw(texturaClienteVirtual, clienteX, clienteY, 64f, 64f);
            }
        } else {
            batch.draw(texturaCajaApagada, area.x, area.y, area.width, area.height);
        }
    }

    @Override
    public void dibujarEstado(SpriteBatch batch) {
        dibujarEstadoCaja(batch);
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
        // Opcional: mostrar info del pedido virtual
    }

    @Override
    protected void alLiberar() {
        // El cliente se libera cuando se entrega el pedido
    }
}
