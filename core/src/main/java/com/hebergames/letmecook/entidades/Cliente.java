package com.hebergames.letmecook.entidades;

import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.maquinas.EstacionTrabajo;
import com.hebergames.letmecook.pedidos.EstadoPedido;
import com.hebergames.letmecook.pedidos.Pedido;

public class Cliente {
    private int id;
    private Pedido pedido;
    private EstacionTrabajo estacionAsignada;
    private float tiempoEspera;
    private float tiempoMaximoEspera;
    private static int contadorId = 0;

    public Cliente(Producto productoSolicitado, float tiempoMaximoEspera) {
        this.id = contadorId++;
        this.pedido = new Pedido(id, productoSolicitado);
        this.tiempoMaximoEspera = tiempoMaximoEspera;
        this.tiempoEspera = 0f;
    }

    public void actualizar(float delta) {
        if (pedido.getEstadoPedido() == EstadoPedido.EN_PREPARACION) {
            tiempoEspera += delta;
        }
    }

    public boolean haExpiradoTiempo() {
        return tiempoEspera >= tiempoMaximoEspera;
    }

    public float getTiempoRestante() {
        return Math.max(0, tiempoMaximoEspera - tiempoEspera);
    }

    public float getPorcentajeTiempo() {
        return tiempoEspera / tiempoMaximoEspera;
    }

    public int getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public EstacionTrabajo getEstacionAsignada() {
        return estacionAsignada;
    }

    public void setEstacionAsignada(EstacionTrabajo estacion) {
        this.estacionAsignada = estacion;
    }

    public void resetearTiempo() {
        this.tiempoEspera = 0f;
    }
}
