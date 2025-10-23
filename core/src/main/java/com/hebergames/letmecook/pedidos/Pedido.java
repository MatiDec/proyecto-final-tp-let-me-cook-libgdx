package com.hebergames.letmecook.pedidos;

import com.hebergames.letmecook.entregables.productos.Producto;

import java.util.ArrayList;
import java.util.Random;

public class Pedido {

    private ArrayList<Producto> productosSolicitados;
    private static final int MAX_PRODUCTOS_POR_PEDIDO = 3; // Ajusta este valor según necesites
    private int idClienteSolicitante;
    private EstadoPedido estadoPedido;

    public Pedido(int idClienteSolicitante, ArrayList<Producto> productosSolicitados) {
        this.idClienteSolicitante = idClienteSolicitante;
        this.productosSolicitados = productosSolicitados;
        this.estadoPedido = EstadoPedido.EN_ESPERA;
    }

    public EstadoPedido getEstadoPedido() {
        return this.estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estado) {
        this.estadoPedido = estado;
    }

    public ArrayList<Producto> getProductosSolicitados() {
        return this.productosSolicitados;
    }

    public static int getCantidadProductosAleatorios(Random random) {
        return 1 + random.nextInt(MAX_PRODUCTOS_POR_PEDIDO); // Genera entre 1 y MAX_PRODUCTOS_POR_PEDIDO
    }
}
