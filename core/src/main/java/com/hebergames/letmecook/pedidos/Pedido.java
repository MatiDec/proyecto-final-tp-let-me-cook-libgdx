package com.hebergames.letmecook.pedidos;

import com.hebergames.letmecook.entregables.productos.Producto;

public class Pedido {

    private Producto productoSolicitado;
    private int idClienteSolicitante;
    private EstadoPedido estadoPedido;

    public Pedido (int idClienteSolicitante, Producto productoSolicitado) {
        this.idClienteSolicitante = idClienteSolicitante;
        this.productoSolicitado = productoSolicitado;
        this.estadoPedido = EstadoPedido.EN_ESPERA;
    }

    public Producto getProductoSolicitado() {
        return this.productoSolicitado;
    }

    public int getIdClienteSolicitante() {
        return this.idClienteSolicitante;
    }
}
