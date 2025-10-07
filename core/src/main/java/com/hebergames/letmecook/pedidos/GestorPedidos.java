package com.hebergames.letmecook.pedidos;

import java.util.ArrayList;

public class GestorPedidos {

    private ArrayList<PedidoConTiempo> pedidosActivos;
    private float tiempoPreparacion = 60f; // 60 segundos para preparar

    public GestorPedidos() {
        this.pedidosActivos = new ArrayList<>();
    }

    public void agregarPedido(Pedido pedido) {
        PedidoConTiempo pedidoConTiempo = new PedidoConTiempo(pedido, tiempoPreparacion);
        pedidosActivos.add(pedidoConTiempo);
    }

    public void actualizar(float delta) {
        for (int i = pedidosActivos.size() - 1; i >= 0; i--) {
            PedidoConTiempo pedidoConTiempo = pedidosActivos.get(i);
            pedidoConTiempo.actualizar(delta);

            if (!pedidoConTiempo.isActivo()) {
                // Pedido expiró
                pedidosActivos.remove(i);
            }
        }
    }

    public void removerPedido(int idCliente) {
        pedidosActivos.removeIf(p ->
            p.getPedido().getIdClienteSolicitante() == idCliente);
    }

    public PedidoConTiempo buscarPedidoPorCliente(int idCliente) {
        for (PedidoConTiempo pedido : pedidosActivos) {
            if (pedido.getPedido().getIdClienteSolicitante() == idCliente) {
                return pedido;
            }
        }
        return null;
    }

    public ArrayList<PedidoConTiempo> getPedidosActivos() {
        return new ArrayList<>(pedidosActivos);
    }

    public void setTiempoPreparacion(float tiempo) {
        this.tiempoPreparacion = tiempo;
    }
}
